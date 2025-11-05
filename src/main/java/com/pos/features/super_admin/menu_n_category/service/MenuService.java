package com.pos.features.super_admin.menu_n_category.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.request.MenuCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.repo.MenuRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class MenuService {

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Transactional
    public MenuResponse createMenu(MenuCreateRequest obj){
       Category c =  categoryService.getCategoryObjById(obj.getCategoryId());
       User u = userService.getUser(obj.getCategoryId());
       return convertObjToRes(menuRepo.save(convertCreateReqToMenu(obj, c, u)));
    }

    @Transactional
    public MenuResponse updateMenu(String menuId, MenuUpdateRequest obj){
        User updatedBy = userService.getUser(obj.getCategoryId());
        MenuItem existingMenu = getMenuItemById(menuId);
        Category category;

        // if update category, we need to update this
        if (existingMenu.getCategory().getCategoryId() != obj.getCategoryId()){
            category = categoryService.getCategoryObjById(obj.getCategoryId());
            existingMenu.setCategory(category);
        }
        return convertObjToRes(menuRepo.save(convertUpdateReqToMenu(obj,existingMenu, updatedBy)));
    }

    @Transactional
    public MenuItem getMenuItemById(String menuId){
        return menuRepo.findById(menuId).orElseThrow(
                () -> new NotFoundException("menu not found with id " + menuId)
        );
    }

    @Transactional
    public MenuResponse getMenuResponseById(String menuId){
        return convertObjToRes(getMenuItemById(menuId));
    }

    @Transactional
    public void  deleteMenu(String menuId){
        MenuItem menu = getMenuItemById(menuId);
        menuRepo.save(menu);
    }

    @Transactional
    public MenuResponse updateMenuImage(String menuId, String imageUrl){
        MenuItem menuItem = getMenuItemById(menuId);
        menuItem.setMenuImageUrl(imageUrl);
        return convertObjToRes(menuRepo.save(menuItem));
    }

    @Transactional
    public List<MenuResponse> getAllMenu(){
        return menuRepo.findAll()
                .stream().filter(m -> !m.isDeleted())
                .map(this::convertObjToRes)
                .toList();
    }

    private MenuItem convertCreateReqToMenu(MenuCreateRequest obj, Category category, User user){
        return MenuItem.builder()
                .menuName(obj.getMenuName())
                .price(obj.getPrice())
                .category(category)
                .isThereDiscount(false)
                .description(obj.getDescription())
                .createdDate(LocalDate.now())
                .createdBy(user)
                .isDeleted(false)
                .build();
    }

    private MenuItem convertUpdateReqToMenu(MenuUpdateRequest obj,MenuItem existing, User updatedBy){
        return MenuItem.builder()
                .menuName(obj.getMenuName())
                .price(obj.getPrice())
                .category(existing.getCategory())
                .isThereDiscount(false)
                .description(obj.getDescription())
                .createdDate(existing.getCreatedDate())
                .createdBy(existing.getCreatedBy())
                .updatedBy(updatedBy)
                .updatedDate(LocalDate.now())
                .isDeleted(false)
                .build();
    }

    private MenuResponse convertObjToRes(MenuItem obj){
        return new MenuResponse(
                obj.getMenuId(),
                obj.getMenuName(),
                obj.getPrice(),
                obj.getCategory(),
                obj.getMenuImageUrl(),
                obj.isThereDiscount(),
                obj.getDescription(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getUpdatedBy(),
                obj.getUpdatedDate()
        );

    }
}
