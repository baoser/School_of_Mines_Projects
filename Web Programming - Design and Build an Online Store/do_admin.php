<?php
  include 'database.php';
  $conn = getConnection();
  $action = $_POST['action'];
  $name = $_POST['name'];
  $image = $_POST['image'];
  $quantity = $_POST['quantity'];
  $price = $_POST['price'];
  if (isset($_POST['inactive']) && $_POST['inactive'] == "yes"){
    $inactive = 1;
  }
  else {
    $inactive = 0;
  }

  $data = getProduct($conn, $name);
  $row = $data->fetch_assoc();
  $productId = $row['product_id'];

  if ($action === "add"){
    addProduct($conn, $name, $image, $quantity, $price, $inactive);
    // echo "<tr><td>" . $name . "</td><td>" . $image . "</td><td>" . $quantity .
    //    "</td><td>" . $price . "</td><td>" . $inactive . "</td></tr>";
  }
  else if ($action === "update") {
    updateProduct($conn, $name, $image, $quantity, $price, $inactive, $productId);
  }
  else if ($action === "delete") {
    deleteProduct($conn, $productId);
    $data = getProduct($conn, $name);
    $row = $data->fetch_assoc();
    if (empty($row)){
      $result = "";
      echo $result;
    }
    else {
      $result = "Cannot delete item because orders exist.";
      echo $result;
    }
  }
  
?>