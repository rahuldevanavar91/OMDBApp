package android.com.omdb.network;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 * Created by Rahul D on 4/22/17.
 */

public class CustomRequest extends Request<JSONObject> {
    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int DEFAULT_RETRY = 3;
    private static final float DEFAULT_MULT = 2;
    private VolleyResponseListener listener;
    private Map<String, String> params;

    public CustomRequest(int method, String url, Map<String, String> params,
                         VolleyResponseListener responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, DEFAULT_RETRY, DEFAULT_MULT));
    }

    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {

        if (listener != null) listener.onSuccess(response);

    }
}