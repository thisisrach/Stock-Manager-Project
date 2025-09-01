package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return (List<Stock>) stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Integer id) {
        return stockRepository.findById(id);
    }

    // PUT
    public Stock updateStock(Integer id, Stock stockDetails) {
        return stockRepository.findById(id).map(stock -> {
            stock.setStockName(stockDetails.getStockName());
            stock.setCurrentPrice(stockDetails.getCurrentPrice());
            stock.setListingPrice(stockDetails.getListingPrice());
            stock.setAvailableQuantity(stockDetails.getAvailableQuantity());
            return stockRepository.save(stock);
        }).orElse(null);
    }

    // PATCH
    public Stock patchStock(Integer id, Map<String, Object> updates) {
        return stockRepository.findById(id).map(stock -> {
            if (updates.containsKey("stockName")) {
                stock.setStockName((String) updates.get("stockName"));
            }
            if (updates.containsKey("listingPrice")) {
                stock.setListingPrice(Double.valueOf(updates.get("listingPrice").toString()));
            }
            if (updates.containsKey("currentPrice")) {
                stock.setCurrentPrice(Double.valueOf(updates.get("currentPrice").toString()));
            }
            if (updates.containsKey("availableQuantity")) {
                stock.setAvailableQuantity(Integer.valueOf(updates.get("availableQuantity").toString()));
            }
            if (updates.containsKey("listingDate")) {
                stock.setListingDate(LocalDate.parse((String) updates.get("listingDate")));
            }
            if (updates.containsKey("exchange")) {
                stock.setExchange((String) updates.get("exchange"));
            }
            if (updates.containsKey("status")) {
                stock.setStatus((String) updates.get("status"));
            }
            return stockRepository.save(stock);
        }).orElse(null);
    }
    
    public boolean deleteStock(Integer id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

