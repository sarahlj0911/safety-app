-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema safetywalkthrough
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema safetywalkthrough
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `safetywalkthrough` DEFAULT CHARACTER SET utf8 ;
USE `safetywalkthrough` ;

-- -----------------------------------------------------
-- Table `safetywalkthrough`.`schools`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`schools` (
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`schoolName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`user` (
  `userId` INT NOT NULL,
  `userName` VARCHAR(100) NOT NULL,
  `emailAddress` VARCHAR(100) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`, `schoolName`),
  INDEX `fk_user_schools_idx` (`schoolName` ASC),
  CONSTRAINT `fk_user_schools`
    FOREIGN KEY (`schoolName`)
    REFERENCES `safetywalkthrough`.`schools` (`schoolName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`location` (
  `locationId` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `locationInstruction` VARCHAR(100) NOT NULL,
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`locationId`, `schoolName`),
  INDEX `fk_location_schools1_idx` (`schoolName` ASC),
  CONSTRAINT `fk_location_schools1`
    FOREIGN KEY (`schoolName`)
    REFERENCES `safetywalkthrough`.`schools` (`schoolName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
  `rationOption3` VARCHAR(32) NULL,
  `ratingOption4` VARCHAR(32) NULL,
  PRIMARY KEY (`questionId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`question_mapping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`question_mapping` (
  `mappingId` INT NOT NULL,
  `schoolName` VARCHAR(100) NOT NULL,
  `locationId` INT NOT NULL,
  `questionId` INT NOT NULL,
  PRIMARY KEY (`mappingId`, `schoolName`),
  INDEX `fk_question_mapping_schools1_idx` (`schoolName` ASC),
  INDEX `fk_question_mapping_location1_idx` (`locationId` ASC),
  INDEX `fk_question_mapping_question1_idx` (`questionId` ASC),
  CONSTRAINT `fk_question_mapping_schools1`
    FOREIGN KEY (`schoolName`)
    REFERENCES `safetywalkthrough`.`schools` (`schoolName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_mapping_location1`
    FOREIGN KEY (`locationId`)
    REFERENCES `safetywalkthrough`.`location` (`locationId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_mapping_question1`
    FOREIGN KEY (`questionId`)
    REFERENCES `safetywalkthrough`.`question` (`questionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`walkthroughs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`walkthroughs` (
  `walkthroughId` INT NOT NULL,
  `name` VARCHAR(32) NULL,
  `lastUpdatedDate` VARCHAR(32) NULL,
  `createdDate` VARCHAR(32) NULL,
  `percentComplete` DECIMAL(5,2) NULL,
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`walkthroughId`, `schoolName`),
  INDEX `fk_walkthroughs_schools1_idx` (`schoolName` ASC),
  CONSTRAINT `fk_walkthroughs_schools1`
    FOREIGN KEY (`schoolName`)
    REFERENCES `safetywalkthrough`.`schools` (`schoolName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `safetywalkthrough`.`responses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `safetywalkthrough`.`responses` (
  `responseId` INT NOT NULL,
  `actionPlan` VARCHAR(256) NULL,
  `priority` INT NULL,
  `rating` INT NULL,
  `timestamp` VARCHAR(32) NULL,
  `isActionItem` INT NULL,
  `questionId` INT NOT NULL,
  `walkthroughId` INT NOT NULL,
  `locationId` INT NOT NULL,
  `schoolName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`responseId`, `questionId`, `walkthroughId`, `locationId`, `schoolName`),
  INDEX `fk_responses_question1_idx` (`questionId` ASC),
  INDEX `fk_responses_walkthroughs1_idx` (`walkthroughId` ASC),
  INDEX `fk_responses_location1_idx` (`locationId` ASC),
  INDEX `fk_responses_schools1_idx` (`schoolName` ASC),
  CONSTRAINT `fk_responses_question1`
    FOREIGN KEY (`questionId`)
    REFERENCES `safetywalkthrough`.`question` (`questionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_responses_walkthroughs1`
    FOREIGN KEY (`walkthroughId`)
    REFERENCES `safetywalkthrough`.`walkthroughs` (`walkthroughId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_responses_location1`
    FOREIGN KEY (`locationId`)
    REFERENCES `safetywalkthrough`.`location` (`locationId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_responses_schools1`
    FOREIGN KEY (`schoolName`)
    REFERENCES `safetywalkthrough`.`schools` (`schoolName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
