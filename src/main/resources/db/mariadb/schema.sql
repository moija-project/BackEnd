#CREATE DATABASE moija;
USE `moija`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `user` (
                        `user_id`	VARCHAR(15)	NOT NULL,
                        `nickname`	VARCHAR(15)	NOT NULL,
                        `gender`	BOOLEAN	NOT NULL	DEFAULT false,
                        `birth`	DATE	NOT NULL,
                        `phone_number`	INT(11)	NOT NULL,
                        `name`	VARCHAR(20)	NOT NULL,
                        `profile`	BLOB	NULL,
                        `time_join`	TIMESTAMP	NOT NULL,
                        `reliability_user`	FLOAT	NOT NULL	DEFAULT 3,
                        `password`	VARCHAR(50)	NOT NULL,
                            `is_available`	BOOLEAN	NOT NULL DEFAULT true,
                        CONSTRAINT PRIMARY KEY (`user_id`)
);

CREATE TABLE `recruit` (
                           `recruit_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                           `title`	VARCHAR(20)	NOT NULL,
                           `contents`	VARCHAR(2000)	NOT NULL,
                           `category`	VARCHAR(15)	NOT NULL	DEFAULT 'etc',
                           `reliability_recruit`	FLOAT	NOT NULL	DEFAULT 0,
                           `penalty`	INT	NOT NULL	DEFAULT 0,
                           `num_condition`	INT(10)	NOT NULL	DEFAULT 0,
                           `state_recruit`	BOOLEAN	NOT NULL	DEFAULT true,
                           `views`	BIGINT	NOT NULL	DEFAULT 0,
                           `likes`	BIGINT	NOT NULL	DEFAULT 0,
                           `time_first_write`	TIMESTAMP	NOT NULL,
                           `is_changed`	BOOLEAN	NOT NULL	DEFAULT false,
                           `time_last_write`	TIMESTAMP	NULL,
                           `is_available`	BOOLEAN	NOT NULL DEFAULT true,
                           `latest_write`	TIMESTAMP	NOT NULL,
                           `leader_id`	VARCHAR(15)	NOT NULL,
                           CONSTRAINT PRIMARY KEY (`recruit_id`),
                           CONSTRAINT `FK_USER_TO_RECRUIT_1` FOREIGN KEY (`leader_id`) REFERENCES `USER` (`user_id`)
);

CREATE TABLE `member` (
                          `team_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                          `user_id`	VARCHAR(15)	NOT NULL,
                          `recruit_id`	BIGINT	NOT NULL,
                          `score_team`	FLOAT	NULL,
                          CONSTRAINT PRIMARY KEY (
                                                  `team_id`,
                                                  `user_id`,
                                                  `recruit_id`
                              ),
                          CONSTRAINT `FK_USER_TO_MEMBER_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`),
                          CONSTRAINT `FK_RECRUIT_TO_MEMBER_1` FOREIGN KEY (`recruit_id`) REFERENCES `RECRUIT` (`recruit_id`)
);

CREATE TABLE `waiting` (
                           `waiting_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                           `is_permitted`	BOOLEAN	NOT NULL	DEFAULT false,
                           `is_ask`	BOOLEAN	NOT NULL	DEFAULT false,
                           `num_answer`	SMALLINT NOT NULL	DEFAULT 0,
                           `user_id`	VARCHAR(15)	NOT NULL,
                           `recruit_id`	BIGINT	NOT NULL,
                           CONSTRAINT PRIMARY KEY (`waiting_id`),
                           CONSTRAINT `FK_USER_TO_WAITING_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`),
                           CONSTRAINT `FK_RECRUIT_TO_WAITING_1` FOREIGN KEY (`recruit_id`) REFERENCES `RECRUIT` (`recruit_id`)
);

CREATE TABLE `clip` (
                        `clip_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                        `user_id`	VARCHAR(15)	NOT NULL,
                        `recruit_id`	BIGINT	NOT NULL,
                        CONSTRAINT PRIMARY KEY (
                                                `clip_id`,
                                                `user_id`,
                                                `recruit_id`
                            ),
                        CONSTRAINT `FK_USER_TO_CLIP_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`),
                        CONSTRAINT `FK_RECRUIT_TO_CLIP_1` FOREIGN KEY (`recruit_id`) REFERENCES `RECRUIT` (`recruit_id`)
);

CREATE TABLE `score` (
                         `score_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                         `score`	FLOAT	NULL,
                         `grant_id`	VARCHAR(15) NOT NULL,
                         `granted_id`	VARCHAR(15) NOT NULL,
                         CONSTRAINT PRIMARY KEY (`score_id`),
                         CONSTRAINT `FK_USER_TO_SCORE_1` FOREIGN KEY (`grant_id`) REFERENCES `USER` (`user_id`),
                         CONSTRAINT `FK_USER_TO_SCORE_2` FOREIGN KEY (`granted_id`) REFERENCES `USER` (`user_id`)
);

CREATE TABLE `likes` (
                        `like_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                        `user_id`	VARCHAR(15)	NOT NULL,
                        `recruit_id`	BIGINT	NOT NULL,
                        CONSTRAINT `PK_LIKE` PRIMARY KEY (
                            `like_id`,
                            `user_id`,
                            `recruit_id`
                            ),
                        CONSTRAINT `FK_USER_TO_LIKE_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`),
                        CONSTRAINT `FK_RECRUIT_TO_LIKE_1` FOREIGN KEY (`recruit_id`) REFERENCES `RECRUIT` (`recruit_id`)
);




