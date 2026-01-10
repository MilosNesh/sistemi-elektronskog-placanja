package org.example.pspbackend.service;

public interface CallMerchantApiService
{
    public void notifyPaymentSuccess(String successUrl);
    public void notifyPaymentFailed(String failedUrl);
    public void notifyPaymentError(String errorUrl);
}
