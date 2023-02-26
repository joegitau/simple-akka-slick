-- database setup
DROP DATABASE IF EXISTS simple_akka_slick_db;
CREATE DATABASE simple_akka_slick_db;
\c simple_akka_slick_db;

-- players table
CREATE TABLE "players" (
    "id"          SERIAL PRIMARY KEY,
    "first_name"  VARCHAR NOT NULL,
    "last_name"   VARCHAR NOT NULL,
    "nationality" VARCHAR NOT NULL,
    "team"        VARCHAR NOT NULL,
    "created"     timestamptz DEFAULT CURRENT_TIMESTAMP,
    "modified"    timestamptz
);
