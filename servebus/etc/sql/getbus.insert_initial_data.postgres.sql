-- TODO UPSERT

INSERT INTO lang (code) VALUES ('--');
INSERT INTO lang (code) VALUES ('uk');
INSERT INTO lang (code) VALUES ('en');
INSERT INTO lang (code) VALUES ('ru');

INSERT INTO country (code, sequence) VALUES ('UA', 10);
INSERT INTO country (code, sequence) VALUES ('PL', 20);
INSERT INTO country (code, sequence) VALUES ('CZ', 30);

INSERT INTO time_zone (zone_id, utc_offset) VALUES ('Europe/Kiev', 120);