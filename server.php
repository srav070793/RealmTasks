<?php 
	header("Access-Control-Allow-Origin: *");
	header("Access-Control-Allow-Headers: Content-type");
	header("Access-Control-Allow-Methods: ['OPTIONS, 'GET', 'POST']");
	require_once __DIR__.'/php-graph-sdk-5.0.0/src/Facebook/autoload.php'; 
	
	$queryString = $_GET['query'];
	$searchType = $_GET['searchType'];
	
	$access_token="EAAULAQ5nmWIBAKJsm8hdYwqZARur3oOANv07bheHLCK2U6WmDcBRZC9kx60bYb6Nd8KHow4BK3ONreZAeptl7ZCqMiZCY877hONUufxBs91sJdgl2PQcsh4x4kotAOiav3e8eRc04pIjIpmTO6ItWwWYYc3CwPcYZD";

	$fb = new Facebook\Facebook(['app_id'=>'1419474048096610','app_secret'=>'7bd208ef074a9e5a5bbe2299a272081b', 'default_graph_version' => 'v2.5',]);

	if ($searchType == 'user') {	
		$call = "/search?q=".$queryString."&type=".$searchType."&fields=id,name,picture.width(700).height(700)";

		try{
			$results = $fb->get($call, $access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'page') {
		$call = "/search?q=".$queryString."&type=".$searchType."&fields=id,name,picture.width(700).height(700)";

		try{
			$results = $fb->get($call, $access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'group') {
		$call = "/search?q=".$queryString."&type=".$searchType."&fields=id,name,picture.width(700).height(700)";

		try{
			$results = $fb->get($call, $access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'event') {
		$call = "/search?q=".$queryString."&type=".$searchType."&fields=id,name,picture.width(700).height(700)";

		try{
			$results = $fb->get($call, $access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'place') {
		$lat=$_GET['queryLat']; 
		$lng=$_GET['queryLong']; 

		$call = "/search?q=".$queryString."&type=".$searchType."&center=".$lat.",".$lng."&fields=id,name,picture.width(700).height(700)";

		try{
			$results = $fb->get($call, $access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'next') {
		echo $queryString;
		$apinext = $queryString;

		//copy the substring from $apinext
		$call = substr($apinext, 32,strlen($apinext)-1);
		//echo $apinext;
		//echo $apinext;

		try{
			$results_next = $fb->get($call);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			//echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			//echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results_next = ($results_next->getDecodedBody());
		
		//$results = $results["data"];

		echo json_encode($results_next);
	}
	elseif ($searchType == 'prev') {
		$apinext = $queryString;

		//copy the substring from $apinext
		$call = substr($apinext, 32);

		try{
			$results_prev = $fb->get($call);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results_prev = ($results_prev->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results_prev);
	}
	elseif ($searchType == 'details') {
		$call= $queryString."?fields=id,picture,name,albums.limit(5){name,photos.limit(2){name,picture}},posts.limit(5){created_time,message}";

		try{
			$results = $fb->get($call,$access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	elseif ($searchType == 'albumphotos') {
		$call = "/".$queryString."/picture?fields=id,url&redirect=false";

		try{
			$results = $fb->get($call,$access_token);
		}catch(Facebook\Exceptions\FacebookResponseException $e){
			echo "Facebook Exceptions returned an error".$e->getMessage();
			exit;
		}
		catch(Facebook\Exceptions\FacebookSDKException $e){
			echo "Facebook SDK returned an error".$e->getMessage();
			exit;
		}
		$results = ($results->getDecodedBody());
		//$results = $results["data"];

		echo json_encode($results);
	}
	//$arr = array('Profile photo' => 'Load Pic', 'Name' => $searchType, 'Favorite' => 4, 'Details' => 5);
	
//echo json_encode($arr);
?>