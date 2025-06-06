$(document).ready(function(){
  $("#purchase").click(function () {
    var postData = $('form').serialize();
    $.ajax({
      type: "POST",
      url: "ajaxsubmit.php",
      data: postData,
      cache: false,
      success: function(result){
        $('#orderMessage').text(result);
      }
    });
    return false;
  });

  // AJAX add item
  $("#add").click(function () {
    var validate = validateFields();
    if (validate === true){
      var postData = $('form').serializeArray();
      postData.push({name: "action", value: "add"});
      $.ajax({
        type: "POST",
        url: "do_admin.php",
        data: postData,
        cache: false,
        success: function(result){
          // $('#productTable').after(result);
          // alert(result);
          location.reload();
        }
      });
      return false;
    }
  });  

  // AJAX Update item
  $("#update").click(function () {
    var validate = validateFields();
    if (validate === true){
      var postData = $('form').serializeArray();
      postData.push({name: "action", value: "update"});
      $.ajax({
        type: "POST",
        url: "do_admin.php",
        data: postData,
        cache: false,
        success: function(result){
          // $('#productTable').after(result);
          location.reload();
        }
      });
      return false;
    }
  });

  // AJAX Delete item 
  $("#delete").click(function (e) {
    e.preventDefault();
    var validate = validateFields();
    if (validate === true){
      var confirmDelete = confirm("Do you really want to delete?");
      if (confirmDelete) {
        var postData = $('form').serializeArray();
        postData.push({name: "action", value: "delete"});
        $.ajax({
          type: "POST",
          url: "do_admin.php",
          data: postData,
          cache: false,
          success: function(result){
            if (result){
              $('#validationMessage').text(result);
            }
            else{
              location.reload();
            } 
          }
        });
      }
      return false;
    }
  });

  // AJAX Sort orders
  $("#run").click(function () {
    // if ($('#productFilter').val() != "") {

    // }
    // else if ($('#customerFilter').val() != ""){

    // }
    if ($("#displayOption").prop("checked") == true && !($('#customerSort').is(':checked')) && !($('#productSort').is(':checked'))) {
      alert("Must select customer or product!");
    }
    else {
      var postData = $('form').serialize();
      $.ajax({
        type: "POST",
        url: "ajaxLoad.php",
        data: postData,
        cache: false,
        success: function(result){
          // $('#ordersTableArea').text(result);
          document.getElementById('ordersTableArea').innerHTML = result;
          // alert(result);
        }
      });
    }
    return false;
  });
});

function suggest(str, field) {
  if (str.length == 0) {
    document.getElementById("existingCustomer").innerHTML = "";
    return;
  } else {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this .status == 200) {
        document.getElementById("existingCustomer").innerHTML = this.responseText;
        highlight_row();
      }
    };
    xmlhttp.open("GET","get_customer_table.php?q=" + str + "&f=" + field, true);
    xmlhttp.send();
  }
}

function highlight_row() {
    var table = document.getElementById('display-table');
    var cells = table.getElementsByTagName('td');

    for (var i = 0; i < cells.length; i++) {
        // Take each cell
        var cell = cells[i];
        // do something on onclick event for cell
        cell.onclick = function () {
            // Get the row id where the cell exists
            var rowId = this.parentNode.rowIndex;

            var rowsNotSelected = table.getElementsByTagName('tr');
            for (var row = 0; row < rowsNotSelected.length; row++) {
                rowsNotSelected[row].style.backgroundColor = "";
                rowsNotSelected[row].classList.remove('selected');
            }
            var rowSelected = table.getElementsByTagName('tr')[rowId];
            rowSelected.style.backgroundColor = "yellow";
            rowSelected.className += " selected";

            $('#fname').val(rowSelected.cells[0].innerHTML);
            $('#lname').val(rowSelected.cells[1].innerHTML);
            $('#email').val(rowSelected.cells[2].innerHTML);
        }
    }
}

highlight_row_products();
function highlight_row_products() {
    var table = document.getElementById('productTable');
    var cells = table.getElementsByTagName('td');

    for (var i = 0; i < cells.length; i++) {
        // Take each cell
        var cell = cells[i];
        // do something on onclick event for cell
        cell.onclick = function () {
            // Get the row id where the cell exists
            var rowId = this.parentNode.rowIndex;

            var rowsNotSelected = table.getElementsByTagName('tr');
            for (var row = 0; row < rowsNotSelected.length; row++) {
                rowsNotSelected[row].style.backgroundColor = "";
                rowsNotSelected[row].classList.remove('selected');
            }
            var rowSelected = table.getElementsByTagName('tr')[rowId];
            rowSelected.style.backgroundColor = "yellow";
            rowSelected.className += " selected";

            $('#name').val(rowSelected.cells[0].innerHTML);
            $('#image').val(rowSelected.cells[1].innerHTML);
            $('#quantity').val(rowSelected.cells[2].innerHTML);
            $('#price').val(rowSelected.cells[3].innerHTML);
            if (rowSelected.cells[4].innerHTML == "yes")
              $('#inactive').prop("checked", true);
        }
    }
}

function validateFields(){
  var validate = true;
  if ($('#name').val() == ""){
      $('#validationMessage').text("Item name must not be blank!");
      $('#name').focus();
      validate = false;
      return validate; 
  }
  if ($('#image').val() == ""){
    $('#validationMessage').text("Item image must not be blank!");
    $('#image').focus();
    validate = false;
    return validate; 
  }
  return validate;
}

function productFilterSelected (){
  $("#customerSort").prop('checked', false);
  $("#productSort").prop('checked', false);
  $("#displayOption").prop('checked', false);
  $("#customerFilter").prop('selectedIndex', 0);
}

function customerFilterSelected (){
  $("#customerSort").prop('checked', false);
  $("#productSort").prop('checked', false);
  $("#displayOption").prop('checked', false);
  $("#productFilter").prop('selectedIndex', 0);
}

function sortBySelected (){
  $("#productFilter").prop('selectedIndex', 0);
  $("#customerFilter").prop('selectedIndex', 0);
}
