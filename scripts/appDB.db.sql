BEGIN TRANSACTION;
DROP TABLE IF EXISTS `walkthroughs`;
CREATE TABLE IF NOT EXISTS `walkthroughs` (
	`walkthroughId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`schoolId`	INTEGER NOT NULL,
	`name`	TEXT,
	`lastUpdatedDate`	TEXT,
	`createdDate`	TEXT,
	`percentComplete`	REAL NOT NULL,
	`isDeleted`	INTEGER NOT NULL,
	FOREIGN KEY(`schoolId`) REFERENCES `schools`(`schoolId`)
);
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
	`userId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`emailAddress`	TEXT,
	`schoolId`	INTEGER NOT NULL,
	`userName`	TEXT,
	`role`	TEXT,
	`remoteId`	INTEGER NOT NULL,
	FOREIGN KEY(`schoolId`) REFERENCES `schools`(`schoolId`)
);
DROP TABLE IF EXISTS `schools`;
CREATE TABLE IF NOT EXISTS `schools` (
	`schoolId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`schoolName`	TEXT,
	`remoteId`	INTEGER NOT NULL
);
DROP TABLE IF EXISTS `responses`;
CREATE TABLE IF NOT EXISTS `responses` (
	`responseId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`questionId`	INTEGER NOT NULL,
	`actionPlan`	TEXT,
	`priority`	INTEGER NOT NULL,
	`rating`	INTEGER NOT NULL,
	`timestamp`	TEXT,
	`locationId`	INTEGER NOT NULL,
	`image`	TEXT,
	`isActionItem`	INTEGER NOT NULL,
	`userId`	INTEGER NOT NULL,
	`walkthroughId`	INTEGER NOT NULL,
	FOREIGN KEY(`walkthroughId`) REFERENCES `walkthroughs`(`walkthroughId`) ON DELETE CASCADE,
	FOREIGN KEY(`locationId`) REFERENCES `location`(`locationId`),
	FOREIGN KEY(`questionId`) REFERENCES `question`(`questionId`),
	FOREIGN KEY(`userId`) REFERENCES `user`(`userId`)
);
DROP TABLE IF EXISTS `question_mapping`;
CREATE TABLE IF NOT EXISTS `question_mapping` (
	`mappingId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`locationId`	INTEGER NOT NULL,
	`questionId`	INTEGER NOT NULL
);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (1,1,1);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (2,1,2);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (3,1,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (4,1,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (5,1,5);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (6,1,6);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (7,1,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (8,1,8);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (9,2,9);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (10,2,10);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (11,2,1);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (12,2,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (13,2,5);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (14,2,11);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (15,2,12);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (16,3,13);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (17,3,14);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (18,3,15);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (19,3,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (20,3,6);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (21,3,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (22,4,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (23,4,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (24,4,2);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (25,4,16);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (26,4,17);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (27,4,18);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (28,4,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (29,4,19);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (30,4,20);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (31,5,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (32,5,6);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (33,5,20);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (34,6,21);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (35,6,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (36,6,22);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (37,6,23);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (38,6,24);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (39,6,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (40,6,25);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (41,6,20);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (42,6,26);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (43,7,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (44,7,6);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (45,7,27);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (46,7,15);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (47,8,29);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (48,8,30);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (49,8,31);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (50,8,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (51,8,32);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (52,8,33);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (53,8,34);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (54,8,35);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (55,8,36);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (56,8,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (57,8,37);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (58,8,38);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (59,9,29);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (60,9,30);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (61,9,31);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (62,9,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (63,9,32);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (64,9,33);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (65,9,34);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (66,9,35);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (67,9,36);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (68,9,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (69,9,37);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (70,9,38);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (71,10,29);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (72,10,30);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (73,10,31);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (74,10,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (75,10,32);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (76,10,33);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (77,10,34);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (78,10,35);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (79,10,36);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (80,10,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (81,10,37);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (82,10,38);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (83,11,29);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (84,11,30);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (85,11,31);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (86,11,3);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (87,11,32);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (88,11,33);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (89,11,34);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (90,11,35);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (91,11,36);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (92,11,7);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (93,11,37);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (94,11,38);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (95,12,1);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (96,12,4);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (97,12,5);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (98,12,15);
INSERT INTO `question_mapping` (mappingId,locationId,questionId) VALUES (99,12,12);
DROP TABLE IF EXISTS `question`;
CREATE TABLE IF NOT EXISTS `question` (
	`questionId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`questionText`	TEXT,
	`shortDesc`	TEXT,
	`ratingOption1`	TEXT,
	`ratingOption2`	TEXT,
	`ratingOption3`	TEXT,
	`ratingOption4`	TEXT
);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (1,'Count evidence of school-ownership. Look for things with the school name or logo prominently displayed.','School Ownership','None','1-3','4 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (2,'Count signs with positive behavioral expecations of students.
','Positive Signs','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (3,'Count groupings of student work.
','Student Work Groupings','0-1','2-4','5 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (4,'Count evidence of defacement. This includes markings/etchings of names, symbols, or profanity on walls, doors, etc.','Defacement','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (5,'Count evidence of vandalism. Look for things that have been intentionally broken or bent.','Vandalism','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (6,'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.','Broken Lights','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (7,'Students consistently follow rules appropriate to the setting.
','Rule Following','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (8,'Are adults present to monitor student entrance during morning arrival?','Adults Present','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (9,'Are there signs clearly posted to indicate the entrance to the school grounds?','Entrance Signs','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (10,'What is the condition of the landscaping around the building?','Landscaping','Not maintained','Maintained','Well-maintained',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (11,'Count trash.
','Trash','≤ 1 full grocery bag
','= 2 full grocery bags
','≥ 3 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (12,'Count evidence of substance use. Look for bottles, cans, or paraphernalia that once held alccohol, prescriptions, or illicit drugs.','Substance Use','None','1 or more',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (13,'Bathroom is clean and in good repair (e.g. fixtures not leaking).','Overall Condition','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (14,'Bathroom has needed supplies.','Has Supplies','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (15,'Count trash.','Trash','None or a few pieces','= 1 full grocery bags','≥ 2 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (16,'Adults are visible in the hallway during transition.','Adults Visible','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (17,'Adults scan the area and are aware of what is occurring.','Adults Aware','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (18,'Adults and students have positive interactions (i.e., greet each other).','Positive Interactions','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (19,'Hallway is crowded.
','Crowded Hallway','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (20,'Noise level is appropriate for the activities in the location.','Appropriate Noise Level','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (21,'Cafeteria is clean and maintained aesthetically.','Clean/Maintained','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (22,'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.','Broken Lights','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (23,'Count signs with positive behavioral expecations of students.
','Positive Signs','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (24,'The cafeteria promotes healthy eating.','Promotes Healthy Eating','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (25,'Students treat their peers with respect.
','Respectful Peer Treatment','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (26,'Count trash immediately after students leave the cafeteria.','Trash','None or a few pieces','= 1 full grocery bags','≥ 2 full grocery bags',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (27,'Count vandalism. Look for things that have been intentionally broken or bent.','Vandalism','None','1-7','8 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (28,'The classroom is clean and maintained aesthetically.','Clean/Maintained','Strongly disagree','Disagree','Agree','Strongly agree');
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (29,'Count signs with positive behavioral expecations of students.
','Positive Signs','0-2','3-5','6 or more',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (30,'A behavioral matrix is present.
','Behavioral Matrix Present','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (31,'Observed evidence that the teacher has a reinforcement system to reward positive behaviors.','Evidence of Reinforcement System','Yes','No',NULL,NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (32,'Teacher shows some evidence that she/he is aware of students'' interests and backgrounds.
','Awareness of Student Interests','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (33,'Teacher integrates cultural artifacts reflective of students'' interests into learning activities (e.g., music, local landmarks, artwork).','Cultural Artifacts Integrated','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (34,'Teacher is calm and attentive when problems arise.
','Calm and Attentive','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (35,'Teacher uses active listening techniques (e.g., eye contact, focusing, body language, not interrupting, paraphrasing, asking for more details, offering information, responding in full sentences rather than yes or no).
','Active Listening','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (36,'Teacher interacts positively with students (i.e., uses more praise than reprimands).
','Positive Interactions','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (37,'Students are interested and engaged.
','Students Engaged','Never','Some of the time','A lot of the time',NULL);
INSERT INTO `question` (questionId,questionText,shortDesc,ratingOption1,ratingOption2,ratingOption3,ratingOption4) VALUES (38,'Students treat their peers with respect. (e.g., listen when peers are talking).
','Respectful Peer Treatment','Never','Some of the time','A lot of the time',NULL);
DROP TABLE IF EXISTS `location`;
CREATE TABLE IF NOT EXISTS `location` (
	`locationId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT,
	`type`	TEXT,
	`locationInstruction`	TEXT
);
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (1,'Student Entrance','Entrance','Observe the entrance that most students use upon arrival.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (2,'School Grounds','Grounds','Walk the perimeter of the school buildings.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (3,'Bathrooms','Bathroom','Choose a commonly used restroom. If possible, visit both the girls and boys room.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (4,'Hallway','Hallway','Observe a space that includes two classrooms during the transition between classes.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (5,'Stairwell','Stairwell','Observe a space between 2 floors.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (6,'Cafeteria','Cafeteria','Walk the perimeter of the cafeteria during lunch.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (7,'Hotspot','Hotspot','Observe one area where students engage in problem or dangerous behavior.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (8,'Classroom 1','Classroom','Observe a classroom from each grade level for 5 minutes.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (9,'Classroom 2','Classroom','Observe a classroom from each grade level for 5 minutes.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (10,'Classroom 3','Classroom','Observe a classroom from each grade level for 5 minutes.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (11,'Classroom 4','Classroom','Observe a classroom from each grade level for 5 minutes.');
INSERT INTO `location` (locationId,name,type,locationInstruction) VALUES (12,'Pick-up Area','Pick-up Area','Observe the space most students are picked-up by buses/cars.');
COMMIT;
