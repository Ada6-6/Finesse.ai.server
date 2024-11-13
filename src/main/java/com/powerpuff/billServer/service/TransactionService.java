package com.powerpuff.billServer.service;

import com.powerpuff.billServer.model.Transaction;

import java.util.List;

public interface TransactionService {
    public Transaction saveTransaction(Transaction transaction);

    public List<Transaction> getAllTransaction(String sortOrder, String category, String transactionType);

    String deleteTransaction(Integer id);
}
