<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="store.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<?php
  include 'header.php';
  include 'database.php';
  // include 'Unit3_ajaxsubmit.php';
  $conn = getConnection();
?>
<section class="mainBackground">
<!-- PRODUCT TABLE -->
  <div class="tableArea">
  <table id=productTable>
    <caption>Items</caption>
    <thead>
    <tr>
      <th>Name</th>
      <th>Image</th>
      <th>Quantity</th>
      <th>Price</th>
      <th>Inactive?</th>
    </tr>
    </thead>
    <tbody>
        <?php $data = getAllProducts($conn); if ($data): ?>
        <?php foreach($data as $row): ?>    
        <tr>
          <td><?= $row['product_name']?></td>
          <td><?= $row['product_image']?></td>
          <td><?= $row['quantity_in_stock']?></td>
          <td><?= $row['price']?></td>
          <?php if ($row['inactive'] == 1): ?>
          <td>yes</td>
          <?php else: ?>
          <td></td>
          <?php endif ?>
        </tr>
        <?php endforeach ?>
        <?php endif ?>
    </tbody>
  </table>
  </div>
  <div id="validationMessage"></div>
<!-- FORM -->
  <form id=itemInfo>
    <fieldset class="info">
      <legend class="legend">Item Info</legend>
      <!-- item name input-->
      <label for="name">Item Name:<span class="error">*&nbsp&nbsp&nbsp&nbsp</span></label>
      <input type="text" id="name" name="name" pattern="[A-Za-z &#39]*" value="" required><br><br>
      <!-- item image input-->
      <label for="image">Item Image:<span class="error">*&nbsp&nbsp&nbsp</span></label>
      <input type="text" id="image" name="image" value="" required><br><br>
      <!-- quantity input-->
      <label for="quantity">Quantity:<span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span></label>
      <input type="number" id="quantity" name="quantity"><br><br>
      <!-- price input -->
      <label for="price">Price:<span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span></label>
      <input type="number" id="price" name="price" pattern="[0-9]+([\,.][0-9]+)?" step="0.01"><br><br>
      <!-- inactive button input -->
      <label for="inactive">Make Inactive<span>&nbsp&nbsp</span></label>
      <input type="checkbox" id="inactive" name="inactive" value="yes">
    </fieldset>
    <input type="submit" id="add" value="Add Item">
    <input type="submit" id="update" value="Update">
    <input type="submit" id="delete" value="Delete">
  </form>
</section>
<script src="script.js"></script>
</body>
</html>