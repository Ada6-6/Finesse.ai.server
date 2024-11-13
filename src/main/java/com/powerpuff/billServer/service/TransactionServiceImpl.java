package com.powerpuff.billServer.service;

import com.powerpuff.billServer.model.Transaction;
import com.powerpuff.billServer.model.UsingType;
import com.powerpuff.billServer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransaction(String sortOrder, String category, String transactionType) {
        // Check if transactionType is provided; if not, return all types
        if (category == null || category.isEmpty()) {
            if (transactionType == null || transactionType.isEmpty()) {
                // No transactionType filter
                if (sortOrder == null || sortOrder.isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
                    return transactionRepository.findByUsingTypeNotOrderByCreatedAtDesc(UsingType.DELETED);
                } else {
                    return transactionRepository.findByUsingTypeNotOrderByCreatedAtAsc(UsingType.DELETED);
                }
            } else {
                // Filter by transactionType
                if (sortOrder == null || sortOrder.isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
                    return transactionRepository.findByUsingTypeNotAndTransactionTypeOrderByCreatedAtDesc(UsingType.DELETED, transactionType);
                } else {
                    return transactionRepository.findByUsingTypeNotAndTransactionTypeOrderByCreatedAtAsc(UsingType.DELETED, transactionType);
                }
            }
        } else {
            // Category-specific filtering with transactionType
            if (transactionType == null || transactionType.isEmpty()) {
                if (sortOrder == null || sortOrder.isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
                    return transactionRepository.findByUsingTypeNotAndCategoryOrderByCreatedAtDesc(UsingType.DELETED, category);
                } else {
                    return transactionRepository.findByUsingTypeNotAndCategoryOrderByCreatedAtAsc(UsingType.DELETED, category);
                }
            } else {
                // Category and transactionType-specific filtering
                if (sortOrder == null || sortOrder.isEmpty() || sortOrder.equalsIgnoreCase("desc")) {
                    return transactionRepository.findByUsingTypeNotAndCategoryAndTransactionTypeOrderByCreatedAtDesc(UsingType.DELETED, category, transactionType);
                } else {
                    return transactionRepository.findByUsingTypeNotAndCategoryAndTransactionTypeOrderByCreatedAtAsc(UsingType.DELETED, category, transactionType);
                }
            }
        }
    }

    @Override
    public String deleteTransaction(Integer id){
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if(transaction != null){
            transaction.setUsingType(UsingType.DELETED);
            transactionRepository.save(transaction);
            return "Deleted successfully.";
        }else {
            return "can not find this record.";
        }
    }
}
