CREATE TABLE stop_place (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  address_id BIGINT NOT NULL REFERENCES address_i(id)
--   lat
--   lon
--   geo_position_id BIGINT NOT NULL REFERENCES geo_position(id)
--   stop_point_id BIGINT NOT NULL REFERENCES stop_point(id)
);
CREATE UNIQUE INDEX stop_place_uniq_in ON stop_place(id, name);
CREATE UNIQUE INDEX stop_place_uniq_na ON stop_place(name, address_id);

CREATE TABLE pathway ( -- not implemented
--   id BIGSERIAL PRIMARY KEY,
  stop_a BIGINT NOT NULL REFERENCES stop_place(id),
  stop_b BIGINT NOT NULL REFERENCES stop_place(id),
  distance INTEGER NOT NULL CHECK (distance > 0),
  PRIMARY KEY (stop_a, stop_b)
);
-- CREATE UNIQUE INDEX pathway_uniq_abd ON pathway (stop_a, stop_b, distance);

