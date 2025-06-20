-- This script initializes a PostgreSQL database environment with a user and a database.

-- Create User Service DB
DROP DATABASE IF EXISTS userdb;
CREATE DATABASE userdb WITH OWNER = admin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS contentdb;
CREATE DATABASE contentdb WITH OWNER = admin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS projectdb;
CREATE DATABASE projectdb WITH OWNER = admin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS mediadb;
CREATE DATABASE mediadb WITH OWNER = admin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;