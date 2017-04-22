package android.com.omdb.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;
/**
 * Created by Rahul D on 4/22/17.
 */

public interface VolleyResponseListener {
    void onSuccess(JSONObject jsonObject);

    void onError(VolleyError volleyError);
}
