<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

//Get parameters
$ip=mysql_real_escape_string($_GET['ip']);
$c=mysql_real_escape_string($_GET['c']);

//Create the query
$request = "UPDATE Pants SET Count = '" . $c . "' WHERE ServerIP='" . $ip . "';";
//Send the query
$result = mysql_query($request);
echo $c;
mysql_free_result($result);
?>