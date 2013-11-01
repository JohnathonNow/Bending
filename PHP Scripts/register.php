<?php
require("pants.php");
mysql_connect($dbsod, $user, $pass) or die(mysql_error());
mysql_select_db($dbod) or die(mysql_error());
$name = mysql_real_escape_string($_GET['name']);
$username =  mysql_real_escape_string($_GET['username']);
$password=  mysql_real_escape_string($_GET['password']);
$email=  mysql_real_escape_string($_GET['email']);
$verify=  mysql_real_escape_string($_GET['verify']);

$request = "SELECT User FROM Accounts WHERE User = '" . $username . "'";
$result = mysql_query($request);
$row = mysql_fetch_array($result);
if ($row['User'] == "")
{
$request = "INSERT INTO Accounts (User,Pass,Email,Verify,Active) VALUES ('" . $username . "','" .$password . "','" . $email . "','" . $verify . "'," . 0 . ")";
$result = mysql_query($request);
echo "true";
}
else
{
echo "false";
}
?>	