<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

//Get parameters
$user=  mysql_real_escape_string($_GET['username']);
$pass=  mysql_real_escape_string($_GET['password']);

//Create the query
$request = "SELECT User,Pass FROM Accounts WHERE User = '" . $user . "' AND Pass='" . $pass . "' AND Active=1";

//Send the query
$result = mysql_query($request);

//Return the result
$row = mysql_fetch_array($result);
echo $row['User'];
mysql_free_result($result);
?>