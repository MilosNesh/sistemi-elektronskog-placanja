package org.example.pspbackend.service;

import org.example.pspbackend.domain.Transaction;

import java.time.LocalDateTime;

public interface TransactionService {
    public Transaction getById(String id);

    public void save(Transaction transaction);

    public Transaction getByStan(String stan);

    public Transaction get(String stan, Long merchantId, LocalDateTime pspTimestamp);
}
