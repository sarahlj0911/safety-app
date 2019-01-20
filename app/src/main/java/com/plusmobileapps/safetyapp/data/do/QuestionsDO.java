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

@DynamoDBTable(tableName = "safetyapp-mobilehub-2118900508-Questions")

public class QuestionsDO {
    private String _questionId;
    private String _questionText;
    private String _ratingOption1;
    private String _ratingOption2;
    private String _ratingOption3;
    private String _ratingOption4;
    private String _shortDesc;

    @DynamoDBHashKey(attributeName = "questionId")
    @DynamoDBAttribute(attributeName = "questionId")
    public String getQuestionId() {
        return _questionId;
    }

    public void setQuestionId(final String _questionId) {
        this._questionId = _questionId;
    }
    @DynamoDBAttribute(attributeName = "questionText")
    public String getQuestionText() {
        return _questionText;
    }

    public void setQuestionText(final String _questionText) {
        this._questionText = _questionText;
    }
    @DynamoDBAttribute(attributeName = "ratingOption1")
    public String getRatingOption1() {
        return _ratingOption1;
    }

    public void setRatingOption1(final String _ratingOption1) {
        this._ratingOption1 = _ratingOption1;
    }
    @DynamoDBAttribute(attributeName = "ratingOption2")
    public String getRatingOption2() {
        return _ratingOption2;
    }

    public void setRatingOption2(final String _ratingOption2) {
        this._ratingOption2 = _ratingOption2;
    }
    @DynamoDBAttribute(attributeName = "ratingOption3")
    public String getRatingOption3() {
        return _ratingOption3;
    }

    public void setRatingOption3(final String _ratingOption3) {
        this._ratingOption3 = _ratingOption3;
    }
    @DynamoDBAttribute(attributeName = "ratingOption4")
    public String getRatingOption4() {
        return _ratingOption4;
    }

    public void setRatingOption4(final String _ratingOption4) {
        this._ratingOption4 = _ratingOption4;
    }
    @DynamoDBAttribute(attributeName = "shortDesc")
    public String getShortDesc() {
        return _shortDesc;
    }

    public void setShortDesc(final String _shortDesc) {
        this._shortDesc = _shortDesc;
    }

}
