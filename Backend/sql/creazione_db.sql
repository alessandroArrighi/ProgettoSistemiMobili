CREATE DATABASE IF NOT EXISTS `PasswordManager`;
USE `PasswordManager`;

CREATE TABLE IF NOT EXISTS Utenze (
    IDService       int AUTO_INCREMENT PRIMARY KEY,
    Service         varchar(100),
    User            varchar(100) NOT NULL,
    Password        varchar(512) NOT NULL);


CREATE USER 'Arrighi'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON PasswordManager.* TO 'Arrighi'@'%';