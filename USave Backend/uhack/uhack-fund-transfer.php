<?php
include('conn.php');
$curl = curl_init();
$data = file_get_contents('php://input');
$json = json_decode($data, true);
$userId = $json['userId'];
$vendorId = $json['vendorId'];
$channelId = "USaver";
$query = mysql_query("select max(transaction_id) from transaction");
$row = mysql_fetch_row($query);
$maxTransactionId = $row[0];
$query2 = mysql_query("select accountNo from users where u_id = '$vendorId'");
$row2 = mysql_fetch_array($query2);
$sourceAccount = $row2['accountNo'];
//$sourceAccount = getUserAccount($vendorId);
$sourceCurrency = "PHP";
$query3 = mysql_query("select accountNo from users where u_id = '$userId'");
$row3 = mysql_fetch_array($query3);
$targetAccount = $row3['accountNo'];
//$targetAccount = getUserAccount($userId);
$targetCurrency = "PHP";
$amount = $json['amount'];
curl_setopt_array($curl, array(
  CURLOPT_URL => "https://api-uat.unionbankph.com/uhac/sandbox/transfers/initiate",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "{\"channel_id\":\"$channelId\",
						  \"transaction_id\":\"$maxTransactionId\",
						  \"source_account\":\"$sourceAccount\",
						  \"source_currency\":\"$sourceCurrency\",
						  \"target_account\":\"$targetAccount\",
						  \"target_currency\":\"$targetCurrency\",
						  \"amount\":$amount}",
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
	$result = mysql_query("INSERT INTO transaction (vendor_id, user_id, amount) VALUES ('$vendorId', '$userId', '$amount')");
	begin();
	
	if($result){
		commit();
		echo "success";
	}else{
		rollback();
		echo "fail";
	}
}
?>