CREATE DATABASE Hotel;
USE Hotel;
-- DROP Database Hotel;
-- DROP TABLE IF EXISTS reservation;
CREATE TABLE room(
room_type_id int AUTO_INCREMENT NOT NULL,
room_name VARCHAR(50) NOT NULL CHECK(room_name !=''),
room_capacity INT NOT NULL default 10,
price INT NOT NULL,
PRIMARY KEY (room_type_id)
);
INSERT INTO room (room_name, price) VALUES
('Luxury Double Room', 1500),
('Luxury Single Room', 1000),
('Deluxe Double Room',3000),
('Deluxe Single Room', 2000);


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
-- drop table customer;

CREATE TABLE customer(
userName VARCHAR(50) NOT NULL CHECK(userName !=''),
firstName VARCHAR(50) NOT NULL CHECK(firstName !=''),
lastName VARCHAR(50) NOT NULL CHECK(lastName !=''),
telephone VARCHAR(50) NOT NULL CHECK(telephone !=''),
password VARCHAR(50) NOT NULL CHECK(password !=''),
PRIMARY KEY (userName)
);
INSERT INTO customer (userName,firstName, lastName,telephone,password) VALUES
('name1','5','0811390','name1','hej1'),
('name2','test','name2','jabbe','123'),
('name3','1','0811390','name3','hej111');
-- ('hej','hå','0811390','hasse1','hej222'),
-- ('Hasse','Hansson','0811390','hasse','hej');

SELECT * from customer;




CREATE TABLE reservation(
reservation_id int AUTO_INCREMENT NOT NULL,
userName VARCHAR(50) NOT NULL CHECK(userName !=''),
room_nmbr INT NOT NULL,
amount_days INT NOT NULL default 1, 
check_in DATETIME,
check_out DATETIME default NULL,
booked boolean default true,
foreign key (userName) references customer(userName),
foreign key (room_nmbr) references roomNumber(room_nmbr),
PRIMARY KEY (reservation_id)
);

select * from reservation;

INSERT INTO reservation (userName, room_nmbr,amount_days,check_in) VALUES
('name1',2,5,CURRENT_timestamp),('name2',5,1,CURRENT_timestamp);

INSERT INTO reservation (userName, room_nmbr,amount_days,check_out) VALUES
('name1',2,5,CURRENT_timestamp),('name2',5,1,CURRENT_timestamp);

UPDATE reservation SET check_out = CURRENT_timestamp, booked = false WHERE userName='ha' AND booked = true;

CREATE TABLE items(
item_id int AUTO_INCREMENT NOT NULL,
item  VARCHAR(50) NOT NULL CHECK(item !=''),
item_stock INT NOT NULL,
item_price INT NOT NULL,
PRIMARY KEY (item_id)
);
INSERT INTO items (item, item_stock, item_price) VALUES
('Pasta',10,150),('Noodles',10,100),('Drink',10,30),('Sandwich',10,130);
SELECT item_price FROM items WHERE item_id = 1;

CREATE TABLE bought(
buy_id int AUTO_INCREMENT NOT NULL,
item_id int not null,
amount INT NOT NULL default 1, 
buy_date DATETIME default current_timestamp,
userName VARCHAR(50) NOT NULL CHECK(userName !=''),
foreign key (item_id) references items(item_id),
foreign key (userName) references customer(userName),
PRIMARY KEY (buy_id)
);
INSERT INTO bought (item_id, amount, userName) VALUES
(1,1,'name1'),(1,2,'name2'),(3,5,'name3');


SELECT * from bought
LEFT Join items ON items.item_id = bought.item_id; 

select * from bought;
-- DROP TABLE IF EXISTS items;

SELECT * from customer;
SELECT * from roomnumber;

SELECT * from room 
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName;

select * from items
LEFT JOIN bought ON bought.item_id = items.item_id;

SELECT * from room 
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName;

Create VIEW reservationBooked AS
SELECT room.room_name, roomNumber.room_nmbr, reservation.check_in, reservation.userName, reservation.booked
FROM room
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName
WHERE reservation.booked = true;

-- dROP View reservationNotBooked;
Create VIEW reservationNotBooked AS
SELECT room.room_name, room.price, roomNumber.room_nmbr,reservation.booked
FROM room
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName
WHERE reservation.booked is not true;

-- DRop view Allrooms;

Create VIEW allRooms AS
SELECT room.room_name, room.price, roomnumber.room_nmbr, reservation.booked, customer.userName
FROM room
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName
WHERE booked IS NOT true
-- AND booked IS not false
ORDER BY booked;

drop view checkOut;
Create VIEW checkIn AS
SELECT room.room_name, room.price, roomnumber.room_nmbr, reservation.booked, customer.userName
FROM room
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName
WHERE booked IS true
OR booked IS NOT false;





drop view customerInfo;
drop view customerInfoCheckedIn;
Create VIEW customerInfoCheckedIn AS
-- room.room_name, room.price,
SELECT customer.userName,items.item,  bought.amount, items.item_price, bought.buy_date
FROM room
LEFT join roomnumber ON roomnumber.room_type_id = room.room_type_id
LEFT join reservation ON reservation.room_nmbr = roomnumber.room_nmbr
LEFT join customer ON customer.userName = reservation.userName
LEFT JOIN bought ON bought.userName = customer.userName
LEFT JOIN items on items.item_id = bought.item_id
WHERE booked IS true

-- WHERE customer.username = 'ha';
-- GROUP BY userName;
-- WHERE booked IS NOT true;
-- ORDER BY booked;




SELECT * FROM customerInfo Where userName = 'ha';
