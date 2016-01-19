package ushale.core;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import ushale.entities.MyMessage;
import ushale.entities.*;
import ushale.secvk.MainActivity;

/**
 * Created by ushale on 03.03.15.
 */
public class Core {
    private static volatile Core instance = null;
    private MainActivity ma;
    private Core() { }

    public MainActivity getMainActivity(){
        return ma;
    }

    public static Core getInstance() {
        if (instance == null) {
            synchronized (Core.class) {
                if (instance == null) {
                    instance = new Core();
                }
            }
        }
        return instance;
    }

    public void setMainAct(MainActivity mainctivity){
        this.ma = mainctivity;
    }

    public void parseMessage10(JSONArray response){
        for(int i = 0; i< response.length(); i++){
            try {
                MySeqMessage a = new MySeqMessage(response.getJSONObject(i));
                View l = a.draw(this.ma, this.ma.getClicklist());
                ma.addViewOnPanel(l);
            }catch (Exception e){

            }
        }
    }

    public void parseDialogs10(JSONArray response){
        for(int i = 0; i< response.length(); i++){
            try {
                MySecDialog a = new MySecDialog(response.getJSONObject(i));
                View l = a.draw(this.ma,this.ma.getClicklist());
                ma.addViewOnPanel(l);
            }catch (Exception e){

            }
        }
    }

}
