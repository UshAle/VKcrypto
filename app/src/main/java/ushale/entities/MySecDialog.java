package ushale.entities;

import android.app.ActionBar;
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
 * Created by ushale on 04.03.15.
 */

public class MySecDialog implements Viewable{

    private JSONObject jRVK;
    private JSONObject js;
    private TextView textName;
    private TextView tw;
    private ImageView iv;
    private List<ImageView> liv;

    public void respondOfVK(Object response){
        if(response instanceof JSONObject){
            Log.i("MySecDialog",((JSONObject)response).toString());
            this.jRVK = (JSONObject)response;
            try{
                Log.i("MySecDialog1",((JSONObject)this.jRVK).toString());
                Log.i("MySecDialog1",((JSONObject)this.js).toString());
                if (!this.js.getJSONObject("message").optString("title").equals(" ... ")){
                    Log.i("MySecDialog1","chatid="+this.js.getJSONObject("message").optString("chat_id"));
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
        if(response instanceof Bitmap){
            this.iv.setImageBitmap((Bitmap)response);
        }
    }

    public MySecDialog(JSONObject resp) throws JSONException{
        this.textName = new TextView(Core.getInstance().getMainActivity());
        this.textName.setTextColor(Color.rgb(89, 125, 163));
        Log.i("MySecDialog",((JSONObject)resp).toString());
        this.jRVK = null;
        this.js = resp;
        this.tw = new TextView(Core.getInstance().getMainActivity());
        this.iv = new ImageView(Core.getInstance().getMainActivity());
        this.liv = new LinkedList<ImageView>();
        for(int i = 0; i<4;i++){
            this.liv.add(new ImageView(Core.getInstance().getMainActivity()));
            this.liv.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
            this.liv.get(i).setAdjustViewBounds(true);
            this.liv.get(i).setMaxHeight(50);
            this.liv.get(i).setMaxWidth(50);
        }
        VkCore.getInstance().getFriedName(this.js.getJSONObject("message").optString("user_id"),this);
    }

    @Override
    public void click() {
        this.tw.setText("asdas");
    }

    public View draw(Activity activity, View.OnClickListener list){
        MyLinearLayout l = new MyLinearLayout(activity);
        LinearLayout l2 = new LinearLayout(activity);
        l2.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l2.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);

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
        this.textName.setText(name);
        try {
            if(this.js.getJSONObject("message").has("chat_id")){
                l.setMyParams("chat_id", this.js.getJSONObject("message").optString("chat_id"));
                LinearLayout lk = new LinearLayout(activity);
                lk.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lk.setOrientation(LinearLayout.VERTICAL);
                LinearLayout lkup = new LinearLayout(activity);
                lkup.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lkup.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout lkbot = new LinearLayout(activity);
                lkbot.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lkbot.setOrientation(LinearLayout.HORIZONTAL);

                l.addView(lk);
                lk.addView(lkup);
                lk.addView(lkbot);
                lkup.addView(this.liv.get(0));
                lkup.addView(this.liv.get(1));
                lkbot.addView(this.liv.get(2));
                lkbot.addView(this.liv.get(3));

//                l.addView(this.iv);

            }else{
                l.setMyParams("user_id", this.js.getJSONObject("message").optString("user_id"));
                l.addView(this.iv);
            }
        }catch (Exception e){
            Log.e("MySecDialogErr",e.toString());
        }


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

    public static class MyButton extends Button{

        private String name;

        private String param;

        private String id;

        public MyButton(Activity a){
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
