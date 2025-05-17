-- users
INSERT INTO users ( name, date_of_birth, password, role, enabled)
VALUES
  ('Ivan Petrov', TO_DATE('22.11.1990', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Anna Ivanova', TO_DATE('15.05.1985', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Oleg Smirnov', TO_DATE('01.01.1992', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Daria Sokolova', TO_DATE('12.12.1980', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Maxim Orlov', TO_DATE('30.06.1995', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Elena Volkova', TO_DATE('25.03.1988', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Artem Fedorov', TO_DATE('05.09.1990', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Maria Belova', TO_DATE('28.08.1993', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Andrey Kiselev', TO_DATE('07.07.1986', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu', 'USER', true),
  ('Natalia Lebedeva', TO_DATE('19.04.1989', 'DD.MM.YYYY'), '$2a$10$Qui803HZ2kadBF59Lg5HOOP0zxt0yecFMm6n6J5XN1pPI02hO70xu0', 'USER', true);

-- users emails
INSERT INTO email_data (user_id, email)
VALUES
  (1, 'ivan.petrov@example.com'),
  (2, 'anna.ivanova@example.com'),
  (3, 'oleg.sidorov@example.com'),
  (4, 'elena.smirnova@example.com'),
  (5, 'dmitry.orlov@example.com'),
  (6, 'olga.nikolaeva@example.com'),
  (7, 'sergey.pavlov@example.com'),
  (8, 'irina.volkova@example.com'),
  (9, 'maxim.egorov@example.com'),
  (10, 'natalia.kuznetsova@example.com');

-- users phones
INSERT INTO phone_data (user_id, phone)
VALUES
  (1, '79201111111'),
  (2, '79202222222'),
  (3, '79203333333'),
  (4, '79204444444'),
  (5, '79205555555'),
  (6, '79206666666'),
  (7, '79207777777'),
  (8, '79208888888'),
  (9, '79209999999'),
  (10, '79201010101');

-- users accounts
INSERT INTO account (user_id, balance, initial_balance_limit)
VALUES
  (1, 100.00, 207.00),
  (2, 150.00, 310.50),
  (3, 200.00, 414.00),
  (4, 50.00, 103.50),
  (5, 120.00, 248.40),
  (6, 300.00, 621.00),
  (7, 80.00, 165.60),
  (8, 400.00, 828.00),
  (9, 60.00, 124.20),
  (10, 500.00, 1035.00);