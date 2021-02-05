CREATE DATABASE Hotel;
USE Hotel;
-- DROP Database Library;
DROP TABLE IF EXISTS room;
CREATE TABLE room(
room_type_id int AUTO_INCREMENT NOT NULL,
room_name VARCHAR(50) NOT NULL CHECK(room_name !=''),
room_capacity INT NOT NULL default 10,
PRIMARY KEY (room_type_id)
);
CREATE TABLE roomNumber(
room_nmbr int AUTO_INCREMENT NOT NULL,
room_type_id int not null,
foreign key (room_type_id) references room(room_type_id),
PRIMARY KEY (room_nmbr)
);
INSERT INTO roomnumber (room_type_id, room_nmbr) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),
(2,6),(2,7),(2,8),(2,9),(2,10),
(3,11),(3,12),(3,13),(3,14),(3,15),
(4,16),(4,17),(4,18),(4,19),(4,20);

CREATE TABLE customer(
customer_id int AUTO_INCREMENT NOT NULL,
firstName VARCHAR(50) NOT NULL CHECK(firstName !=''),
lastName VARCHAR(50) NOT NULL CHECK(lastName !=''),
PRIMARY KEY (customer_id)
);

SELECT * from roomnumber;
INSERT INTO room (room_name) VALUES
('Luxury Double Room'),
('Deluxe Single Room'),
('Deluxe Double Room'),
('Deluxe Single Room');



-- ('Hasse', 'hasse');
SELECT * from room 
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id;