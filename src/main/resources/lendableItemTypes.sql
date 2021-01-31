USE lending;

INSERT INTO item_type(image_path, name_en_us,
    name_hi_in, name_sw_tz, name_ar_eg, name_zh_cn,
    name_es_mx, name_fr_fr)
    VALUES ('', 'Books', 'पुस्तकें', 'Vitabu',
    'كُتُب‎', '书', 'Libros', 'Livres'),
    ('', 'Transport', 'HINDI', 'Usafiri', 'نَقْل',
    '载体', 'Transporte', 'Transport'),
    ('', 'Information Technology', 'सूचना प्रौद्योगिकी',
    'Elektroniki', 'تقنية المعلومات', '信息技术', 'Informática',
    'Informatique'),
    ('', 'Appliances', 'HINDI', 'SWAHILI', 'أَجْهِزَة',
    '器具', 'Aparatos', 'Appareils');
UPDATE item_type 
	SET name_hi_in = 'यंत्र',
    name_sw_tz = 'Vifaa'
	WHERE name_en_us = 'Appliances';
UPDATE item_type
	SET name_hi_in = 'परिवहन'
    WHERE name_en_us = 'Transport';
INSERT INTO item_type(image_path, name_en_us,
    name_hi_in, name_sw_tz, name_ar_eg, name_zh_cn,
    name_es_mx, name_fr_fr)
    VALUES ('', 'Nonfiction', 'अधिसूचना', 'Hadithi za Kweli',
    'غير الخيالية', '非虚构文学', 'Literatura de no ficción', 'Nonfiction');
    