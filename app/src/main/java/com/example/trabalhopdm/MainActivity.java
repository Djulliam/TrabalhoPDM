package com.example.trabalhopdm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

//Djulliam Andrei Krueger

                    //OBSERVAÇÃO
//Adicionar a seguinte linha no build.gradle
//implementation 'com.android.volley:volley:1.1.1'

public class MainActivity extends AppCompatActivity {

    //Declaração de variáveis
    Button btn_procurar, btn_link;
    EditText txt_art, txt_mus;
    ListView lst_resultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_procurar = findViewById(R.id.btn_procurar);
        btn_link = findViewById(R.id.btn_link);
        txt_art = findViewById(R.id.txt_art);
        txt_mus = findViewById(R.id.txt_mus);
        lst_resultados = findViewById(R.id.lst_resultados);

        final Consulta consulta = new Consulta(MainActivity.this);

        btn_procurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chama o método para fazer o request e passa os dados de ambas TextViews para a classe Consulta
                consulta.getMusic(txt_art.getText().toString(), txt_mus.getText().toString(), new Consulta.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String name, String link, String text) {

                        String message = "MÚSICA: \n" + name + "\n\nLETRA: \n" + text;

                        //Gera um hyperlink para o botão que redireciona para o url da música consultada
                        String linkedText = String.format("\n<a href=\"%s\">Visite em Vagalume.com</a> ", link);
                        btn_link.setText(Html.fromHtml(linkedText));
                        btn_link.setMovementMethod(LinkMovementMethod.getInstance());
                        btn_link.setVisibility(View.VISIBLE);

                        //Método para preencher a ListView
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, Collections.singletonList(message)) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view =super.getView(position, convertView, parent);
                                TextView lst_resultados = view.findViewById(android.R.id.text1);
                                lst_resultados.setTextColor(Color.WHITE);
                                return view;
                            }
                        };

                       lst_resultados.setAdapter(arrayAdapter);
                    }
                });

                //Esconde o teclado após o botão Procurar ser pressionado
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                    }
            }
        });
    }
}