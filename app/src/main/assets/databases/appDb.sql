

CREATE TABLE "location" (
"locationId" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ON CONFLICT REPLACE,
"name" TEXT,
"type" TEXT,
"locationInstruction" TEXT
);
INSERT INTO "location" VALUES (1, 'Student Entrance', 'Entrance', 'Observe the entrance that most students use upon arrival.');
INSERT INTO "location" VALUES (2, 'School Grounds', 'Grounds', 'Walk the perimeter of the school buildings.');
INSERT INTO "location" VALUES (3, 'Bathrooms', 'Bathroom', 'Choose a commonly used restroom. If possible, visit both the girls and boys room.');
INSERT INTO "location" VALUES (4, 'Hallway', 'Hallway', 'Observe a space that includes two classrooms during the transition between classes.');
INSERT INTO "location" VALUES (5, 'Stairwell', 'Stairwell', 'Observe a space between 2 floors.');
INSERT INTO "location" VALUES (6, 'Cafeteria', 'Cafeteria', 'Walk the perimeter of the cafeteria during lunch.');
INSERT INTO "location" VALUES (7, 'Hotspot', 'Hotspot', 'Observe one area where students engage in problem or dangerous behavior.');
INSERT INTO "location" VALUES (8, 'Classroom 1', 'Classroom', 'Observe a classroom from each grade level for 5 minutes.');
INSERT INTO "location" VALUES (9, 'Classroom 2', 'Classroom', 'Observe a classroom from each grade level for 5 minutes.');
INSERT INTO "location" VALUES (10, 'Classroom 3', 'Classroom', 'Observe a classroom from each grade level for 5 minutes.');
INSERT INTO "location" VALUES (11, 'Classroom 4', 'Classroom', 'Observe a classroom from each grade level for 5 minutes.');
INSERT INTO "location" VALUES (12, 'Pick-up Area', 'Pick-up Area', 'Observe the space most students are picked-up by buses/cars.');
INSERT INTO "location" VALUES (13, NULL, NULL, NULL);


