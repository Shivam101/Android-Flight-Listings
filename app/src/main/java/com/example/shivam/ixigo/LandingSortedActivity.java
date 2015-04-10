package com.example.shivam.ixigo;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.view.MaterialListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*This activity contains a list of flights sorted from earliest landing time to last*/
public class LandingSortedActivity extends ActionBarActivity {

    MaterialListView takeOffCardList;
    JSONArray mJSONArr,mSortedJSONArr;
    JSONObject JSONobj,ob,airlineMapob,airportMapob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeoff_sorted);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab=getSupportActionBar();
        Resources r=getResources();
        Drawable d=r.getDrawable(R.color.royalBlue);
        ab.setBackgroundDrawable(d);
        takeOffCardList = (MaterialListView)findViewById(R.id.timeCardList1);
        new JSONTask().execute();
    }

    public class JSONTask extends AsyncTask<String,String,JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LandingSortedActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONobj = new JSONObject(loadJSONFromAsset());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return JSONobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                mJSONArr = JSONobj.getJSONArray("flightsData");
                mSortedJSONArr = getSortedList(mJSONArr);
                airlineMapob = JSONobj.getJSONObject("airlineMap");
                airportMapob = JSONobj.getJSONObject("airportMap");
                for(int i=0;i<mJSONArr.length();i++)
                {
                    ob = mSortedJSONArr.getJSONObject(i);
                    BigImageButtonsCard card = new BigImageButtonsCard(LandingSortedActivity.this);
                    long minute = (Long.parseLong(ob.getString("takeoffTime")) / (1000 * 60)) % 60;
                    long hour = (Long.parseLong(ob.getString("takeoffTime")) / (1000 * 60 * 60)) % 24;
                    String time = String.format("%02d:%02d", hour, minute);
                    long minute2 = (Long.parseLong(ob.getString("landingTime")) / (1000 * 60)) % 60;
                    long hour2 = (Long.parseLong(ob.getString("landingTime")) / (1000 * 60 * 60)) % 24;
                    String time2 = String.format("%02d:%02d", hour2, minute2);
                    long hourDiff = hour2-hour;
                    long minDiff = minute2-minute;
                    String dur = String.format("%02dh:%02d", hourDiff, minDiff);
                    if(ob.getString("originCode").equals("DEL")&&ob.getString("destinationCode").equals("MUM")) {

                        card.setTitle(airportMapob.getString("DEL")+" "+getResources().getString(R.string.arrow)+" "+airportMapob.getString("MUM"));
                    }
                    if(ob.getString("originCode").equals("MUM")&&ob.getString("destinationCode").equals("DEL")) {

                        card.setTitle(airportMapob.getString("MUM")+" "+getResources().getString(R.string.arrow)+" "+airportMapob.getString("DEL"));
                    }

                    if(ob.getString("airlineCode").equals("SJ"))
                    {
                        card.setDescription(ob.getString("class")+"\n\n" + "Departure : "+time+"\t\t"+" Arrival : "+time2+"\n\n"+"Duration : "+dur);
                        card.setRightButtonText(airlineMapob.getString("SJ"));
                    }
                    else if(ob.getString("airlineCode").equals("AI"))
                    {
                        card.setDescription(ob.getString("class")+"\n\n" + "Departure : "+time+"\t\t"+" Arrival : "+time2+"\n\n"+"Duration : "+dur);
                        card.setRightButtonText(airlineMapob.getString("AI"));
                    }
                    else if(ob.getString("airlineCode").equals("G8"))
                    {
                        card.setDescription(ob.getString("class")+"\n\n" + "Departure : "+time+"\t\t"+" Arrival : "+time2+"\n\n"+"Duration : "+dur);
                        card.setRightButtonText(airlineMapob.getString("G8"));
                    }
                    else if(ob.getString("airlineCode").equals("JA"))
                    {
                        card.setDescription(ob.getString("class")+"\n\n" + "Departure : "+time+"\t\t"+" Arrival : "+time2+"\n\n"+"Duration : "+dur);
                        card.setRightButtonText(airlineMapob.getString("JA"));
                    }
                    else if(ob.getString("airlineCode").equals("IN"))
                    {
                        card.setDescription(ob.getString("class")+"\n\n" + "Departure : "+time+"\t\t\t\t"+" Arrival : "+time2+"\n\n"+"Duration : "+dur);
                        card.setRightButtonText(airlineMapob.getString("IN"));
                    }

                    card.setLeftButtonText(getResources().getString(R.string.rs) +" "+ ob.getString("price"));
                    card.setDividerVisible(true);
                    card.setDrawable(R.drawable.back3);
                    takeOffCardList.add(card);
                }
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static JSONArray getSortedList(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }
        Collections.sort(list, new LandSorter());

        JSONArray resultArray = new JSONArray(list);

        return resultArray;

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("data.js");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_sorted, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(LandingSortedActivity.this, "This doesn't do anything at the moment", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
