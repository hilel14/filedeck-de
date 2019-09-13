CREATE TABLE "dbo"."fd5_envelope_codes" (
   envelope_code int PRIMARY KEY NOT NULL,
   envelope_text varchar(50)
);

CREATE TABLE "dbo"."fd5_users" (
   user_id int PRIMARY KEY NOT NULL IDENTITY,
   user_name varchar(50) NOT NULL
);
CREATE UNIQUE INDEX fd5_users_unique_user_name ON "dbo"."fd5_users"(user_name);

CREATE TABLE "dbo"."fd5_jobs" (
   job_id int PRIMARY KEY NOT NULL IDENTITY,
   user_name varchar(50) NOT NULL,
   paper_code varchar(6) NOT NULL,
   start_time date NOT NULL DEFAULT GETDATE(),
   status_code varchar(50) NOT NULL DEFAULT ('dev'),
   remarks text
);
CREATE UNIQUE INDEX fd5_jobs_unique_paper_code ON "dbo"."fd5_jobs"(paper_code);
