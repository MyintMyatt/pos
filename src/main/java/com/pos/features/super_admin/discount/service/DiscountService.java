package com.pos.features.super_admin.discount.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.discount.model.response.DiscountResponse;
import com.pos.features.super_admin.discount.repo.DiscountRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public DiscountResponse createDiscount(DiscountRequest req){
       Discount dis =  discountRepo.save(convertDisRequestToDiscount(req, false));
       return convertDiscountToDisResponse(dis);
    }

    @Transactional
    public DiscountResponse updateDiscount(String disId, DiscountRequest req){
        if (getDiscount(disId) == null)
            throw new NotFoundException("Discount not found with id " + disId);
        return convertDiscountToDisResponse(discountRepo.save(convertDisRequestToDiscount(req, true)));
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
                .map(this::convertDiscountToDisResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDiscount(String disId){
        Discount dis = discountRepo.findById(disId).orElseThrow(() -> new NotFoundException("Discount not found with id " + disId));
        dis.setDeleted(true);
        dis.setDeletedDate(LocalDate.now());
        discountRepo.save(dis);
    }

    private DiscountResponse convertDiscountToDisResponse(Discount obj) {
        return DiscountResponse.builder()
                .discountId(obj.getDiscountId())
                .discountType(obj.getDiscountType())
                .discountValue(obj.getDiscountValue())
                .validFrom(obj.getValidFrom())
                .validTo(obj.getValidTo())
                .createdDate(obj.getCreatedDate())
                .createdBy(obj.getCreatedBy())
                .updatedDate(obj.getUpdatedDate())
                .updatedBy(obj.getUpdatedBy())
                .build();
    }

    private Discount convertDisRequestToDiscount(DiscountRequest obj, boolean isUpdated) {
        User user = userService.getUser(obj.getUserId());
        return Discount.builder()
                .discountType(obj.getDiscountType())
                .discountValue(obj.getDiscountValue() > 0 ? obj.getDiscountValue() : 0)
                .validFrom(obj.getValidFrom())
                .validTo(obj.getValidTo())
                .isDeleted(false)
                .createdDate(LocalDate.now())
                .createdBy(user)
                .updatedDate( isUpdated ? LocalDate.now() : null)
                .updatedBy(isUpdated ? user : null)
                .build();
    }
}
