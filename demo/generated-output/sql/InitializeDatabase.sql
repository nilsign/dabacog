-- Global Setup.
SET client_encoding = 'UTF8';

-- Configures a global id sequence shared by all generated primary keys.
CREATE SEQUENCE public.shared_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Creates normal tables and the according indices
CREATE TABLE IF NOT EXISTS tbl_genre (
    id BIGINT CONSTRAINT cstr_tbl_genre_pk PRIMARY KEY,
    name TEXT NOT NULL,
    CONSTRAINT cstr_tbl_genre_name_unique UNIQUE
);

CREATE TABLE IF NOT EXISTS tbl_author (
    id BIGINT CONSTRAINT cstr_tbl_author_pk PRIMARY KEY,
    id_book BIGINT NOT NULL,
    name TEXT NOT NULL,
    CONSTRAINT cstr_tbl_author_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_author_id_book ON tbl_author(id_book);

CREATE TABLE IF NOT EXISTS tbl_loan (
    id BIGINT CONSTRAINT cstr_tbl_loan_pk PRIMARY KEY,
    id_book BIGINT NOT NULL,
    id_customer BIGINT NOT NULL,
    start TIMESTAMP NOT NULL,
    end TIMESTAMP NOT NULL,
    return TIMESTAMP,
    dunning_letter BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT cstr_tbl_loan_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_loan_customer_fk FOREIGN KEY(id_customer)
        REFERENCES tbl_customer(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_loan_id_book ON tbl_loan(id_book);
CREATE INDEX idx_tbl_loan_id_customer ON tbl_loan(id_customer);

CREATE TABLE IF NOT EXISTS tbl_book (
    id BIGINT CONSTRAINT cstr_tbl_book_pk PRIMARY KEY,
    id_author BIGINT NOT NULL,
    id_genre BIGINT NOT NULL,
    id_location BIGINT NOT NULL,
    name TEXT NOT NULL,
    isbn TEXT NOT NULL,
    price NUMERIC,
    CONSTRAINT cstr_tbl_book_author_fk FOREIGN KEY(id_author)
        REFERENCES tbl_author(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_book_genre_fk FOREIGN KEY(id_genre)
        REFERENCES tbl_genre(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_book_location_fk FOREIGN KEY(id_location)
        REFERENCES tbl_location(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_book_isbn_unique UNIQUE
);
CREATE INDEX idx_tbl_book_id_author ON tbl_book(id_author);
CREATE INDEX idx_tbl_book_id_genre ON tbl_book(id_genre);
CREATE INDEX idx_tbl_book_id_location ON tbl_book(id_location);

CREATE TABLE IF NOT EXISTS tbl_customer (
    id BIGINT CONSTRAINT cstr_tbl_customer_pk PRIMARY KEY,
    id_address BIGINT,
    id_contact BIGINT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    CONSTRAINT cstr_tbl_customer_address_fk FOREIGN KEY(id_address)
        REFERENCES tbl_address(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_customer_contact_fk FOREIGN KEY(id_contact)
        REFERENCES tbl_contact(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_customer_id_address ON tbl_customer(id_address);
CREATE INDEX idx_tbl_customer_id_contact ON tbl_customer(id_contact);

CREATE TABLE IF NOT EXISTS tbl_address (
    id BIGINT CONSTRAINT cstr_tbl_address_pk PRIMARY KEY,
    street TEXT NOT NULL,
    zipcode TEXT NOT NULL,
    city TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_location (
    id BIGINT CONSTRAINT cstr_tbl_location_pk PRIMARY KEY,
    id_book BIGINT NOT NULL,
    floor TEXT NOT NULL DEFAULT 'warehouse',
    shelf TEXT NOT NULL DEFAULT '',
    position INTEGER NOT NULL DEFAULT -1,
    CONSTRAINT cstr_tbl_location_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_location_id_book ON tbl_location(id_book);

CREATE TABLE IF NOT EXISTS tbl_book_order (
    id BIGINT CONSTRAINT cstr_tbl_book_order_pk PRIMARY KEY,
    id_book BIGINT,
    floor TEXT NOT NULL DEFAULT 'warehouse',
    shelf TEXT NOT NULL DEFAULT '',
    position INTEGER NOT NULL DEFAULT -1,
    CONSTRAINT cstr_tbl_book_order_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_book_order_id_book ON tbl_book_order(id_book);

CREATE TABLE IF NOT EXISTS tbl_employee (
    id BIGINT CONSTRAINT cstr_tbl_employee_pk PRIMARY KEY,
    id_employee BIGINT,
    id_address BIGINT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    CONSTRAINT cstr_tbl_employee_employee_fk FOREIGN KEY(id_employee)
        REFERENCES tbl_employee(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_employee_address_fk FOREIGN KEY(id_address)
        REFERENCES tbl_address(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_employee_id_employee ON tbl_employee(id_employee);
CREATE INDEX idx_tbl_employee_id_address ON tbl_employee(id_address);

CREATE TABLE IF NOT EXISTS tbl_contact (
    id BIGINT CONSTRAINT cstr_tbl_contact_pk PRIMARY KEY,
    id_customer BIGINT NOT NULL,
    mobile TEXT,
    home TEXT,
    email TEXT NOT NULL,
    CONSTRAINT cstr_tbl_contact_customer_fk FOREIGN KEY(id_customer)
        REFERENCES tbl_customer(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_contact_id_customer ON tbl_contact(id_customer);

-- Creates relational tables (n..n) and the according indices
CREATE TABLE IF NOT EXISTS tbl_author_tbl_book (
    id_author BIGINT NOT NULL
    id_book BIGINT NOT NULL
    CONSTRAINT cstr_tbl_author_tbl_book_pks PRIMARY KEY (id_author, id_book),
    CONSTRAINT cstr_tbl_author_tbl_book_id_author_fk FOREIGN KEY(id_author)
        REFERENCES tbl_author(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_author_tbl_book_id_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_author_tbl_book_id_author ON tbl_author_tbl_book(id_author);
CREATE INDEX idx_tbl_author_tbl_book_id_book ON tbl_author_tbl_book(id_book);


CREATE TABLE IF NOT EXISTS tbl_book_tbl_genre (
    id_book BIGINT NOT NULL
    id_genre BIGINT NOT NULL
    CONSTRAINT cstr_tbl_book_tbl_genre_pks PRIMARY KEY (id_book, id_genre),
    CONSTRAINT cstr_tbl_book_tbl_genre_id_book_fk FOREIGN KEY(id_book)
        REFERENCES tbl_book(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cstr_tbl_book_tbl_genre_id_genre_fk FOREIGN KEY(id_genre)
        REFERENCES tbl_genre(id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE INDEX idx_tbl_book_tbl_genre_id_book ON tbl_book_tbl_genre(id_book);
CREATE INDEX idx_tbl_book_tbl_genre_id_genre ON tbl_book_tbl_genre(id_genre);

