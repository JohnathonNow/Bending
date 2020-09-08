<?php
require("pants.php");
mysql_connect($dbsod, $user, $pass) or die(mysql_error());
mysql_select_db($dbod) or die(mysql_error());
$name = mysql_real_escape_string($_GET['name']);
$request = "UPDATE Accounts SET Active=1 WHERE Verify = '" . $name . "'";
$result = mysql_query($request);
$request2 = "SELECT User FROM Accounts WHERE Verify = '" . $name . "'";
$result2 = mysql_query($request2);
$row = mysql_fetch_array($result2);
echo "Account " . $row['User'] . " successfully verified!";
header( 'Location: https://www.west-it.webs.com' ) ;
?>