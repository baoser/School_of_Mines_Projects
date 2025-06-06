<?php
  include 'database.php';
  $conn = getConnection();
  $productFilter = $_POST['productFilter'];
  $customerFilter = $_POST['customerFilter'];
  // $showTotal = $_POST['displayOption'];
  
  // product filter
  if ($productFilter != "") {
    $product = getProduct($conn, $productFilter);
    $product = $product->fetch_assoc();
    $productId = $product['product_id'];
    $data = ordersProducts_fitler($conn, $productId);
    echo "<table id='ordersTable'>
      <caption>Orders By Product Filter</caption>";
      displayTable_big($data);
  }

  // customer filter
  else if ($customerFilter !=""){
    $customer = getCustomerId($conn, $customerFilter);
    $customer = $customer->fetch_assoc();
    $customerId = $customer['customer_id'];
    $data = ordersCustomers_fitler($conn, $customerId);
    echo "<table id='ordersTable'>
      <caption>Orders By Customer Filter</caption>";
      displayTable_big($data);
  }

  // Just show totals is checked
  else if (isset($_POST['displayOption']) && $_POST['displayOption'] == "yes"){
    // sort by last name
    $sortBy = $_POST['sortBy'];
    if ($sortBy == "customerSort") {
      $data = ordersCustomers_totals($conn);
      echo "<table id='ordersTable'>
      <caption>Orders By Last Name</caption>";
      displayTable_totals($data);
    }
    // sort by products
    else if ($sortBy == "productSort") {
      $data = ordersProducts_totals($conn);
      echo "<table id='ordersTable'>
      <caption>Orders By Product</caption>";
      displayTable_totals($data);
    }
  }

  // Just show totals is NOT checked
  else {
    // sort by last name
    $sortBy = $_POST['sortBy'];
    if ($sortBy == "customerSort") {
      $data = ordersCustomers_sortBy($conn);
      echo "<table id='ordersTable'>
      <caption>Orders By Last Name</caption>";
      displayTable_big($data);
    }
    // sort by product
    else if ($sortBy == "productSort") {
      $data = ordersProducts_sortby($conn);
      echo "<table id='ordersTable'>
      <caption>Orders By Product</caption>";
      displayTable_big($data);
    }
  }

  

  function displayTable_big ($data) {
    $totalQuantity = 0;
    $totalCost = 0.00;
    echo "
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
      </thead>";
      if ($data->num_rows > 0) {
		    while($row = mysqli_fetch_array($data)) {
			  echo "<tr><td>";
			  echo $row['first_name'] . " " . $row['last_name'] . "</td><td>" . $row['product_name'] . "</td><td>" . date("m/d/y h:i A", $row['time_ordered']) .
             "</td><td>" . $row['quantity_purchased'] . "</td><td>" . number_format($row['price'], 2) . "</td><td>" . number_format($row['tax'], 2) . "</td><td>" .
             number_format($row['donation'], 2) . "</td><td>" . number_format($row['total'], 2) . "</td></tr>";
        $totalQuantity += $row['quantity_purchased'];
        $totalCost += $row['total'];
		    }
        echo "
        <tr>
          <td><b>Total all orders</b></td>
          <td></td>
          <td></td>
          <td>" . $totalQuantity . "</td>
          <td></td>
          <td></td>
          <td></td>
          <td>" . number_format($totalCost, 2) . "</td>
        </tr>";
	    }
	    else {
		    echo "No orders to display";
	    }		
  }

  function displayTable_totals ($data) {
    $totalQuantity = 0;
    $totalCost = 0.00;
    echo "
      <thead>
		  <tr>
		    <th>Customer</th>
        <th>Quantity</th>
        <th>Total</th>
		  </tr>
      </thead>";
      if ($data->num_rows > 0) {
		    while($row = mysqli_fetch_array($data)) {
			  echo "<tr><td>";
			  echo $row['first_name'] . " " . $row['last_name'] . "</td><td>" . $row['quantity_purchased'] . "</td><td>" . number_format($row['total'], 2) . "</td></tr>";
        $totalQuantity += $row['quantity_purchased'];
        $totalCost += $row['total'];
		    }
        echo "
        <tr>
          <td><b>Total all orders</b></td>
          <td>" . $totalQuantity . "</td>
          <td>" . number_format($totalCost, 2) . "</td>
        </tr>";
	    }
	    else {
		    echo "No orders to display";
	    }		
  }
?>