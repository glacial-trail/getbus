CREATE TABLE country (
  code CHAR(2) PRIMARY KEY,
  sequence INTEGER
);

CREATE TABLE country_l10n ( -- not implemented
  code CHAR(2) NOT NULL REFERENCES country(code),
  lang CHAR(2) NOT NULL REFERENCES lang(code),
  name VARCHAR (100) NOT NULL
);
CREATE UNIQUE INDEX country_l10n_uniq_cl ON country_l10n (code, lang);

CREATE TABLE admin_area1 (
  country CHAR(2) NOT NULL REFERENCES country(code),
  code CHAR(2) PRIMARY KEY
);

CREATE TABLE admin_area1_l10n ( -- not implemented
  lang CHAR(2) NOT NULL REFERENCES lang(code),
  code CHAR(2) NOT NULL REFERENCES admin_area1 (code) ON UPDATE CASCADE,
  name VARCHAR (100) NOT NULL
);
CREATE UNIQUE INDEX admin_area1_l10n_uniq_cl ON admin_area1_l10n (code, lang);

CREATE TABLE address_i (
  id BIGSERIAL PRIMARY KEY,
  country CHAR(2) NOT NULL REFERENCES country (code),
  admin_area1 CHAR(2) REFERENCES admin_area1 (code) ON UPDATE CASCADE,
  zone_id VARCHAR(50) REFERENCES time_zone(zone_id),
  zip VARCHAR(10),
  utc_offset INTEGER NOT NULL
);

CREATE TABLE address_l10n (
  lang CHAR(2) NOT NULL REFERENCES lang(code),
  id BIGINT NOT NULL REFERENCES address_i(id) ON DELETE CASCADE,
  admin_area1 VARCHAR(100),
  city VARCHAR(100) NOT NULL,
  street_building VARCHAR(100) NOT NULL,
  street VARCHAR(100) NOT NULL,
  building VARCHAR(100) NOT NULL
);
