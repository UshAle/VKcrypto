package ushale.entities;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.*;

/**
 * Created by ushale on 06.03.15.
 */
public class MyMessage implements Viewable {

    private String body;

    @Override
    public void click() {

    }

    public void respondOfVK(Object response){

    }

    public MyMessage(String body){
        this.body = body;
    }

    public View draw(Activity activity, View.OnClickListener list){
        LinearLayout l =new LinearLayout(activity);
        l.setLayoutParams(  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(activity);
        tv.setText(body);
        l.addView(tv);
        //b.setOnClickListener(list);
        return (View)l;
    }
}
