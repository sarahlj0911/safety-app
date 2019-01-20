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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-Location")

public class LocationDO {
    private String _locationId;
    private String _isFinished;
    private String _locationInstruction;
    private String _name;
    private String _progess;
    private String _type;

    @DynamoDBHashKey(attributeName = "locationId")
    @DynamoDBAttribute(attributeName = "locationId")
    public String getLocationId() {
        return _locationId;
    }

    public void setLocationId(final String _locationId) {
        this._locationId = _locationId;
    }
    @DynamoDBAttribute(attributeName = "isFinished")
    public String getIsFinished() {
        return _isFinished;
    }

    public void setIsFinished(final String _isFinished) {
        this._isFinished = _isFinished;
    }
    @DynamoDBAttribute(attributeName = "locationInstruction")
    public String getLocationInstruction() {
        return _locationInstruction;
    }

    public void setLocationInstruction(final String _locationInstruction) {
        this._locationInstruction = _locationInstruction;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "progess")
    public String getProgess() {
        return _progess;
    }

    public void setProgess(final String _progess) {
        this._progess = _progess;
    }
    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return _type;
    }

    public void setType(final String _type) {
        this._type = _type;
    }

}
