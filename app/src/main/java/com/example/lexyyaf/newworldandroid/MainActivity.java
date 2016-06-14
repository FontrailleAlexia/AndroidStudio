package com.example.lexyyaf.newworldandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String requete, url, id, url2;
    private RequestQueue pileRequete;
    private Context context;
    private JSONArray jsonNewWorld;
    private boolean connecte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        final EditText editTextPseudo = (EditText) findViewById(R.id.editTextPseudoInscription);
        final EditText editTextMotDePasse = (EditText) findViewById(R.id.editTextMotDePasse);
        final Button buttonConnexion = (Button) findViewById(R.id.buttonConnexion);

        //Bouton passer
        TextView buttonPasser = (TextView) findViewById(R.id.textViewPasser);
        buttonPasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(myIntent);

                //Passage de la variable en etat de déconnecté
                connecte = false;
            }
        });


        //Bouton Inscrivez-vous
        TextView buttonInscrivezVous = (TextView) findViewById(R.id.textViewInscrivezVous);
        buttonInscrivezVous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(myIntent);
            }
        });

        //Bouton Deconnexion
        final Button buttonDeconnexion = (Button) findViewById(R.id.buttonDeconnexion);
        buttonDeconnexion.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 connecte = false;
                 //Désactivation des champs
                 editTextPseudo.setEnabled(true);
                 editTextMotDePasse.setEnabled(true);
                 buttonConnexion.setEnabled(true);

                 buttonDeconnexion.setEnabled(false);

                 //Coloration du bouton de deconnexion en gris
                 ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#BDBDBD"));
                 buttonDeconnexion.setBackground(colorDrawable);

                 //Coloration du bouton de connexion en bleu
                 ColorDrawable colorDrawable2 = new ColorDrawable(Color.parseColor("#243D91"));
                 buttonConnexion.setBackground(colorDrawable2);

                 //Vidage des champs
                 editTextPseudo.setText("");
                 editTextMotDePasse.setText("");
                 connecte = false;
                 //Suppression de l'id de l'utilisateur connecté
                 id = "";
                }
            }
        );

        //Bouton Connexion
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pseudo;
                final String motDePasse;

                pseudo = editTextPseudo.getText().toString();
                motDePasse = editTextMotDePasse.getText().toString();
                Log.i("VERIF", pseudo);

                if (pseudo.isEmpty() || motDePasse.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Saisissez tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    url = "http://alexiafontraille.esy.es/wp-content/newWorld3/api/login.php?nickname=" + pseudo + "&password=" + motDePasse;
                    pileRequete = Volley.newRequestQueue(context);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //jsonNewWorld = new JSONArray(response);
                                JSONObject newworld = new JSONObject(response);
                                boolean error = newworld.getBoolean("error");
                                String.valueOf(error);
                                //"Error = " + error;
                                if(!error){
                                    JSONObject user = newworld.getJSONObject("user");
                                    //on recupère l'identifiant
                                    id = user.getString("id");
                                    Log.e("Utilisateur", id);
                                    if (id != "") {
                                        //Ouvrir la page d'accueil
                                        Intent myIntent = new Intent(MainActivity.this, Main3Activity.class);
                                        startActivity(myIntent);

                                        connecte = true;
                                        //Désactivation des champs
                                        editTextPseudo.setEnabled(false);
                                        editTextMotDePasse.setEnabled(false);
                                        buttonConnexion.setEnabled(false);

                                        buttonDeconnexion.setEnabled(true);

                                        //Coloration du boutton de deconnexion en rouge
                                        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF0000"));
                                        buttonDeconnexion.setBackground(colorDrawable);
                                        buttonDeconnexion.setTextColor(Color.WHITE);

                                        //Coloration du boutton de connexion en gris
                                        ColorDrawable colorDrawable2 = new ColorDrawable(Color.parseColor("#BDBDBD"));
                                        buttonConnexion.setBackground(colorDrawable2);
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(), newworld.getString("error_msg"), Toast.LENGTH_SHORT).show();
                                    editTextMotDePasse.setText("");
                                    Log.e("User", "aaa");
                                    String.valueOf(error);

                                }
                            } catch (JSONException exception) {
                                Log.i("newworld", "Le json reçu est incorrect");
                                Toast.makeText(getApplicationContext(), "L'identifiant ou le mot de passe est incorrect", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());
                        }
                    });
                    pileRequete.add(stringRequest);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
