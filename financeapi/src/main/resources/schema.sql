-- Drop tables 
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Accounts;
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS Budgets;

-- Create the Users table
CREATE TABLE IF NOT EXISTS Users(
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    email VARCHAR(100) NOT NULL,
    hashed_password VARCHAR(100) NOT NULL
);

-- Create the Accounts table
CREATE TABLE IF NOT EXISTS Accounts(
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    type VARCHAR(20),
    balance DECIMAL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Create the Categories table
CREATE TABLE IF NOT EXISTS Categories(
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    UNIQUE (user_id, category_name)
);

-- Create the Transactions table
CREATE TABLE IF NOT EXISTS Transactions(
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL ,
    amount DECIMAL,
    date DATE,
    description VARCHAR(250),
    transaction_type VARCHAR(20),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id), 
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id)
);


-- Create the Budgets table
CREATE TABLE IF NOT EXISTS Budgets(
    budget_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    budget_limit DECIMAL,
    period VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    UNIQUE (user_id, category_id, period)
);