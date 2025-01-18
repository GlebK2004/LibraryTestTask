package com.example.demo.controllers;

import com.example.demo.model.Author;
import com.example.demo.model.Reader;
import com.example.demo.model.Transaction;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.services.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepo;

    public LibraryController(TransactionService transactionService, TransactionRepository transactionRepo) {
        this.transactionService = transactionService;
        this.transactionRepo = transactionRepo;
    }

    // Метод который осуществит транзакцию с книгой. Передавать ID клиента и книги
    @PostMapping("/transaction")
    public String transaction(@RequestParam String readerPhone, @RequestParam Long bookId, @RequestParam String type) {
        Transaction.OperationType operationType = Transaction.OperationType.valueOf(type.toUpperCase());
        return transactionService.processTransaction(readerPhone, bookId, operationType);
    }

    //Метод который покажет “самого популярного” автора за определенный диапазон дат (с…по)
    @GetMapping("/popular-author")
    public Author getPopularAuthor(@RequestParam String from, @RequestParam String to) {
        LocalDateTime start = LocalDateTime.parse(from);
        LocalDateTime end = LocalDateTime.parse(to);

        return transactionRepo.findAll().stream()
                .filter(t -> t.getOperationType() == Transaction.OperationType.BORROW)
                .filter(t -> !t.getDateTime().isBefore(start) && !t.getDateTime().isAfter(end))
                .flatMap(t -> t.getBook().getAuthors().stream())
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    // Метод который покажет самого читающего клиента (кто больше взял книг)
    @GetMapping("/main-reader")
    public Reader getTopReader() {
        return transactionRepo.findAll().stream()
                .filter(t -> t.getOperationType() == Transaction.OperationType.BORROW)
                .collect(Collectors.groupingBy(Transaction::getReader, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    // Метод который вернет список всех читателей отсортированных по убыванию количества не сданных книг
    @GetMapping("/debtors")
    public List<Reader> getDebtors() {
        Map<Reader, Long> borrowed = transactionRepo.findAll().stream()
                .filter(t -> t.getOperationType() == Transaction.OperationType.BORROW)
                .collect(Collectors.groupingBy(Transaction::getReader, Collectors.counting()));

        Map<Reader, Long> returned = transactionRepo.findAll().stream()
                .filter(t -> t.getOperationType() == Transaction.OperationType.RETURN)
                .collect(Collectors.groupingBy(Transaction::getReader, Collectors.counting()));

        return borrowed.entrySet().stream()
                .filter(e -> borrowed.getOrDefault(e.getKey(), 0L) > returned.getOrDefault(e.getKey(), 0L))
                .sorted(Map.Entry.<Reader, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
