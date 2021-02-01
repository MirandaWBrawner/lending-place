USE lending;
SELECT * FROM lendable;
DESC lendable;
SELECT * FROM item_type;
DELETE FROM item_type WHERE type_id = 6;
SELECT lendable_id, name_en_us FROM lendable;
DESC lendable_type_join_table;
INSERT INTO lendable_type_join_table
	(lendable_id, type_id)
    VALUES (1, 1), (2, 3), (2, 4), (3, 2),
    (3, 4), (5, 1), (6, 1);
SELECT * FROM lendable_type_join_table;

SELECT lt.lendable_id, lt.creator, 
	lt.name_ar_eg, jt.name_ar_eg cat_ar_eg,
	lt.name_en_us, jt.name_en_us cat_en_us,
    lt.name_es_mx, jt.name_es_mx cat_es_mx,
    lt.name_fr_fr, jt.name_fr_fr cat_fr_fr,
    lt.name_hi_in, jt.name_hi_in cat_hi_in,
    lt.name_sw_tz, jt.name_sw_tz cat_sw_tz,
    lt.name_zh_cn, jt.name_zh_cn cat_zh_cn
	FROM lendable lt LEFT JOIN (
	SELECT j.lendable_id, tt.image_path, tt.name_ar_eg, 
    tt.name_en_us, tt.name_es_mx, tt.name_fr_fr, tt.name_hi_in,
    tt.name_sw_tz, tt.name_zh_cn 
    FROM lendable_type_join_table j, item_type tt
	WHERE j.type_id = tt.type_id
) jt ON lt.lendable_id = jt.lendable_id;

DELETE FROM lendable WHERE lendable_id = 8;
INSERT INTO lendable_type_join_table
	(lendable_id, type_id)
    VALUES (7, 1);

UPDATE lendable SET image_path = ''
	WHERE image_path IS NULL;

DESC library_user;
DESC user_role_join_table;
ALTER TABLE user_role_join_table
	ADD COLUMN join_key INT PRIMARY KEY AUTO_INCREMENT;

SELECT * FROM role;
SELECT * FROM library_user;
SELECT * FROM user_role_join_table;
INSERT INTO library_user (user_id, username, password, email, language)
	VALUES (-7, 'asdfjksjfiase', 'alsnjfdajskfwefew', 'mskdf@sjidfjs.com', 'en');
INSERT INTO user_role_join_table (user_id, role_id)
	VALUES (2, 1);
INSERT INTO library_user (user_id, username, password, email, language)
	VALUES (-6, 'syf7389afw7wfasdfh', 'sajsfshdfusfa23', 'sfa3nsdfnasdhbj@usadfn.com', 'fr');
    
SET foreign_key_checks = 0;

ALTER TABLE user_role_join_table
	DROP FOREIGN KEY user_role_join_table_ibfk_1,
    DROP FOREIGN KEY user_role_join_table_ibfk_2;
ALTER TABLE user_role_join_table
    ADD CONSTRAINT FOREIGN KEY (user_id) 
		REFERENCES library_user (user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    ADD CONSTRAINT FOREIGN KEY (role_id) 
		REFERENCES role (role_id)
		ON UPDATE CASCADE
        ON DELETE SET NULL;
        
SELECT * FROM library_user, user_role_join_table, role
	WHERE library_user.user_id = user_role_join_table.user_id
    AND user_role_join_table.role_id = role.role_id;
    
UPDATE library_user SET language = 'sw';
DELETE FROM library_user WHERE  user_id = 2 OR user_id = 1;
INSERT INTO lendable (name_en_us, name_hi_in,
    name_sw_tz, name_ar_eg, name_zh_cn,
    name_es_mx, name_fr_fr, creator, image_path, number_available)
	VALUES ('The Jungle', 'जंगल', 'Mwitu', 'الأدغال', '丛林', 'La Maraña', 'La Jungle', 'Upton Sinclair', '', 2),
    ('Phone Charger', 'चार्जर', 'Chaja Ya Simu', 'شاحن', '充电器', 'Cargador', 'Chargeur', 'Apple', '', 5),
    ('Bicycle', 'साइकिल', 'Baisikeli', 'دَرَّاجَة', '自行车', 'Bicicleta', 'Vélo', 'Chicago Bicycle Company', '', 3);
INSERT INTO role (name_en_us, name_hi_in,
    name_sw_tz, name_ar_eg, name_zh_cn,
    name_es_mx, name_fr_fr) 
    VALUES ('User', 'HINDI', 'SWAHILI', 'مُستخدِم',
    'CHINESE', 'SPANISH', 'FRENCH'), 
    ('Librarian', 'HINDI', 'SWAHILI', 'أمين المكتبة',
    'CHINESE', 'Bibliotecario', 'FRENCH');
    
UPDATE role SET name_es_mx = 'Bibliotecarix', name_fr_fr = 'Documentaliste'
	WHERE role_id = 2;
UPDATE role SET name_fr_fr = 'Internaute'
	WHERE role_id = 1;
    
UPDATE lendable SET number_available = 0, name_en_us = 'Example Book', image_path = ''
	WHERE lendable_id = '9';
SELECT * FROM lendable WHERE lendable_id = 9;

SHOW CREATE TABLE community_member;
SELECT * FROM community_member;
INSERT INTO community_member (member_name, member_email, member_phone)
	VALUES ('Guest', 'guest@example.com', '1-555-555-5555');
UPDATE lendable SET number_available = 1000
	WHERE lendable_id = 3;
SELECT * FROM items_on_loan;