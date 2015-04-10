package com.example.shivam.flights;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by Shivam on 08/04/15 at 1:25 PM.
 * This class is a comparator which compares the takeofftime of successive JSONObjects in the given JSONArray.
 * This is then used in the TakeoffSortedActivity
 */
public class TakeOffSorter implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            return lhs.getDouble("takeoffTime") > rhs.getDouble("takeoffTime") ? 1 : (lhs
                    .getDouble("takeoffTime") < rhs.getDouble("takeoffTime") ? -1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
