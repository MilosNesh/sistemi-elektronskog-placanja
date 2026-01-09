package org.example.pspbackend.service.impl;

import org.example.pspbackend.repository.TransactionRepository;
import org.example.pspbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
}
