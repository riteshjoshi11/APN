use antrackerdb;

ALTER TABLE employee ADD UNIQUE KEY `uqnameid` (`orgid`, `first`,`last`);

ALTER TABLE employee ADD UNIQUE KEY `uqmobid` (`orgid`, `mobile`);

ALTER TABLE customer ADD UNIQUE KEY `uqfirmnameid` (`orgid`, `firmname`,`name`);

ALTER TABLE customer ADD UNIQUE KEY `uqmob1id` (`orgid`, `mobile1`);

ALTER TABLE customer ADD UNIQUE KEY `uqmob2id` (`orgid`, `mobile2`);

ALTER TABLE city ADD UNIQUE KEY `uqcity` (`name`);