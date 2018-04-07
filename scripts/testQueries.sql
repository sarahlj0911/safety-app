-- Basic select statements
SELECT * FROM safetywalkthrough.schools;

select * from safetywalkthrough.user;

select * from safetywalkthrough.location
order by schoolId, locationId;

select * from safetywalkthrough.question;

select * from safetywalkthrough.question_mapping
order by schoolId, locationId;

select * from safetywalkthrough.walkthroughs;

select * from safetywalkthrough.responses
order by schoolId, walkthroughId, responseId;

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

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
truncate responses;
truncate walkthroughs;
truncate user;
truncate schools;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- In-app queries
-- getSchoolWalkthroughsAndResponses
select w.walkthroughId AS WALKTHROUGH_ID, w.userId AS WALKTHROUGH_USER, w.name AS NAME, w.lastUpdatedDate AS LAST_UPDATED_DATE, w.createdDate AS CREATED_DATE, w.percentComplete AS PERCENT_COMPLETE,
 r.userId AS RESPONSE_USER, r.locationId AS LOCATION_ID, r.questionId AS QUESTION_ID, r.actionPlan AS ACTION_PLAN, r.priority AS PRIORITY, r.rating AS RATING, r.timestamp AS TIMESTAMP,
 r.isActionItem AS IS_ACTION_ITEM, r.image AS IMAGE_PATH
from safetywalkthrough.walkthroughs w
join safetywalkthrough.responses r on w.schoolId = r.schoolId and w.walkthroughId = r.walkthroughId
where w.schoolId = 3;
