define(['knockout'], function (ko) {
  function TransactionsViewModel() {
    var self = this;
    var role = sessionStorage.getItem("role");
    if (role !== "ADMIN") {
      if (window.appRouter) window.appRouter.go({ path: 'login' });
      return;
    }
    self.transactionList = ko.observableArray([]);

    self.showReports = ko.observable(false);
    self.moreTxnType = ko.observable('');
    self.reportTransactions = ko.observableArray([]);

    self.loadTransactions = function () {
      fetch('http://localhost:8181/transactions')
        .then(response => {
          if (!response.ok) throw new Error("Failed to load transactions");
          return response.json();
        })
        .then(data => {
          if (!Array.isArray(data)) data = [data];
          self.transactionList(data);
        })
        .catch(() => {
          // fallback for demo if backend isn't running
          self.transactionList([
            {
              txnId: 1, customer: { custId: 101, custName: "Sammy Kapoor" },
              stock: { stockId: 1, stockName: "Infosys" },
              txnType: "BUY", txnPrice: 1600, quantity: 5, txnDate: "2025-01-04"
            },
            {
              txnId: 2, customer: { custId: 102, custName: "Demo User" },
              stock: { stockId: 2, stockName: "Reliance" },
              txnType: "SELL", txnPrice: 2350, quantity: 2, txnDate: "2025-01-05"
            }
          ]);
        });
    };

    self.exportCSV = function () {
      if (self.transactionList().length === 0) {
        alert("No transactions to export!");
        return;
      }
      let csvContent = "Txn ID,Customer,Stock,Type,Quantity,Price/Share,Date\n";
      self.transactionList().forEach(txn => {
        let row = [
          txn.txnId,
          txn.customer && txn.customer.custName ? txn.customer.custName : "",
          txn.stock && txn.stock.stockName ? txn.stock.stockName : "",
          txn.txnType,
          txn.quantity,
          txn.txnPrice,
          txn.txnDate
        ].join(",");
        csvContent += row + "\n";
      });
      let blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
      let link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = "transactions.csv";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    };

    self.showTxnReports = function () {
      self.showReports(true);
      fetch("http://localhost:8181/transactions/moreType")
        .then(res => res.text())
        .then(data => self.moreTxnType(data));
      fetch("http://localhost:8181/transactions/all")
        .then(res => res.json())
        .then(data => {
          if (!Array.isArray(data)) data = [data];
          self.reportTransactions(data);
        });
    };
    self.hideTxnReports = function () { self.showReports(false); };

    self.connected = function() {
      self.loadTransactions();
      fetch("http://localhost:8181/transactions/moreType")
        .then(res => res.text())
        .then(data => self.moreTxnType(data));
    };
  }
  return TransactionsViewModel;
});