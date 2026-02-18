CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    license_plate VARCHAR(10) NOT NULL,
    number_of_seats INTEGER NOT NULL CHECK (number_of_seats >= 0),
    number_of_child_seats INTEGER NOT NULL CHECK (number_of_child_seats >= 0),
    folding_rear_seat BOOLEAN NOT NULL,
    tow_bar BOOLEAN NOT NULL,
    owner_email VARCHAR(255) NOT NULL
);

CREATE TABLE rentals (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    city VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    postal_code INTEGER NOT NULL CHECK (
        postal_code BETWEEN 1000 AND 9999
    ),
    car_id INTEGER NOT NULL REFERENCES cars (id)
);

CREATE TABLE rents (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    rental_id INTEGER NOT NULL REFERENCES rentals (id),
    renter_email VARCHAR(255) NOT NULL
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    rent_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL
);
