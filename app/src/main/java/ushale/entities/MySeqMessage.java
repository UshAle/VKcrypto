package ushale.entities;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import org.json.*;

import java.util.List;
import ushale.core.Core;
import ushale.core.VkCore;
/**
 * Created by ushale on 09/11/15.
 */
public class MySeqMessage implements Viewable{

    private JSONObject jsOK;
    private MyTextView b;
    private TextView textName;
    private JSONObject js;
    private ImageView iv;

    public void respondOfVK(Object response){
        if(response instanceof JSONObject) {
            this.jsOK = (JSONObject)response;
            try {
                VkCore.getInstance().getImage(this.jsOK.optString("photo_100"), this);
                textName.setText(this.jsOK.optString("first_name")+" "+this.jsOK.optString("last_name"));
                b.setText(this.js.optString("body"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(response instanceof Bitmap){
            Log.i("BitmaPrespondOfVK","Bitmap");
            this.iv.setImageBitmap((Bitmap)response);
        }
    }

    @Override
    public void click() {

    }

    public MySeqMessage(JSONObject resp){
        this.jsOK = null;
        js = resp;
        this.iv = new ImageView(Core.getInstance().getMainActivity());
        VkCore.getInstance().getFriedName(this.js.optString("from_id"),this);
    }

    public View draw(Activity activity, View.OnClickListener list){
        LinearLayout l =new LinearLayout(activity);
        l.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout l2 = new LinearLayout(activity);
        l2.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l2.setOrientation(LinearLayout.VERTICAL);
        this.b = new MyTextView(activity);
        this.textName = new TextView(activity);
        this.textName.setTextColor(Color.rgb(89, 125, 163));
        textName.setText(this.jsOK.optString("first_name")+" "+this.jsOK.optString("last_name"));
        b.setText(this.js.optString("body"));
        b.setMyParams(this.js.optString("body"), this.js.optString("from_id"));
        l.addView(this.iv);
        l2.addView(this.textName);
        l2.addView(b);
        l.addView(l2);
//        b.setOnClickListener(list);
        return (View)l;
    }

    public static class MyTextView extends TextView{

        private String body;

        private String uid;

        public MyTextView(Activity a){
            super(a);
        }

        public String getUsde(){
            return this.uid;
        }

        public void setMyParams(String body, String uid){
            this.body = body;
            this.uid = uid;
        }

        public String toString(){
            return uid+"/"+body;
        }

    }


}