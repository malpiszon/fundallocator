CREATE TABLE fund (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  fund_type VARCHAR(31) NOT NULL
);

INSERT INTO fund (name, fund_type) VALUES
  ('Fundusz Polski 1', 'POLISH'),
  ('Fundusz Polski 2', 'POLISH'),
  ('Fundusz Polski 3', 'POLISH'),
  ('Fundusz Zagraniczny 1', 'FOREIGN'),
  ('Fundusz Zagraniczny 2', 'FOREIGN'),
  ('Fundusz Zagraniczny 3', 'FOREIGN'),
  ('Fundusz Pieniężny 1', 'FINANCIAL');