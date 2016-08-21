package kr.co.jbnu.remedi.serverIDO;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class ServerConnectionManager {
    private static String BASE_URL = "https://remedi-baicalin.c9users.io/";
    private ServerConnectionService serverConnection;
    private static ServerConnectionManager serverConnectionManager;

    public ServerConnectionManager(){
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
        this.serverConnection = getRetrofit().create(ServerConnectionService.class);
    }

    public static synchronized ServerConnectionManager getInstance () {
        if (serverConnectionManager==null){
            serverConnectionManager = new ServerConnectionManager();
        }
        return serverConnectionManager;
        }

    public ServerConnectionService getServerConnection(){
        return serverConnection;
    }



}
