package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Service
public class CryptoPaymentProvider implements PaymentProviderService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CryptoRateService cryptoRateService;
    @Autowired
    private CallMerchantApiService callMerchantApiService;
    private final Web3j web3j;
    private final Credentials credentials;
    public CryptoPaymentProvider(@Value("${eth.rpc.url}") String rpcUrl,
                             @Value("${eth.wallet.private-key}") String privateKey){
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
    }


    @Override
    public String processPayment(String transactionId, PaymentMethodDTO request) {

        Transaction transaction = transactionService.getById(transactionId);
        BigDecimal rsdAmount = BigDecimal.valueOf(transaction.getAmount());

        //BigDecimal ethAmount = cryptoRateService.advancedConvertRsdToEth(rsdAmount); // pretvaranje preko odnosa kursa u realnom vremenu
        BigDecimal ethAmount = cryptoRateService.convertRsdToEth(rsdAmount);

        TransactionReceipt receipt = null;
        try {
            receipt = Transfer.sendFunds(
                    web3j,
                    credentials,
                    "0xEbC45d552E0947cFf740426A25fD1A3f50CaCf7e",
                    ethAmount,
                    Convert.Unit.ETHER
            ).send();

            System.out.println("TRANSACTION STATUS: " + receipt.getStatus());
            String status = receipt.getStatus().equals("0x1") ? "SUCCESS" : "FAILED";

            transaction.setStatus(status);
            transactionService.save(transaction);

            Merchant merchant = transaction.getMerchant();
            if(merchant == null){
                return null;
            }
            if(status.equals("SUCCESS"))
                callMerchantApiService.notifyPaymentSuccess(merchant.getSuccessUrl(), transaction.getMerchantOrderId());
            else
                callMerchantApiService.notifyPaymentFailed(merchant.getFailedUrl(), transaction.getMerchantOrderId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "https://sepolia.etherscan.io/tx/" + receipt.getTransactionHash();
    }

    @Override
    public String getSupportedPaymentType() {
        return "CRYPTO";
    }
}
