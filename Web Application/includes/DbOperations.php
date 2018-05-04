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
			$stmt = $this->con->prepare("SELECT * from restaurants WHERE cvr = ?");
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

		public function createRestaurantRequest($userId, $restaurantId){
			$stmt = $this->con->prepare("INSERT INTO restaurant_requests (user_id, restaurant_id) VALUES (?, ?)");
			$stmt->bind_param("ii", $userId, $restaurantId);

			if(!$this->checkIfRestaurantRequestExists($userId, $restaurantId)){
				$stmt->execute();
				return 1;
			}

			return 0;
		}

		private function checkIfRestaurantRequestExists($userId, $restaurantId){
			$stmt = $this->con->prepare("SELECT id FROM restaurant_requests WHERE user_id = ? AND restaurant_id = ?");
			$stmt->bind_param("ii", $userId, $restaurantId);
			$stmt->execute();
			$stmt->store_result();

			return $stmt->num_rows > 0;
		}

		
	}