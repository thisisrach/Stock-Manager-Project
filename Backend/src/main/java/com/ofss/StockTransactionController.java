package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/stocks")
public class StockTransactionController {

    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private StockRepository stockRepository;
    @Autowired private TransactionRepository transactionRepository;

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestBody BuySellRequest req) {

        User user = userRepository.findByUsername(req.getUsername());
        if (user == null || !"CUSTOMER".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can buy stocks.");
        }
        Customer customer = customerRepository.findByUsername(req.getUsername());
        if (customer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");

        Stock stock = stockRepository.findById(req.getStockId()).orElse(null);
        if (stock == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock not found");


        if (stock.getAvailableQuantity() < req.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough stock available");
        }


        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setStock(stock);
        tx.setTxnType("BUY");
        tx.setTxnPrice(stock.getCurrentPrice());
        tx.setQuantity(req.getQuantity());
        tx.setTxnDate(LocalDate.now());
        transactionRepository.save(tx);


        stock.setAvailableQuantity(stock.getAvailableQuantity() - req.getQuantity());
        stockRepository.save(stock);

        return ResponseEntity.ok("Stock bought!"); // Success
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestBody BuySellRequest req) {
        User user = userRepository.findByUsername(req.getUsername());
        if (user == null || !"CUSTOMER".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can sell stocks.");
        }
        Customer customer = customerRepository.findByUsername(req.getUsername());
        if (customer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");

        Stock stock = stockRepository.findById(req.getStockId()).orElse(null);
        if (stock == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock not found");


        Integer holding = transactionRepository.getNetQuantityForCustomerAndStock(customer.getCustId(), stock.getStockId());
        if (holding == null) holding = 0;

        if (holding < req.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("You cannot sell more than you own. Current holding: " + holding);
        }


        Transaction tx = new Transaction();
        tx.setCustomer(customer);
        tx.setStock(stock);
        tx.setTxnType("SELL");
        tx.setTxnPrice(stock.getCurrentPrice());
        tx.setQuantity(req.getQuantity());
        tx.setTxnDate(LocalDate.now());
        transactionRepository.save(tx);

        stock.setAvailableQuantity(stock.getAvailableQuantity() + req.getQuantity());
        stockRepository.save(stock);

        return ResponseEntity.ok("Stock sold!");
    }
}
