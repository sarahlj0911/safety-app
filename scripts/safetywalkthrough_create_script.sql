-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema safetywalkthrough
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `safetywalkthrough` DEFAULT CHARACTER SET utf8 ;
USE `safetywalkthrough` ;

-- -----------------------------------------------------
-- Table `safetywalkthrough`.`schools`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`schools` (
  `schoolId` INT NOT NULL AUTO_INCREMENT,
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`schoolId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`user` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `schoolId` INT NOT NULL,
  `userName` VARCHAR(100) NOT NULL,
  `emailAddress` VARCHAR(100) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`userId`, `schoolId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`location` (
  `locationId` INT NOT NULL,
  `schoolId` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `locationInstruction` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`locationId`, `schoolId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`question` (
  `questionId` INT NOT NULL,
  `questionText` VARCHAR(256) NULL,
  `shortDesc` VARCHAR(32) NULL,
  `ratingOption1` VARCHAR(32) NULL,
  `ratingOption2` VARCHAR(32) NULL,
  `ratingOption3` VARCHAR(32) NULL,
  `ratingOption4` VARCHAR(32) NULL,
  PRIMARY KEY (`questionId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`question_mapping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`question_mapping` (
  `mappingId` INT NOT NULL,
  `schoolId` INT NOT NULL,
  `locationId` INT NOT NULL,
  `questionId` INT NOT NULL,
  PRIMARY KEY (`mappingId`, `schoolId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`walkthroughs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`walkthroughs` (
  `walkthroughId` INT NOT NULL,
  `schoolId` INT NOT NULL,
  `userId` INT,
  `name` VARCHAR(32) NOT NULL,
  `lastUpdatedDate` DATETIME DEFAULT NULL,
  `createdDate` DATETIME DEFAULT NULL,
  `percentComplete` DECIMAL(5,2) NULL,
  PRIMARY KEY (`walkthroughId`, `schoolId`, `name`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`responses`
-- -----------------------------------------------------
CREATE TABLE `safetywalkthrough`.`responses` (
  `responseId` INT NOT NULL,
  `schoolId` INT NOT NULL,
  `userId` INT NOT NULL,
  `walkthroughId` INT NOT NULL,
  `locationId` INT NOT NULL,
  `questionId` INT NOT NULL,
  `actionPlan` VARCHAR(256) NULL DEFAULT NULL,
  `priority` INT NULL DEFAULT NULL,
  `rating` INT NULL DEFAULT NULL,
  `timestamp` VARCHAR(32) NULL DEFAULT NULL,
  `isActionItem` INT NULL DEFAULT 0,
  `image` VARCHAR(256) NULL DEFAULT NULL,
PRIMARY KEY (`responseId`, `walkthroughId`, `schoolId`, `locationId`, `questionId`),
  INDEX `fk_responses_walkthroughs_idx` (`walkthroughId` ASC, `schoolId` ASC),
  CONSTRAINT `fk_responses_walkthroughs`
    FOREIGN KEY (`walkthroughId` , `schoolId`)
    REFERENCES `safetywalkthrough`.`walkthroughs` (`walkthroughId` , `schoolId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

GRANT SELECT, INSERT, DELETE, UPDATE, TRIGGER ON TABLE `safetywalkthrough`.* TO 'safety_app';

INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (1,'Count evidence of school-ownership. Look for things with the school name or logo prominently displayed.','School Ownership','None','1-3','4 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (2,'Count signs with positive behavioral expecations of students.','Positive Signs','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (3,'Count groupings of student work.','Student Work Groupings','0-1','2-4','5 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (4,'Count evidence of defacement. This includes markings/etchings of names, symbols, or profanity on walls, doors, etc.','Defacement','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (5,'Count evidence of vandalism. Look for things that have been intentionally broken or bent.','Vandalism','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (6,'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.','Broken Lights','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (7,'Students consistently follow rules appropriate to the setting.','Rule Following','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (8,'Are adults present to monitor student entrance during morning arrival?','Adults Present','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (9,'Are there signs clearly posted to indicate the entrance to the school grounds?','Entrance Signs','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (10,'What is the condition of the landscaping around the building?','Landscaping','Not maintained','Maintained','Well-maintained',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (11,'Count trash.','Trash','≤ 1 full grocery bag','= 2 full grocery bags','≥ 3 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (12,'Count evidence of substance use. Look for bottles, cans, or paraphernalia that once held alccohol, prescriptions, or illicit drugs.','Substance Use','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (13,'Bathroom is clean and in good repair (e.g. fixtures not leaking).','Overall Condition','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (14,'Bathroom has needed supplies.','Has Supplies','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (15,'Count trash.','Trash','None or a few pieces','= 1 full grocery bags','≥ 2 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (16,'Adults are visible in the hallway during transition.','Adults Visible','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (17,'Adults scan the area and are aware of what is occurring.','Adults Aware','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (18,'Adults and students have positive interactions (i.e., greet each other).','Positive Interactions','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (19,'Hallway is crowded.','Crowded Hallway','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (20,'Noise level is appropriate for the activities in the location.','Appropriate Noise Level','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (21,'Cafeteria is clean and maintained aesthetically.','Clean/Maintained','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (22,'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.','Broken Lights','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (23,'Count signs with positive behavioral expecations of students.','Positive Signs','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (24,'The cafeteria promotes healthy eating.','Promotes Healthy Eating','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (25,'Students treat their peers with respect.','Respectful Peer Treatment','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (26,'Count trash immediately after students leave the cafeteria.','Trash','None or a few pieces','= 1 full grocery bags','≥ 2 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (27,'Count vandalism. Look for things that have been intentionally broken or bent.','Vandalism','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (28,'The classroom is clean and maintained aesthetically.','Clean/Maintained','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (29,'Count signs with positive behavioral expecations of students.','Positive Signs','0-2','3-5','6 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (30,'A behavioral matrix is present.','Behavioral Matrix Present','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (31,'Observed evidence that the teacher has a reinforcement system to reward positive behaviors.','Evidence of Reinforcement System','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (32,'Teacher shows some evidence that she/he is aware of students'' interests and backgrounds.','Awareness of Student Interests','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (33,'Teacher integrates cultural artifacts reflective of students'' interests into learning activities (e.g., music, local landmarks, artwork).','Cultural Artifacts Integrated','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (34,'Teacher is calm and attentive when problems arise.','Calm and Attentive','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (35,'Teacher uses active listening techniques (e.g., eye contact, focusing, body language, not interrupting, paraphrasing, asking for more details, offering information, responding in full sentences rather than yes or no).','Active Listening','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (36,'Teacher interacts positively with students (i.e., uses more praise than reprimands).','Positive Interactions','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (37,'Students are interested and engaged.','Students Engaged','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (38,'Students treat their peers with respect. (e.g., listen when peers are talking).','Respectful Peer Treatment','Never','Some of the time','A lot of the time',NULL);
COMMIT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
