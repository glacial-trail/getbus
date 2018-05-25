CREATE TABLE lang (
  code CHAR(2) NOT NULL PRIMARY KEY
);

CREATE TABLE users (
--   TODO id BIGSERIAL
  username VARCHAR(100) PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  phone VARCHAR(15) UNIQUE NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(100) NOT NULL REFERENCES users(username),
  authority VARCHAR(50) NOT NULL
);

CREATE TABLE user_profile (
  username VARCHAR(100) NOT NULL REFERENCES users(username),
  time_zone VARCHAR(50) NOT NULL REFERENCES time_zone(zone_id)
);

CREATE TABLE time_zone (
  zone_id VARCHAR(50) PRIMARY KEY,
  utc_offset INTEGER NOT NULL
);
