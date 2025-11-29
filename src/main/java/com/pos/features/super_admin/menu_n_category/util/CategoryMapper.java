package com.pos.features.super_admin.menu_n_category.util;

import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.response.CategoryFullResponse;
import com.pos.features.super_admin.menu_n_category.model.response.CategorySimpleResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CategoryMapper {

    @Named("toFullCategoryResponse")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "categoryName", source = "categoryName")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "createdDate", expression = "java(category.getCreatedDate() != null ? category.getCreatedDate().toString() : null)")
    @Mapping(target = "updatedBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "updatedDate", expression = "java(category.getUpdatedDate() != null ? category.getUpdatedDate().toString() : null)")
    CategoryFullResponse toFullResponse(Category category);

    @Named("toSimpleCategoryResponse")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "categoryName", source = "categoryName")
    CategorySimpleResponse toSimpleResponse(Category category);
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)


}
