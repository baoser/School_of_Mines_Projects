<?php
  include 'database.php';
  $conn = getConnection();
  $fname = $_POST['fname'];
  $lname = $_POST['lname'];
  $email = $_POST['email'];
  $quantity = $_POST['quantity'];
  $product = $_POST['products'];
  $donationCheck = $_POST['donation'];
  $time = $_POST['timestamp'];
  $data = findCustomerByName($conn, $fname, $lname);
  $row = $data->fetch_assoc();
  // Add new customer to database
  if(empty($row)) {
      createCustomer($conn, $fname, $lname, $email); 
    }

  $data = getProduct($conn, $product);
  $productRow = $data->fetch_assoc();
  $price = $productRow['price'];
  $noTax = $quantity * $price;
  $tax = number_format(($noTax / 100 * 2), 2);
  $withTax = $noTax + $tax;
  $total = $withTax;
  $donation = 0.00;
  if ($donationCheck == "yes"){
    $total = ceil($total);
    $donation = $total - $withTax;
  }

  $productID = $productRow['product_id'];
  $data = getCustomer($conn, $fname, $lname);
  $row = $data->fetch_assoc();
  $customerID = $row['customer_id'];
  $donation = $total - $withTax;
  createOrder($conn, $time, $productID, $noTax, $customerID, $quantity, $tax, $donation, $total);

  // update quantity in stock after an order
  $data = getInStock($conn, $productID);
  $row = $data->fetch_assoc();
  $difference = $row['quantity_in_stock'] - $quantity;
  if($difference >= 0){
    // call update quantity
    updateInStock($conn, $difference, $productID);
  }
  else{
    updateInStock($conn, 0, $productID);
  }

  $orderMessage = "Order submitted for: " . $fname . " " . $lname . " " . $quantity . " " . $product
  . ". Total - $" . number_format($total, 2);
  
  echo $orderMessage;
?>