package ushale.entities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ushale.core.VkCore;

/**
 * Created by ushale on 06/12/15.
 */
public class LongPoll implements Runnable{

    private static LongPoll instance = null;
    String key;
    String server;
    String ts;

    public void run(){
        HttpURLConnection urlConnection = null;
        int i;
        char c;
        boolean b = true;
        while(b){
            String line = "";
            String href = "http://"+server+"?act=a_check&key="+key+"&ts="+ts+"&wait=25&mode=2";
            try {
                URL url = new URL(href);
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                while((i=in.read())!=10)
                {
                    c=(char)i;
                    line+=c;
                }
                JSONObject jsonRootObject = new JSONObject(line);
                this.ts = jsonRootObject.optString("ts");
                JSONArray jsonArray = jsonRootObject.getJSONArray("updates");
                Log.i("LongPollJSONlen",String.valueOf(jsonArray.length()));
                Log.i("LongPollJSON",jsonArray.toString());
                for(int j=0;  j < jsonArray.length(); j++){
                    JSONArray jsonObject = jsonArray.optJSONArray(j);
                    VkCore.getInstance().processLongPoll(jsonObject);
                    Log.i("LongPollJSON",jsonObject.toString());
                }
                Log.i("LongPollline",line);
            } catch (IOException e) {
                e.printStackTrace();
                b = false;
            } catch (JSONException e) {
                e.printStackTrace();
//                b = false;
            } finally {
                urlConnection.disconnect();
            }
        }
    }

    public LongPoll(String key, String server, String ts){
        Log.i("LongPoll","started");
        this.server = server;
        this.key = key;
        this.ts = ts;
    }


}
