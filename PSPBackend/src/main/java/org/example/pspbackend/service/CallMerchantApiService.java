package org.example.pspbackend.service;

public interface CallMerchantApiService
{
    public void notifyPaymentSuccess(String successUrl, String merchantOrderId);
    public void notifyPaymentFailed(String failedUrl, String merchantOrderId);
    public void notifyPaymentError(String errorUrl, String merchantOrderId);
}
