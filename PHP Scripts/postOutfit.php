<?php
require("pants.php");
mysql_connect($dbsod, $user, $pass);
mysql_select_db($dbod);

$name = mysql_real_escape_string($_GET['username']);
$spells = mysql_real_escape_string($_GET['spells']);
$request = "UPDATE Accounts SET Outfit = '" . $spells . "' WHERE User = '" . $name . "'";
$result = mysql_query($request);
echo $request;
mysql_free_result($result);
?>