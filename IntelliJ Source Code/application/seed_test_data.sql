BEGIN;

-- Compatibility for the current Java DAO. Some local schemas were created before vehicle_type existed.
ALTER TABLE vehicle ADD COLUMN IF NOT EXISTS vehicle_type VARCHAR(50);

INSERT INTO employee (employee_id, name, password)
VALUES (999, 'Admin', 'Admin999')
ON CONFLICT (employee_id) DO UPDATE
SET name = EXCLUDED.name,
    password = EXCLUDED.password;

INSERT INTO driving_license (license_no, is_category_a, is_category_b, is_category_c, is_category_d)
VALUES
    ('DL10001', false, true, false, false),
    ('DL10002', false, true, false, false),
    ('DL10003', false, true, false, false),
    ('DL10004', false, true, false, false),
    ('DL10005', false, true, false, false),
    ('DL10006', false, true, false, false),
    ('DL10007', false, true, false, false),
    ('DL10008', false, true, false, false),
    ('DL10009', false, true, false, false),
    ('DL10010', false, true, false, false)
ON CONFLICT (license_no) DO UPDATE
SET is_category_a = EXCLUDED.is_category_a,
    is_category_b = EXCLUDED.is_category_b,
    is_category_c = EXCLUDED.is_category_c,
    is_category_d = EXCLUDED.is_category_d;

INSERT INTO customer (customer_id, name, phone_no, email, cpr, passport_no, license_no)
VALUES
    (1, 'Sara Ahmed', '22114455', 'sara@example.com', '010190-1111', 'P10001', 'DL10001'),
    (2, 'Mads Jensen', '22334455', 'mads@example.com', '020290-2222', 'P10002', 'DL10002'),
    (3, 'Lina Ali', '22445566', 'lina@example.com', '030390-3333', 'P10003', 'DL10003'),
    (4, 'Noah Petersen', '22556677', 'noah@example.com', '040490-4444', 'P10004', 'DL10004'),
    (5, 'Emma Hansen', '22667788', 'emma@example.com', '050590-5555', 'P10005', 'DL10005'),
    (6, 'Ali Khan', '22778899', 'ali@example.com', '060690-6666', 'P10006', 'DL10006'),
    (7, 'Sofia Larsen', '22889900', 'sofia@example.com', '070790-7777', 'P10007', 'DL10007'),
    (8, 'Jonas Holm', '22990011', 'jonas@example.com', '080890-8888', 'P10008', 'DL10008'),
    (9, 'Aisha Noor', '22001122', 'aisha@example.com', '090990-9999', 'P10009', 'DL10009'),
    (10, 'Victor Berg', '22112233', 'victor@example.com', '101090-1010', 'P10010', 'DL10010')
ON CONFLICT (customer_id) DO UPDATE
SET name = EXCLUDED.name,
    phone_no = EXCLUDED.phone_no,
    email = EXCLUDED.email,
    cpr = EXCLUDED.cpr,
    passport_no = EXCLUDED.passport_no,
    license_no = EXCLUDED.license_no;

