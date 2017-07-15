<?php
include('conn.php');
/*
$accountName = "Jessie Anosa";
$userName = "Jessie";
$password = "jessie";
$email = "jessie@jessie.jessie";
*/
$data = file_get_contents('php://input');
$json = json_decode($data, true);
$accountName = $json['accountName'];
$userName = $json['userName'];
$password = $json['password'];
$email = $json['email'];
$role = $json['role'];

$curl = curl_init();

curl_setopt_array($curl, array(
  CURLOPT_URL => "https://api-uat.unionbankph.com/uhac/sandbox/test/accounts",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "{\"accountName\":\"$accountName\"}",
  CURLOPT_HTTPHEADER => array(
    "accept: application/json",
    "content-type: application/json",
    "x-ibm-client-id: ed000e77-71f0-4592-991d-aa1bd4bc49cc",
    "x-ibm-client-secret: qV7gL5oL8kX2rL7tR3jX5dO7fC8iX4pL5kU7kX5lG1fB7dT7yK"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
	echo "cURL Error #:" . $err;
} else {
	$json = json_decode($response);
	$accountNo = $json[0]->account_no;
	$result = mysql_query("INSERT INTO users (username, password, email, accountNo, role) VALUES ('$userName', '$password', '$email', '$accountNo', '$role')");
	
	begin();
	if($result){
		commit();
		$data = ['result' => 'success', 'userName' => $userName, 'accountNo' => $accountNo];
		echo json_encode($data);
	}else{
		rollback();
		$data = ['result' => 'fail', 'userName' => "", 'accountNo' => ""];
		echo json_encode($data);
	}
}
?>