TRUNCATE TABLE merchant RESTART IDENTITY CASCADE;

INSERT INTO merchant (name, account_number, active) VALUES
 ( 'Merchant One',   'ACC-100001', true);