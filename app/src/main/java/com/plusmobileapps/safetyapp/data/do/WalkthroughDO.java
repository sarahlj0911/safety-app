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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-Walkthrough")

public class WalkthroughDO {
    private String _walkthroughId;
    private String _createdDate;
    private String _isDeleted;
    private String _lastUpdatedDate;
    private String _name;
    private String _percentComplete;
    private String _schoolId;

    @DynamoDBHashKey(attributeName = "walkthroughId")
    @DynamoDBAttribute(attributeName = "walkthroughId")
    public String getWalkthroughId() {
        return _walkthroughId;
    }

    public void setWalkthroughId(final String _walkthroughId) {
        this._walkthroughId = _walkthroughId;
    }
    @DynamoDBAttribute(attributeName = "createdDate")
    public String getCreatedDate() {
        return _createdDate;
    }

    public void setCreatedDate(final String _createdDate) {
        this._createdDate = _createdDate;
    }
    @DynamoDBAttribute(attributeName = "isDeleted")
    public String getIsDeleted() {
        return _isDeleted;
    }

    public void setIsDeleted(final String _isDeleted) {
        this._isDeleted = _isDeleted;
    }
    @DynamoDBAttribute(attributeName = "lastUpdatedDate")
    public String getLastUpdatedDate() {
        return _lastUpdatedDate;
    }

    public void setLastUpdatedDate(final String _lastUpdatedDate) {
        this._lastUpdatedDate = _lastUpdatedDate;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "percentComplete")
    public String getPercentComplete() {
        return _percentComplete;
    }

    public void setPercentComplete(final String _percentComplete) {
        this._percentComplete = _percentComplete;
    }
    @DynamoDBAttribute(attributeName = "schoolId")
    public String getSchoolId() {
        return _schoolId;
    }

    public void setSchoolId(final String _schoolId) {
        this._schoolId = _schoolId;
    }

}
