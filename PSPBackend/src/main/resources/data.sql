INSERT INTO public.merchants(
    port, merchant_id, merchant_email, error_url, failed_url, merchant_password, seller_url, success_url)
VALUES (80, 1, 'cars@gmail.com', '', '', '$2a$12$vMJga57Pqt4ZwktqirCGF.MUaVR0Fi4l8EUlSOqu05zUylEwlPTrm', '', '');

INSERT INTO public.payment_methods(
    is_enabled, id, merchant_id, payment_method)
VALUES (true, 1, 1, 'CREDIT_CARD'),
       (false, 2, 1, 'QR_CODE');