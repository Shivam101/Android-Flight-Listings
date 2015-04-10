package com.example.shivam.flights;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    JSONArray mJSONArr;
    JSONObject JSONobj,ob,airlineMapob,airportMapob;
    FloatingActionsMenu menu;
    MaterialListView cardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardList = (MaterialListView)findViewById(R.id.cardList);
        cardList.getLayoutManager().offsetChildrenVertical(30);
        menu = (FloatingActionsMenu)findViewById(R.id.fab1);
        /*
        To set the ActionBar color for API<21
        For API 21, see values-v21/styles.xml
         */
        ActionBar ab=getSupportActionBar();
        Resources r=getResources();
        Drawable d=r.getDrawable(R.color.royalBlue);
        ab.setBackgroundDrawable(d);

        new JSONTask().execute();
        FloatingActionButton fare = (FloatingActionButton)findViewById(R.id.sortFare);
        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.collapse();
                Intent i = new Intent(MainActivity.this,FareSortedActivity.class);
                startActivity(i);
            }
        });
        FloatingActionButton time = (FloatingActionButton)findViewById(R.id.sortTime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.collapse();
                Intent i = new Intent(MainActivity.this, TakeoffSortedActivity.class);
                startActivity(i);
            }
        });
        FloatingActionButton time2 = (FloatingActionButton)findViewById(R.id.sortTime2);
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.collapse();
                Intent i = new Intent(MainActivity.this,LandingSortedActivity.class);
                startActivity(i);
            }
        });
        cardList.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int i) {
                //Toast.makeText(MainActivity.this,)
            }
        });
        }



    /*
    Loads the JSON and adds list items on a background thread
    Displays a progress dialog while loading and shows the result once done
     */
    public class JSONTask extends AsyncTask<String,String,JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
                airlineMapob = JSONobj.getJSONObject("airlineMap");
                airportMapob = JSONobj.getJSONObject("airportMap");
                for(int i=0;i<mJSONArr.length();i++)
                {
                    ob = mJSONArr.getJSONObject(i);
                    BigImageButtonsCard card = new BigImageButtonsCard(MainActivity.this);

                    /*Converting milliseconds to hours and minutes */
                    long minute = (Long.parseLong(ob.getString("takeoffTime")) / (1000 * 60)) % 60;
                    long hour = (Long.parseLong(ob.getString("takeoffTime")) / (1000 * 60 * 60)) % 24;
                    String time = String.format("%02d:%02d", hour, minute);
                    long minute2 = (Long.parseLong(ob.getString("landingTime")) / (1000 * 60)) % 60;
                    long hour2 = (Long.parseLong(ob.getString("landingTime")) / (1000 * 60 * 60)) % 24;
                    String time2 = String.format("%02d:%02d", hour2, minute2);
                    /* For calculation of flight duration */
                    long hourDiff = hour2-hour;
                    long minDiff = minute2-minute;
                    String dur = String.format("%02dh:%02d", hourDiff, minDiff);
                    /* These buttons are used just to display information and hence must have empty listener functions */
                    card.setOnRightButtonPressedListener(new OnButtonPressListener() {
                        @Override
                        public void onButtonPressedListener(View view, Card card) {

                        }
                    });
                    card.setOnLeftButtonPressedListener(new OnButtonPressListener() {
                        @Override
                        public void onButtonPressedListener(View view, Card card) {

                        }
                    });
                    /*Assigning card title based on origin code using the map objects in the JSON file*/
                    if(ob.getString("originCode").equals("DEL")&&ob.getString("destinationCode").equals("MUM")) {

                        card.setTitle(airportMapob.getString("DEL")+" "+getResources().getString(R.string.arrow)+" "+airportMapob.getString("MUM"));
                    }
                    if(ob.getString("originCode").equals("MUM")&&ob.getString("destinationCode").equals("DEL")) {

                        card.setTitle(airportMapob.getString("MUM")+" "+getResources().getString(R.string.arrow)+" "+airportMapob.getString("DEL"));
                    }
                    /*Assigning card description using the departure and arrival time as well as airline map object*/
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

                        card.setLeftButtonText(getResources().getString(R.string.rs) + " " + ob.getString("price"));
                        card.setDividerVisible(true);
                        card.setDrawable(R.drawable.back3);
                        cardList.add(card);
                    }
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /*Returns the JSON content from the file in the form of a string*/
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "This doesn't do anything at the moment", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
