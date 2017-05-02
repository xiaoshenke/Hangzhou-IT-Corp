package wuxian.me.lagoujob.geoapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagoujob.util.OkhttpProvider;

import java.io.IOException;

import static wuxian.me.lagoujob.geoapi.Api.KEY;
import static wuxian.me.lagoujob.geoapi.Api.URL_GAODE;

/**
 * Created by wuxian on 2/5/2017.
 */
public class GeoService {

    private GeoService() {
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
