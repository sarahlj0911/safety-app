package com.plusmobileapps.safetyapp.main;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-walkthroughs")

public class WalkthroughsDO {
    private String _walkthroughId;

    @DynamoDBHashKey(attributeName = "walkthroughId")
    @DynamoDBAttribute(attributeName = "walkthroughId")
    public String getWalkthroughId() {
        return _walkthroughId;
    }

    public void setWalkthroughId(final String _walkthroughId) {
        this._walkthroughId = _walkthroughId;
    }

}
