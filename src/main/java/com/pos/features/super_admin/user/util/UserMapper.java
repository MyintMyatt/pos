package com.pos.features.super_admin.user.util;

import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("toFullUserResponse")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userEmail", source = "userEmail")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "permissions", source = "permissions")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "profileImgUrl", source = "profileImgUrl")
    @Mapping(target = "isAccountIsActive", source = "accountIsActive")
    @Mapping(target = "isAccountNotLocked", source = "accountNotLocked")
    UserResponse toFullResponse(User user);

    @Named("toSimpleUserResponse")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userName", source = "userName")
//    @Mapping(target = "userEmail", ignore = true)
//    @Mapping(target = "role", ignore = true)
//    @Mapping(target = "permissions", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "profileImgUrl", ignore = true)
//    @Mapping(target = "isAccountIsActive", ignore = true)
//    @Mapping(target = "isAccountNotLocked", ignore = true)
    UserSimpleResponse toSimpleResponse(User user);
}
