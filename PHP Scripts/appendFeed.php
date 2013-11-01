<?php 
date_default_timezone_set('America/New_York');
$fn = "feed.xml"; 



$lines = file($fn); 
$last = sizeof($lines) - 1 ; 
unset($lines[$last]); 

// write the new data to the file 
$fp = fopen($fn, 'w'); 
fwrite($fp, implode('', $lines)); 
fclose($fp); 



$file = fopen($fn, "a+"); 
$size = filesize($fn); 

fwrite($file, "<item><title>");
fwrite($file, str_replace("_"," ",$_GET['title']));
fwrite($file, "</title>");

fwrite($file, "<description>");
fwrite($file, str_replace("_"," ",$_GET['description']));
fwrite($file, "</description>");

fwrite($file, "<pubDate>");
fwrite($file, strftime ("%a, %d %b %Y %H:%M:%S %z"));
fwrite($file, "GMT</pubDate></item>\n</channel></rss>");

$text = fread($file, $size); 
fclose($file); 
echo $text;
?> 	