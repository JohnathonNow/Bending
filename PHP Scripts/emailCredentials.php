<?php
$verify = $_GET['name'];
$message =    "<h1>Hello! To complete your registration<br>"
              . "just click the following link:<br>"
              . "<a href = \"http://johnbot.net78.net/yes.php?name=" . $verify . "\"> Verify Account </a></h1>";
$to = $_GET['to'];
$subject = "Complete your registration!";
$from = $to;//"JohnJWesthoff@Gmail.com";
$headers = "From: " . $from . "\r\n";;
$headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";
mail($to,$subject,$message,$headers);
echo "Success";
?>	