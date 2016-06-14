package com.example.lexyyaf.newworldandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private String nickname, lastname, firstname, email, password, retypePassword, msgErreur = "";
    private String requete, url, url2, url3;
    private RequestQueue pileRequete;
    private Context context;
    private JSONArray jsonNewWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final EditText editTextPseudo = (EditText) findViewById(R.id.editTextPseudo);
        final EditText editTextNom = (EditText) findViewById(R.id.editTextLastName);
        final EditText editTextPrenom = (EditText) findViewById(R.id.editTextFirstName);
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editTextMdp = (EditText) findViewById(R.id.editTextMdp);
        final EditText editTextMdp2 = (EditText) findViewById(R.id.editTextMdp2);
        Button buttonAnnulerInscription = (Button) findViewById(R.id.buttonAnnulerInscription);
        Button buttonValiderInscription = (Button) findViewById(R.id.buttonConfirmerInscription);

        pileRequete = Volley.newRequestQueue(this);

        //Changer la couleur d'arrière plan de l'actionBar
        //ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#821B1B"));
        //getActionBar().setBackgroundDrawable(colorDrawable);
        //Changer la couleur de texte de l'actionBar
        //getActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Inscription</font>"));

        //Bouton Annuler
        buttonAnnulerInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        //Bouton Valider
        buttonValiderInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = editTextPseudo.getText().toString();
                lastname = editTextNom.getText().toString();
                firstname = editTextPrenom.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextMdp.getText().toString();
                retypePassword = editTextMdp2.getText().toString();

                //Si un champ est vide
                if(nickname.isEmpty() || lastname.isEmpty() || firstname.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty())
                {
                    //Alors j'affiche une alerte
                    Toast.makeText(getApplicationContext(), "Veuillez saisir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Vérifier que l'email n'est pas déjà utilisé dans la base de données
                    url = "http://alexiafontraille.esy.es/wp-content/newWorld3/api/registration.php";

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
                                    Toast.makeText(getApplicationContext(), "Compte créé avec succés, veuillez vous connecter", Toast.LENGTH_SHORT).show();
                                }else{
                                    String error_msg = newworld.getString("error_msg");
                                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException exception) {
                                Log.i("newworld", "le json reçu est incorrect");
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
                            params.put("nickname", nickname);
                            params.put("lastname", lastname);
                            params.put("firstname", firstname);
                            params.put("email", email);
                            params.put("password", password);
                            params.put("retypePassword", retypePassword);
                            return params;
                        };
                    };
                    pileRequete.add(stringRequest);

/*

                    //Vérifier le pseudo et le mot de passe dans la base de données
                    url2 = "http://alexiafontraille.esy.es/wp-content/newWorld3/api/registration.php?nickname=" + pseudo + "&password=" + mdp;

                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonNewWorld = new JSONArray(response);
                                JSONObject newworld = jsonNewWorld.getJSONObject(0);
                                //on recupère l'identifiant
                                id = newworld.getString("utilId");
                                if (id != "") {
                                    if(msgErreur != "") {
                                        msgErreur += "\n";
                                    }
                                    msgErreur += "Cette adresse est déjà prise, veuillez en choisir un autre";
                                }
                            } catch (JSONException exception) {
                                Log.i("email", "le json reçu est incorrect");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());
                        }
                    });
                    pileRequete.add(stringRequest2);

                    //CORRIGER ERREUR
                    //Vérifier que les mots de passe sont identiques
                    if(mdp != mdp2)
                    {
                        if(msgErreur != "") {
                            msgErreur += "\n";
                        }
                        msgErreur += "Les mots de passe doivent être identiques";
                    }

                    //Vérifier que le mot de passe fasse plus de 8 caractères
                    if(mdp.length() < 8)
                    {
                        if(msgErreur != "")
                        {
                            msgErreur += "\n";
                        }
                        msgErreur += "Le mot de passe doit faire plus de 8 caractères";
                    }*/
                }


                /*
                if(msgErreur.isEmpty())
                {
                    //Enregistrement
                    url3 = "http://alexiafontraille.esy.es/wp-content/newWorld3/api/registration.php?nickname=" + pseudo + "&password=" + mdp + "&nom=" + nom + "&prenom=" + prenom + "&email=" + email;

                    StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonNewWorld = new JSONArray(response);
                                JSONObject newworld = jsonNewWorld.getJSONObject(0);

                            } catch (JSONException exception) {
                                Log.i("enregistrement", "le json reçu est incorrect");

                                //Fermer la fentre et revenir sur la page de connexion
                                Toast.makeText(getApplicationContext(), "Compte créé avec succés, veuillez vous connecter", Toast.LENGTH_SHORT).show();

                                Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
                                startActivity(myIntent);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.getLocalizedMessage());
                        }
                    });
                    pileRequete.add(stringRequest3);
                }
                else
                {
                    //Affichage des erreurs
                    Toast.makeText(getApplicationContext(), msgErreur, Toast.LENGTH_SHORT).show();
                    msgErreur = "";
                }*/
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
