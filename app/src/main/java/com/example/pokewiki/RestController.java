package com.example.pokewiki;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class RestController {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static Gson gson = new Gson();

    public static void get(String url, AsyncHttpResponseHandler responseHandler){
        client.get(url, null, responseHandler);
    }

    public static <T> T converterParaClasse(String json, Class classe){
        return (T) gson.fromJson(json, classe);
    }
}
