package com.example.dp_project;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Connectivity {
    public static String send (String url_esp32,String id,String data) {

        OkHttpClient client = new OkHttpClient();

        String get_req=url_esp32+"?"+id+"="+data;

        Request request = new Request.Builder()
                .url(get_req)
                .build();

        try  {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException error) {
            return error.toString();
        }

    }
    public static String ping(String url)
    {
        OkHttpClient client=new OkHttpClient();

        Request req=new Request.Builder().url(url).build();

        try  {
            Response response = client.newCall(req).execute();
            return response.body().string();

        } catch (IOException error) {
            return error.toString();
        }

    }
};
