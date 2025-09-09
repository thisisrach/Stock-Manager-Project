package com.ofss;

public class BuySellRequest {
 private String username; 
 private Integer stockId;
 private Integer quantity;


 public String getUsername() { return username; }
 public void setUsername(String username) { this.username = username; }
 public Integer getStockId() { return stockId; }
 public void setStockId(Integer stockId) { this.stockId = stockId; }
 public Integer getQuantity() { return quantity; }
 public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
