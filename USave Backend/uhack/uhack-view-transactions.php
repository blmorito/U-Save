<?php
include('conn.php');
$data = file_get_contents('php://input');
$json = json_decode($data, true);
$id = $json['id'];
$role = $json['role'];
$query = "";
if ($role == "user") {
	$query = mysql_query("select * from transaction a inner join users b on a.user_id = b.u_id where a.user_id='$id' and b.role = '$role'");
}else if ($role == "vendor") {
	$query = mysql_query("select * from transaction a inner join users b on a.vendor_id = b.u_id where a.vendor_id='$id' and b.role = '$role'");
}

$json = array();
while($row = mysql_fetch_assoc($query)) {
	$rows[] = $row;
}


    print json_encode($rows);
?>