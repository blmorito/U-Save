<?php

include('conn.php');

$data = file_get_contents('php://input');
$json = json_decode($data, true);
$username = $json['username'];
$password = $json['password'];

$query = mysql_query("select * from users where username='$username' and password='$password'");
$row = mysql_fetch_array($query);
$accountNo = $row['accountNo'];
if($row>0){
		$data = ['result' => 'success', 'accountNo' => $accountNo];
		echo json_encode($data);
}else{
		$data = ['result' => 'fail'];
		echo json_encode($data);
}
?>