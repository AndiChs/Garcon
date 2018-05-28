<?php
$agent = $_SERVER['HTTP_USER_AGENT'];
$agent=strtolower($agent);

if (strpos($agent, 'android') == false) {
	$response['error'] = true;
	$response['message'] = "Access denied.";
	die(json_encode($response));
}

