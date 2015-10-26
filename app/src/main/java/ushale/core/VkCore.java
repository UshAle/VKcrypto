package ushale.core;

import android.app.Activity;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import ushale.core.*;

import ushale.secvk.DialogActivity;
import ushale.secvk.MainActivity;

/**
 * Created by ushale on 03.03.15.
 */
public class VkCore{
    private static volatile VkCore instance = null;
    private MainActivity ma;
    private VkCore() { }

    public static VkCore getInstance() {
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

    public void getFriends(){
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.FIELDS, "name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Core.getInstance().parsefriends(response.responseString);
            }
            @Override
            public void onError(VKError error) {
                //Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
    }

    public void getDialogs10(){
        VKRequest request = new VKRequest("messages.getDialogs", VKParameters.from("count","10"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Core.getInstance().parseDialogs10(response.responseString);
            }
            @Override
            public void onError(VKError error) {
                //Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
    }

    public void getHistory(String param, String id,DialogActivity da){
        VKRequest request = new VKRequest("messages.getHistory ", VKParameters.from(param,id));
        Core.getInstance().getMainActivity().setTextOnTextView(param+" "+id);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Core.getInstance().getMainActivity().setTextOnTextView(response.responseString);
            }
            @Override
            public void onError(VKError error) {

                //Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
        //VKRequest.VKRequestListener b = new MyVKRequestListener(da);
//        b.se

       // request.executeWithListener(b);
    }

    private static class MyVKRequestListener extends VKRequest.VKRequestListener{
        Activity activity;
        public MyVKRequestListener(Activity activity){
            super();
            this.activity = activity;
        }

        @Override
        public void onComplete(VKResponse response) {
            //Core.getInstance().parseHistory(response.responseString, activity);
        }
        @Override
        public void onError(VKError error) {
            //Do error stuff
        }
        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            //I don't really believe in progress
        }
    }

}
