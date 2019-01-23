create table users (
				user_id INT not null AUTO_INCREMENT Primary key,
				user_login VARCHAR(255) UNIQUE not null,
				user_password VARCHAR(255) not null,
				first_name VARCHAR(255) not null,
				last_name VARCHAR(255),
				register_date datetime not null DEFAULT CURRENT_TIMESTAMP
)

create table friends(
				friends_id Int AUTO_INCREMENT Primary key,
				user_id INT not null,
				friend_id Int not null,
				Foreign Key (user_id) References users(user_id),
				Foreign Key (friend_id) References users(user_id)
 )


Create table messages(
	message_from_id int not null,
	message_to_id int not null,
	message text not null,
	readed boolean not null,
	send_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	Foreign key (message_from_id) References users(user_id),
	Foreign key (message_to_id) References users(user_id)
)

create table friend_request(
	friend_request_id int not null AUTO_INCREMENT Primary key,
	request_from_id int not null,
	request_to_id int not null,
	readed boolean not null DEFAULT 0,
	aproved boolean not null DEFAULT 0,
	friendship_date datetime not null DEFAULT CURRENT_TIMESTAMP,
	Foreign key (request_from_id) References users(user_id),
	Foreign key (request_to_id) References users (user_id)
	)
