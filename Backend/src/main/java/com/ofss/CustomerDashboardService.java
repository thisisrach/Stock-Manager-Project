package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerDashboardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, Object> getDashboardForCustomer(String username) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) throw new RuntimeException("Customer not found");

        List<Transaction> transactions = transactionRepository.findByCustomer_CustId(customer.getCustId());

        Map<String, StockHoldingSummary> holdingSummary = new HashMap<>();

        for (Transaction tx : transactions) {
            String stockName = tx.getStock() != null ? tx.getStock().getStockName() : "UNKNOWN";
            Stock stock = tx.getStock();
            if (!holdingSummary.containsKey(stockName)) {
                holdingSummary.put(stockName, new StockHoldingSummary(stockName, 0, 0.0, stock != null ? stock.getCurrentPrice() : 0.0));
            }
            StockHoldingSummary summary = holdingSummary.get(stockName);
            if ("BUY".equalsIgnoreCase(tx.getTxnType())) {
                summary.quantity += tx.getQuantity();
            } else if ("SELL".equalsIgnoreCase(tx.getTxnType())) {
                summary.quantity -= tx.getQuantity();
            }
            summary.currentPrice = stock != null ? stock.getCurrentPrice() : 0.0;
        }

        List<Map<String, Object>> holdings = new ArrayList<>();
        int totalStocks = 0;
        double portfolioValue = 0.0;

        for (StockHoldingSummary summary : holdingSummary.values()) {
            if (summary.quantity > 0) {
                Map<String, Object> stockInfo = new HashMap<>();
                stockInfo.put("stock", summary.stockName);
                stockInfo.put("quantity", summary.quantity);
                stockInfo.put("currentPrice", summary.currentPrice);
                stockInfo.put("value", summary.quantity * summary.currentPrice);

                holdings.add(stockInfo);
                totalStocks += summary.quantity;
                portfolioValue += summary.quantity * summary.currentPrice;
            }
        }

        String lastTransaction = transactions.stream()
                .map(Transaction::getTxnDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .map(Object::toString)
                .orElse(null);

        Map<String, Object> accountSummary = new HashMap<>();
        accountSummary.put("totalStocks", totalStocks);
        accountSummary.put("lastTransaction", lastTransaction);
        accountSummary.put("portfolioValue", portfolioValue);
        accountSummary.put("holdings", holdings); 

        List<Map<String, Object>> txList = transactions.stream()
            .sorted(Comparator.comparing(Transaction::getTxnDate).reversed())
            .limit(10)
            .map(tx -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", tx.getTxnId());
                m.put("stock", tx.getStock() != null ? tx.getStock().getStockName() : null);
                m.put("action", tx.getTxnType());
                m.put("quantity", tx.getQuantity());
                m.put("date", tx.getTxnDate() != null ? tx.getTxnDate().toString() : null);
                return m;
            }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("accountSummary", accountSummary);
        result.put("transactions", txList);
        return result;
    }

    static class StockHoldingSummary {
        String stockName;
        int quantity;
        double value;
        double currentPrice;
        StockHoldingSummary(String stockName, int quantity, double value, double currentPrice) {
            this.stockName = stockName; this.quantity = quantity; this.value = value; this.currentPrice = currentPrice;
        }
    }
}