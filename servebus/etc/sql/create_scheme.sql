CREATE TABLE users (
  username VARCHAR(100) NOT NULL PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  phone CHAR(13) NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(100) NOT NULL REFERENCES users(username),
  authority VARCHAR(50) NOT NULL
);
