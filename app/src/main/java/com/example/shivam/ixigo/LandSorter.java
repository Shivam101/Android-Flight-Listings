package com.example.shivam.ixigo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by Shivam on 08/04/15 at 1:25 PM.
 */
public class LandSorter implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            return lhs.getLong("landingTime") > rhs.getLong("landingTime") ? 1 : (lhs
                    .getLong("landingTime") < rhs.getLong("landingTime") ? -1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
