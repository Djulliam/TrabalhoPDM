package com.example.trabalhopdm;
import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Consulta {

    public static final String apiKey = "477220f157bf147e317f7c7185944c1e";
    String name = "", link = "", text = "";
    Context context;

    public Consulta(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(String name, String link, String text);
    }

    public void getMusic(String artName, String musName, VolleyResponseListener volleyResponseListener) {

        String url = "https://api.vagalume.com.br/search.php?art=" + artName + "&mus=" + musName + "&apikey=" + apiKey;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Retira os dados do JSONObject
                    try {
                        JSONObject res = new JSONObject(String.valueOf(response));
                        JSONArray array = res.getJSONArray("mus");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            name = json.get("name").toString();
                            link = json.get("url").toString();
                            text = json.get("text").toString();

                            //Retorna a resposta para a Main Activity
                            volleyResponseListener.onResponse(name, link, text);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError("Erro");
                }
            });
            //Instância Singleton
            MySingleton.getInstance(context).addToRequestQueue(request);

        }
}
