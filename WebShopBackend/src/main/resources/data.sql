INSERT INTO users (name, surname, email, password) VALUES
('Marko', 'Markovic', 'marko@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6'),
('Jelena', 'Jovanovic', 'jelena@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6'),
('Nikola', 'Nikolic', 'nikola@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6');

INSERT INTO vehicles (model, image, price_per_day, description, currency) VALUES
('Toyota Corolla', 'toyota_corolla.jpg', 3600.0, 'Compact and fuel efficient', 'RSD'),
('BMW X5', 'bmw_x5.jpg', 2080.0, 'Luxury SUV with comfortable seating', 'RSD'),
('Ford Fiesta', 'ford_fiesta.jpg', 2005.0, 'Small city car', 'RSD');

INSERT INTO insurances (name, price_per_day, currency) VALUES
('Basic Insurance', 1550.0, 'RSD'),
('Full Coverage', 1750.0, 'RSD'),
('Theft Protection', 1770.0, 'RSD');

INSERT INTO additional_service (name, price, currency) VALUES
('GPS Navigation', 3.0, 'RSD'),
('Child Seat', 4.0, 'RSD'),
('Extra Driver', 6.0, 'RSD');
--
-- INSERT INTO reservation (total_price, currency, date_from, date_to, payment_status, user_id, insurance_id) VALUES
-- (50.0, 'RSD', '2026-01-10', '2026-01-12', 0, 1, 1),
-- (200.0, 'RSD', '2026-01-15', '2026-01-20', 0, 1, 2);
--
-- INSERT INTO reservation_additional_service (reservation_id, additional_service_id) VALUES
-- (1, 1),
-- (1, 2);

