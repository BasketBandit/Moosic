CREATE TABLE `socket_clients` (
	`client_id` int NOT NULL AUTO_INCREMENT,
	`client_inet` int NOT NULL UNIQUE,
	`client_nickname` varchar(32) NOT NULL UNIQUE,
	`time_connected` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`time_lastaction` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`flag_disconnect` BOOLEAN NOT NULL DEFAULT '0',
	PRIMARY KEY (`client_id`)
);

CREATE TABLE `track_queue` (
	`track_id` int NOT NULL AUTO_INCREMENT,
	`uri` varchar(255) NOT NULL,
	`preview_url` varchar(255) NOT NULL,
	`artist` varchar(255) NOT NULL,
	`title` varchar(255) NOT NULL,
	`length_ms` int(9) NOT NULL DEFAULT '0',
	`played` BOOLEAN NOT NULL DEFAULT '0',
	PRIMARY KEY (`track_id`)
);
