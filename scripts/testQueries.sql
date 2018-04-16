-- Basic select statements
SELECT * FROM safetywalkthrough.schools;

select * from safetywalkthrough.user;

select * from safetywalkthrough.location
order by schoolId, locationId;

select * from safetywalkthrough.question;

select qm.locationId, qm.questionId, q.questionText from safetywalkthrough.question_mapping qm
join safetywalkthrough.question q on q.questionId = qm.questionId
where qm.schoolId = 3
and qm.locationId = 1
order by qm.locationId, qm.questionId;



select * from safetywalkthrough.walkthroughs;

select * from safetywalkthrough.responses
order by schoolId, walkthroughId, responseId;

select * from safetywalkthrough.responses
where walkthroughId = 1
and schoolId = 3
order by locationId;

-- Insert/Replace examples for manipulating dat
replace into safetywalkthrough.user (userName, emailAddress, role, schoolId) VALUES ('Robert', 'rbeerma@asu.edu', 'Teacher', 1);
replace into safetywalkthrough.schools (schoolId, schoolName) VALUES (2, 'Tempe High School');

INSERT INTO walkthroughs (walkthroughId, schoolId, userId, name, lastUpdatedDate, createdDate, percentComplete)
VALUES (1, 1, 1, 'Fall 2018', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 50.0) ON DUPLICATE KEY UPDATE name = 'Fall 2018', lastUpdatedDate = CURRENT_TIMESTAMP, percentComplete = 50.0;

-- Delete statement examples
delete from safetywalkthrough.schools where schoolId in (1, 2);
delete from safetywalkthrough.user where userId = 4;
delete from safetywalkthrough.location where schoolId = 0;

delete from safetywalkthrough.question_mapping;

delete from safetywalkthrough.walkthroughs;

delete from safetywalkthrough.responses
where schoolId = 3;

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
truncate responses;
truncate walkthroughs;
truncate user;
truncate schools;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- In-app queries
-- getSchoolWalkthroughsAndResponses
select w.walkthroughId AS WALKTHROUGH_ID, w.userId AS WALKTHROUGH_USER, w.name AS NAME, w.lastUpdatedDate AS LAST_UPDATED_DATE, w.createdDate AS CREATED_DATE, w.percentComplete AS PERCENT_COMPLETE,
r.responseId, r.userId AS RESPONSE_USER, r.locationId AS LOCATION_ID, r.questionId AS QUESTION_ID, r.actionPlan AS ACTION_PLAN, r.rating AS RATING, r.priority AS PRIORITY, r.timestamp AS TIMESTAMP,
r.isActionItem AS IS_ACTION_ITEM, r.image AS IMAGE_PATH, q.questionText
from safetywalkthrough.walkthroughs w
left outer join safetywalkthrough.responses r on w.schoolId = r.schoolId and w.walkthroughId = r.walkthroughId
join safetywalkthrough.schools s on s.schoolId = w.schoolId
join safetywalkthrough.question q on r.questionId = q.questionId
where w.schoolId = 3
order by r.locationId, r.responseId;

select * from safetywalkthrough.walkthroughs
where schoolId = 3;

INSERT INTO `safetywalkthrough`.`walkthroughs` (`walkthroughId`,`schoolId`,`userId`,`name`,`lastUpdatedDate`,`createdDate`,`percentComplete`)
VALUES
(1, 1, 1, 'dstc', '2018-04-03 20:32:22', '2018-04-03 20:31:59', 1.01),
(1, 3, 3, 'Summer 2018', '2018-04-07 17:09:57', '2018-04-05 18:40:24', 0.0),
(1, 4, 4, 'gsryx', '2018-03-28 01:48:02', '2018-03-28 01:48:02', 0.0),
(1, 6, 5, 'asdafs', '2018-04-06 05:06:38', '2018-04-06 05:06:38', 0.0),
(1, 7, 6, 'asdfas', '2018-04-06 05:09:31', '2018-04-06 05:09:31', 0.0);

ALTER TABLE safetywalkthrough.responses DROP PRIMARY KEY;
ALTER TABLE safetywalkthrough.responses ADD PRIMARY KEY (`responseId`, `walkthroughId`, `schoolId`, `locationId`, `questionId`);

update safetywalkthrough.walkthroughs set percentComplete  = ((11 / 99) * 100) where walkthroughId = 1 and schoolId = 3;

