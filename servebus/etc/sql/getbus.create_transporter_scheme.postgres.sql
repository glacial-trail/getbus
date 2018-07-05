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

CREATE TABLE transporter_stop_place ( -- not implemented
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id) ON DELETE CASCADE,
  stop_id BIGINT NOT NULL REFERENCES stop_place(id) ON DELETE RESTRICT,
  name VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX transporter_stop_uniq_tn ON transporter_stop_place (transporter_area_id, name);