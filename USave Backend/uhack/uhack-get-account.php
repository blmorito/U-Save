<?php
include('conn.php');
$curl = curl_init();
$data = file_get_contents('php://input');
$json = json_decode($data, true);
$account = $json['account'];
$role = $json['role'];

//$account = "testing Jessie yamagnito";
curl_setopt_array($curl, array(
  CURLOPT_URL => "https://api-uat.unionbankph.com/uhac/sandbox/accounts/".$account,
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "GET",
  CURLOPT_HTTPHEADER => array(
    "accept: application/json",
    "x-ibm-client-id: ed000e77-71f0-4592-991d-aa1bd4bc49cc",
    "x-ibm-client-secret: qV7gL5oL8kX2rL7tR3jX5dO7fC8iX4pL5kU7kX5lG1fB7dT7yK"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);
curl_close($curl);

if ($err) {
		$data = ['result' => 'notfounds'];
		echo json_encode($data);
} else {
	$query = mysql_query("select * from users where accountNo='$account' and role='$role'");
	$row = mysql_fetch_array($query);
	if($row>0){	
		$json = json_decode($response);
		$json[0]->userId = $row['u_id'];
		$json[0]->username = $row['username'];
		$json[0]->email = $row['email'];
		echo json_encode($json);
	}else{
		$data = ['result' => 'notfound'];
		echo json_encode($data);
	}
}
?>