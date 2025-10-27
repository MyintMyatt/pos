package com.pos.features.super_admin.menu_n_category.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.response.CategoryResponse;
import com.pos.features.super_admin.menu_n_category.repo.CategoryRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserService userService;

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        return convertCategoryToRes(
                categoryRepo.save(
                        convertReqToCreateCategory(request)
        ));
    }

    public CategoryResponse updateCategory(CategoryUpdateRequest obj) {
        Category category = categoryRepo.findById(obj.getCategoryId()).orElseThrow(() -> new NotFoundException("category not found with id : " + obj.getCategoryId()));
         return convertCategoryToRes(categoryRepo.save(convertReqToUpdateCategory(obj)));
    }

    public CategoryResponse getCategoryById(String categoryId){
        Category c = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id " + categoryId));
        return convertCategoryToRes(c);
    }

    public void deleteCategory(String categoryId){
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id : " + categoryId));// if category id doesn't exit, will be throw exception
        category.setDeleted(true);
        categoryRepo.save(category);
    }

    private Category convertReqToUpdateCategory(CategoryUpdateRequest obj) {
        com.pos.features.super_admin.menu_n_category.model.entity.Category category = categoryRepo.findById(obj.getCategoryId()).orElseThrow(() -> new NotFoundException("category not found with id : " + obj.getCategoryId()));
        User updatedUser = userService.getUser(obj.getActionBy());
        return Category
                .builder()
                .categoryId(obj.getCategoryId())
                .categoryName(obj.getCategoryName())
                .createdDate(category.getCreatedDate())
                .createdBy(category.getCreatedBy())
                .updatedBy(updatedUser)
                .updatedDate(LocalDate.now())
                .isDeleted(false)
                .build();
    }

    private Category convertReqToCreateCategory(CategoryCreateRequest obj) {
        User user = userService.getUser(obj.getActionBy());
        return Category
                .builder()
                .categoryName(obj.getCategoryName())
                .createdBy(user)
                .createdDate(LocalDate.now())
                .build();
    }

    private CategoryResponse convertCategoryToRes(Category obj) {
        return new CategoryResponse(obj.getCategoryId(), obj.getCategoryName(), obj.getCreatedDate(), obj.getCreatedBy().getUserId(),
                obj.getUpdatedDate(), obj.getUpdatedBy().getUserId());
    }
}
