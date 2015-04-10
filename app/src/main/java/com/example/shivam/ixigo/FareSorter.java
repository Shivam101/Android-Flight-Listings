package com.example.shivam.ixigo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by Shivam on 08/04/15 at 1:25 PM.
 */
public class FareSorter implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            return lhs.getInt("price") > rhs.getInt("price") ? 1 : (lhs
                    .getInt("price") < rhs.getInt("price") ? -1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
