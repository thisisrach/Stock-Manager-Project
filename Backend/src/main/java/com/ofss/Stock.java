package com.ofss;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Integer stockId;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "listing_price")
    private Double listingPrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "listing_date")
    private LocalDate listingDate;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "status")
    private String status = "ACTIVE";

    public Stock() {
    }

    public Stock(Integer stockId, String stockName, Double listingPrice, Double currentPrice, Integer availableQuantity,
                 LocalDate listingDate, String exchange, String status) {
        super();
        this.stockId = stockId;
        this.stockName = stockName;
        this.listingPrice = listingPrice;
        this.currentPrice = currentPrice;
        this.availableQuantity = availableQuantity;
        this.listingDate = listingDate;
        this.exchange = exchange;
        this.status = status;
    }

    // Getters and setters

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(Double listingPrice) {
        this.listingPrice = listingPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public LocalDate getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDate listingDate) {
        this.listingDate = listingDate;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
