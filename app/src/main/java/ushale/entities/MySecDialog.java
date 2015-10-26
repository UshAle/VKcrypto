package ushale.entities;

import android.app.Activity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by ushale on 04.03.15.
 */
public class MySecDialog implements Viewable{

    private String id;
    private String uid;

    public MySecDialog(String dialogid, String userid){
        this.id=dialogid;
        this.uid = userid;
    }

    public View draw(Activity activity, View.OnClickListener list){
        LinearLayout l =new LinearLayout(activity);
        l.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);
        MyButton b = new MyButton(activity);
        b.setText(id);
        b.setMyParams(id,uid);
        l.addView(b);
        b.setOnClickListener(list);
             return (View)l;
    }

    private static class MyButton extends Button{

        private String ide;

        private String usde;

        public MyButton(Activity a){
            super(a);
        }

        public void setMyParams(String id, String uid){
            this.ide = id;
            this.usde = uid;
        }

        public String toString(){
            return ide+"/"+usde;
        }

    }
}
