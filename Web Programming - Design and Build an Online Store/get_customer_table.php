

<?php 
// https://phpdelusions.net/mysqli_examples/prepared_select
include "database.php";
$conn = getConnection();
// get the q parameter from URL
$q = $_REQUEST["q"];
$f = $_REQUEST["f"];
findCustomerName($conn, $q, $f);

function findCustomerName($conn, $name, $field) {
  if ($field == "fname")
	  $query = "SELECT * FROM Customers WHERE first_name LIKE ?";
  else if ($field =="lname")
    $query = "SELECT * FROM Customers WHERE last_name LIKE ?";
	$stmt = $conn->prepare($query);
	$name = $name."%";
	$stmt->bind_param("s", $name);
	
	$stmt->execute();
	$result = $stmt->get_result(); // get the mysqli result
	//echo $result->num_rows;
	if ($result->num_rows <= 0) {
		echo "No suggestions";
		return;
	}
  // $fnameData = $result->fetch_assoc();
  // $firstName = $fnameData['first_name'];
 
	echo "<table id='display-table'>
		<tr>
		<th>Firstname</th>
		<th>Lastname</th>
    <th>Email</th>
		</tr>";
	
	if ($result->num_rows > 0) {
		while($row = mysqli_fetch_array($result)) {
			echo "<tr><td>";
			echo $row['first_name']."</td><td>".$row['last_name']."</td><td>".$row['email']."</td></tr>";
		}
	}
	else {
		echo "No suggestions";
	}		

}

?>