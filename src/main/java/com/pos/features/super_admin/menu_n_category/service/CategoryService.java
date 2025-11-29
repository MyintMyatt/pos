package com.pos.features.super_admin.menu_n_category.service;

import com.pos.common.service.SecurityService;
import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryRequest;
import com.pos.features.super_admin.menu_n_category.model.response.CategoryFullResponse;
import com.pos.features.super_admin.menu_n_category.repo.CategoryRepo;
import com.pos.features.super_admin.menu_n_category.util.CategoryMapper;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SecurityService securityService;

    @Transactional
    public CategoryFullResponse createCategory(CategoryRequest request) {
        return categoryMapper.toFullResponse(
                categoryRepo.save(
                        convertReqToCreateCategory(request)
                ));
    }

    @Transactional
    public CategoryFullResponse updateCategory(String categoryId, CategoryRequest obj) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id : " + categoryId));

        /*
         * @ get current login user
         * */
        String currentLoginUser = securityService.getCurrentLoginUserId();
        User currentUser = userService.getUser(currentLoginUser);

        category.setCategoryName(obj.getCategoryName());
        category.setUpdatedBy(currentUser);
        category.setUpdatedDate(LocalDate.now());

        return categoryMapper.toFullResponse(categoryRepo.save(category));
    }

    @Transactional
    public CategoryFullResponse getCategoryById(String categoryId) {
        Category c = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id " + categoryId));
        return categoryMapper.toFullResponse(c);
    }

    @Transactional
    public Category getCategoryObjById(String categoryId) {
        Category c = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id " + categoryId));
        return c;
    }

    @Transactional
    public List<CategoryFullResponse> getAllCat() {
        return categoryRepo.findAll().stream()
                .filter(c -> !c.isDeleted())
                .map(categoryMapper::toFullResponse)
                .toList();
    }

    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("category not found with id : " + categoryId));// if category id doesn't exit, will be throw exception
        category.setDeleted(true);
        categoryRepo.save(category);
    }


    private Category convertReqToCreateCategory(CategoryRequest obj) {
        /*
        * @ get current login user
        * */
        String currentLoginUser = securityService.getCurrentLoginUserId();
        User user = userService.getUser(currentLoginUser);

        return Category
                .builder()
                .categoryName(obj.getCategoryName())
                .createdBy(user)
                .createdDate(LocalDate.now())
                .build();
    }



}
