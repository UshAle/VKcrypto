package ushale.entities;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Layout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.*;

import ushale.core.Core;
import ushale.core.VkCore;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ushale on 05/12/15.
 */
public class MyNewSecDialog implements Viewable{

    private JSONObject jRVK;
    private JSONObject js;
    private TextView textName;
    private TextView tw;
    private List<ImageView> liv;

    public void respondOfVK(Object response){
        if(response instanceof JSONObject){
            this.jRVK = (JSONObject)response;
            try{
                if (!this.js.getJSONObject("message").optString("title").equals(" ... ")){
                    VkCore.getInstance().getChatImages(this.js.getJSONObject("message").optString("chat_id"), this.liv);
                    textName.setText(this.js.getJSONObject("message").optString("title"));
                    tw.setText(this.jRVK.optString("first_name")+" "+this.jRVK.optString("last_name")+"\n"+this.js.getJSONObject("message").optString("body"));
                }else{
                    VkCore.getInstance().getImage(this.jRVK.optString("photo_100"), this);
                    textName.setText(this.jRVK.optString("first_name") + " " + this.jRVK.optString("last_name"));
                    this.tw.setText(this.js.getJSONObject("message").optString("body"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void click() {
        this.tw.setText("asdas");
    }

    public MyNewSecDialog(JSONObject resp) throws JSONException{
        this.jRVK = null;
        this.js = resp;
        this.liv = new LinkedList<ImageView>();
        for(int i = 0; i<1;i++){
            this.liv.add(new ImageView(Core.getInstance().getMainActivity()));
        }
        VkCore.getInstance().getFriedName(this.js.getJSONObject("message").optString("user_id"),this);
    }

    public View draw(Activity activity, View.OnClickListener list){
        MyLinearLayout l = new MyLinearLayout(activity);
        LinearLayout l2 = new LinearLayout(activity);
        l2.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l2.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);
        this.tw = new TextView(activity);
        String name = "...";
        if(this.jRVK!=null){
            try{
                if (!this.js.getJSONObject("message").optString("title").equals(" ... ")){
//                    this.iv = this.liv.get(0);
                    name = this.js.getJSONObject("message").optString("title");
                    this.tw.setText(this.jRVK.optString("first_name")+" "+this.jRVK.optString("last_name")+"\n"+this.js.getJSONObject("message").optString("body"));
                }else{
                    name = this.jRVK.optString("first_name")+" "+this.jRVK.optString("last_name");
                    this.tw.setText(this.js.getJSONObject("message").optString("body"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        this.textName = new TextView(activity);
        this.textName.setTextColor(Color.rgb(89, 125, 163));
        textName.setText(name);
        try {
            if(this.js.getJSONObject("message").has("chat_id")){
                l.setMyParams("chat_id", this.js.getJSONObject("message").optString("chat_id"));

            }else{
                l.setMyParams("user_id", this.js.getJSONObject("message").optString("user_id"));
            }
        }catch (Exception e){
            Log.e("drawErr","json");
        }

//        l.addView(this.iv);
        l.addView(l2);
//        this.b.set
        l2.addView(textName);
        l2.addView(this.tw);
        for(int i=0; i<l.getChildCount(); i++){
            l.getChildAt(i).setClickable(true);
            l.getChildAt(i).setOnClickListener(list);
        }
        l.setClickable(true);
        l.setOnClickListener(list);
        return (View)l;
    }

    public static class MyLinearLayout extends LinearLayout{

        private String name;

        private String param;

        private String id;

        public MyLinearLayout(Activity a){
            super(a);
        }

        public String getParam() {
            return this.param;
        }

        public String getUsde(){
            return this.id;
        }

        public String getName(){
            return this.name;
        }

        public void setMyParams(String param, String id){
            this.id = id;
            this.param = param;
        }
    }

}
