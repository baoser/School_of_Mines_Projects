USE Bao;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Products;


CREATE TABLE Customers (
  customer_id int NOT NULL AUTO_INCREMENT,
  first_name varchar(255),
  last_name varchar(255),
  email varchar(255),
  PRIMARY KEY (customer_id)
);

INSERT INTO Customers (first_name, last_name, email)
VALUES ('David', 'Beckham', 'dbeck@gmail.com');
INSERT INTO Customers (first_name, last_name, email)
VALUES ('Christiano', 'Ronaldo', 'cr7@yahoo.com');

CREATE TABLE Products (
  product_id int NOT NULL AUTO_INCREMENT,
  product_name varchar(255),
  price float,
  quantity_in_stock int,
  product_image varchar(255),
  inactive TINYINT,
  PRIMARY KEY (product_id)
);

INSERT INTO Products (product_name, price, quantity_in_stock, product_image) 
VALUES ('watch', '499.99', 6, "watch.png");
INSERT INTO Products (product_name, price, quantity_in_stock, product_image)
VALUES ('bracelet', '99.99', 0, "bracelet.png");
INSERT INTO Products (product_name, price, quantity_in_stock, product_image)
VALUES ('ring', '299.99', 9, "ring.png");
INSERT INTO Products (product_name, price, quantity_in_stock, product_image)
VALUES ('necklace', '999.99', 2, "necklace.png");
INSERT INTO Products (product_name, price, quantity_in_stock, product_image)
VALUES ('earrings', '699.99', 7, "earrings.png");

CREATE TABLE Orders (
  order_id int NOT NULL AUTO_INCREMENT,
  time_ordered BIGINT, 
  product_id int,
  price float,
  customer_id int,
  quantity_purchased int,
  tax float,
  donation float,
  total float,
  PRIMARY KEY (order_id),
  FOREIGN KEY (product_id) REFERENCES Products(product_id),
  FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);
