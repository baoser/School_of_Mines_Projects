<?php
function getConnection(){
  $servername = "localhost";
  $username = "root";
  $password = "";
  $database = "Bao";
  $conn = new mysqli($servername, $username, $password, $database);
  if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
  }
  return $conn;
}

function getAllProducts($connParam) {
  $sql = "Select * FROM Products";
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function getAllCustomers($connParam){
  $sql = "SELECT * FROM Customers ORDER BY last_name";
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function getAllOrders($connParam){
  $sql = "Select * FROM Orders";
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersDisplay($connParam) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Products.product_name, Orders.time_ordered, Orders.quantity_purchased,
  Products.price, Orders.tax, Orders.donation, Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id";
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function findCustomerByName($connParam, $firstName, $lastName) {
  $sql = "SELECT first_name, last_name FROM Customers WHERE first_name=? AND last_name=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("ss", $firstName, $lastName);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function createCustomer($connParam, $firstName, $lastName, $email) {
  $sql = "INSERT INTO Customers (first_name, last_name, email) VALUES (?, ?, ?)";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("sss", $firstName, $lastName, $email);
  $stmt->execute();
}

function getProduct($connParam, $productName) {
  $sql = "SELECT * FROM products WHERE product_name=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("s", $productName);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

// create Order
function createOrder($connParam, $time, $productID, $price, $customerID, $quantity, $tax, $donation, $total){
  $sql = "INSERT INTO Orders (time_ordered, product_id, price, customer_id,
         quantity_purchased, tax, donation, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("iidiiddd", $time, $productID, $price, $customerID, $quantity, $tax, $donation, $total);
  $stmt->execute();
}

function getCustomer ($connParam, $firstName, $lastName){
  $sql = "SELECT * FROM Customers WHERE first_name=? AND last_name=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("ss", $firstName, $lastName);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function getCustomerId ($connParam, $customerId){
  $sql = "SELECT * FROM Customers WHERE customer_id=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("i", $customerId);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function getInStock($connParam, $productID) {
  $sql = "SELECT quantity_in_stock FROM Products WHERE product_id=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("i", $productID);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function updateInStock($connParam, $difference, $productID){
  $sql = "UPDATE Products SET quantity_in_stock=? WHERE product_id=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("ii", $difference, $productID);
  $stmt->execute();
}

function addProduct ($connParam, $name, $image, $quantity, $price, $inactive) {
  $sql = "INSERT INTO Products (product_name, price, quantity_in_stock, product_image, inactive)
          VALUES (?, ?, ?, ?, ?)";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("sdisi", $name, $price, $quantity, $image, $inactive);
  $stmt->execute();
}

function updateProduct ($connParam, $name, $image, $quantity, $price, $inactive, $productId) {
  $sql = "UPDATE Products SET product_name=?, price=?, quantity_in_stock=?, product_image=?, inactive=? WHERE product_id=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("sdisii", $name, $price, $quantity, $image, $inactive, $productId);
  $stmt->execute();
}

function deleteProduct ($connParam, $productId) {
  $order = findOrdersOfProduct($connParam, $productId);
  if ($order == true){
    return;
  }
  else {
    $sql = "DELETE FROM Products WHERE product_id=?";
    $stmt = $connParam->prepare($sql);
    $stmt->bind_param("i", $productId);
    $stmt->execute();
  }
}

function findOrdersOfProduct($connParam, $productId) {
  $sql = "SELECT * FROM Orders WHERE product_id=?";
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("i", $productId);
  $stmt->execute();
  $result = $stmt->get_result();
  $data = $result->fetch_assoc();
  if (empty($data))
    return false;
  else
    return true;
}

function ordersCustomers_sortBy($connParam) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Products.product_name, Orders.time_ordered, Orders.quantity_purchased,
  Products.price, Orders.tax, Orders.donation, Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  ORDER BY Customers.last_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersProducts_sortBy($connParam) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Products.product_name, Orders.time_ordered, Orders.quantity_purchased,
  Products.price, Orders.tax, Orders.donation, Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  ORDER BY Products.product_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersCustomers_totals($connParam) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Orders.quantity_purchased,
  Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  ORDER BY Customers.last_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersProducts_totals($connParam) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Orders.quantity_purchased,
  Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  ORDER BY Products.product_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersProducts_fitler($connParam, $productId) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Products.product_name, Orders.time_ordered, Orders.quantity_purchased,
  Products.price, Orders.tax, Orders.donation, Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  WHERE Orders.product_id=?
  ORDER BY Customers.last_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("i", $productId);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}

function ordersCustomers_fitler($connParam, $customerId) {
  $sql = "SELECT Customers.first_name, Customers.last_name, Products.product_name, Orders.time_ordered, Orders.quantity_purchased,
  Products.price, Orders.tax, Orders.donation, Orders.total FROM Orders
  INNER JOIN Customers ON Orders.customer_id=Customers.customer_id
  INNER JOIN Products ON Orders.product_id=Products.product_id
  WHERE Orders.customer_id=?
  ORDER BY Products.product_name";
  
  $stmt = $connParam->prepare($sql);
  $stmt->bind_param("i", $customerId);
  $stmt->execute();
  $result = $stmt->get_result();
  return $result;
}