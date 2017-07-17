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
    "x-ibm-client-id: REPLACE_WITH_CLIENT_ID",
    "x-ibm-client-secret: REPLACE_WITH_CLIENT_ID"
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
	$query = mysql_query("select * from users where username='$userName' or email='$email'");
	$rows = mysql_fetch_array($query);
	if($rows>0){
		$accountNo = $rows['accountNo'];
		$data = ['result' => 'exists'];
		echo json_encode($data);;
	}else{
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
}
?>