CREATE TABLE "question" (
	`questionId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`questionText`	TEXT,
	`shortDesc`	TEXT,
	`ratingOption1`	TEXT,
	`ratingOption2`	TEXT,
	`ratingOption3`	TEXT,
	`ratingOption4`	TEXT
);
INSERT INTO "question" VALUES (1, 'Count evidence of school-ownership. Look for things with the school name or logo prominently displayed.', NULL, 'None', '1-3', '4 or more', NULL);
INSERT INTO "question" VALUES (2, 'Count signs with positive behavioral expecations of students.
', NULL, 'None', '1 or more', NULL, NULL);
INSERT INTO "question" VALUES (3, 'Count groupings of student work.
', NULL, '0-1', '2-4', '5 or more', NULL);
INSERT INTO "question" VALUES (4, 'Count evidence of defacement. This includes markings/etchings of names, symbols, or profanity on walls, doors, etc.', NULL, 'None', '1-7', '8 or more', NULL);
INSERT INTO "question" VALUES (5, 'Count evidence of vandalism. Look for things that have been intentionally broken or bent.', NULL, 'None', '1-7', '8 or more', NULL);
INSERT INTO "question" VALUES (6, 'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.', NULL, 'None', '1 or more', NULL, NULL);
INSERT INTO "question" VALUES (7, 'Students consistently follow rules appropriate to the setting.
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (8, 'Are adults present to monitor student entrance during morning arrival?', NULL, 'Yes', 'No', NULL, NULL);
INSERT INTO "question" VALUES (9, 'Are there signs clearly posted to indicate the entrance to the school grounds?', NULL, 'Yes', 'No', NULL, NULL);
INSERT INTO "question" VALUES (10, 'What is the condition of the landscaping around the building?', NULL, 'Not maintained', 'Maintained', 'Well-maintained', NULL);
INSERT INTO "question" VALUES (11, 'Count trash.
', NULL, '≤ 1 full grocery bag
', '= 2 full grocery bags
', '≥ 3 full grocery bags', NULL);
INSERT INTO "question" VALUES (12, 'Count evidence of substance use. Look for bottles, cans, or paraphernalia that once held alccohol, prescriptions, or illicit drugs.', NULL, 'None', '1 or more', NULL, NULL);
INSERT INTO "question" VALUES (13, 'Bathroom is clean and in good repair (e.g. fixtures not leaking).', NULL, 'Strongly disagree', 'Disagree', 'Agree', 'Strongly agree');
INSERT INTO "question" VALUES (14, 'Bathroom has needed supplies.', NULL, 'Strongly disagree', 'Disagree', 'Agree', 'Strongly agree');
INSERT INTO "question" VALUES (15, 'Count trash.', NULL, 'None or a few pieces', '= 1 full grocery bags', '≥ 2 full grocery bags', NULL);
INSERT INTO "question" VALUES (16, 'Adults are visible in the hallway during transition.', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (17, 'Adults scan the area and are aware of what is occurring.', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (18, 'Adults and students have positive interactions (i.e., greet each other).', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (19, 'Hallway is crowded.
', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (20, 'Noise level is appropriate for the activities in the location.', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (21, 'Cafeteria is clean and maintained aesthetically.', NULL, 'Strongly disagree', 'Disagree', 'Agree', 'Strongly agree');
INSERT INTO "question" VALUES (22, 'Count broken lights. Look for fixtures that contain burned out bulbs or are otherwise broken or not functioning.', NULL, 'None', '1-7', '8 or more', NULL);
INSERT INTO "question" VALUES (23, 'Count signs with positive behavioral expecations of students.
', NULL, 'None', '1-7', '8 or more', NULL);
INSERT INTO "question" VALUES (24, 'The cafeteria promotes healthy eating.', NULL, 'Strongly disagree', 'Disagree', 'Agree', 'Strongly agree');
INSERT INTO "question" VALUES (25, 'Students treat their peers with respect.
', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (26, 'Count trash immediately after students leave the cafeteria.', NULL, 'None or a few pieces', '= 1 full grocery bags', '≥ 2 full grocery bags', NULL);
INSERT INTO "question" VALUES (27, 'Count vandalism. Look for things that have been intentionally broken or bent.', NULL, 'None', '1-7', '8 or more', NULL);
INSERT INTO "question" VALUES (28, 'The classroom is clean and maintained aesthetically.', NULL, 'Strongly disagree', 'Disagree', 'Agree', 'Strongly agree');
INSERT INTO "question" VALUES (29, 'Count signs with positive behavioral expecations of students.
', NULL, '0-2', '3-5', '6 or more', NULL);
INSERT INTO "question" VALUES (30, 'A behavioral matrix is present.
', NULL, 'Yes', 'No', NULL, NULL);
INSERT INTO "question" VALUES (31, 'Observed evidence that the teacher has a reinforcement system to reward positive behaviors.', NULL, 'Yes', 'No', NULL, NULL);
INSERT INTO "question" VALUES (32, 'Teacher shows some evidence that she/he is aware of students'' interests and backgrounds.
', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (33, 'Teacher integrates cultural artifacts reflective of students'' interests into learning activities (e.g., music, local landmarks, artwork).', NULL, 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (34, 'Teacher is calm and attentive when problems arise.
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (35, 'Teacher uses active listening techniques (e.g., eye contact, focusing, body language, not interrupting, paraphrasing, asking for more details, offering information, responding in full sentences rather than yes or no).
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (36, 'Teacher interacts positively with students (i.e., uses more praise than reprimands).
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (37, 'Students are interested and engaged.
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);
INSERT INTO "question" VALUES (38, 'Students treat their peers with respect. (e.g., listen when peers are talking).
', '', 'Never', 'Some of the time', 'A lot of the time', NULL);


CREATE TABLE "question_mapping" (
	`mappingId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`locationId`	INTEGER NOT NULL,
	`questionId`	INTEGER NOT NULL
);
INSERT INTO "question_mapping" VALUES (1, 1, 1);
INSERT INTO "question_mapping" VALUES (2, 1, 2);
INSERT INTO "question_mapping" VALUES (3, 1, 3);
INSERT INTO "question_mapping" VALUES (4, 1, 4);
INSERT INTO "question_mapping" VALUES (5, 1, 5);
INSERT INTO "question_mapping" VALUES (6, 1, 6);
INSERT INTO "question_mapping" VALUES (7, 1, 7);
INSERT INTO "question_mapping" VALUES (8, 1, 8);
INSERT INTO "question_mapping" VALUES (9, 2, 9);
INSERT INTO "question_mapping" VALUES (10, 2, 10);
INSERT INTO "question_mapping" VALUES (11, 2, 1);
INSERT INTO "question_mapping" VALUES (12, 2, 4);
INSERT INTO "question_mapping" VALUES (13, 2, 5);
INSERT INTO "question_mapping" VALUES (14, 2, 11);
INSERT INTO "question_mapping" VALUES (15, 2, 12);
INSERT INTO "question_mapping" VALUES (16, 3, 13);
INSERT INTO "question_mapping" VALUES (17, 3, 14);
INSERT INTO "question_mapping" VALUES (18, 3, 15);
INSERT INTO "question_mapping" VALUES (19, 3, 4);
INSERT INTO "question_mapping" VALUES (20, 3, 6);
INSERT INTO "question_mapping" VALUES (21, 3, 7);
INSERT INTO "question_mapping" VALUES (22, 4, 3);
INSERT INTO "question_mapping" VALUES (23, 4, 4);
INSERT INTO "question_mapping" VALUES (24, 4, 2);
INSERT INTO "question_mapping" VALUES (25, 4, 16);
INSERT INTO "question_mapping" VALUES (26, 4, 17);
INSERT INTO "question_mapping" VALUES (27, 4, 18);
INSERT INTO "question_mapping" VALUES (28, 4, 7);
INSERT INTO "question_mapping" VALUES (29, 4, 19);
INSERT INTO "question_mapping" VALUES (30, 4, 20);
INSERT INTO "question_mapping" VALUES (31, 5, 4);
INSERT INTO "question_mapping" VALUES (32, 5, 6);
INSERT INTO "question_mapping" VALUES (33, 5, 20);
INSERT INTO "question_mapping" VALUES (34, 6, 21);
INSERT INTO "question_mapping" VALUES (35, 6, 4);
INSERT INTO "question_mapping" VALUES (36, 6, 22);
INSERT INTO "question_mapping" VALUES (37, 6, 23);
INSERT INTO "question_mapping" VALUES (38, 6, 24);
INSERT INTO "question_mapping" VALUES (39, 6, 7);
INSERT INTO "question_mapping" VALUES (40, 6, 25);
INSERT INTO "question_mapping" VALUES (41, 6, 20);
INSERT INTO "question_mapping" VALUES (42, 6, 26);
INSERT INTO "question_mapping" VALUES (43, 7, 4);
INSERT INTO "question_mapping" VALUES (44, 7, 6);
INSERT INTO "question_mapping" VALUES (45, 7, 27);
INSERT INTO "question_mapping" VALUES (46, 7, 15);
INSERT INTO "question_mapping" VALUES (47, 8, 29);
INSERT INTO "question_mapping" VALUES (48, 8, 30);
INSERT INTO "question_mapping" VALUES (49, 8, 31);
INSERT INTO "question_mapping" VALUES (50, 8, 3);
INSERT INTO "question_mapping" VALUES (51, 8, 32);
INSERT INTO "question_mapping" VALUES (52, 8, 33);
INSERT INTO "question_mapping" VALUES (53, 8, 34);
INSERT INTO "question_mapping" VALUES (54, 8, 35);
INSERT INTO "question_mapping" VALUES (55, 8, 36);
INSERT INTO "question_mapping" VALUES (56, 8, 7);
INSERT INTO "question_mapping" VALUES (57, 8, 37);
INSERT INTO "question_mapping" VALUES (58, 8, 38);
INSERT INTO "question_mapping" VALUES (59, 9, 29);
INSERT INTO "question_mapping" VALUES (60, 9, 30);
INSERT INTO "question_mapping" VALUES (61, 9, 31);
INSERT INTO "question_mapping" VALUES (62, 9, 3);
INSERT INTO "question_mapping" VALUES (63, 9, 32);
INSERT INTO "question_mapping" VALUES (64, 9, 33);
INSERT INTO "question_mapping" VALUES (65, 9, 34);
INSERT INTO "question_mapping" VALUES (66, 9, 35);
INSERT INTO "question_mapping" VALUES (67, 9, 36);
INSERT INTO "question_mapping" VALUES (68, 9, 7);
INSERT INTO "question_mapping" VALUES (69, 9, 37);
INSERT INTO "question_mapping" VALUES (70, 9, 38);
INSERT INTO "question_mapping" VALUES (71, 10, 29);
INSERT INTO "question_mapping" VALUES (72, 10, 30);
INSERT INTO "question_mapping" VALUES (73, 10, 31);
INSERT INTO "question_mapping" VALUES (74, 10, 3);
INSERT INTO "question_mapping" VALUES (75, 10, 32);
INSERT INTO "question_mapping" VALUES (76, 10, 33);
INSERT INTO "question_mapping" VALUES (77, 10, 34);
INSERT INTO "question_mapping" VALUES (78, 10, 35);
INSERT INTO "question_mapping" VALUES (79, 10, 36);
INSERT INTO "question_mapping" VALUES (80, 10, 7);
INSERT INTO "question_mapping" VALUES (81, 10, 37);
INSERT INTO "question_mapping" VALUES (82, 10, 38);
INSERT INTO "question_mapping" VALUES (83, 11, 29);
INSERT INTO "question_mapping" VALUES (84, 11, 30);
INSERT INTO "question_mapping" VALUES (85, 11, 31);
INSERT INTO "question_mapping" VALUES (86, 11, 3);
INSERT INTO "question_mapping" VALUES (87, 11, 32);
INSERT INTO "question_mapping" VALUES (88, 11, 33);
INSERT INTO "question_mapping" VALUES (89, 11, 34);
INSERT INTO "question_mapping" VALUES (90, 11, 35);
INSERT INTO "question_mapping" VALUES (91, 11, 36);
INSERT INTO "question_mapping" VALUES (92, 11, 7);
INSERT INTO "question_mapping" VALUES (93, 11, 37);
INSERT INTO "question_mapping" VALUES (94, 11, 38);
INSERT INTO "question_mapping" VALUES (95, 12, 1);
INSERT INTO "question_mapping" VALUES (96, 12, 4);
INSERT INTO "question_mapping" VALUES (97, 12, 5);
INSERT INTO "question_mapping" VALUES (98, 12, 15);
INSERT INTO "question_mapping" VALUES (99, 12, 12);


