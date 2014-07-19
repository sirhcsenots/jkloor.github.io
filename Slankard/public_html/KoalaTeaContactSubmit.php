<?php
$to = "info@koalateafilms.com" ;
$from = $_REQUEST['contact_email'] ;
$name = $_REQUEST['contact_name'] ;
$headers = "From: $from";
$subject = "Koala Tea Films Website Contact";

$fields = array();
$fields{"contact_name"} = "contact_name";
$fields{"contact_email"} = "contact_email";
$fields{"contact_phone"} = "contact_phone";
$fields{"message"} = "message";

$body = "We have received the following contact:\n\n"; foreach($fields as $a => $b){ $body .= sprintf("%20s: %s\n",$b,$_REQUEST[$a]); }

//$headers2 = "From: noreply@cohenscarpeting.com";
//$subject2 = "Thank you for contacting us";
//$autoreply = "Thank you for contacting us. Somebody will get back to you as soon as possible, usualy within 48 hours. If you have any more questions, please consult our website at www.stovesandsuch.com";

mail($to, $subject, $body, $headers);
//$send2 = mail($from, $subject2, $autoreply, $headers2);

{print "<p><center><font face='verdana' size='2'>Your message was sent. Thank you for contacting Koala Tea Films.<p><center><a href='javascript:///' onClick='window.close();'>Close This Window</a>";}

?> 



