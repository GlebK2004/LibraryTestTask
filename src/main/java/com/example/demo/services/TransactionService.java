package com.example.demo.services;

import com.example.demo.model.Book;
import com.example.demo.model.Reader;
import com.example.demo.model.Transaction;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.ReaderRepository;
import com.example.demo.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final ReaderRepository readerRepo;
    private final BookRepository bookRepo;

    public TransactionService(TransactionRepository transactionRepo, ReaderRepository readerRepo, BookRepository bookRepo) {
        this.transactionRepo = transactionRepo;
        this.readerRepo = readerRepo;
        this.bookRepo = bookRepo;
    }

    public String processTransaction(String readerPhone, Long bookId, Transaction.OperationType type) {
        Reader reader = readerRepo.findById(readerPhone).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setReader(reader);
        transaction.setBook(book);
        transaction.setOperationType(type);
        transaction.setDateTime(LocalDateTime.now());

        transactionRepo.save(transaction);
        return "Transaction successful: " + type;
    }
}
