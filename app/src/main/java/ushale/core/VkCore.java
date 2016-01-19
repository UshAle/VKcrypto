package ushale.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ushale.core.*;

import ushale.entities.ImageLoadTask;
import ushale.entities.LongPoll;
import ushale.entities.Viewable;
import ushale.secvk.MainActivity;
import org.json.*;

/**
 * Created by ushale on 03.03.15.
 */
public class VkCore{

    private HashMap<String, JSONObject> dialogs = null;
    private HashMap<String, Bitmap> images = null;
    private Object[] lastparams;
    private Method lastmethod;
//    private HashMap<String, JSONObject> friends = null;
    private static volatile VkCore instance = null;
    private MainActivity ma;

    private VkCore() {
        this.dialogs = new HashMap<String, JSONObject>();
//        this.friends = new HashMap<String, JSONObject>();
        this.images = new HashMap<String, Bitmap>();
    }
//
//    public HashMap<String, JSONObject> getFriends(){
//        return this.friends;
//    }

    public void processLongPoll(JSONArray jsa){
        try {
            switch (Integer.parseInt(jsa.optString(0))){
                case 1:

                    break;
                case 4: //new message
                    Message msg = new Message();
//                    msg.obj = text;
                    ma.getHandler().sendMessage(msg);
                    break;
            }
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    public void getChatImages(String id, List<ImageView> respondto){
        VKRequest request = new VKRequest("messages.getChat", VKParameters.from("chat_id",id));
        class myVkRequest extends VKRequest.VKRequestListener{

            private List<ImageView> respondto;
            private String id;

            public myVkRequest(String id, List<ImageView> viewable){
                super();
                Log.i("getChatImages","created "+id);
                this.respondto = viewable;
                this.id = id;
            }

            @Override
            public void onComplete(VKResponse response) {
                try{
                    JSONObject  jsonRootObject = new JSONObject(response.responseString);
                    JSONArray jsonResp = jsonRootObject.optJSONObject("response").getJSONArray("users");
                    Log.i("array","ok");
//                    JSONObject jsonArray =jsonRootObject.getJSONObject("users");
//                    Log.i("array", jsonResp.toString());
                    for(int i=0;i<jsonResp.length();i++){
                        if(i>3) {
                            ImageView iv = new ImageView(Core.getInstance().getMainActivity());
                            this.respondto.add(iv);
                            Log.i("array" + id, jsonResp.get(i).toString());
                            VkCore.getInstance().getFriedName(jsonResp.get(i).toString(), iv);
                        }
                        if(i<4){
                            Log.i("xarray" + id, jsonResp.get(i).toString()+respondto.get(i).toString());
                            VkCore.getInstance().getFriedName(jsonResp.get(i).toString(), (ImageView)respondto.get(i));
                        }
                    }
//
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                    System.out.print(this.id);
//                respondto.respondOfVK(VkCore.getInstance().getDialogs().get(this.id));
            }
            @Override
            public void onError(VKError error) {

                if (error.errorCode == -101){
                    try {
                        Thread.sleep(100);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    VkCore.getInstance().getChatImages(id,respondto);
                }else{
//                        respondto.respondOfVK(String.valueOf(error.errorCode));aaaa
                }
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//                respondto.respondOfVK(null);
            }
        }
        request.executeWithListener(new myVkRequest(id, respondto));
    }

    public void connectToLongPoll(){
        VKRequest request = new VKRequest("messages.getLongPollServer", VKParameters.from());
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try{
                    JSONObject  jsonRootObject = new JSONObject(response.responseString);
                    String key = jsonRootObject.getJSONObject("response").optString("key");
                    String server = jsonRootObject.getJSONObject("response").optString("server");
                    String ts = jsonRootObject.getJSONObject("response").optString("ts");
                    String href = "http://"+server+"?act=a_check&key="+key+"&ts="+ts+"&wait=25&mode=2";
                    LongPoll lp = new LongPoll(key, server, ts);
                    (new Thread(lp)).start();
//                    ma.runOnUiThread(lp);
                    Log.i("VKCore",href);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                if (error.errorCode == -101){
                    try {
                        Thread.sleep(100);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    VkCore.getInstance().connectToLongPoll();
                }else{
//                        respondto.respondOfVK(String.valueOf(error.errorCode));aaaa
                }
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });
    }

    public HashMap<String,JSONObject> getDialogs(){
        return this.dialogs;
    }

    public HashMap<String, Bitmap> images(){
        return this.images;
    }
    public void setDialogs(HashMap<String,JSONObject> dialogs){
        this.dialogs = dialogs;
    }

    public static VkCore getInstance() { //STAN
        if (instance == null) {
            synchronized (VkCore.class) {
                if (instance == null) {
                    instance = new VkCore();
                }
            }
        }
        return instance;
    }

    public void setMainAct(MainActivity mainctivity){
        this.ma = mainctivity;
    }

    public void getImage(String id, Object respondto){
        if(!this.images.containsKey(id)){
            ImageLoadTask TLT = new ImageLoadTask(id, respondto);
            TLT.execute();
            Log.i("has no image",id);
        }else{
            Log.i("has image",id);
            if(respondto instanceof ImageView){
                ((ImageView)respondto).setImageBitmap(this.images.get(id));
            }
            if(respondto instanceof Viewable){
                ((Viewable)respondto).respondOfVK(this.images.get(id));
            }
        }
    }

    public void getFriedName(String id, Object respondto){
//        respondto.respondOfVK("asdassa");
        if(!this.dialogs.containsKey(id)){
            VKRequest request = new VKRequest("users.get", VKParameters.from("user_ids",id,"fields","photo_100"));
            class myVkRequest extends VKRequest.VKRequestListener{

                private Object respondto;
                private String id;

                public myVkRequest(String id, Object viewable){
                    super();
                    this.respondto = viewable;
                    this.id = id;
                }

                @Override
                public void onComplete(VKResponse response) {
                    try{
                        JSONObject  jsonRootObject = new JSONObject(response.responseString);
                        JSONArray jsonArray = jsonRootObject.optJSONArray("response");
                        for(int i=0;  i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            VkCore.getInstance().getDialogs().put(this.id,jsonObject);
                        }
                        if(this.respondto instanceof Viewable){
                            ((Viewable)respondto).respondOfVK(VkCore.getInstance().getDialogs().get(this.id));
                        }
                        if(this.respondto instanceof ImageView){
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Log.i("arrayImage"+id,jsonObject.optString("photo_100"));
                            VkCore.getInstance().getImage(jsonObject.optString("photo_100"), this.respondto);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    System.out.print(this.id);


                }
                @Override
                public void onError(VKError error) {

                    if (error.errorCode == -101){
                        try {
                            Thread.sleep(100);                 //schlafen
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        VkCore.getInstance().getFriedName(id,respondto);
                    }else{
//                        respondto.respondOfVK(String.valueOf(error.errorCode));aaaa
                    }
                }
                @Override
                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    if(this.respondto instanceof Viewable) {
                        ((Viewable) respondto).respondOfVK(null);
                    }
                }
            }
            request.executeWithListener(new myVkRequest(id,respondto));
        }else{
            if(respondto instanceof Viewable){
                ((Viewable)respondto).respondOfVK(VkCore.getInstance().getDialogs().get(id));
            }
            if (respondto instanceof ImageView){
                Log.i("arrayImageaaaa"+id,VkCore.getInstance().getDialogs().get(id).optString("photo_100"));
                VkCore.getInstance().getImage(VkCore.getInstance().getDialogs().get(id).optString("photo_100"), respondto);

            }

        }

    }
    public void refresh(){
        this.ma.deleteAllOnPanel();
        try {
            this.lastmethod.invoke(this, this.lastparams);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        this.lastreq.executeWithListener(this.lastlist);
//        VkCore.getInstance().getDialogs10();
    }

    public void getMessages10(String param, String idsendTo){
        Class[] paramCl = new Class[2];
        paramCl[0] = String.class;
        paramCl[1] = String.class;
        this.lastparams = new Object[2];
        this.lastparams[0] = param;
        this.lastparams[1] = idsendTo;
        try {
            this.lastmethod = this.getClass().getDeclaredMethod("getMessages10", paramCl);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(param,idsendTo,"count","100"));

        VKRequest.VKRequestListener vkr = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try{
                    JSONObject  jsonRootObject = new JSONObject(response.responseString);
                    JSONArray jsonArray = jsonRootObject.getJSONObject("response").getJSONArray("items");
                    Core.getInstance().parseMessage10(jsonArray);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {

            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        };
        request.executeWithListener(vkr);
    }

    public void getDialogs10(){
        Class[] paramCl = {};
        this.lastparams = null;
        try {
            this.lastmethod = this.getClass().getDeclaredMethod("getDialogs10", paramCl);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        VKRequest request = new VKRequest("messages.getDialogs", VKParameters.from("count","20"));
        VKRequest.VKRequestListener vkr =new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try{
                    JSONObject  jsonRootObject = new JSONObject(response.responseString);
                    JSONArray jsonArray = jsonRootObject.getJSONObject("response").getJSONArray("items");
                    Log.i("JsonLen",String.valueOf(jsonArray.length()));
                    Core.getInstance().parseDialogs10(jsonArray);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(VKError error) {
                if (error.errorCode == -101){
                    try {
                        Thread.sleep(100);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    VkCore.getInstance().getDialogs10();
                }else{
//                        respondto.respondOfVK(String.valueOf(error.errorCode));aaaa
                }
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        };
        request.executeWithListener(vkr);
    }

    public void sendMessage(){
        VKRequest request = new VKRequest("messages.getDialogs", VKParameters.from("count","2"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

            }
            @Override
            public void onError(VKError error) {
                if (error.errorCode == -101){
                    try {
                        Thread.sleep(100);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    VkCore.getInstance().sendMessage();
                }else{
//                        respondto.respondOfVK(String.valueOf(error.errorCode));aaaa
                }
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
    }

}
