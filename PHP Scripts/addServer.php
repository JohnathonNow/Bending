<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass) or die(mysql_error());
mysql_select_db($dbod) or die(mysql_error());

//Get parameters
$name=  mysql_real_escape_string($_GET['name']);
$ip=  mysql_real_escape_string($_GET['ip']);

//Create the query
$request = "INSERT INTO Pants (ServerName, ServerIP, Count) VALUES ('" . $name . "', '" . $ip ."', 0)";
//Send the query
$result = mysql_query($request);

//Return the result
$row = mysql_fetch_array($result);
echo $row['User'];
mysql_free_result($result);
?>