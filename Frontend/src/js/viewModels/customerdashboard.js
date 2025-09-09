define(['knockout'], function(ko) {
  function CustomerDashboardViewModel() {
    var self = this;

    // Guard: Only allow customers
    var role = sessionStorage.getItem("role");
    if (role !== "CUSTOMER") {
      if (window.appRouter) {
        window.appRouter.go({ path: 'login' });
      }
      return;
    }

    var role = sessionStorage.getItem("role");
if (role !== "CUSTOMER") {
  if (window.appRouter) {
    window.appRouter.go({ path: 'login' });
  }
  return;
}

    self.username = ko.observable(sessionStorage.getItem("username") || "Customer");
    self.accountSummary = ko.observable({});  // Filled by API
    self.transactions = ko.observableArray([]);  // Filled by API

    self.fetchDashboardData = function() {
      var username = sessionStorage.getItem("username");
      if (!username) return;
      fetch("http://localhost:8181/api/customer/dashboard?username=" + encodeURIComponent(username))
        .then(function(response) {
          if (!response.ok) throw new Error("Failed to fetch dashboard data.");
          return response.json();
        })
        .then(function(data) {
          self.accountSummary(data.accountSummary || {});
          self.transactions(data.transactions || []);
        })
        .catch(function(error) {
          console.error("Dashboard loading error:", error);
          alert("Could not load dashboard data.");
        });
    };

    self.connected = function() {
      self.fetchDashboardData();
    };
  }
  return CustomerDashboardViewModel;
});