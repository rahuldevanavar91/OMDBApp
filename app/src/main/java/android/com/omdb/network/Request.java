package android.com.omdb.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

/**
 * Created by Rahul D on 4/22/17.
 */

public class Request {

    public static final String OMDB_URL = "http://www.omdbapi.com/";

    public void getRequest(Context context, String url, HashMap<String, String> param, final VolleyResponseListener listener) {
        StringBuilder builder = new StringBuilder();
        for (String key : param.keySet()) {
            Object value = param.get(key);
            if (value != null) {
                value = Uri.encode(String.valueOf(value), "UTF-8");
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(key).append("=").append(value);
            }
            url += "?" + builder.toString();
            CustomRequest getRequest = new CustomRequest(com.android.volley.Request.Method.GET, url, param, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError(error);
                }
            });
            VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(getRequest);
        }

    }
}