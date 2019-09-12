CREATE TABLE fd5_envelope_codes (
  envelope_code int PRIMARY KEY,
  envelope_text varchar(50) NOT NULL
);

CREATE TABLE fd5_users (
  user_id int PRIMARY KEY auto_increment,
  user_name varchar(255) NOT NULL
);

CREATE TABLE fd5_jobs (
  job_id int PRIMARY KEY auto_increment,
  user_name varchar(255) NOT NULL,
  paper_code varchar(6) UNIQUE NOT NULL,
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status_code varchar(255) NOT NULL DEFAULT 'dev',
  remarks text DEFAULT NULL
);
