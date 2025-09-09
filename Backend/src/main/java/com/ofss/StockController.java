package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRepository stockRepo;
    @Autowired
    private TransactionRepository txnRepo;


    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.saveStock(stock));
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Integer id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Integer id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        if (updatedStock != null) return ResponseEntity.ok(updatedStock);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Stock> patchStock(
      @PathVariable Integer id,
      @RequestBody Map<String, Object> updates) {
        Stock updatedStock = stockService.patchStock(id, updates);
        if (updatedStock != null) return ResponseEntity.ok(updatedStock);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Integer id) {
        if (stockService.deleteStock(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/transacted")
    public Map<String, String> transactedStock() {
        // FIX: Convert Iterable to List
        List<Transaction> txns = new ArrayList<>();
        txnRepo.findAll().forEach(txns::add);

        Map<String, Integer> counts = new HashMap<>();
        for (Transaction t : txns) {
            String stockName = t.getStock().getStockName();
            counts.put(stockName, counts.getOrDefault(stockName, 0) + 1);
        }
        String most = counts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        String least = counts.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        Map<String, String> result = new HashMap<>();
        result.put("mostTransacted", most);
        result.put("leastTransacted", least);
        return result;
    }

    @GetMapping("/highestPrice")
    public Stock highestPriceStock() {
        List<Stock> stocks = new ArrayList<>();
        stockRepo.findAll().forEach(stocks::add);

        return stocks.stream()
            .max(Comparator.comparingDouble(Stock::getCurrentPrice)).orElse(null);
    }

    @GetMapping("/all")
    public List<Stock> getAllStockReport() {
        List<Stock> stocks = new ArrayList<>();
        stockRepo.findAll().forEach(stocks::add);
        return stocks;
    }
}