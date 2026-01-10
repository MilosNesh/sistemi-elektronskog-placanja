INSERT INTO users (name, surname, email, password) VALUES
('Marko', 'Markovic', 'marko@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6'),
('Jelena', 'Jovanovic', 'jelena@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6'),
('Nikola', 'Nikolic', 'nikola@gmail.com', '$2a$12$0CNp/4lKQAp9imFVF7layu6tSMC4u2jOLa.t0Vpg9h/f5GZwce5G6');

INSERT INTO vehicles (model, image, price_per_day, description, currency) VALUES
('Toyota Corolla', 'assets/toyota_corolla.jpg', 3600.0, 'A reliable and fuel-efficient compact car, ideal for both daily commuting and longer trips. The Toyota Corolla offers a comfortable interior, smooth driving experience, and low fuel consumption, making it a perfect choice for city driving as well as highway journeys. Known for its durability and safety, it is an excellent option for drivers seeking practicality and dependability.', 'RSD'),
('BMW X5', 'assets/bmw_x5.jpg', 2080.0, 'A luxury SUV that combines powerful performance, premium comfort, and modern design. The BMW X5 features a spacious cabin, high-quality materials, and advanced technology that enhance both safety and driving pleasure. It is ideal for families, business trips, or anyone looking for a prestigious and comfortable driving experience.', 'RSD'),
('Ford Fiesta', 'assets/ford_fiesta.jpg', 2005.0, 'A compact city car designed for easy urban driving and convenient parking. The Ford Fiesta is known for its agile handling, economical fuel consumption, and practical interior layout. It is an excellent choice for drivers who want a reliable, efficient, and affordable vehicle for everyday city use.', 'RSD');

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

