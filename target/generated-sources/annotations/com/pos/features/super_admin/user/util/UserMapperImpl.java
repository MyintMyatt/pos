package com.pos.features.super_admin.user.util;

import com.pos.constant.Permission;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T16:05:06+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toFullResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.userId( user.getUserId() );
        userResponse.userEmail( user.getUserEmail() );
        userResponse.userName( user.getUserName() );
        userResponse.role( user.getRole() );
        Set<Permission> set = user.getPermissions();
        if ( set != null ) {
            userResponse.permissions( new LinkedHashSet<Permission>( set ) );
        }
        userResponse.createdDate( user.getCreatedDate() );
        userResponse.profileImgUrl( user.getProfileImgUrl() );
        userResponse.isAccountIsActive( user.isAccountIsActive() );
        userResponse.isAccountNotLocked( user.isAccountNotLocked() );

        return userResponse.build();
    }

    @Override
    public UserSimpleResponse toSimpleResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String userId = null;
        String userName = null;

        userId = user.getUserId();
        userName = user.getUserName();

        UserSimpleResponse userSimpleResponse = new UserSimpleResponse( userId, userName );

        return userSimpleResponse;
    }
}
