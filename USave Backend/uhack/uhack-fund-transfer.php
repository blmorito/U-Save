<?php
include('conn.php');
$curl = curl_init();
$data = file_get_contents('php://input');
$json = json_decode($data, true);
$targetAccount = $json['accountNo'];
$vendorId = $json['vendorId'];
$password = $json['password'];
$channelId = "USaver";
$query = mysql_query("select max(transaction_id) from transaction");
$row = mysql_fetch_row($query);
$maxTransactionId = $row[0];
$query2 = mysql_query("select accountNo from users where u_id = '$vendorId'");
$row2 = mysql_fetch_array($query2);
$sourceAccount = $row2['accountNo'];
$sourceCurrency = "PHP";
$targetCurrency = "PHP";
$amount = $json['amount'];
$fee = $amount * 0.03;
$totalFee = $amount - $fee;
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
		"x-ibm-client-id: REPLACE_WITH_CLIENT_ID",
		"x-ibm-client-secret: REPLACE_WITH_CLIENT_SCRET_ID"
	  ),
	));

	$query3 = mysql_query("select u_id from users where accountNo = '$targetAccount' and password = '$password'");
	$row3 = mysql_fetch_array($query3);
	if($row3>0){
		$userId = $row3['u_id'];
		$response = curl_exec($curl);
		$err = curl_error($curl);

		curl_close($curl);

		if ($err) {
		  echo "cURL Error #:" . $err;
		} else {
			$result = mysql_query("INSERT INTO transaction (vendor_id, user_id, amount, fee, total_amount, date_time) VALUES ('$vendorId', '$userId', '$amount', '$fee', '$totalFee', NOW())");
			begin();
			
			if($result){
				commit();
				$data = ['result' => 'success'];
				echo json_encode($data);
			}else{
				rollback();
				$data = ['result' => 'fail'];
				echo json_encode($data);
			}
		}
	}else{
		$data = ['result' => 'invalid'];
		echo json_encode($data);
	}
	
	

?>