-- CREATE TYPE route_direction AS ENUM ('F', 'R');
CREATE TABLE round_route (
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id),
  forward_route_id BIGINT NOT NULL REFERENCES route(id),
  reverse_route_id BIGINT NOT NULL REFERENCES route(id)
);
CREATE UNIQUE INDEX round_route_uniq_tfr ON round_route (transporter_area_id, forward_route_id, reverse_route_id);


CREATE TABLE voyage (
  id BIGSERIAL PRIMARY KEY,
  route_id BIGSERIAL NOT NULL REFERENCES route(id),
  revision INT NOT NULL,
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id),
  name VARCHAR (100) NOT NULL,
  price DECIMAL(6,2) NOT NULL CHECK (price > 0),
  seats_qty INTEGER NOT NULL CHECK (seats_qty > 0)
);
-- CREATE UNIQUE INDEX route_uniq_tn ON route (transporter_area_id, name);

CREATE TABLE route_stop (
  route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
  stop_id BIGINT NOT NULL REFERENCES stop_place(id) ON DELETE RESTRICT,
  name VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX route_stop_uniq_rn ON route_stop (route_id, name);

CREATE TABLE route_stops_arrangement (
  route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
  way_point_id BIGINT NOT NULL REFERENCES route_stop (way_point_id),
  sequence INT NOT NULL -- CHECK (sequence > 0)
);
CREATE UNIQUE INDEX route_stops_uniq_rws ON route_stops_arrangement (route_id, way_point_id, sequence);

CREATE TABLE route_length (
  route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
--   route_version BIGINT REFERENCES route(version),
  direction CHAR(1) NOT NULL, -- (F|R),
  route_stop_id BIGINT NOT NULL REFERENCES route_stop(stop_id) ON DELETE CASCADE,
  length INTEGER NOT NULL CHECK (length > 0)
);

--TODO rename to schedule or timetable. move distance to some another place?
-- CREATE TYPE route_direction AS ENUM ('F', 'R');
CREATE TABLE route_timetable (
  route_id BIGINT NOT NULL REFERENCES route (id) ON DELETE CASCADE,
  --   direction route_direction NOT NULL,
  direction CHAR(1) NOT NULL, -- (F|R)
  stop_id BIGINT NOT NULL REFERENCES route_stop (stop_id) ON DELETE CASCADE,
  arrival TIME NOT NULL /*without time zone*/ CHECK (arrival > 0),
  departure TIME NOT NULL /*without time zone*/ CHECK (departure > 0),
  trip_time BIGINT NOT NULL CHECK (trip_time > 0)
);
CREATE UNIQUE INDEX way_point_data_uniq_rd ON way_point_data (way_point_id, direction);




CREATE TABLE route2calendar (
  route_id BIGINT NOT NULL REFERENCES route(id),
--   route_direction CHAR(1) NOT NULL,
  route_calendar_id BIGINT NOT NULL REFERENCES periodicity(id)
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
