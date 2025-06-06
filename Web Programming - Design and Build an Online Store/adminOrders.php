<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="store.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="script.js"></script>
</head>
<body>
<?php
  include 'header.php';
  include 'database.php';
  // include 'Unit3_ajaxsubmit.php';
  $conn = getConnection();
  $totalQuantity = 0;
  $totalCost = 0.00;
?>
<section class="mainBackground">
<!-- PRODUCT TABLE -->
  <div id="ordersTableArea">
  <table id="ordersTable">
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
        <?php $data = ordersDisplay($conn); if ($data): ?>
        <?php foreach($data as $row): ?>    
        <tr>
          <td><?= $row['first_name']?>&nbsp<?= $row['last_name']?></td>
          <td><?= $row['product_name']?></td>
          <td><?= date("m/d/y h:i A", $row['time_ordered'])?></td>
          <td><?= $row['quantity_purchased']?></td>
          <td><?= number_format($row['price'], 2)?></td>
          <td><?= number_format($row['tax'], 2)?></td>
          <td><?= number_format($row['donation'], 2)?></td>
          <td><?= number_format($row['total'], 2)?></td>
        </tr>
        <!-- adding up total quantity -->
        <?php $totalQuantity += $row['quantity_purchased']?>
        <!-- adding up total cost -->
        <?php $totalCost += $row['total']?>
        <?php endforeach ?>
        <?php endif ?>
        <tr>
          <td><b>Total all orders</b></td>
          <td></td>
          <td></td>
          <td><?= $totalQuantity ?></td>
          <td></td>
          <td></td>
          <td></td>
          <td><?= number_format($totalCost,2) ?></td>
        </tr>
    </tbody>
  </table>
  </div>

  <form id="sortForm">
    <div id="displayOptions">Display Options</div>
    <fieldset id="sortFieldset">
      <b>Sort by:</b><br>
      <input type="radio" id="customerSort" name="sortBy" value="customerSort" onclick="sortBySelected()">
      <label for="customerSort">Customer</label><br>
      <input type="radio" id="productSort" name="sortBy" value="productSort" onclick="sortBySelected()">
      <label for="productSort">Product</label><br>
      <b>Display option:</b><br>
      <input type="checkbox" id="displayOption" name="displayOption" value="yes" onclick="sortBySelected()">
      <label for="displayOption">Just show totals<span>&nbsp&nbsp</span></label>
      <br><br>

      <!-- FILTERS -->
      <b>OR Filter by either:</b><br>
      <?php $products = getAllProducts($conn); ?>
      <select class="filterDropdown" id="productFilter" name="productFilter" onchange="productFilterSelected()">
        <option value="">-- Select an item -- </option>
        <?php if ($products): ?>
        <?php foreach($products as $row): ?>
        <option value=<?=$row['product_name']?>><?=$row['product_name']?></option>
        <?php endforeach ?>
        <?php endif ?>
      </select>
      <br>&nbsp&nbsp&nbsp OR<br>
      <?php $customers = getAllCustomers($conn); ?>
      <select class="filterDropdown" id="customerFilter" name="customerFilter" onchange="customerFilterSelected()">
        <option value="">-- Select a customer -- </option>
        <?php if ($customers): ?>
        <?php foreach($customers as $row): ?>
        <option value=<?=$row['customer_id']?>><?=$row['first_name']?> <?=$row['last_name']?></option>
        <?php endforeach ?>
        <?php endif ?>
      </select><br><br>
      <input type="submit" id="run" value="Run">
    </fieldset>   
  </form>
  
