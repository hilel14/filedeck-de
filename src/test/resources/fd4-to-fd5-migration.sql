insert into fd5_envelope_codes (envelope_code,envelope_text) (select * from Envelope_Codes)

insert into fd5_users (user_name) (select user_name from Users)

INSERT INTO fd5_users (user_name) VALUES ('idan')
INSERT INTO fd5_users (user_name) VALUES ('nectar')
INSERT INTO fd5_users (user_name) VALUES ('gali')
INSERT INTO fd5_users (user_name) VALUES ('lia')

insert into fd5_jobs (job_id, user_name, paper_code, start_time, status_code, remarks) (
	select top 1000 job_id, user_name, paper_code, start_time, status_text, remarks 
	from Jobs,Users,Job_Status_Codes
	where Jobs.user_id = Users.user_id and Jobs.status_code = Job_Status_Codes.status_code
)

update fd5_jobs set status_code = 'dev' where status_code = 'עבודה בפיתוח';
update fd5_jobs set status_code = 'dev' where status_code = 'חזר מהגהה';
update fd5_jobs set status_code = 'qa' where status_code = 'בבקרת איכות';

