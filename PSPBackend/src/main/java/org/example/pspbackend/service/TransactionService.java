package org.example.pspbackend.service;

import org.example.pspbackend.domain.Transaction;

import java.util.Date;

public interface TransactionService {
    public Transaction getById(String id);

    public void save(Transaction transaction);

    public Transaction get(String stan, Long merchantId, Date pspTimestamp);
}
