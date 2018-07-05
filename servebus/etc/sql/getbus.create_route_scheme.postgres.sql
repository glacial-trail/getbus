-- CREATE TYPE route_direction AS ENUM ('F', 'R');

CREATE TABLE route (
  id BIGSERIAL PRIMARY KEY,
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id),
  name VARCHAR (100) NOT NULL,
  base_price DECIMAL(6,2) NOT NULL CHECK (base_price > 0),
  base_seats_qty INTEGER NOT NULL CHECK (base_seats_qty > 0),
  start_sales TIMESTAMPTZ NOT NULL CHECK (start_sales > 0),
  sales_depth INTEGER NOT NULL CHECK (sales_depth > 0),
  lock_owner VARCHAR(100) REFERENCES users(username)
);
CREATE UNIQUE INDEX route_uniq_tn ON route (transporter_area_id, name);

CREATE TABLE route_stop (
  route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
  stop_id BIGINT NOT NULL REFERENCES stop_place(id) ON DELETE RESTRICT,
  name VARCHAR(100),
  sequence INT NOT NULL, -- CHECK (sequence > 0)
  PRIMARY KEY (route_id, stop_id)
);
CREATE UNIQUE INDEX route_stop_uniq_rn ON route_stop (route_id, name);
CREATE UNIQUE INDEX route_stop_uniq_rss ON route_stop (route_id, stop_id, sequence);

CREATE TABLE route_length (
  route_id BIGINT NOT NULL,
--   route_version BIGINT REFERENCES route(version),
  route_stop_id BIGINT NOT NULL,
  direction CHAR(1) NOT NULL, -- (F|R),
  distance INTEGER NOT NULL CHECK (distance >= 0),
  PRIMARY KEY (route_id, route_stop_id, direction),
  FOREIGN KEY (route_id, route_stop_id) REFERENCES route_stop(route_id, stop_id) ON DELETE CASCADE
);

-- CREATE TYPE route_direction AS ENUM ('F', 'R');
CREATE TABLE route_timetable (
  route_id BIGINT NOT NULL,
  route_stop_id BIGINT NOT NULL,
  --   direction route_direction NOT NULL,
  direction CHAR(1) NOT NULL, -- (F|R)
  arrival TIME NOT NULL, --without time zone
  departure TIME NOT NULL, --without time zone
  trip_time BIGINT NOT NULL CHECK (trip_time >= 0),
  PRIMARY KEY (route_id, route_stop_id, direction),
  FOREIGN KEY (route_id, route_stop_id) REFERENCES route_stop(route_id, stop_id) ON DELETE CASCADE
);

--TODO? move route id and direction to another table(link table)? to share periodicity with many routes
CREATE TABLE periodicity ( --rename to route_calendar
  id BIGSERIAL PRIMARY KEY,
  route_id BIGINT NOT NULL REFERENCES route(id),
  route_direction CHAR(1) NOT NULL,
--   start DATE NOT NULL,
  start TIMESTAMPTZ,
  "end" TIMESTAMPTZ,
  strategy VARCHAR(16) NOT NULL,
  param INT NOT NULL
);
CREATE UNIQUE INDEX way_point_data_uniq_p ON periodicity (route_id, route_direction);