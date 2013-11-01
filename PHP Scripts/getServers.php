<?php
//Load required file
require("pants.php");

//Connect
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

//Get parameters


//Create the query
$request = "SELECT ServerName,ServerIP,Count FROM Pants";

//Send the query
$result = mysql_query($request);

//Return the result
while($row = mysql_fetch_array($result)){
	echo $row['ServerName'] . "\n";
        echo $row['ServerIP'] . "\n";
        echo $row['Count'] . "\n";
}
mysql_free_result($result);
echo "<!--";
?>			