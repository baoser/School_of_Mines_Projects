<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="store.css">
</head>
<body>
<?php
  include 'header.php';
  include 'database.php';
  $conn = getConnection();
  date_default_timezone_set("America/Denver");
?>
  <div class=mainBackground>
  <!-- CUSTOMERS TABLE -->
    <table style="width:25em">
      <caption>Customers</caption>
      <thead>
        <tr>
          <th>Last Name</th>
          <th>First Name</th>
          <th>Email</th>
        </tr>
      </thead>
      <tbody>
        <?php $data = getAllCustomers($conn)?> 
        <?php if ($data): ?>
        <?php foreach($data as $row): ?>    
        <tr>
          <td><?= $row['last_name']?></td>
          <td><?= $row['first_name']?></td>
          <td><?= $row['email']?></td>
        </tr>
        <?php endforeach ?>
        <?php endif ?>
      </tbody>
    </table>

<!-- ORDERS TABLE -->
    <?php 
      $data = getAllOrders($conn);
      $row = $data->fetch_assoc();
    ?>
    <?php if ($row): ?>
    <table style="width:55em">
      <caption>Orders</caption>
      <thead>
        <tr>
          <th>Customer</th>
          <th>Item</th>
          <th>Date</th>
          <th>Quantity</th>
          <th>Price</th>
          <th>Tax</th>
          <th>Donation</th>
          <th>Total</th>
        </tr>
      </thead>
      <tbody>
        <?php foreach($data as $row): ?>    
        <tr>
          <td><?= $row['customer_id']?></td>
          <td><?= $row['product_id']?></td>
          <td><?= date("m/d/y h:i A", $row['time_ordered'])?></td>
          <td><?= $row['quantity_purchased']?></td>
          <td><?= number_format($row['price'], 2)?></td>
          <td><?= number_format($row['tax'], 2)?></td>
          <td><?= number_format($row['donation'], 2)?></td>
          <td><?= number_format($row['total'], 2)?></td>
        </tr>
        <?php endforeach?> 
      </tbody>
    </table>
    <?php else: ?>
      <p>No one has ordered anything yet</p>
    <?php endif; ?>

<!-- PRODUCTS TABLE -->
    <table style="width:40em">
      <caption>Items</caption>
      <thead>
        <tr>
          <th>Name</th>
          <th>In Stock</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>
        <?php $data = getAllProducts($conn); if ($data): ?>
        <?php foreach($data as $row): ?>    
        <tr>
          <td><?= $row['product_name']?></td>
          <td><?= $row['quantity_in_stock']?></td>
          <td><?= $row['price']?></td>
        </tr>
        <?php endforeach ?>
        <?php endif ?>
      </tbody>
    </table>
  </div>
<?php include 'footer.php'; ?>
</body>
</html>