CREATE TABLE "response" (
	`responseId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`questionId`	INTEGER,
	`actionPlan`	TEXT,
	`priority`	INTEGER,
	`rating`	INTEGER,
	`timestamp`	TEXT,
	`locationId`	INTEGER,
	`image`	BLOB,
	`isActionItem`	INTEGER,
	`userId`	INTEGER,
	FOREIGN KEY(`userId`) REFERENCES `user`(`userId`),
	FOREIGN KEY(`questionId`) REFERENCES `question`(`questionId`),
	FOREIGN KEY(`locationId`) REFERENCES `location`(`locationId`)
);


CREATE TABLE "school" (
	`schoolId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`schoolName`	TEXT
);


CREATE TABLE "user" (
	`userId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`emailAddress`	TEXT,
	`schoolId`	INTEGER,
	FOREIGN KEY(`schoolId`) REFERENCES `schools`(`schoolId`)
);


CREATE TABLE "walkthrough" (
	`walkthroughId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`schoolId`	INTEGER NOT NULL,
	`name`	TEXT NOT NULL,
	`lastUpdatedDate`	TEXT,
	`createdDate`	TEXT NOT NULL,
	`percentComplete`	REAL,
	FOREIGN KEY(`schoolId`) REFERENCES `schools`(`schoolId`)
);