INSERT INTO vehicle (
    vehicle_id, model, vehicle_type, color, engine, plate_no, price_hour,
    deposit, no_of_tire, late_fee, required_license, condition, no_of_seats, current_state
)
VALUES
    (1, 'Toyota Corolla', 'Sedan', 'White', 'Hybrid', 'AB12345', 450, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (2, 'VW Golf', 'Hatchback', 'Black', 'Petrol', 'CD67890', 520, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (3, 'Tesla model 3', 'Electric', 'Blue', 'Electric', 'EF11223', 850, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (4, 'Ford Transit', 'Van', 'Silver', 'Diesel', 'GH44556', 700, 1000, 4, 75, 'B', 'Good', 5, 'rented'),
    (5, 'BMW 320i', 'Sedan', 'Grey', 'Petrol', 'IJ77889', 780, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (6, 'Nissan Qashqai', 'SUV', 'Red', 'Hybrid', 'KL99001', 630, 1000, 4, 75, 'B', 'Good', 5, 'maintenance'),
    (7, 'Mercedes Vito', 'Van', 'White', 'Diesel', 'MN22334', 760, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (8, 'Audi A4', 'Sedan', 'Black', 'Diesel', 'OP55667', 820, 1000, 4, 75, 'B', 'Good', 5, 'rented'),
    (9, 'Hyundai i30', 'Hatchback', 'Green', 'Petrol', 'QR88990', 410, 1000, 4, 75, 'B', 'Good', 5, 'available'),
    (10, 'Volvo XC60', 'SUV', 'Blue', 'Hybrid', 'ST11224', 900, 1000, 4, 75, 'B', 'Good', 5, 'available')
ON CONFLICT (vehicle_id) DO UPDATE
SET model = EXCLUDED.model,
    vehicle_type = EXCLUDED.vehicle_type,
    color = EXCLUDED.color,
    engine = EXCLUDED.engine,
    plate_no = EXCLUDED.plate_no,
    price_hour = EXCLUDED.price_hour,
    deposit = EXCLUDED.deposit,
    no_of_tire = EXCLUDED.no_of_tire,
    late_fee = EXCLUDED.late_fee,
    required_license = EXCLUDED.required_license,
    condition = EXCLUDED.condition,
    no_of_seats = EXCLUDED.no_of_seats,
    current_state = EXCLUDED.current_state;

INSERT INTO booking (
    booking_id, start_date, end_date, actual_return_date,
    booking_status, customer_id, vehicle_id, employee_id
)
VALUES
    (1024, '2026-07-10 00:00:00', '2026-07-15 00:00:00', null, 'ACTIVE', 1, 2, 999),
    (1027, '2026-07-12 00:00:00', '2026-07-14 00:00:00', null, 'ACTIVE', 2, 1, 999),
    (1031, '2026-07-18 00:00:00', '2026-07-20 00:00:00', null, 'PENDING', 6, 3, 999),
    (1034, '2026-07-21 00:00:00', '2026-07-23 00:00:00', null, 'PENDING', 5, 1, 999),
    (1018, '2026-07-03 00:00:00', '2026-07-06 00:00:00', null, 'ACTIVE', 4, 2, 999),
    (1020, '2026-07-01 00:00:00', '2026-07-04 00:00:00', null, 'OVERDUE', 3, 1, 999),
    (9988, '2026-07-01 00:00:00', '2026-07-02 00:00:00', '2026-07-02 00:00:00', 'COMPLETED', 1, 2, 999),
    (9989, '2026-07-02 00:00:00', '2026-07-03 00:00:00', '2026-07-03 00:00:00', 'COMPLETED', 2, 1, 999),
    (9992, '2026-07-03 00:00:00', '2026-07-04 00:00:00', '2026-07-04 00:00:00', 'COMPLETED', 3, 3, 999),
    (1040, '2026-07-19 00:00:00', '2026-07-22 00:00:00', null, 'CANCELLED', 7, 9, 999),
    (1041, '2026-07-20 10:00:00', '2026-07-20 14:00:00', '2026-07-20 17:00:00', 'COMPLETED', 8, 5, 999)
ON CONFLICT (booking_id) DO UPDATE
SET start_date = EXCLUDED.start_date,
    end_date = EXCLUDED.end_date,
    actual_return_date = EXCLUDED.actual_return_date,
    booking_status = EXCLUDED.booking_status,
    customer_id = EXCLUDED.customer_id,
    vehicle_id = EXCLUDED.vehicle_id,
    employee_id = EXCLUDED.employee_id;

SELECT setval(pg_get_serial_sequence('customer', 'customer_id'), GREATEST((SELECT MAX(customer_id) FROM customer), 1), true)
WHERE pg_get_serial_sequence('customer', 'customer_id') IS NOT NULL;

SELECT setval(pg_get_serial_sequence('vehicle', 'vehicle_id'), GREATEST((SELECT MAX(vehicle_id) FROM vehicle), 1), true)
WHERE pg_get_serial_sequence('vehicle', 'vehicle_id') IS NOT NULL;

SELECT setval(pg_get_serial_sequence('booking', 'booking_id'), GREATEST((SELECT MAX(booking_id) FROM booking), 1), true)
WHERE pg_get_serial_sequence('booking', 'booking_id') IS NOT NULL;

COMMIT;
