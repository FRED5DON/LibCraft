package ts.app.sagosoft.com.libcraft;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;

public class SpringMusicClient {
    private static final String API_URL = "http://www.freddon.com";

    private static SpringMusic springMusicService;

    public static SpringMusic getSpringMusicClient() {

        if (springMusicService == null) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            springMusicService = restAdapter.create(SpringMusic.class);

        }
        return springMusicService;
    }

    interface SpringMusic {
        @POST("/api/home.php")
        void createNewAlbum(@Body com.alibaba.fastjson.JSONObject json, Callback<String> callback);
    }
}
