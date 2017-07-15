<?php

include('conn.php');

$data = file_get_contents('php://input');
$json = json_decode($data, true);
$username = $json['username'];
$password = $json['password'];
$role = $json['role'];
$query = mysql_query("select * from users where username='$username' and password='$password' and role='$role'");
$row = mysql_fetch_array($query);
if($row>0){
		$accountNo = $row['accountNo'];
		$data = ['result' => 'success', 'accountNo' => $accountNo];
		echo json_encode($data);
}else{
		$data = ['result' => 'fail'];
		echo json_encode($data);
}
?>