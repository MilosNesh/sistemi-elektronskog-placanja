--TRUNCATE TABLE merchant RESTART IDENTITY CASCADE;

INSERT INTO merchant (name, account_number, active) VALUES
 ( 'Merchant One',   'ACC-100001', true);

INSERT INTO customer (full_name, balance, account_number, card_last4) VALUES
                    ( 'Marko Markovic', 100000.00, 'ACC-100001', '1111')