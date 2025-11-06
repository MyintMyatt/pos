package com.pos.features.super_admin.tax.service;

import com.pos.features.super_admin.tax.model.entity.Tax;
import com.pos.features.super_admin.tax.model.request.TaxRequest;
import com.pos.features.super_admin.tax.model.response.TaxResponse;
import com.pos.features.super_admin.tax.repo.TaxRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxService {

    @Autowired
    private TaxRepo taxRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public TaxResponse createTax(TaxRequest req) {
        if (req.getValidTo().isBefore(req.getValidFrom()) || req.getValidTo().isBefore(LocalDate.now()))
            throw new RuntimeException("invalid valid from date and valid to date for tax");

        List<Tax> taxList = taxRepo.findAll().stream().filter(t -> t.isActive()).collect(Collectors.toUnmodifiableList());

        if (taxList.size() > 0) throw new RuntimeException("there is already an active tax setup");

        Tax tax = taxRepo.save(convertRequestToTax(req, false));
        return convertTaxToResponse(tax);
    }

//    @Transactional
//    public Tax updateTax(String taxId, TaxRequest req) {
//        if (getDiscount(disId) == null)
//            throw new NotFoundException("Discount not found with id " + disId);
//        return convertDiscountToDisResponse(discountRepo.save(convertDisRequestToDiscount(req, true)));
//    }

    @Transactional
    public Tax getTax() {
        return taxRepo.findAll().get(0);
    }

    @Transactional
    public List<TaxResponse> getAllTax() {
        return taxRepo.findAll()
                .stream()
                .map(this::convertTaxToResponse)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void deleteDiscount(String disId){
//        Discount dis = discountRepo.findById(disId).orElseThrow(() -> new NotFoundException("Discount not found with id " + disId));
//        dis.setDeleted(true);
//        dis.setDeletedDate(LocalDate.now());
//        discountRepo.save(dis);
//    }

    private TaxResponse convertTaxToResponse(Tax obj) {
        return TaxResponse.builder()
                .taxId(obj.getTaxId())
                .taxDesc(obj.getTaxDesc())
                .taxPercentage(obj.getTaxPercentage())
                .validFrom(obj.getValidFromDate())
                .validTo(obj.getValidToDate())
                .createdDate(obj.getCreatedDate())
                .createdBy(obj.getCreatedBy())
                .updatedDate(obj.getUpdatedDate())
                .updatedBy(obj.getUpdatedBy())
                .build();
    }

    private Tax convertRequestToTax(TaxRequest obj, boolean isUpdated) {
        User user = userService.getUser(obj.getUserId());
        return Tax.builder()
                .taxDesc(obj.getTaxDesc())
                .taxPercentage(obj.getTaxPercentage())
                .validFromDate(obj.getValidFrom())
                .validToDate(obj.getValidTo())
                .createdDate(LocalDate.now())
                .createdBy(user)
                .updatedDate(isUpdated ? LocalDate.now() : null)
                .updatedBy(isUpdated ? user : null)
                .build();
    }
}
