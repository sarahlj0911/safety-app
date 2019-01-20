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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-School")

public class SchoolDO {
    private String _schoolId;
    private String _remoteId;
    private String _schoolName;

    @DynamoDBHashKey(attributeName = "schoolId")
    @DynamoDBAttribute(attributeName = "schoolId")
    public String getSchoolId() {
        return _schoolId;
    }

    public void setSchoolId(final String _schoolId) {
        this._schoolId = _schoolId;
    }
    @DynamoDBAttribute(attributeName = "remoteId")
    public String getRemoteId() {
        return _remoteId;
    }

    public void setRemoteId(final String _remoteId) {
        this._remoteId = _remoteId;
    }
    @DynamoDBAttribute(attributeName = "schoolName")
    public String getSchoolName() {
        return _schoolName;
    }

    public void setSchoolName(final String _schoolName) {
        this._schoolName = _schoolName;
    }

}
