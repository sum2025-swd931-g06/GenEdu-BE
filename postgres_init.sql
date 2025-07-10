-- Create User Service DB
DROP DATABASE IF EXISTS userdb;
CREATE DATABASE userdb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS contentdb;
CREATE DATABASE contentdb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS projectdb;
CREATE DATABASE projectdb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS mediadb;
CREATE DATABASE mediadb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS subscriptiondb;
CREATE DATABASE subscriptiondb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;

-- Create Curriculum Service DB
DROP DATABASE IF EXISTS notificationdb;
CREATE DATABASE notificationdb WITH OWNER = avnadmin ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' TABLESPACE = pg_default CONNECTION LIMIT = -1;