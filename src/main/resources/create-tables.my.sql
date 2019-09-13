DROP TABLE IF EXISTS `fd5_envelope_codes`;
CREATE TABLE `fd5_envelope_codes` (
  `envelope_code` int(11) NOT NULL,
  `envelope_text` varchar(50) NOT NULL,
  PRIMARY KEY  (`envelope_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fd5_users`;
CREATE TABLE `fd5_users` (
  `user_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`user_id`),
  UNIQUE INDEX (user_name)
) ENGINE=MyISAM CHARSET=utf8;

DROP TABLE IF EXISTS `fd5_jobs`;
CREATE TABLE `fd5_jobs` (
  `job_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(255) NOT NULL,
  `paper_code` varchar(6) NOT NULL,
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status_code` varchar(255) NOT NULL DEFAULT "dev",
  `remarks` text DEFAULT NULL,
  PRIMARY KEY  (`job_id`),
  UNIQUE INDEX (paper_code)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

