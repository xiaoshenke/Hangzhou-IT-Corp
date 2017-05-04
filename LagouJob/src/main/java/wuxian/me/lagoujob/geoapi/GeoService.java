package wuxian.me.lagoujob.geoapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.istack.internal.NotNull;
import okhttp3.*;
import wuxian.me.lagoujob.util.OkhttpProvider;

import java.io.IOException;
import java.util.List;

import static wuxian.me.lagoujob.geoapi.Api.KEY;
import static wuxian.me.lagoujob.geoapi.Api.URL_GAODE;

/**
 * Created by wuxian on 2/5/2017.
 */
public class GeoService {

    public interface IGeoResultCallback {
        void onResult(GeoResult result);

        void onNetFail();
    }

    private GeoService() {
    }

    public static void asyncSendRequest(List<String> addressList, final @NotNull IGeoResultCallback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_GAODE).newBuilder();
        urlBuilder.addQueryParameter("key", KEY);

        StringBuilder b = new StringBuilder("");
        for (int i = 0; i < addressList.size(); i++) {
            b.append(addressList.get(i) + "|");
        }
        urlBuilder.addQueryParameter("address", b.toString());
        urlBuilder.addQueryParameter("output", "JSON");
        urlBuilder.addQueryParameter("batch", "true");

        urlBuilder.addQueryParameter("city", "hangzhou");
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        OkhttpProvider.getClient().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onNetFail();
                }

            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    GeoResult result = null;
                    try {
                        result = gson.fromJson(json, GeoResult.class);
                    } catch (JsonSyntaxException e) {
                        e.getMessage();
                    }

                    if (result == null) {
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(json);

                        result = new GeoResult();
                        result.status = object.get("status").getAsInt();
                        result.info = object.get("info").getAsString();
                    }
                    result.httpcode = 200;

                    if (callback != null) {
                        callback.onResult(result);
                    }

                } else {
                    GeoResult result = new GeoResult();
                    result.httpcode = response.code();

                    if (callback != null) {
                        callback.onResult(result);
                    }
                }

            }
        });
    }

    //sync call
    public static GeoResult sendRequest(String address) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_GAODE).newBuilder();
        urlBuilder.addQueryParameter("key", KEY);
        urlBuilder.addQueryParameter("address", address);
        urlBuilder.addQueryParameter("output", "JSON");
        urlBuilder.addQueryParameter("batch", "true");

        urlBuilder.addQueryParameter("city", "hangzhou");
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();
        Response response = null;
        response = OkhttpProvider.getClient().newCall(request).execute();

        if (response.isSuccessful()) {
            String json = response.body().string();
            Gson gson = new Gson();
            GeoResult result = gson.fromJson(json, GeoResult.class);

            if (result == null) {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(json);

                result = new GeoResult();
                result.status = object.get("status").getAsInt();
                result.info = object.get("info").getAsString();
            }

            result.httpcode = 200;
            return result;
        } else {
            GeoResult result = new GeoResult();
            result.httpcode = response.code();

            return result;
        }
    }
}
