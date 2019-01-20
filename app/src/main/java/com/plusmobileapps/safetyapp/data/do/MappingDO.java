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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-Mapping")

public class MappingDO {
    private String _mappingId;
    private String _locationId;
    private String _questionId;

    @DynamoDBHashKey(attributeName = "mappingId")
    @DynamoDBAttribute(attributeName = "mappingId")
    public String getMappingId() {
        return _mappingId;
    }

    public void setMappingId(final String _mappingId) {
        this._mappingId = _mappingId;
    }
    @DynamoDBAttribute(attributeName = "locationId")
    public String getLocationId() {
        return _locationId;
    }

    public void setLocationId(final String _locationId) {
        this._locationId = _locationId;
    }
    @DynamoDBAttribute(attributeName = "questionId")
    public String getQuestionId() {
        return _questionId;
    }

    public void setQuestionId(final String _questionId) {
        this._questionId = _questionId;
    }

}
