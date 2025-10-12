package com.pos.features.user.model.entity;


import com.pos.constant.Permission;
import com.pos.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_email")
    )
    @Column(name = "permission")
    private Set<Permission> permissions;

    private LocalDate createdDate = LocalDate.now();

    private LocalDate updateDate;

    private boolean isEnabled = true;

    private boolean isAccountIsActive = true;

    private boolean isAccountNotLocked = true;

    private String profileImgUrl;

}





/*1. DB Sequence + UID in Service
How it Works:

Database Sequence: This is a special kind of database object that automatically generates unique, sequential numbers. The sequence is tied to the database, and it ensures that no two users get the same sequence number, even if they insert records at the same time.

App Logic: You will use the sequence number to generate the custom UID (e.g., UID25100001 for user 1 created in October 2025). This requires reading the sequence and building the UID in your service layer.

Steps for Implementation:

Create a Sequence in the Database:

Example for PostgreSQL:

CREATE SEQUENCE uid_seq START WITH 1 INCREMENT BY 1;


This sequence will generate sequential numbers starting from 1, increasing by 1 each time.

MySQL doesn’t support sequences directly, but you can use an auto-increment column to achieve a similar effect or a custom stored procedure.

In the Service Layer:

You query the database to get the next value of the sequence (nextval), then construct the uid using the sequence, the current date, and padding to get the desired format.

Sample Service Code (for Spring Boot):

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public User createUser() {
        // Fetch next sequence value from database
        Long seq = ((Number) entityManager
            .createNativeQuery("SELECT nextval('uid_seq')")
            .getSingleResult()).longValue();

        // Get the current date (year and month)
        LocalDateTime now = LocalDateTime.now();
        String year = String.format("%ty", now); // Last 2 digits of the year (e.g., 25 for 2025)
        String month = String.format("%02d", now.getMonthValue()); // Month (e.g., 10 for October)

        // Pad the sequence value to a fixed length (e.g., 0001, 0002, ...)
        String paddedSeq = String.format("%04d", seq);

        // Construct UID
        String uid = "UID" + year + month + paddedSeq;

        // Create new User and set the UID
        User user = new User();
        user.setUid(uid);
        user.setCreatedAt(now);

        // Save user to DB
        return userRepository.save(user);
    }
}


Advantages:

Scalable: The database handles sequence generation efficiently even under high load.

Atomic and Safe: The sequence guarantees uniqueness and handles concurrency well. The database ensures that no two users can have the same uid at the same time.

Simple: This solution avoids complex locks or sharding logic. Everything happens in the database, which is optimized for such tasks.

Disadvantages:

Requires Database Support for Sequences: Not all databases support sequences (although many relational databases do).

Single DB Call: While it’s a single insert from the app’s perspective, the database does need to increment the sequence, which could create contention if too many users are creating records at the same time.

2. UUID (Universally Unique Identifier)
How it Works:

UUID: It’s a globally unique identifier (128-bit value), usually represented as a 32-character hexadecimal string (e.g., f47ac10b-58cc-4372-a567-0e02b2c3d479).

The UUID is generated automatically either by the database or in the application itself.

The format is fixed, and you can’t easily customize it into a human-readable or business-specific pattern (like UID25100001).

Steps for Implementation:

Database Auto-Generation:

Most databases (like PostgreSQL, MySQL, and Oracle) can generate UUIDs natively.

PostgreSQL: You can use uuid-ossp extension to generate UUIDs:

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


In your entity, you can define the UUID primary key like this:

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    // other fields
}


Advantages:

Unique Across Systems: UUIDs are globally unique, so they work well in distributed systems.

No Need for Centralized Generation: Each system (or even client) can generate a UUID without central coordination.

Simple: You don’t need any additional logic to handle sequences or formatting; it’s handled automatically.

Disadvantages:

Format Not Customizable: UUIDs are long, opaque strings that don’t follow any business-specific format (like the UID25100001 format you want).

Not Human-Readable: The default format of UUIDs isn’t human-friendly and is hard to interpret quickly (e.g., it’s hard to guess when a user was created based on the ID alone).

Performance: While UUIDs are efficient to generate, they are 128-bit long, which can make indexing and storage less efficient compared to shorter numeric IDs.

3. UID Table + Locking (Advanced Scalable Solution)
How it Works:

UID Table: A dedicated table that tracks the last used sequence for each unique prefix (e.g., month/year).

This approach allows you to have control over sequential generation and sharding across different time periods (year/month) if needed.

Locking: To ensure the sequence is incremented safely under high concurrency, you may need to lock the row representing the current period (e.g., 2025-10). This prevents race conditions where two users could get the same UID.

Steps for Implementation:

Create UID Table:

CREATE TABLE uid_generator (
    prefix VARCHAR PRIMARY KEY,   -- E.g., "2510" for 2025 October
    last_number BIGINT            -- Last generated number
);


Insert or Update:

Before generating a UID, lock the row for the current month/year combination (e.g., 2510 for October 2025).

Increment last_number and then generate the UID based on this number.

Example of acquiring the next UID:

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public User createUser() {
        String yearMonthPrefix = getYearMonthPrefix();

        // Lock the row for the current month (e.g., '2510' for October 2025)
        String query = "SELECT last_number FROM uid_generator WHERE prefix = :prefix FOR UPDATE";
        Long lastNumber = ((Number) entityManager
            .createNativeQuery(query)
            .setParameter("prefix", yearMonthPrefix)
            .getSingleResult()).longValue();

        // Increment the last number
        Long newNumber = lastNumber + 1;

        // Update the table with the new number
        entityManager.createNativeQuery("UPDATE uid_generator SET last_number = :newNumber WHERE prefix = :prefix")
            .setParameter("newNumber", newNumber)
            .setParameter("prefix", yearMonthPrefix)
            .executeUpdate();

        // Generate the UID (e.g., "UID25100001")
        String uid = "UID" + yearMonthPrefix + String.format("%04d", newNumber);

        User user = new User();
        user.setUid(uid);
        return userRepository.save(user);
    }

    private String getYearMonthPrefix() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.format("%ty", now);
        String month = String.format("%02d", now.getMonthValue());
        return year + month;
    }
}


Advantages:

Fully Controlled: You can customize the UID format precisely.

Scalable: If you shard by prefix (e.g., per month or year), you can scale to handle millions of users.

Flexible: This design allows you to add additional logic, such as per-region or per-application UID generation.

Disadvantages:

Complex: More database queries and logic are required compared to the simpler sequence approach.

Locking: You need to ensure that the row is locked and updated atomically to avoid race conditions. This could introduce contention when   very high-load systems with millions of records and a need for precise control over UID format

For high scalability and precise control, DB Sequence + UID in Service is probably the best solution unless you need extreme scaling or plan to shard the UID generation by month or region, in which case the UID Table + Locking approach will shine.*/
