package com.pos.features.super_admin.discount.service;

import com.pos.common.service.SecurityService;
import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.discount.model.response.DiscountResponse;
import com.pos.features.super_admin.discount.repo.DiscountRepo;
import com.pos.features.super_admin.discount.util.DiscountMapper;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepo discountRepo;

    private final UserService userService;

    private final DiscountMapper discountMapper;

    private final SecurityService securityService;

    @Transactional
    public DiscountResponse createDiscount(DiscountRequest req){
        /*
        * @ check valid to date is valid or not
        * */
        if (req.getValidTo().isBefore(LocalDate.now()) || req.getValidTo().isBefore(req.getValidFrom()))
            throw new RuntimeException("Invalid valid to date");
        /*
        *  @ get current login user
        * */
        String currentLoginUserId = securityService.getCurrentLoginUserId();
        User currentLoginUser = userService.getUser(currentLoginUserId);

        Discount discount = discountMapper.toEntity(req);
        discount.setCreatedBy(currentLoginUser);

        return discountMapper.toResponse(discountRepo.save(discount));
    }

    @Transactional
    public DiscountResponse updateDiscount(String disId, DiscountRequest req){
        /*
         * @ check valid to date is valid or not
         * */
        if (req.getValidTo().isBefore(LocalDate.now()) || req.getValidTo().isBefore(req.getValidFrom()))
            throw new RuntimeException("Invalid valid to date");
        /*
         *  @ get current login user
         * */
        String currentLoginUserId = securityService.getCurrentLoginUserId();
        User currentLoginUser = userService.getUser(currentLoginUserId);

        Discount existingDiscount = getDiscount(disId);
        existingDiscount.setUpdatedBy(currentLoginUser);
        existingDiscount.setUpdatedDate(LocalDate.now());
        existingDiscount.setDiscountType(req.getDiscountType());
        existingDiscount.setDiscountValue(req.getDiscountValue());
        existingDiscount.setValidFrom(req.getValidFrom());
        existingDiscount.setValidTo(req.getValidTo());

        return discountMapper.toResponse(discountRepo.save(existingDiscount));
    }

    @Transactional
    public Discount getDiscount(String disId){
       return discountRepo.findById(disId).orElseThrow(() -> new NotFoundException("Discount not found with id " + disId));
    }

    @Transactional
    public List<DiscountResponse> getAllDiscount(){
        return discountRepo.findAll()
                .stream()
                .filter(d -> !d.isDeleted())
                .map(discountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDiscount(String disId){
        Discount dis = discountRepo.findById(disId).orElseThrow(() -> new NotFoundException("Discount not found with id " + disId));
        dis.setDeleted(true);
        dis.setDeletedDate(LocalDate.now());
        discountRepo.save(dis);
    }



}
