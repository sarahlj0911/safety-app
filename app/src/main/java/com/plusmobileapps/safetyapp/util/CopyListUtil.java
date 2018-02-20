package com.plusmobileapps.safetyapp.util;

import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asteinme on 2/20/18.
 */

public class CopyListUtil {

    static public List<Response> copyResponseList(List<Response> responses) {
        List<Response> tempList = new ArrayList<>(0);
        for(Response response : responses) {
            tempList.add(new Response(
                    response.getResponseId(),
                    response.getIsActionItem(),
                    response.getLocationId(),
                    response.getTimeStamp(),
                    response.getRating(),
                    response.getPriority(),
                    response.getActionPlan(),
                    response.getQuestionId(),
                    response.getImagePath(),
                    response.getUserId(),
                    response.getWalkthroughId()
            ));
        }
        return tempList;
    }
}
