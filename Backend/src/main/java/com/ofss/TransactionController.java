package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository txnRepo;


    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.saveTransaction(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsService() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Integer id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        if (transactionService.deleteTransaction(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactionReport() {
        List<Transaction> txns = new ArrayList<>();
        txnRepo.findAll().forEach(txns::add);
        return txns;
    }


    @GetMapping("/moreType")
    public String moreTxnType() {
        List<Transaction> txns = new ArrayList<>();
        txnRepo.findAll().forEach(txns::add);
        long buys = txns.stream().filter(t -> t.getTxnType().equalsIgnoreCase("BUY")).count();
        long sells = txns.size() - buys;
        if (buys > sells) return "BUY";
        if (sells > buys) return "SELL";
        return "BUY = SELL";
    }
}