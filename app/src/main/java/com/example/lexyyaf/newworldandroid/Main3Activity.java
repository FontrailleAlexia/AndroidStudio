package com.example.lexyyaf.newworldandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    private RequestQueue pileRequete;
    private Context context;
    private List<Map<String, Object>> data;
    private int _type, _idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        this.context = this;
        this._type = 0;
        this._idItem = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        final Button buttonReturn = (Button)findViewById(R.id.buttonReturn);
        final ListView listViewMarket = (ListView)findViewById(R.id.listViewMarket);
        listViewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(_type < 2) {
                    buttonReturn.setEnabled(true);
                    TextView tv = (TextView) view.findViewById(R.id.id);
                    _idItem = Integer.valueOf(tv.getText().toString());
                    _type += 1;

                    loadList(_type, _idItem);
                }
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_type > 0) {
                    _type -= 1;
                    buttonReturn.setEnabled(_type != 0);
                    loadList(_type, _idItem);
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        loadList(_type);
        super.onStart();
    }

    void loadList(final int type){
        loadList(type, -1);
    }

    void loadList(final int type, final int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            final ListView listViewMarket = (ListView)findViewById(R.id.listViewMarket);
            String url = "http://alexiafontraille.esy.es/wp-content/newWorld3/api/market.php";
            pileRequete = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //jsonNewWorld = new JSONArray(response);
                        JSONObject newworld = new JSONObject(response);
                        //On récupère l'identifiant
                        String error = newworld.getString("error");
                        if(error == "false")
                        {
                            JSONObject data= newworld.getJSONObject("data");
                            Iterator x = data.keys();

                            //Création de la ArrayList qui nous permettra de remplire la listView
                            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
                            while (x.hasNext()){
                                String key = (String) x.next();
                                JSONObject item = (JSONObject) data.get(key);
                                //String picture = item.getString("picture");

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", key);
                                hashMap.put("libelle", item.getString("libelle"));

                                if(type == 2){
                                    hashMap.put("quantity", item.getString("quantity"));
                                    hashMap.put("price", item.getString("price"));
                                }

                                arrayList.add(hashMap);
                            }

                            String[] from = {"id", "libelle", "quantity", "price"};
                            int[] to = {R.id.id, R.id.libelle, R.id.quantity, R.id.price};
                            SimpleAdapter mSchedule = new SimpleAdapter(Main3Activity.this, arrayList, R.layout.item_list_view, from, to);

                            listViewMarket.setAdapter(mSchedule);
                        }else{
                            String error_msg = newworld.getString("error_msg");
                            Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException exception) {
                        Log.i("newworld", "le json reçu est incorrect. " + exception.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("error", error.toString());
                }

            }) {
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", String.valueOf(type));
                    params.put("id", String.valueOf(id));
                    return params;
                };
            };
            pileRequete.add(stringRequest);

        }catch (Exception e){
            Log.i("errorOUPSS", e.getMessage());
        }
    }
}
