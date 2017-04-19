CREATE TABLE user2transporter (
  transporter_id BIGSERIAL NOT NULL REFERENCES transporter_area (id),
  username VARCHAR(100) NOT NULL REFERENCES users(username),
  authority VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX user2transporter_uniq_tua ON user2transporter (transporter_id, username, authority);

CREATE TABLE transporter_area (
  id BIGSERIAL PRIMARY KEY,
  admin_name VARCHAR(100),
  domain_name VARCHAR(254)
);
CREATE UNIQUE INDEX transporter_area_uniq_a ON transporter_area (admin_name);
CREATE UNIQUE INDEX transporter_area_uniq_d ON transporter_area (domain_name);

CREATE TABLE transporter (
  id BIGSERIAL NOT NULL REFERENCES transporter (id)
);

CREATE TABLE country (
  code CHAR(2) PRIMARY KEY,
  name VARCHAR (100) NOT NULL
);

CREATE TABLE route (
  id BIGSERIAL PRIMARY KEY,
  transporter_area_id BIGSERIAL NOT NULL REFERENCES transporter_area(id),
  name VARCHAR (100) NOT NULL,
  lock_owner VARCHAR(100) REFERENCES users(username)
);
CREATE UNIQUE INDEX route_uniq_tn ON route (transporter_area_id, name);

CREATE TABLE route_point (
  route_id BIGSERIAL NOT NULL REFERENCES route(id),
  id BIGSERIAL PRIMARY KEY,
--  code VARCHAR(40) NOT NULL,
  name VARCHAR(100) NOT NULL,
  country_code CHAR(2) NOT NULL REFERENCES country (code) ,
  address VARCHAR(100) NOT NULL,
  sequence INT NOT NULL
);
CREATE UNIQUE INDEX route_point_uniq_rs ON route_point (route_id, sequence);

-- CREATE TYPE route_direction AS ENUM ('F', 'R');
CREATE TABLE route_point_data (
  route_point_id BIGSERIAL PRIMARY KEY NOT NULL REFERENCES route_point(id),
  direction CHAR(1) NOT NULL, -- (F|R)
  --   direction route_direction NOT NULL,
  arrival TIME NOT NULL /*without time zone*/,
  departure TIME NOT NULL /*without time zone*/,
  trip_time BIGINT NOT NULL,
  distance INT NOT NULL
);
CREATE UNIQUE INDEX route_point_data_uniq_rd ON route_point_data (route_point_id, direction);
