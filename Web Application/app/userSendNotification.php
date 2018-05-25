<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['message'])){
		// operate the date further 
		$db = new DbOperations();
		$token[] = "ddfzNzudWGTI:APA91bFHlxhXwrWQCQiLK-msZd3rOlj-tvbAobSm3vuekUNd491zMsZQWCDznaRc_kV-JlDxYZUeSNd3GOhsrDJaVa-yiRMd5to6Ry4LDM31egTf2Qwy4gGGppSBpURRtH5iYMeyziRe";
		$message = array("message" => $_POST['message']);
		$response = $db->sendNotification($token, $message);
		

	} else{
		$response['error'] = true;
		$response['message'] = "Required fields are missing.";
	}
	
} else{
	$response['error'] = true;
	$response['message'] = "Invalid Request.";
}

echo json_encode($response);