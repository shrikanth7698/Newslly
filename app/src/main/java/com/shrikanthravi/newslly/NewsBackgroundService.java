package com.shrikanthravi.newslly;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.shrikanthravi.newslly.data.model.Article;
import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.remote.APIService;
import com.shrikanthravi.newslly.data.model.remote.GlobalData;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsBackgroundService extends Service {

    public NewsBackgroundService() {
    }

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    List<Article> articles;

    @Override
    public void onCreate() {
        super.onCreate();
        articles = new ArrayList<>();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("inside handler");
                requestHeadlines();
                handler.postDelayed(runnable,60*60*1000);
            }
        };

        handler.postDelayed(runnable, 1*1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void requestHeadlines(){
        System.out.println("inside request headlines");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIService mAPIService1;
        mAPIService1 = retrofit.create(APIService.class);
        Call call1 = mAPIService1.NewsApiCall("in", GlobalData.ApiKeyTesting);
        call1.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                System.out.println("news response code"+response.code()+" "+response.message());

                if (response.isSuccessful()) {
                    try {
                        articles.clear();

                        articles.addAll(response.body().getArticles());

                        try {
                            Collections.sort(articles, new Comparator<Article>() {
                                public int compare(Article m1, Article m2) {
                                    return m2.getDate().compareTo(m1.getDate());
                                }
                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        PushNotification(articles.get(0));


                    }

                    catch (Exception e) {
                        if (e.getMessage() != null) {

                        }
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("news request error");

            }
        });
    }

    public void PushNotification(final Article article)
    {
        try {
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            //Intent notificationIntent = new Intent(context, HomeActivity.class);
            //PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            Bitmap bitmap = null;


            try {
                bitmap = new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        try {
                            return Picasso.with(context).load(article.getUrlToImage())
                                    .error(R.drawable.newslly_icon)
                                    .get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //set
            //builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.drawable.newslly_icon);
            builder.setContentText(article.getDescription());
            builder.setContentTitle(article.getTitle());
            builder.setAutoCancel(true);

            builder.setStyle(new Notification.BigPictureStyle()
                    .bigPicture(bitmap));

            Notification notification = builder.build();
            nm.notify((int) System.currentTimeMillis(), notification);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
