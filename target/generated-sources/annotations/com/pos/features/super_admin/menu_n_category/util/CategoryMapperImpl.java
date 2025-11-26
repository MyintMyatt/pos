package com.pos.features.super_admin.menu_n_category.util;

import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.response.CategoryFullResponse;
import com.pos.features.super_admin.menu_n_category.model.response.CategorySimpleResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T15:03:35+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CategoryFullResponse toFullResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        String categoryId = null;
        String categoryName = null;
        UserSimpleResponse createdBy = null;
        UserSimpleResponse updatedBy = null;

        categoryId = category.getCategoryId();
        categoryName = category.getCategoryName();
        createdBy = userMapper.toSimpleResponse( category.getCreatedBy() );
        updatedBy = userMapper.toSimpleResponse( category.getUpdatedBy() );

        String createdDate = category.getCreatedDate() != null ? category.getCreatedDate().toString() : null;
        String updatedDate = category.getUpdatedDate() != null ? category.getUpdatedDate().toString() : null;

        CategoryFullResponse categoryFullResponse = new CategoryFullResponse( categoryId, categoryName, createdBy, createdDate, updatedBy, updatedDate );

        return categoryFullResponse;
    }

    @Override
    public CategorySimpleResponse toSimpleResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        String categoryId = null;
        String categoryName = null;

        categoryId = category.getCategoryId();
        categoryName = category.getCategoryName();

        CategorySimpleResponse categorySimpleResponse = new CategorySimpleResponse( categoryId, categoryName );

        return categorySimpleResponse;
    }
}
