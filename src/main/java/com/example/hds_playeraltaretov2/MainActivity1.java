package com.example.hds_playeraltaretov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity1 extends AppCompatActivity {

    String displayID = "display0";
    ArrayList<String> arrayList = new ArrayList<String>();
    List<String> urls1=new ArrayList<String>();
    ArrayList<String> urls2=new ArrayList<String>();
    String[] str1;
    int index = 0;
    List<MediaItem> arrayList1 = new ArrayList<MediaItem>();
    List<String> urls3=new ArrayList<String>();
    ArrayList<String> urls4=new ArrayList<String>();
    String[] str3;
    int index1 = 0;
    int t=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        if (savedInstanceState == null) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> urlg;
                    urlg = Arrays.asList("https://guc.s2ecotech.com/disps/"+displayID+"/vlinks.txt","https://guc.s2ecotech.com/disps/"+displayID+"/Links.txt");

                    try {
                        URL url = new URL(urlg.get(1));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(60000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str0;

                        while ((str0 = in.readLine()) != null) {
                            urls1 = Arrays.asList(str0.split(";"));
                        }

                        in.close();
                    } catch (Exception e) {

                    }
                    try {
                        URL url = new URL(urlg.get(0));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(60000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str;

                        while ((str = in.readLine()) != null) {
                            urls3 = Arrays.asList(str.split(";"));
                        }

                        in.close();
                    } catch (Exception e) {

                    }

                }

            }).start();
        }
        while(urls3.size()<1) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (urls1.size() > 0) {

            int i = 0;
            int j = 0;
            while (i < urls1.size()) {
                str1 = urls1.get(i).split("##");
                Log.i("str1", str1[1]);
                arrayList.add(str1[1]);
                urls2.add(str1[0]);
                i++;
            }
            while (j < urls3.size()) {
                str3 = urls3.get(j).split("##");
                Log.i("str3", str3[1]);
                //arrayList1.add(str3[1]);
                arrayList1.add(MediaItem.fromUri(str3[1]));
                urls4.add(str3[0]);
                j++;
            }
            act(index,index1);
            //MediaItem mediaItem = MediaItem.fromUri(arrayList1.toString());
        }


    }
    private void act(int s, int s1) {

        StyledPlayerView playerView = findViewById(R.id.player_view);
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        ImageView img = findViewById(R.id.imageView);
        playerView.setPlayer(player);
        playerView.setVisibility(View.GONE);

        //player.setRepeatMode(player.REPEAT_MODE_ALL);
        player.prepare();
        final long[] dur = {0};
        //Toast.makeText(getApplicationContext(), urls4.get(index1), Toast.LENGTH_LONG).show();
        Picasso.with(this).load(arrayList.get(s)).into(img);
        img.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Published by :"+urls2.get(index1), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Prepare the player.
                player.setMediaItem(arrayList1.get(s1));
                dur[0] = player.getDuration();
                player.play();
                img.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Published by :"+urls4.get(index1), Toast.LENGTH_LONG).show();

            }
        }, 45000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.release();
                Log.i("dur", String.valueOf(dur[0]));
                if(index!=arrayList.size()-1) {
                    index++;
                }else{
                    index=0;
                }
                if(index1!=arrayList1.size()-1) {
                    index1++;
                }else{
                    index1=0;
                }
                t++;
                act(index,index1);

            }
        },130000);


    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}