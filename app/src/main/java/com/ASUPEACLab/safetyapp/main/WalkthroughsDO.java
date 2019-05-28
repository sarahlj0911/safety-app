package com.ASUPEACLab.safetyapp.main;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

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
