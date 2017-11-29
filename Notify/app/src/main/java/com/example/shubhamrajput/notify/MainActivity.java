package com.example.shubhamrajput.notify;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    String link="";

    public class DownloadTask extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... params) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url=new URL(params[0]);
                urlConnection=(HttpURLConnection) url.openConnection();

                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader =new InputStreamReader(in);

                int data=reader.read();
                int count=0;
                while (data!=-1 && count<23741){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                    count++;
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
    }

    public void onButtonClick(View view){
        EditText address=(EditText)findViewById(R.id.editText);
        link=address.getText().toString();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {

                            DownloadTask downloadTask =new DownloadTask();
                            try {
                                result1=result;
                                result =downloadTask.execute(link).get();
                                if(result1!=result){
                                    notification();
                                }
                                times++;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }


                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0,3000);

    }

    int times=0;String result1="";String result="";
    int notificationID=0;
    public void notification(){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.not);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
        notificationID++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
