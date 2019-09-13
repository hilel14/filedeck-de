TRUNCATE TABLE fd5_users;
INSERT INTO fd5_users (user_name) VALUES ('user one');
INSERT INTO fd5_users (user_name) VALUES ('user two');
INSERT INTO fd5_users (user_name) VALUES ('user tree');
TRUNCATE TABLE fd5_envelope_codes;
INSERT INTO fd5_envelope_codes (envelope_code, envelope_text) VALUES (1, 'env11');
INSERT INTO fd5_envelope_codes (envelope_code, envelope_text) VALUES (2, 'env12');
INSERT INTO fd5_envelope_codes (envelope_code, envelope_text) VALUES (3, 'env13');
/*
env1
env2
env3
env4
env5
env6
env7
env8
env9
env10
env11
env12
env13
env14
env15
env16
env101
env102
return_env
special_env
*/