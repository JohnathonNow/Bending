<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

//Get parameters


//Create the query
$request = "SELECT * FROM Accounts";

//Send the query
$result = mysql_query($request);

//Return the result
echo mysql_num_rows($result);
mysql_free_result($result);
?>	