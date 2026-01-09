package org.example.webshopbackend.service;

import org.example.webshopbackend.dto.MerchantRequest;

public interface CallApiService {
    public String callApi(MerchantRequest merchantRequest);
}
