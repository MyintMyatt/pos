package com.pos.features.super_admin.tax.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.tax.model.entity.Tax;
import com.pos.features.super_admin.tax.model.request.TaxRequest;
import com.pos.features.super_admin.tax.model.response.TaxResponse;
import com.pos.features.super_admin.tax.repo.TaxRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    /**
     * Runs every day at 00:00 to update tax active status.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    @Transactional
    public void updateTaxStatus() {
        List<Tax> taxes = taxRepo.findAll();
        LocalDate today = LocalDate.now();

        for (Tax tax : taxes) {
            boolean shouldBeActive = !today.isBefore(tax.getValidFromDate())
                    && (tax.getValidToDate() == null || !today.isAfter(tax.getValidToDate()));

            if (tax.isActive() != shouldBeActive) {
                tax.setActive(shouldBeActive);
                taxRepo.save(tax);
            }
        }
    }

    @Transactional
    public TaxResponse createTax(TaxRequest req) {
        if (req.getValidTo().isBefore(req.getValidFrom()) || req.getValidTo().isBefore(LocalDate.now()))
            throw new RuntimeException("invalid valid from date and valid to date for tax");

        List<Tax> taxList = taxRepo.findAll().stream().filter(t -> t.isActive()).collect(Collectors.toUnmodifiableList());

        if (taxList.size() > 0) throw new RuntimeException("there is already an active tax setup");

        Tax tax = taxRepo.save(convertRequestToTax(req, false));
        return convertTaxToResponse(tax);
    }


    @Transactional
    public Tax getTaxById(String taxId) {
        Tax tax = taxRepo.findById(taxId).orElseThrow(() -> new NotFoundException("tax not found: " + taxId));
        return tax;
    }

    @Transactional
    public List<TaxResponse> getAllTax() {
        return taxRepo.findAll()
                .stream()
                .map(this::convertTaxToResponse)
                .collect(Collectors.toList());
    }



    private TaxResponse convertTaxToResponse(Tax obj) {
        return TaxResponse.builder()
                .taxId(obj.getTaxId())
                .taxDesc(obj.getTaxDesc())
                .taxPercentage(obj.getTaxPercentage())
                .validFrom(obj.getValidFromDate())
                .validTo(obj.getValidToDate())
                .createdDate(obj.getCreatedDate())
                .createdBy(userService.convertUserToUserResponse(obj.getCreatedBy()))
//                .updatedDate(obj.getUpdatedDate())
//                .updatedBy(obj.getUpdatedBy())
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
//                .updatedDate(isUpdated ? LocalDate.now() : null)
//                .updatedBy(isUpdated ? user : null)
                .build();
    }
}
