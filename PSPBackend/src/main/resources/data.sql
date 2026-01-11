INSERT INTO public.merchants(
    port, merchant_id, merchant_email, error_url, failed_url, merchant_password, seller_url, success_url)
VALUES (80, 1, 'cars@gmail.com', 'https://localhost:8444/payment/error', 'https://localhost:8444/payment/fail', '$2a$12$vMJga57Pqt4ZwktqirCGF.MUaVR0Fi4l8EUlSOqu05zUylEwlPTrm', '', 'https://localhost:8444/payment/success');


INSERT INTO public.payment_methods(
    payment_method_id, type, image, description)
VALUES
    (1, 'CREDIT_CARD', '/images/credit-card.png', 'Pay with Visa, Master Card'),
    (2, 'QR_CODE', '/images/qrcode.png', 'Instant pay');


INSERT INTO public.merchant_payment_method(
    merchant_id, payment_method_id, is_enabled)
VALUES
    (1, 1, true),  -- CREDIT_CARD
    (1, 2, false); -- QR_CODE