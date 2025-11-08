CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO categories (name) VALUES
('Food'),
('Rent/Mortgage'),
('Travel'),
('Utilities'),
('Transport'),
('Shopping'),
('Health'),
('Entertainment'),
('Other');

CREATE TABLE expenses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    expense_date DATE NOT NULL,
    description VARCHAR(500),

    CONSTRAINT fk_expense_user FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id)
        REFERENCES categories(id)
);
