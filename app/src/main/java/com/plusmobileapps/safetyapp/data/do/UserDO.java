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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-User")

public class UserDO {
    private String _userId;
    private String _emailAddress;
    private String _lastLogin;
    private String _remoteId;
    private String _role;
    private String _schoolId;
    private String _userName;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "emailAddress")
    public String getEmailAddress() {
        return _emailAddress;
    }

    public void setEmailAddress(final String _emailAddress) {
        this._emailAddress = _emailAddress;
    }
    @DynamoDBAttribute(attributeName = "lastLogin")
    public String getLastLogin() {
        return _lastLogin;
    }

    public void setLastLogin(final String _lastLogin) {
        this._lastLogin = _lastLogin;
    }
    @DynamoDBAttribute(attributeName = "remoteId")
    public String getRemoteId() {
        return _remoteId;
    }

    public void setRemoteId(final String _remoteId) {
        this._remoteId = _remoteId;
    }
    @DynamoDBAttribute(attributeName = "role")
    public String getRole() {
        return _role;
    }

    public void setRole(final String _role) {
        this._role = _role;
    }
    @DynamoDBAttribute(attributeName = "schoolId")
    public String getSchoolId() {
        return _schoolId;
    }

    public void setSchoolId(final String _schoolId) {
        this._schoolId = _schoolId;
    }
    @DynamoDBAttribute(attributeName = "userName")
    public String getUserName() {
        return _userName;
    }

    public void setUserName(final String _userName) {
        this._userName = _userName;
    }

}
