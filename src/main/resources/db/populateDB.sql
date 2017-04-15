DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories) VALUES
  (100000, '2017-04-10 08:00:00', 'Breakfast', 500),
  (100000, '2017-04-10 13:00:00', 'Lunch', 1000),
  (100000, '2017-04-10 20:00:00', 'Dinner', 800),
  (100000, '2017-04-11 08:00:00', 'Breakfast', 500),
  (100000, '2017-04-11 13:00:00', 'Lunch', 1200),
  (100001, '2017-04-11 08:00:00', 'Breakfast', 550),
  (100001, '2017-04-11 13:00:00', 'Lunch', 850);
