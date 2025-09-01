package com.ofss;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY) 
	 @Column(name = "cust_id")
	 private Integer custId;

	 @Column(name = "cust_name")
	 private String custName;

	 @Column(name = "phone_no")
	 private String phoneNo;

	 @Column(name = "email")
	 private String email;

	 @Column(name = "address")
	 private String address;
	 
	 public Customer() {
		 
	 }

	public Customer(Integer custId, String custName, String phoneNo, String email, String address) {
		super();
		this.custId = custId;
		this.custName = custName;
		this.phoneNo = phoneNo;
		this.email = email;
		this.address = address;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
