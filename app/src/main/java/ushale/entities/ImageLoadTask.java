package ushale.entities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ushale.core.VkCore;

/**
 * Created by ushale on 21/11/15.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private String url;

//    private ImageView imageView;
    private Object returnto;

    public ImageLoadTask(String url, Object returnto) {
        this.url = url;
        this.returnto = returnto;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.i("ImageLoadTask","started");
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        VkCore.getInstance().images().put(this.url,result);

        if(this.returnto instanceof Viewable){
            ((Viewable)this.returnto).respondOfVK(VkCore.getInstance().images().get(this.url));}
        if(this.returnto instanceof ImageView){
            ((ImageView)this.returnto).setImageBitmap(result);
        }
        Log.i("ImageLoadTask","ended");
    }
}