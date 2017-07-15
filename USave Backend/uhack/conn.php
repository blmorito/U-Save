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
?>