package kr.co.jbnu.remedi.serverIDO;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class ImageServerConnectionManager {
    private static String BASE_URL = "http://kossi.iptime.org:2000/";
    private ImageServerConnectionService serverConnection;
    private static ImageServerConnectionManager serverConnectionManager;

    public ImageServerConnectionManager(){
        initRetrofit();
    }

    private void initRetrofit(){
        setServerConnection();
    }

    private Retrofit getRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setServerConnection(){
        this.serverConnection = getRetrofit().create(ImageServerConnectionService.class);
    }

    public static synchronized ImageServerConnectionManager getInstance () {
        if (serverConnectionManager==null){
            serverConnectionManager = new ImageServerConnectionManager();
        }
        return serverConnectionManager;
        }

    public ImageServerConnectionService getServerConnection(){
        return serverConnection;
    }



}
