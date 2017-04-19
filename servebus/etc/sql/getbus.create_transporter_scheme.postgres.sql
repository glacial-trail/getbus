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
