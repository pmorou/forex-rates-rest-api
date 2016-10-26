CREATE TABLE currencies (
  id bigint PRIMARY KEY,
  code_name varchar(255) NOT NULL,
  "precision" integer NOT NULL
);

CREATE TABLE rates (
  id bigint PRIMARY KEY,
  date date NOT NULL,
  exchange_rate numeric(15,7) NOT NULL,
  currencies_id bigint NOT NULL,
  FOREIGN KEY (currencies_id) REFERENCES currencies (id)
);