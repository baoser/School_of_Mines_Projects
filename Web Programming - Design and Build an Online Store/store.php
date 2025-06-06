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
  $data = getAllProducts($conn);
?>
<section class="mainBackground">
  <form>
    <fieldset class="info">
      <legend class="legend">Personal Info</legend>
      <!-- first name input-->
      <label for="fname">First Name:<span class="error">*&nbsp&nbsp</span></label>
      <input type="text" id="fname" name="fname" pattern="[A-Za-z &#39]*"
      onkeyup="suggest(this.value, this.name)" 
      title="Names can only include letters, spaces, and apostrophe" required><br><br>
      <!-- last name input-->
      <label for="lname">Last Name:<span class="error">*&nbsp&nbsp</span></label>
      <input type="text" id="lname" name="lname" pattern="[A-Za-z &#39]*"
      onkeyup="suggest(this.value, this.name)"
      title="Names can only include letters, spaces, and apostrophe" required><br><br>
      <!-- email input-->
      <label for="email">Email:<span class="error">*&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span></label>
      <input type="email" id="email" name="email" required>
    </fieldset>

    <fieldset class="info">
      <legend class="legend">Product Info</legend>
      <select id="products" name="products" required onchange="showInStock()">
        <option value="">-- Select an item -- </option>
        <?php if ($data): ?>
        <?php foreach($data as $row): ?>
        <?php if ($row['inactive'] == NULL): ?>
        <option value=<?=$row['product_name']?> data-quantity=<?=$row['quantity_in_stock']?>>
        <?=$row['product_name']?> ---- $<?=$row['price']?> ---- <?=$row['quantity_in_stock']?>
        </option>
        <?php endif ?>
        <?php endforeach ?>
        <?php endif ?>
      </select><br><br>
      <label for="available">Available</label>
      <input type="text" id="available" name="available" value="" size="5" readonly><br><br>
      <!-- quantity input -->
      <label for="quantity">Quantity:</label>
      <input type="number" id="quantity" name="quantity" min="1" max="100" required>
    </fieldset>

    Round up to the nearest dollar for a donation?<br>
    <input type="radio" id="yes" name="donation" value="yes">
    <label for="yes">Yes</label><br>
    <input type="radio" id="no" name="donation" value="no">
    <label for="no">No</label><br><br>
    
    <!-- input for time -->
    <input type="hidden" name="timestamp" value="<?php echo time(); ?>">    
    <input type="submit" id="purchase" value="Purchase">
    <input type="reset" class="clearButton" id="clearButton" value="Clear Fields"></button>
  </form>
</section>
  <div id="existingMessage"><em>Choose an existing customer:</em><br></div>
  <div id="existingCustomer"><br></div>
  <div id="imageArea">
    <p id="orderMessage"></p>
  </div>
<?php
  include 'footer.php';
?>

<script>
  function showInStock() {
    var available = $('#available');
    var inStock = $('#products option:selected').attr('data-quantity');
    available.attr('value', inStock);
  }
</script>
<script src="script.js"></script>
</body>
</html>