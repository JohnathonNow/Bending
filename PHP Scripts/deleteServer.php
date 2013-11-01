<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

//Get parameters
$ip=  mysql_real_escape_string($_GET['ip']);

//Create the query
$request = "DELETE FROM Pants WHERE ServerIP='" . $ip . "'";
//Send the query
$result = mysql_query($request);

//Return the result
$row = mysql_fetch_array($result);
echo $row['User'];
mysql_free_result($result);
?>