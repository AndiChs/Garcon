<?php

	class DbOperations{
		private $con;

		function __construct(){
			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();

		}

		public function createUser($username, $password, $name){
			if($this->doesUserExist($username) == true){
				return 0;
			}
			else{
				$password = password_hash($password, PASSWORD_BCRYPT);

				$stmt = $this->con->prepare("INSERT INTO users (username, password, name) VALUES (?, ?, ?)");
				$stmt->bind_param("sss", $username, $password, $name);
				
				if($stmt->execute()){
					return 1;
				}
				
				return 2;
			}

		}

		private function doesUserExist($username){
			$stmt = $this->con->prepare("SELECT id FROM users WHERE username = ? LIMIT 1");
			$stmt->bind_param("s", $username);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}

		public function checkCredentials($username, $password){

			$stmt = $this->con->prepare("SELECT password FROM users WHERE username = ? LIMIT 1");
			$stmt->bind_param("s", $username);
			$stmt->execute();
			$user = $stmt->get_result()->fetch_assoc();
			if(isset($user['password'])){
				if (password_verify($password, $user['password'])) {
					return 1;
				}
			}
			return 0;
		}

		public function getUser($username){
			$stmt = $this->con->prepare("SELECT * FROM users WHERE username = ? LIMIT 1");
			$stmt->bind_param("s", $username);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}

		public function getRestaurantByCvr($cvr){
			$stmt = $this->con->prepare("SELECT * from restaurants WHERE cvr = ? LIMIT 1");
			$stmt->bind_param("s", $cvr);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}

		private function isCvrUsed($cvr){
			$stmt = $this->con->prepare("SELECT id FROM restaurants WHERE cvr = ? LIMIT 1");
			$stmt->bind_param("s", $cvr);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}

		public function createRestaurant($cvr, $name, $address){
			$stmt = $this->con->prepare("INSERT INTO restaurants (cvr, name, address) VALUES (?, ?, ?)");
			$stmt->bind_param("sss", $cvr, $name, $address);

			if(!$this->isCvrUsed($cvr)){
				$stmt->execute();
				return 1;
			}
			return 0;
		}

		public function createRestaurantRequest($userId, $restaurnatCVR){
			if(!$this->isCvrUsed($restaurnatCVR)){
				return 2;
			}
			
			$restaurant = $this->getRestaurantByCvr($restaurnatCVR);
			if(!$this->checkIfRestaurantRequestExists($userId, $restaurant['id'])){

				$stmt = $this->con->prepare("INSERT INTO restaurant_requests (user_id, restaurant_id) VALUES (?, ?)");
				$stmt->bind_param("ii", $userId, $restaurant['id']);

				$stmt->execute();
				return 1;
			}

			return 0;
		}

		private function checkIfRestaurantRequestExists($userId, $restaurantId){
			$stmt = $this->con->prepare("SELECT id FROM restaurant_requests WHERE user_id = ? AND restaurant_id = ? LIMIT 1");
			$stmt->bind_param("ii", $userId, $restaurantId);
			$stmt->execute();
			$stmt->store_result();

			return $stmt->num_rows > 0;
		}

		// Waiter Functions 

		// This function returns all the shifts available in the database. 
		public function getWaiterShifts($userId){
			$stmt = $this->con->prepare("SELECT date_start, working_hours FROM user_shifts WHERE user_id = ? ORDER BY date_start DESC");
			$stmt->bind_param("i", $userId);
			$stmt->execute();
			$stmt->store_result();
			$stmt->bind_result($date_start, $working_hours);

			$return_array = array();


			while ( $row = $stmt->fetch() ) {
			    $row_array["date_start"] = $date_start;
			    $row_array["working_hours"] = $working_hours;

			    array_push($return_array, $row_array);
			}
			return $return_array;
		}

		//This functions returns the items added to a restaurant.
		public function getRestaurantItems($restaurantId){
			$stmt = $this->con->prepare("SELECT price, name, description FROM items WHERE restaurant_id = ? ORDER BY created_at DESC");
			$stmt->bind_param("i", $restaurantId);
			$stmt->execute();
			$stmt->store_result();
			$stmt->bind_result($price, $name, $description);

			$return_array = array();


			while ( $row = $stmt->fetch() ) {
			    $row_array["price"] = $price;
			    $row_array["name"] = $name;
			    $row_array["description"] = $description;

			    array_push($return_array, $row_array);
			}
			return $return_array;
		}

		//This functions returns all the orders from a Restaurant.
		public function getRestaurantOrders($restaurantId){
			$stmt = $this->con->prepare("SELECT price, ready_at, description, table_id, id FROM orders WHERE restaurant_id = ? ORDER BY ready_at DESC");
			$stmt->bind_param("i", $restaurantId);
			$stmt->execute();
			$stmt->store_result();
			$stmt->bind_result($price, $readyAt, $description, $table_id, $id);

			$return_array = array();


			while ( $row = $stmt->fetch() ) {
			    $row_array["price"] = $price;
			    $row_array["ready_at"] = $readyAt;
			    $row_array["description"] = $description;
			    $row_array["table_id"] = $table_id;
			    $row_array["id"] = $id;

			    array_push($return_array, $row_array);
			}
			return $return_array;
		}

		public function getWaiterStatistics($userId){
			$return_array = array();

			// Query for getting the number of the orderes completed by a user
			$stmt = $this->con->prepare("SELECT COUNT(id) AS rows FROM orders WHERE user_id = ?");
			$stmt->bind_param("i", $userId);
			$stmt->execute();
			$row = $stmt->get_result()->fetch_assoc();

			$return_array['orders'] = $row['rows'];

			// Query for getting the number of working hours 
			$stmt = $this->con->prepare("SELECT COALESCE(SUM(working_hours), 0) AS working_hours FROM user_shifts WHERE user_id = ? AND CURRENT_TIMESTAMP > date_start");
			$stmt->bind_param("i", $userId);
			$stmt->execute();
			$row = $stmt->get_result()->fetch_assoc();

			$return_array['working_hours'] = $row['working_hours'];

			// Query for getting the number of shifts
			$stmt = $this->con->prepare("SELECT COUNT(id) AS rows FROM user_shifts WHERE user_id = ?");
			$stmt->bind_param("i", $userId);
			$stmt->execute();
			$row = $stmt->get_result()->fetch_assoc();

			$return_array['shifts'] = $row['rows'];

			return $return_array;
		}

		public function updateUserProfile($userId, $name, $newPassword, $password, $newUsername, $username){
			//If the user does not complete the field NewPassword
			if(strlen($newPassword) == 0)
				$newPassword = $password;

			// Check if the user inserted the right password
			if(!$this->checkCredentials($username, $password)){
				return 0;
			}

			// Check if the email address is used
			if($this->doesUserExist($newUsername) && $username !== $newUsername){
				return -1;
			}

			$newPassword = password_hash($newPassword, PASSWORD_BCRYPT);

			$stmt = $this->con->prepare("UPDATE users SET username = ?, password = ?, name = ? WHERE id = ? LIMIT 1");
			$stmt->bind_param("sssi", $newUsername, $newPassword, $name, $userId);
			$stmt->execute();

			return 1;
		}

		public function getRestaurantShifts($restaurantId){
			$stmt = $this->con->prepare("SELECT date_start, working_hours, name FROM user_shifts, users WHERE user_shifts.user_id = users.id AND users.restaurant_id = ?");
			$stmt->bind_param("i", $restaurantId);
			$stmt->execute();
			$stmt->store_result();
			$stmt->bind_result($date_start, $working_hours, $name);

			$return_array = array();


			while ( $row = $stmt->fetch() ) {
				$row_array['name'] = $name;
			    $row_array["date_start"] = $date_start;
			    $row_array["working_hours"] = $working_hours;

			    array_push($return_array, $row_array);
			}
			return $return_array;
		}

		public function deleteRestaurantShift($date, $workingHours){
			$stmt = $this->con->prepare("DELETE FROM user_shifts WHERE date_start = ? AND working_hours = ? LIMIT 1");
			$stmt->bind_param("si", $date, $workingHours);
			$stmt->execute();

			return 1;
		}

		public function addRestaurantOrder($userId, $orderPrice, $orderDescription, $table, $restaurantId){
			$stmt = $this->con->prepare("INSERT INTO orders (user_id, restaurant_id, price, description, ready_at, table_id) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP + interval 15 minute, ?)");
			$stmt->bind_param("issss", $userId, $restaurantId, $orderPrice, $orderDescription, $table);
			return $stmt->execute();
		}

		public function deleteRestaurantItem($name, $restaurantId){
			$stmt = $this->con->prepare("DELETE FROM items WHERE name = ? AND restaurant_id = ? LIMIT 1");
			$stmt->bind_param("si", $name, $restaurantId);
			$stmt->execute();

			return 1;
		}

		
	}