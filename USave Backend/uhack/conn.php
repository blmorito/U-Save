<?php
$conn = mysql_connect("localhost","root","") or die(mysql_error());
mysql_select_db("uhack") or die (mysql_error());

function begin(){
	mysql_query("BEGIN");
}

function commit(){
	mysql_query("COMMIT");
}

function rollback(){
	mysql_query("ROLLBACK");
}

function getUserAccount($id){
	$query = mysql_query("select accountNo from users where u_id = '$id'");
	$row = mysql_fetch_array($query);
	$accountNo = $row['accountNo'];	
	echo $accountNo;
}
?>