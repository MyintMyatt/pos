package com.pos.features.super_admin.discount.util;

import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.discount.model.response.DiscountResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.time.LocalDate.class}, uses = {UserMapper.class})
public interface DiscountMapper {

    @Mapping(target = "discountType", source = "discountType")
    @Mapping(target = "discountValue", source = "discountValue")
    @Mapping(target = "validFrom", source = "validFrom")
    @Mapping(target = "validTo", source = "validTo")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedDate", ignore = true)
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Discount toEntity(DiscountRequest request);

    @Mapping(target = "discountId", source = "discountId")
    @Mapping(target = "discountType", source = "discountType")
    @Mapping(target = "discountValue", source = "discountValue")
    @Mapping(target = "validFrom", source = "validFrom")
    @Mapping(target = "validTo", source = "validTo")
//    @Mapping(target = "isDeleted", constant = "false")
//    @Mapping(target = "deletedDate", ignore = true)
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "updatedDate", source = "updatedDate")
    @Mapping(target = "updatedBy", qualifiedByName = "toSimpleUserResponse")
    DiscountResponse toResponse(Discount discount);
}
