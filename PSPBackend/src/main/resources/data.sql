INSERT INTO public.merchants(
    port, merchant_id, merchant_email, error_url, failed_url, merchant_password, seller_url, success_url, role,
    failed_attempts, lock_until, mfa_code)
VALUES (80, 1, 'onlybuns1@gmail.com',
        'https://host.minikube.internal:8444/payment/error',
        'https://host.minikube.internal:8444/payment/fail',
        '$2a$12$vqucDycnkT51NuOTnVWtfuAmKHCdIIQmCn3G.fYCMIFFyXUdyekLe',
        'https://localhost:4300/payment/',
        'https://host.minikube.internal:8444/payment/success',
        'ROLE_MERCHANT',
        0,
        NULL,
        NULL)
    ON CONFLICT (merchant_id) DO NOTHING;

INSERT INTO public.merchants(
    merchant_id,
    merchant_email,
    merchant_password,
    role,
    port, error_url, failed_url, seller_url, success_url, failed_attempts, lock_until, mfa_code
)
VALUES (
           2,
           'markopetr062@gmail.com',
           '$2a$12$vqucDycnkT51NuOTnVWtfuAmKHCdIIQmCn3G.fYCMIFFyXUdyekLe', -- ista lozinka kao za merchant
           'ROLE_ADMIN',
           NULL, NULL, NULL, NULL, NULL,
           0,
           NULL,
           NULL
       ) ON CONFLICT (merchant_id) DO NOTHING;

INSERT INTO public.merchants(
    merchant_id,
    merchant_email,
    merchant_password,
    role,
    port, error_url, failed_url, seller_url, success_url, failed_attempts, lock_until, mfa_code
)
VALUES (
           3,
           'nenaddubovac056@gmail.com',
           '$2a$12$vqucDycnkT51NuOTnVWtfuAmKHCdIIQmCn3G.fYCMIFFyXUdyekLe', -- ista lozinka kao za merchant
           'ROLE_SUPERADMIN',
           NULL, NULL, NULL, NULL, NULL,
        0,
           NULL,
           NULL
       ) ON CONFLICT (merchant_id) DO NOTHING;;

SELECT setval('merchants_merchant_id_seq', (SELECT MAX(merchant_id) FROM merchants));

INSERT INTO public.payment_methods(
    payment_method_id, type, image, description, is_available)
VALUES
    (1, 'CREDIT_CARD', '/images/credit-card.png', 'Pay with Visa, Master Card', true),
    (2, 'QR_CODE', '/images/qrcode.png', 'Instant pay', true),
    (3, 'CRYPTO', '/images/ethereum.png', 'Pay with Ethereum', true),
    (4, 'PAYPAL', '/images/paypal.png', 'Pay with PayPal', false);


SELECT setval('payment_methods_payment_method_id_seq', (SELECT MAX(payment_method_id) FROM payment_methods));

INSERT INTO public.merchant_payment_method(
    merchant_id, payment_method_id, is_enabled)
VALUES
    (1, 1, true),  -- CREDIT_CARD
    (1, 2, true), -- QR_CODE
    (1,3,true), -- CRYPTO
    (1,4, true);

SELECT setval('merchant_payment_method_merchant_payment_method_id_seq',
              (SELECT MAX(merchant_payment_method_id) FROM merchant_payment_method));
