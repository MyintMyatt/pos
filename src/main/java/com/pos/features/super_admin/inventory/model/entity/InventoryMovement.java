package com.pos.features.super_admin.inventory.model.entity;

import com.pos.constant.InventoryMovementType;
import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_inventory_movement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryMovementId;

    @Column(nullable = false)
    private Double quantityChange;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType inventoryMovementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_inventory_id", referencedColumnName = "inventory_id",nullable = false)
    private Inventory inventory;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;


}
