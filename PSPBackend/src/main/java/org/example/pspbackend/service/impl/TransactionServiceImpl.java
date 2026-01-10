package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.repository.TransactionRepository;
import org.example.pspbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getById(String id) {
        return transactionRepository.getById(id);
    }

    public void save(Transaction transaction) { transactionRepository.save(transaction); }

    public Transaction get(String stan, Long merchantId, Date pspTimestamp) { return transactionRepository.find(stan, merchantId, pspTimestamp); }
}
