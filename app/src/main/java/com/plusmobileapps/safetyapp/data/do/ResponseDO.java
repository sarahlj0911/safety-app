package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-Response")

public class ResponseDO {
    private String _responseId;
    private String _actionPlan;
    private String _imagePath;
    private String _isActionItem;
    private String _locationId;
    private String _locationName;
    private String _persisted;
    private String _priority;
    private String _questionId;
    private String _rating;
    private String _ratingText;
    private String _timeStamp;
    private String _title;
    private String _userId;
    private String _walkthroughId;

    @DynamoDBHashKey(attributeName = "responseId")
    @DynamoDBAttribute(attributeName = "responseId")
    public String getResponseId() {
        return _responseId;
    }

    public void setResponseId(final String _responseId) {
        this._responseId = _responseId;
    }
    @DynamoDBAttribute(attributeName = "actionPlan")
    public String getActionPlan() {
        return _actionPlan;
    }

    public void setActionPlan(final String _actionPlan) {
        this._actionPlan = _actionPlan;
    }
    @DynamoDBAttribute(attributeName = "imagePath")
    public String getImagePath() {
        return _imagePath;
    }

    public void setImagePath(final String _imagePath) {
        this._imagePath = _imagePath;
    }
    @DynamoDBAttribute(attributeName = "isActionItem")
    public String getIsActionItem() {
        return _isActionItem;
    }

    public void setIsActionItem(final String _isActionItem) {
        this._isActionItem = _isActionItem;
    }
    @DynamoDBAttribute(attributeName = "locationId")
    public String getLocationId() {
        return _locationId;
    }

    public void setLocationId(final String _locationId) {
        this._locationId = _locationId;
    }
    @DynamoDBAttribute(attributeName = "locationName")
    public String getLocationName() {
        return _locationName;
    }

    public void setLocationName(final String _locationName) {
        this._locationName = _locationName;
    }
    @DynamoDBAttribute(attributeName = "persisted")
    public String getPersisted() {
        return _persisted;
    }

    public void setPersisted(final String _persisted) {
        this._persisted = _persisted;
    }
    @DynamoDBAttribute(attributeName = "priority")
    public String getPriority() {
        return _priority;
    }

    public void setPriority(final String _priority) {
        this._priority = _priority;
    }
    @DynamoDBAttribute(attributeName = "questionId")
    public String getQuestionId() {
        return _questionId;
    }

    public void setQuestionId(final String _questionId) {
        this._questionId = _questionId;
    }
    @DynamoDBAttribute(attributeName = "rating")
    public String getRating() {
        return _rating;
    }

    public void setRating(final String _rating) {
        this._rating = _rating;
    }
    @DynamoDBAttribute(attributeName = "ratingText")
    public String getRatingText() {
        return _ratingText;
    }

    public void setRatingText(final String _ratingText) {
        this._ratingText = _ratingText;
    }
    @DynamoDBAttribute(attributeName = "timeStamp")
    public String getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(final String _timeStamp) {
        this._timeStamp = _timeStamp;
    }
    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return _title;
    }

    public void setTitle(final String _title) {
        this._title = _title;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "walkthroughId")
    public String getWalkthroughId() {
        return _walkthroughId;
    }

    public void setWalkthroughId(final String _walkthroughId) {
        this._walkthroughId = _walkthroughId;
    }

}
