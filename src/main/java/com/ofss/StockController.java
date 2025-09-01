package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

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

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Integer id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        }
        return ResponseEntity.notFound().build();
    }

    // PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<Stock> patchStock(
        @PathVariable Integer id,
        @RequestBody Map<String, Object> updates) {

        Stock updatedStock = stockService.patchStock(id, updates);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Integer id) {
        if (stockService.deleteStock(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

