CREATE SCHEMA IF NOT EXISTS lending;
USE lending;
DROP TABLE IF EXISTS donation;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS user_role_join_table;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS library_user;
DROP TABLE IF EXISTS items_on_loan;
DROP TABLE IF EXISTS community_member;
DROP TABLE IF EXISTS lendable_type_join_table;
DROP TABLE IF EXISTS lendable;
DROP TABLE IF EXISTS item_type;
CREATE TABLE item_type (
	type_id INT PRIMARY KEY AUTO_INCREMENT,
	image_path VARCHAR(1000),
    name_en_us VARCHAR(250),
    name_hi_in VARCHAR(250),
    name_sw_tz VARCHAR(250),
    name_ar_eg VARCHAR(250),
	name_zh_cn VARCHAR(250),
    name_es_mx VARCHAR(250),
    name_fr_fr VARCHAR(250)
);
CREATE TABLE lendable (
	lendable_id INT PRIMARY KEY AUTO_INCREMENT,
    number_available INT,
	image_path VARCHAR(1000),
    creator VARCHAR(1000),
    name_en_us VARCHAR(250),
    name_hi_in VARCHAR(250),
    name_sw_tz VARCHAR(250),
    name_ar_eg VARCHAR(250),
	name_zh_cn VARCHAR(250),
    name_es_mx VARCHAR(250),
    name_fr_fr VARCHAR(250)
);
CREATE TABLE lendable_type_join_table (
	lendable_id INT,
    type_id INT,
    FOREIGN KEY (lendable_id) REFERENCES lendable(lendable_id),
    FOREIGN KEY (type_id) REFERENCES item_type(type_id)
);
CREATE TABLE community_member (
	member_id INT PRIMARY KEY AUTO_INCREMENT,
    member_name VARCHAR(1000),
    member_email VARCHAR(1000),
    member_phone VARCHAR(20)
);
CREATE TABLE items_on_loan (
	instance_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	member_id INT,
    lendable_id INT,
    loan_start_date TIMESTAMP,
    due_date TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES community_member (member_id),
    FOREIGN KEY (lendable_id) REFERENCES lendable (lendable_id)
);
CREATE TABLE library_user (
	user_id INT PRIMARY KEY,
    username VARCHAR(250) UNIQUE,
    password VARCHAR(1000),
    email VARCHAR(1000),
    language VARCHAR(250)
);
CREATE TABLE role (
	role_id INT PRIMARY KEY AUTO_INCREMENT,
	name_en_us VARCHAR(250),
    name_hi_in VARCHAR(250),
    name_sw_tz VARCHAR(250),
    name_ar_eg VARCHAR(250),
	name_zh_cn VARCHAR(250),
    name_es_mx VARCHAR(250),
    name_fr_fr VARCHAR(250)
);
CREATE TABLE user_role_join_table (
	join_key INT PRIMARY KEY AUTO_INCREMENT,
	user_id INT,
    role_id INT
    /*
    FOREIGN KEY (user_id) REFERENCES library_user (user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (role_id) REFERENCES role (role_id)
		ON UPDATE CASCADE
        ON DELETE SET NULL */
);
CREATE TABLE request (
	request_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(1000),
    content VARCHAR(20000),
    date_posted TIMESTAMP,
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES community_member (member_id)
);
CREATE TABLE donation (
	donation_id INT PRIMARY KEY AUTO_INCREMENT,
    donor_name VARCHAR(1000),
	amount DOUBLE,
    donation_date TIMESTAMP
);