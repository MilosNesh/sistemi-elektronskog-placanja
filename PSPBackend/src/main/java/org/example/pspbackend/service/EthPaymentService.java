package org.example.pspbackend.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class EthPaymentService {
    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${eth.wallet.address}")
    private String fromAddress;

    public EthPaymentService(@Value("${eth.rpc.url}") String rpcUrl,
                             @Value("${eth.wallet.private-key}") String privateKey){
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
    }

    public String sendEth(String toAddress, BigDecimal ethAmount) throws Exception{
        TransactionReceipt receipt = Transfer.sendFunds(
                web3j,
                credentials,
                toAddress,
                ethAmount,
                Convert.Unit.ETHER
        ).send();


        return receipt.getTransactionHash();
    }

    public BigInteger getBalance() throws Exception{
        return web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).send().getBalance();
    }
}
