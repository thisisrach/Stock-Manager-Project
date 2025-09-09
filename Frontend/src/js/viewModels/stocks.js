define([
  '../accUtils',
  'knockout',
  'ojs/ojarraydataprovider',
  'ojs/ojknockout',
  'ojs/ojtable',
  'ojs/ojbutton'
], function(accUtils, ko, ArrayDataProviderModule) {
  var ArrayDataProvider = ArrayDataProviderModule.ArrayDataProvider || ArrayDataProviderModule;
  function StockViewModel() {
    var self = this;

    // Access guard: Only allow logged in users
    var role = sessionStorage.getItem("role");
    if (!role) {
      if (window.appRouter) {
        window.appRouter.go({ path: 'login' });
      }
      return;
    }
    self.isAdmin = ko.observable(role === "ADMIN");
    self.isCustomer = ko.observable(role === "CUSTOMER");

    self.username = ko.observable(sessionStorage.getItem("username") || "");

    self.stockList = ko.observableArray([]);
    self.stockDataProvider = new ArrayDataProvider(self.stockList, { keyAttributes: 'stockId' });

    self.addFormVisible = ko.observable(false);
    self.updateFormVisible = ko.observable(false);
    self.showBuyForm = ko.observable(false);
    self.showSellForm = ko.observable(false);

    self.selectedStock = ko.observable();
    self.txnQuantity = ko.observable(1);

    self.editStockId = ko.observable();
    self.newStockName = ko.observable("");
    self.newListingPrice = ko.observable("");
    self.newCurrentPrice = ko.observable("");
    self.newAvailableQuantity = ko.observable("");
    self.newListingDate = ko.observable("");
    self.newExchange = ko.observable("");
    self.newStatus = ko.observable("");

    self.columns = [
      { headerText: "ID", field: "stockId" },
      { headerText: "Name", field: "stockName" },
      { headerText: "Listing Price", field: "listingPrice" },
      { headerText: "Current Price", field: "currentPrice" },
      { headerText: "Quantity", field: "availableQuantity" },
      { headerText: "Listing Date", field: "listingDate" },
      { headerText: "Exchange", field: "exchange" },
      { headerText: "Status", field: "status" },
      { headerText: "Actions", template: "actionsTemplate" }
    ];

    // List Stocks
    self.listStock = function() {
      self.mode("list");
      self.addFormVisible(false);
      self.updateFormVisible(false);
      fetch('http://localhost:8181/stocks')
        .then(function(response) {
          if (!response.ok) throw new Error("Failed to fetch stocks");
          return response.json();
        })
        .then(function(data) {
          if (!Array.isArray(data)) data = data ? [data] : [];
          data.forEach(function(stock) {
            stock.doUpdate = self.startUpdateStockRow;
            stock.doDelete = self.deleteStockRow;
            stock.openBuy = self.openBuy;
            stock.openSell = self.openSell;
          });
          self.stockList(data);
        })
        .catch(function(error) {
          console.error("Error fetching stocks:", error);
          alert("Unable to fetch stocks!");
        });
    };

    self.showAddStockForm = function() {
      self.mode("add");
      self.editStockId(null);
      self.newStockName("");
      self.newListingPrice("");
      self.newCurrentPrice("");
      self.newAvailableQuantity("");
      self.newListingDate("");
      self.newExchange("");
      self.newStatus("ACTIVE");
      self.addFormVisible(true);
      self.updateFormVisible(false);
    };

    self.addStock = function() {
      var stock = {
        stockName: self.newStockName(),
        listingPrice: parseFloat(self.newListingPrice()),
        currentPrice: parseFloat(self.newCurrentPrice()),
        availableQuantity: parseInt(self.newAvailableQuantity()),
        listingDate: self.newListingDate(),
        exchange: self.newExchange(),
        status: self.newStatus()
      };
      fetch('http://localhost:8181/stocks', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(stock)
      })
      .then(response => {
        if (!response.ok) throw new Error("Failed to add stock");
      })
      .then(() => {
        alert("Stock added successfully!");
        self.addFormVisible(false);
        self.listStock();
      })
      .catch(error => {
        console.error("Error adding stock:", error);
        alert("Failed to add stock.");
      });
    };

    self.startUpdateStockRow = function(stock) {
      if (!stock) return;
      self.editStockId(stock.stockId);
      self.newStockName(stock.stockName);
      self.newListingPrice(stock.listingPrice);
      self.newCurrentPrice(stock.currentPrice);
      self.newAvailableQuantity(stock.availableQuantity);
      self.newListingDate(stock.listingDate || '');
      self.newExchange(stock.exchange);
      self.newStatus(stock.status);
      self.updateFormVisible(true);
      self.addFormVisible(false);
      self.mode("edit");
    };

    self.updateStock = function() {
      var updatedStock = {
        stockId: self.editStockId(),
        stockName: self.newStockName(),
        listingPrice: parseFloat(self.newListingPrice()),
        currentPrice: parseFloat(self.newCurrentPrice()),
        availableQuantity: parseInt(self.newAvailableQuantity()),
        listingDate: self.newListingDate(),
        exchange: self.newExchange(),
        status: self.newStatus()
      };
      fetch('http://localhost:8181/stocks/' + self.editStockId(), {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedStock)
      })
      .then(response => {
        if (!response.ok) throw new Error("Failed to update stock");
      })
      .then(() => {
        alert("Stock updated successfully!");
        self.updateFormVisible(false);
        self.listStock();
      })
      .catch(error => {
        console.error("Error updating stock:", error);
        alert("Failed to update stock.");
      });
    };

    self.deleteStockRow = function(stock) {
      if (!stock || !stock.stockId) {
        alert("Invalid stock for delete.");
        return;
      }
      if (!confirm("Are you sure you want to delete this stock?")) return;
      fetch('http://localhost:8181/stocks/' + stock.stockId, {
        method: 'DELETE'
      })
      .then(response => {
        if (!response.ok) throw new Error("Failed to delete stock");
      })
      .then(() => {
        alert("Stock deleted successfully!");
        self.listStock();
      })
      .catch(error => {
        console.error("Error deleting stock:", error);
        alert("Failed to delete stock.");
      });
    };

    // Customer actions (Buy/Sell)
    self.openBuy = function(stock) {
      self.selectedStock(stock);
      self.txnQuantity(1);
      self.showBuyForm(true);
    };
    self.openSell = function(stock) {
      self.selectedStock(stock);
      self.txnQuantity(1);
      self.showSellForm(true);
    };
    self.closeForms = function() {
      self.showBuyForm(false);
      self.showSellForm(false);
      self.addFormVisible(false);
      self.updateFormVisible(false);
    };
    self.buyStock = function() {
      var buyReq = {
        username: sessionStorage.getItem("username"),
        stockId: self.selectedStock().stockId,
        quantity: self.txnQuantity()
      };
      fetch('http://localhost:8181/api/stocks/buy', {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(buyReq)
      })
      .then(res => {
        if (!res.ok) throw new Error('Failed to buy stock');
        alert("Stock bought!");
        self.closeForms();
        // Optionally, refresh stock list
      })
      .catch(err => {
        alert("Failed to buy: " + err.message);
      });
    };
    self.sellStock = function() {
      var sellReq = {
        username: sessionStorage.getItem("username"),
        stockId: self.selectedStock().stockId,
        quantity: self.txnQuantity()
      };
      fetch('http://localhost:8181/api/stocks/sell', {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(sellReq)
      })
      .then(res => {
        if (!res.ok) throw new Error('Failed to sell stock');
        alert("Stock sold!");
        self.closeForms();
      })
      .catch(err => {
        alert("Failed to sell: " + err.message);
      });
    };
    

self.mode = ko.observable("list");  
    self.showReports = ko.observable(false); 

    self.mostTransacted = ko.observable('');
    self.leastTransacted = ko.observable('');
    self.highestPriceStock = ko.observable();

    self.showStockReports = function () {
      self.mode("report")
      self.showReports(true);
      fetch("http://localhost:8181/stocks/transacted")
        .then(res => res.json())
        .then(data => {
          self.mostTransacted(data.mostTransacted);
          self.leastTransacted(data.leastTransacted);
        });
        fetch("http://localhost:8181/stocks/highestPrice")
        .then(res => res.json())
        .then(data => self.highestPriceStock(data));
    };
    self.hideStockReports = function () {
      self.mode("list");
      self.showReports(false);
    };


    self.connected = function() {
      accUtils.announce('Stocks page loaded.', 'assertive');
      document.title = "Stocks";
      self.listStock();
    };
  }
  return StockViewModel;
});