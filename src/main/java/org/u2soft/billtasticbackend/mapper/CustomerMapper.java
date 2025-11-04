package org.u2soft.billtasticbackend.mapper;

import org.u2soft.billtasticbackend.dto.CustomerDto;
import org.u2soft.billtasticbackend.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {}

    public static CustomerDto toDto(Customer entity) {
        if (entity == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setContactName(entity.getContactName());
        dto.setInvoiceEmail(entity.getInvoiceEmail());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setEuroAmount(entity.getEuroAmount());
        dto.setDollarAmount(entity.getDollarAmount());
        dto.setTlAmount(entity.getTlAmount());
        dto.setPriority(entity.getPriority());
        dto.setReceivableTotal(entity.getReceivableTotal());


        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer entity = new Customer();
        entity.setId(dto.getId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setContactName(dto.getContactName());
        entity.setInvoiceEmail(dto.getInvoiceEmail());
        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setEuroAmount(dto.getEuroAmount());
        entity.setDollarAmount(dto.getDollarAmount());
        entity.setTlAmount(dto.getTlAmount());
        entity.setPriority(dto.getPriority());
        entity.setReceivableTotal(dto.getReceivableTotal());

        return entity;
    }
}
