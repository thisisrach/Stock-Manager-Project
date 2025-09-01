package com.ofss;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name="transactions")
public class Transaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Integer txnId;

    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "txn_type")
    private String txnType; 

    @Column(name = "txn_price")
    private Double txnPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "txn_date")
    private LocalDate txnDate;

	public Transaction(Integer txnId, Customer customer, Stock stock, String txnType, Double txnPrice, Integer quantity,
			LocalDate txnDate) {
		super();
		this.txnId = txnId;
		this.customer = customer;
		this.stock = stock;
		this.txnType = txnType;
		this.txnPrice = txnPrice;
		this.quantity = quantity;
		this.txnDate = txnDate;
	}
	
	public Transaction() {
		
	}

	public Integer getTxnId() {
		return txnId;
	}

	public void setTxnId(Integer txnId) {
		this.txnId = txnId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public Double getTxnPrice() {
		return txnPrice;
	}

	public void setTxnPrice(Double txnPrice) {
		this.txnPrice = txnPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDate getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(LocalDate txnDate) {
		this.txnDate = txnDate;
	}
}
