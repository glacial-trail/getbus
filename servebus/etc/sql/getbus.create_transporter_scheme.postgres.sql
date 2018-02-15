CREATE TABLE user2transporter (
  transporter_id BIGINT NOT NULL REFERENCES transporter_area (id),
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
  id BIGINT NOT NULL REFERENCES transporter_area (id)
);

CREATE TABLE country (
  code CHAR(2) PRIMARY KEY,
  sequence INTEGER,
  name VARCHAR (100) NOT NULL
);

CREATE TABLE route (
  id BIGSERIAL PRIMARY KEY,
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id),
  name VARCHAR (100) NOT NULL,
  base_price DECIMAL(6,2) NOT NULL,
  base_seats_qty INTEGER NOT NULL,
  start_sales TIMESTAMPTZ NOT NULL,
  sales_depth INTEGER NOT NULL,
  lock_owner VARCHAR(100) REFERENCES users(username)
);
CREATE UNIQUE INDEX route_uniq_tn ON route (transporter_area_id, name);

CREATE TABLE way_point (
  route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
  id BIGSERIAL PRIMARY KEY,
--  code VARCHAR(40) NOT NULL,
  name VARCHAR(100) NOT NULL,
  country_code CHAR(2) NOT NULL REFERENCES country (code) ,
  address VARCHAR(100) NOT NULL,
  sequence INT NOT NULL
);
CREATE UNIQUE INDEX way_point_uniq_rs ON way_point (route_id, sequence);

-- CREATE TYPE route_direction AS ENUM ('F', 'R');
CREATE TABLE way_point_data (
  way_point_id BIGINT REFERENCES way_point(id) ON DELETE CASCADE,
  direction CHAR(1) NOT NULL, -- (F|R)
  --   direction route_direction NOT NULL,
  arrival TIME NOT NULL /*without time zone*/,
  departure TIME NOT NULL /*without time zone*/,
  trip_time BIGINT NOT NULL,
  distance INT NOT NULL
);
CREATE UNIQUE INDEX way_point_data_uniq_rd ON way_point_data (way_point_id, direction);

CREATE TABLE TZ (
  zone VARCHAR(16) PRIMARY KEY
);
--TODO? move route id and direction to another table(link table)? to share periodicity with many routes
CREATE TABLE periodicity (
  id BIGSERIAL PRIMARY KEY,
  route_id BIGINT NOT NULL REFERENCES route(id),
  route_direction CHAR(1) NOT NULL,
--   start DATE NOT NULL,
  start TIMESTAMPTZ NOT NULL,
  --tz VARCHAR(16) NOT NULL,-- REFERENCES TZ(zone),
  strategy VARCHAR(16) NOT NULL,
  param INT NOT NULL
);

CREATE UNIQUE INDEX way_point_data_uniq_p ON periodicity (route_id, route_direction);
