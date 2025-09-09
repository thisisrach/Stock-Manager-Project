package com.ofss;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
	List<Transaction> findByCustomer_CustId(Integer custId); // Add this
	@Query("SELECT COALESCE(SUM(CASE WHEN t.txnType='BUY' THEN t.quantity ELSE -t.quantity END),0) "
		     + "FROM Transaction t WHERE t.customer.custId = :custId AND t.stock.stockId = :stockId")
		Integer getNetQuantityForCustomerAndStock(Integer custId, Integer stockId);
}
