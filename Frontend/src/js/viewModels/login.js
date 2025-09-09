define(['knockout', 'jquery'], function(ko, $) {
  function LoginViewModel() {
    var self = this;
    self.username = ko.observable("");
    self.password = ko.observable("");
    self.message = ko.observable("");

    self.regUsername = ko.observable('');
    self.regPassword = ko.observable('');
    self.regRole = ko.observable('CUSTOMER');
    self.regMessage = ko.observable('');
    self.regPhone = ko.observable('');
    self.regEmail = ko.observable('');
    self.regAddress = ko.observable('');
    // Toggle
    self.showRegister = ko.observable(false);

    // Toggle handlers
    self.showRegisterForm = function() {
      self.showRegister(true);
      self.clearLoginFields();
    };
    self.showLoginForm = function() {
      self.showRegister(false);
      self.clearRegisterFields();
    };

    self.clearLoginFields = function() {
      self.username('');
      self.password('');
      self.message('');
    };
    self.clearRegisterFields = function() {
      self.regUsername('');
      self.regPassword('');
      self.regRole('CUSTOMER');
      self.regMessage('');
    };

    self.login = function() {
      $.ajax({
        url: "http://localhost:8181/api/users/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          username: self.username(),
          password: self.password()
        }),
        success: function(response) {
          sessionStorage.setItem("username", response.username);
          sessionStorage.setItem("role", response.role);
          self.message("Login successful");
          // Redirect based on role
          if (window.appRouter) {
            if (response.role === "ADMIN") {
              window.appRouter.go({ path: 'stocks' });
            } else if (response.role === "CUSTOMER") {
              window.appRouter.go({ path: 'customerdashboard' });
            }
          }
        },
        error: function(xhr) {
          self.message("Invalid username or password!");
        }
      });
      return false;
    };

    // Register logic
    self.registerUser = function() {
      $.ajax({
        url: "http://localhost:8181/api/users/register",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          username: self.regUsername(),
  password: self.regPassword(),
  role: self.regRole(),
  custName: self.regRole() === 'CUSTOMER' ? self.regUsername() : null,
  phone: self.regPhone(),
  email: self.regEmail(),
  address: self.regAddress()
        }),
        success: function() {
          self.regMessage("Registration successful! Please login.");
          setTimeout(self.showLoginForm, 1500);
        },
        error: function(xhr) {
          self.regMessage(xhr.responseJSON ? xhr.responseJSON.message : "Registration error.");
        }
      });
      return false;
    };
  }
  return LoginViewModel;
});