package com.plusmobileapps.safetyapp;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

import static com.amazonaws.regions.Regions.US_WEST_2;

// AWS User Pool:           https://us-west-2.console.aws.amazon.com/cognito/users?region=us-west-2#/pool/us-west-2_syFk4QKhg/users?_k=wjemxd
// AWS Android SDK:         https://aws-amplify.github.io/docs/android/start?ref=amplify-android-btn
// AWS Android Git Repo:    https://github.com/aws-amplify/aws-sdk-android
// AWS Services API:        https://docs.aws.amazon.com/cognito/latest/developerguide/using-amazon-cognito-user-identity-pools-android-sdk.html
// AWS UserType:            https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_UserType.html

public class AwsServices {
    private String POOL_ID;
    private String APP_ClIENT_ID;
    private String APP_ClIENT_SECRET;
    private Regions REGION;

    public AwsServices(){
        POOL_ID             = "us-west-2_syFk4QKhg";
        APP_ClIENT_ID       = "5eoapgm3te1ti096josbnpuaab";
        APP_ClIENT_SECRET   = "as8mj88k7flu6vtolpdfo1t40a4kr0keb2i8pr0dmn0omjp8ns8";
        REGION = US_WEST_2;
    }

    public String getAPP_ClIENT_ID() {
        return APP_ClIENT_ID;
    }

    public String getAPP_ClIENT_SECRET() {
        return APP_ClIENT_SECRET;
    }

    public String getPOOL_ID() {
        return POOL_ID;
    }

    public Regions getREGION() { return REGION; }

    public CognitoUserPool initAWSUserPool(Context context){
        return new CognitoUserPool(context, POOL_ID, APP_ClIENT_ID, APP_ClIENT_SECRET, REGION);
    }
}
