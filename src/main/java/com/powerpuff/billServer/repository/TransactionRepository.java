package com.powerpuff.billServer.repository;

import com.powerpuff.billServer.model.Transaction;
import com.powerpuff.billServer.model.UsingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    // sorting by create time //desc or asc
    List<Transaction> findByUsingTypeNotOrderByCreatedAtDesc(UsingType usingType);
    List<Transaction> findByUsingTypeNotOrderByCreatedAtAsc(UsingType usingType);


    // find transactions by category, usingType is not equal to DELETED (value 3), sorting by create time //desc or asc
    List<Transaction> findByUsingTypeNotAndCategoryOrderByCreatedAtDesc(UsingType usingType, String category);

    List<Transaction> findByUsingTypeNotAndCategoryOrderByCreatedAtAsc(UsingType usingType, String category);


    //filtering transactions by transactionType (income or outcome), usingType is not equal to DELETED (value 3),
    List<Transaction> findByUsingTypeNotAndTransactionTypeOrderByCreatedAtDesc(UsingType usingType, String transactionType);
    List<Transaction> findByUsingTypeNotAndTransactionTypeOrderByCreatedAtAsc(UsingType usingType, String transactionType);


    List<Transaction> findByUsingTypeNotAndCategoryAndTransactionTypeOrderByCreatedAtDesc(UsingType usingType, String category, String transactionType);
    List<Transaction> findByUsingTypeNotAndCategoryAndTransactionTypeOrderByCreatedAtAsc(UsingType usingType, String category, String transactionType);

}
