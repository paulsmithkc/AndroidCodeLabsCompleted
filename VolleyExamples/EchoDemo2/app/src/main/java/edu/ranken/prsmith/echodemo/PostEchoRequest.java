package edu.ranken.prsmith.echodemo;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class PostEchoRequest extends Request<EchoApiResponse> {
    private static final String LOG_TAG = PostEchoRequest.class.getSimpleName();

    private String message;
    private Response.Listener<EchoApiResponse> listener;

    public PostEchoRequest(
        String message,
        @Nullable Response.Listener<EchoApiResponse> listener,
        @Nullable Response.ErrorListener errorListener
    ) {
        super(Method.POST, "https://awd-example-services.herokuapp.com/api/echo", errorListener);
        this.message = message;
        this.listener = listener;
    }

//    private static String buildUrl(String message) {
//        return Uri.parse("https://awd-example-services.herokuapp.com/api/echo")
//            .buildUpon()
//            .appendQueryParameter("method", "GET")
//            .appendQueryParameter("message", message)
//            .toString();
//    }


    @Override
    protected Map<String, String> getParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("method", "POST");
        params.put("message", message);
        return params;
    }

    @Override
    protected Response<EchoApiResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            // parse bytes into a string
            String charset = HttpHeaderParser.parseCharset(response.headers, "utf-8");
            String json = new String(response.data, charset);

            // parse string into an object
            Gson gson = new Gson();
            EchoApiResponse obj = gson.fromJson(json, EchoApiResponse.class);

            // return response
            // Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            return Response.success(obj, null);
        } catch (Exception ex) {
            return Response.error(new ParseError(ex));
        }
    }

    @Override
    protected void deliverResponse(EchoApiResponse response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }
}
