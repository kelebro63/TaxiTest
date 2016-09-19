package com.kelebro63.taxitest.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kelebro63.taxitest.App;
import com.kelebro63.taxitest.BuildConfig;
import com.kelebro63.taxitest.api.ITaxiAPI;
import com.kelebro63.taxitest.api.MockRequestCarsITaxiAPI;
import com.kelebro63.taxitest.error_handler.RxErrorHandlingCallAdapterFactory;
import com.kelebro63.taxitest.location.GenymotionLocationUtil;
import com.kelebro63.taxitest.location.ILocationUtil;
import com.kelebro63.taxitest.location.LocationUtil;
import com.kelebro63.taxitest.prefs.IPrefs;
import com.kelebro63.taxitest.prefs.Prefs;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }


    /*
     genymotion buildConfig returns GenymotionLocationUtil, in other cases
     LocationUtil
     */
    @Singleton
    @Provides
    ILocationUtil provideLocationUtil() {
        if (BuildConfig.FLAVOR.equals("genymotion"))
            return new GenymotionLocationUtil();
        return new LocationUtil(app);
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(IPrefs prefs) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(15, TimeUnit.SECONDS);
        httpClient.readTimeout(15, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);

        httpClient.interceptors().add(chain -> {
            Request original = chain.request();
            if (prefs.isLoggedIn()) {
                Request request = original.newBuilder()
                        .header("Authorization", String.format("token %s", prefs.getSessionKey()))
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);
                return response;
            }
            return chain.proceed(original);
        });
        httpClient.interceptors().add(interceptor);
        httpClient.followRedirects(false);
        return httpClient.build();
    }


    /*
     production buildConfig returns ITaxiAPI, in other cases
     MockRequestCarsITaxiAPI
     */
    @Singleton
    @Provides
    ITaxiAPI provideAPI(OkHttpClient client, IPrefs prefs) {
        if (BuildConfig.FLAVOR.equals("prod")) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Retrofit adapter = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return adapter.create(ITaxiAPI.class);
        } else {
            return new MockRequestCarsITaxiAPI();
        }

    }

    @Singleton
    @Provides
    IPrefs providePrefs() {
        return new Prefs(app);
    }

}
