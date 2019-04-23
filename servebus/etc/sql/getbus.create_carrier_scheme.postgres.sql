CREATE TABLE user2transporter (
  transporter_id BIGINT NOT NULL REFERENCES transporter_area (id),
  username VARCHAR(100) NOT NULL REFERENCES users(username),
  authority VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX user2transporter_uniq_tua ON user2transporter (transporter_id, username, authority);

CREATE TABLE transporter_area (
  id BIGSERIAL PRIMARY KEY,
  admin_name VARCHAR(100)
--   domain_name VARCHAR(254)
);
CREATE UNIQUE INDEX transporter_area_uniq_a ON transporter_area (admin_name);
-- CREATE UNIQUE INDEX transporter_area_uniq_d ON transporter_area (domain_name);

CREATE TABLE transporter (
  id BIGINT NOT NULL REFERENCES transporter_area (id)
);

CREATE TABLE transporter_stop_place ( -- not implemented
  transporter_area_id BIGINT NOT NULL REFERENCES transporter_area(id) ON DELETE CASCADE,
  stop_id BIGINT NOT NULL REFERENCES stop_place(id) ON DELETE RESTRICT,
  name VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX transporter_stop_uniq_tn ON transporter_stop_place (transporter_area_id, name);


CREATE TABLE carrier_store (
  id BIGSERIAL PRIMARY KEY,
  carrier_id BIGINT NOT NULL REFERENCES transporter_area(id),
  domain_name VARCHAR(254),
  global_name VARCHAR(254)
);
CREATE UNIQUE INDEX carrier_store_uniq_d ON carrier_store (domain_name);
CREATE UNIQUE INDEX carrier_store_uniq_g ON carrier_store (global_name);

-- CREATE TABLE carrier_store_l10n (
--   id BIGINT NOT NULL REFERENCES carrier_store(id),
--   lang CHAR(2) NOT NULL REFERENCES lang(code),
--   name VARCHAR(254),
--   short_description VARCHAR(8848),
--   long_description VARCHAR(8848)
-- );
-- CREATE UNIQUE INDEX carrier_store_l10n_uniq_n ON carrier_store_l10n (name);


CREATE TABLE carrier_store_payment_details_bank_ua (
--   id BIGSERIAL PRIMARY KEY, -- for future use?
  carrier_store_id BIGINT PRIMARY KEY REFERENCES carrier_store(id),
  phone VARCHAR(15) NOT NULL,
  account VARCHAR(32) NOT NULL,
  mfo VARCHAR(32) NOT NULL,
  okpo VARCHAR(32) NOT NULL
);
CREATE UNIQUE INDEX carrier_store_payment_details_bank_ua_a ON public.carrier_store_payment_details_bank_ua (account);
CREATE UNIQUE INDEX carrier_store_payment_details_bank_ua_m ON public.carrier_store_payment_details_bank_ua (mfo);
CREATE UNIQUE INDEX carrier_store_payment_details_bank_ua_o ON public.carrier_store_payment_details_bank_ua (okpo);
