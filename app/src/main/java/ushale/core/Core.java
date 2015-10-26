package ushale.core;

import android.app.Activity;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import ushale.entities.MyMessage;
import ushale.entities.MySecDialog;
import ushale.secvk.DialogActivity;
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

    public void parsefriends(String response){
        ma.setTextOnTextView(response);
    }

    public void parseMessage10(String response){
        Parser parser = new Parser();
        //int k = response.length();
        // ma.setTextOnTextView(parser.getArgs(response,"[,:\" ]+","user_id").get(1));//parser.getArgs(response,"[,:\" ]+","id").get(1)
        List<String> uids = parser.getArgs(response,"[,:\" ]+","user_id");
        List<String> ids = parser.getArgs(response,"[,:\" ]+","id");
        Iterator<String> itIds = ids.iterator();
        Iterator<String> itUids = uids.iterator();
        while(itIds.hasNext() && itUids.hasNext()) {
            MySecDialog a = new MySecDialog(itIds.next(),itUids.next());
//            View.OnClickListener
            View l = a.draw(this.ma,this.ma.getClicklist());
            ma.addViewOnPanel(l);
        }
    }

    public void parseDialogs10(String response){
       Parser parser = new Parser();
        //int k = response.length();
      // ma.setTextOnTextView(parser.getArgs(response,"[,:\" ]+","user_id").get(1));//parser.getArgs(response,"[,:\" ]+","id").get(1)
        List<String> uids = parser.getArgs(response,"[,:\" ]+","user_id");
        List<String> ids = parser.getArgs(response,"[,:\" ]+","id");
        Iterator<String> itIds = ids.iterator();
        Iterator<String> itUids = uids.iterator();
        while(itIds.hasNext() && itUids.hasNext()) {
            MySecDialog a = new MySecDialog(itIds.next(),itUids.next());
//            View.OnClickListener
            View l = a.draw(this.ma,this.ma.getClicklist());
            ma.addViewOnPanel(l);
        }
        //ma.setTextOnTextView(String.valueOf(response.length())+"|"+String.valueOf(k));
        //ma.setTextOnTextView(response);

       /* String[] ids = new String[10];
        int buf=0;
        for(int i=0;i<10;i++){
            buf = response.indexOf("\"id\"",buf);
            ids[i]=response.substring(buf+5,response.indexOf(",",buf));
            buf++;
        }
        String[] uids = new String[10];
        buf=0;
        for(int i=0;i<10;i++){
            buf = response.indexOf("\"user_id\"",buf);
            uids[i]=response.substring(buf+10,response.indexOf(",",buf));
            buf++;
        }
        response="";
        for(int i=0;i<10;i++)response+=uids[i]+"/";
        ma.setTextOnTextView(response);*/
    }

    public void parseHistory(String response, Activity da) {

        String username = "username";

        Parser parser = new Parser();
        //int k = response.length();
        // ma.setTextOnTextView(parser.getArgs(response,"[,:\" ]+","user_id").get(1));//parser.getArgs(response,"[,:\" ]+","id").get(1)
        //List<String> uids = parser.getArgs(response, "[,:\" ]+", "from_id");
        List<String> bodys = parser.getArgs(response, "[,:\" ]+", "body");
        Iterator<String> itBo  = bodys.iterator();
//        Iterator<String> itUids = uids.iterator();
        while (itBo.hasNext() ) {
            MyMessage a = new MyMessage(itBo.next());
//            View.OnClickListener
            View l = a.draw(da, this.ma.getClicklist());
            try {
                ((DialogActivity)da).addViewOnPanel(l);
            }catch (Exception e){

            }
        }
    }

}
