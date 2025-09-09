define([
  '../accUtils',
  'knockout','ojs/ojchart','ojs/ojselectcombobox'
], function (accUtils, ko) {
  function CustomerViewModel() {
    var self = this;
    var role = sessionStorage.getItem("role");
    if (role !== "ADMIN") {
      if (window.appRouter) window.appRouter.go({ path: 'login' });
      return;
    }

    self.customerList = ko.observableArray([]);
    self.addFormVisible = ko.observable(false);
    self.updateFormVisible = ko.observable(false);
    self.tableVisible = ko.observable(false);
    self.newCustName = ko.observable("");
    self.newCustPhone = ko.observable("");
    self.newCustEmail = ko.observable("");
    self.newCustAddress = ko.observable("");
    self.editCustId = ko.observable("");

    self.showAddCustomerForm = function () {
      self.addFormVisible(true);
      self.updateFormVisible(false);
      self.tableVisible(false);
      self.newCustName("");
      self.newCustPhone("");
      self.newCustEmail("");
      self.newCustAddress("");
    };

    self.listCustomer = function () {
      self.addFormVisible(false);
      self.updateFormVisible(false);
      fetch("http://localhost:8181/customers")
        .then(res => res.json())
        .then(data => {
          self.customerList(data);
          self.tableVisible(true);
        })
        .catch(err => {
          console.error("Error fetching customers:", err);
          alert("Failed to load customers");
        });
    };

    self.addCustomer = function () {
      var newCust = {
        custName: self.newCustName(),
        phoneNo: self.newCustPhone(),
        email: self.newCustEmail(),
        address: self.newCustAddress()
      };
      fetch("http://localhost:8181/customers", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newCust)
      })
        .then(res => res.json())
        .then(() => {
          alert("Customer added successfully");
          self.addFormVisible(false);
          self.listCustomer();
        })
        .catch(err => {
          console.error("Error adding customer:", err);
          alert("Failed to add customer");
        });
    };

    self.deleteCustomerRow = function (customer) {
      if (!confirm("Delete customer " + customer.custName + "?")) return;
      fetch("http://localhost:8181/customers/" + customer.custId, {
        method: "DELETE"
      })
        .then(res => {
          if (!res.ok) throw new Error("Delete failed");
          alert("Customer deleted");
          self.listCustomer();
        })
        .catch(err => {
          console.error("Error deleting:", err);
          alert("Failed to delete customer");
        });
    };

    self.startUpdateCustomerRow = function (customer) {
      self.editCustId(customer.custId);
      self.newCustName(customer.custName);
      self.newCustPhone(customer.phoneNo);
      self.newCustEmail(customer.email);
      self.newCustAddress(customer.address);
      self.updateFormVisible(true);
      self.addFormVisible(false);
      self.tableVisible(false);
    };

    self.updateCustomer = function () {
      var updatedCust = {
        custName: self.newCustName(),
        phoneNo: self.newCustPhone(),
        email: self.newCustEmail(),
        address: self.newCustAddress()
      };
      fetch("http://localhost:8181/customers/" + self.editCustId(), {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedCust)
      })
        .then(res => res.json())
        .then(() => {
          alert("Customer updated");
          self.updateFormVisible(false);
          self.listCustomer();
        })
        .catch(err => {
          console.error("Error updating:", err);
          alert("Failed to update customer");
        });
    };


    self.showReports = ko.observable(false);
    self.customerAssets = ko.observableArray([]);
    self.richestCustomer = ko.observable();
    self.poorestCustomer = ko.observable();
    self.totalAssets = ko.observable();

    self.showCustomerReports = function() {
      self.showReports(true);
      fetch("http://localhost:8181/customers/assets")
        .then(res => res.json())
        .then(data => {
          self.customerAssets(data);
        });
      fetch("http://localhost:8181/customers/maxAsset")
        .then(res => res.json()).then(self.richestCustomer);
      fetch("http://localhost:8181/customers/minAsset")
        .then(res => res.json()).then(self.poorestCustomer);
      fetch("http://localhost:8181/customers/totalAssets")
        .then(res => res.json()).then(self.totalAssets);
    };

    self.hideCustomerReports = function () {
      self.showReports(false);
    };

    self.connected = function () {
      accUtils.announce("Customers page loaded", "assertive");
      document.title = "Customers";
      self.listCustomer();
    };
  }
  return CustomerViewModel;
});