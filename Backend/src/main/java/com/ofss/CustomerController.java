package com.ofss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private StockRepository stockRepo;
    @Autowired
    private TransactionRepository txnRepo;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.saveCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> patchCustomer(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Customer updatedCustomer = customerService.patchCustomer(id, updates);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        if (customerService.deleteCustomer(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    
    // reports
    @GetMapping("/assets")
    public List<CustomerAssetReport> getAllCustomerAssets() {
        List<Customer> customers = new ArrayList<>();
        customerRepo.findAll().forEach(customers::add);
        List<CustomerAssetReport> result = new ArrayList<>();
        for (Customer c : customers) {
            double totalAsset = 0.0;
            Map<Integer, Integer> net = new HashMap<>();
            List<Transaction> txns = txnRepo.findByCustomer_CustId(c.getCustId());
            for (Transaction t : txns) {
                if (t.getStock() == null) continue; // skip if no stock
                int stockId = t.getStock().getStockId();
                int qty = t.getTxnType().equalsIgnoreCase("BUY") ? t.getQuantity() : -t.getQuantity();
                net.put(stockId, net.getOrDefault(stockId, 0) + qty);
            }
            for (Map.Entry<Integer, Integer> e : net.entrySet()) {
                if (e.getValue() > 0) {
                    Stock s = stockRepo.findById(e.getKey()).orElse(null);
                    if (s != null) totalAsset += e.getValue() * s.getCurrentPrice();
                }
            }
            result.add(new CustomerAssetReport(
                c.getCustId(), c.getCustName(), totalAsset
            ));
        }
        return result;
    }
    
    @GetMapping("/maxAsset")
    public CustomerAssetReport maxAssetCustomer() {
        return getAllCustomerAssets()
            .stream().max(Comparator.comparingDouble(CustomerAssetReport::getAsset)).orElse(null);
    }

    @GetMapping("/minAsset")
    public CustomerAssetReport minAssetCustomer() {
        return getAllCustomerAssets()
            .stream().min(Comparator.comparingDouble(CustomerAssetReport::getAsset)).orElse(null);
    }

    @GetMapping("/totalAssets")
    public Double totalAssets() {
        return getAllCustomerAssets()
            .stream().mapToDouble(CustomerAssetReport::getAsset).sum();
    }


    public static class CustomerAssetReport {
        private Integer custId;
        private String custName;
        private double asset;
        public CustomerAssetReport(Integer id, String name, double asset) {
            this.custId = id; this.custName = name; this.asset = asset;
        }
        public Integer getCustId() { return custId; }
        public String getCustName() { return custName; }
        public double getAsset() { return asset; }
    }
}