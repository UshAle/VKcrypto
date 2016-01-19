package ushale.secvk;
import ushale.core.*;
import ushale.entities.*;

import android.app.*;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.vk.sdk.*;
import com.vk.sdk.api.*;
import com.vk.sdk.dialogs.*;
import com.vk.sdk.util.*;



import ushale.entities.ImageLoadTask;

public class MainActivity extends ActionBarActivity {

    private String sendparam = "user_id";
    private String idsendTo = "13171599";
    private static String[] sMyScope = new String[]{VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS, VKScope.NOHTTPS, VKScope.MESSAGES};
    private static String sTokenKey = "Nse3bnAwwSYBA45Ce2P7";
    private View.OnClickListener clicklist;
    private Handler handler;
//    public

    public void deleteAllOnPanel(){
        ((LinearLayout)findViewById(R.id.lo1)).removeAllViewsInLayout();
    }

    public void addViewOnPanel(View vi){
        ((LinearLayout)findViewById(R.id.lo1)).addView(vi);
    }

    public View.OnClickListener getClicklist(){
        return this.clicklist;
    }

    private void authar(){
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, "ushale.secvk");
        VKSdk.initialize(sdkListener, "4797929", VKAccessToken.tokenFromSharedPreferences(this, sTokenKey));
        VKSdk.authorize(sMyScope);
    }

    public void getHui(View view){
        VKRequest request = new VKRequest("messages.send", VKParameters.from(this.sendparam,this.idsendTo, "message",((EditText)findViewById(R.id.editText2)).getText().toString()));//account.getAppPermissions
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                ((EditText)findViewById(R.id.editText2)).setText("");
            }
            @Override
            public void onError(VKError error) {
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Core c = Core.getInstance();
        c.setMainAct(this);
        VkCore vc  = VkCore.getInstance();
        this.clicklist = new MyListener(this);
        vc.setMainAct(this);
        this.authar();
        getBaseContext().getMainLooper();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                VkCore.getInstance().refresh();
            }
        };
    }

    public Handler getHandler(){
        return this.handler;
    }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


//    private class MyHandler extends Handler() {
//
//        public MyHandler(){
//            super();
//
//        }
//        @Override
//        public void handleMessage(Message msg) {
//
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private static final int IO_BUFFER_SIZE = 4 * 1024;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.setsBut){
            deleteAllOnPanel();
            VkCore.getInstance().getDialogs10();
            return true;
        }
        if (id == R.id.authBut) {
            this.authar();
            return true;
        }
        if (id == R.id.testBut) {
            VkCore.getInstance().connectToLongPoll();
//            LongPoll.getInstance();
//            ImageLoadTask TLT = new ImageLoadTask("http://www.exploreoneida.com/wp-content/uploads/frog.jpg", iv);
//            TLT.execute();

//            getBitmapFromURL("http://www.exploreoneida.com/wp-content/uploads/frog.jpg");
//            if(bitm != null) {
//                ImageView iv = new ImageView(this);
//                iv.setImageBitmap(bitm);
//                this.addViewOnPanel(iv);
//            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setsendtoid(String param, String idsendTo){
        this.idsendTo = idsendTo;
        this.sendparam = param;
    }

    public void createMessagesVsId(String param, String idsendTo){
        deleteAllOnPanel();
        VkCore.getInstance().getMessages10(param, idsendTo);
    }

    private static class MyListener implements View.OnClickListener{
        private MainActivity mainA;
        public MyListener(MainActivity ma){//(MainActivity ma
            this.mainA = ma;
        }
        @Override
        public void onClick(View v){
           if (v instanceof MySecDialog.MyButton){
               mainA.setsendtoid(((MySecDialog.MyButton)v).getParam(),((MySecDialog.MyButton)v).getUsde());
               mainA.createMessagesVsId(((MySecDialog.MyButton)v).getParam(),((MySecDialog.MyButton)v).getUsde());
               Log.i("list","message2");
           }
//           if (v instanceof Viewable){
//                ((Viewable) v).click();
//               Log.i("list","messalis");
//           }
           if (v instanceof MySecDialog.MyLinearLayout){
               mainA.setsendtoid(((MySecDialog.MyLinearLayout)v).getParam(),((MySecDialog.MyLinearLayout)v).getUsde());
               mainA.createMessagesVsId(((MySecDialog.MyLinearLayout)v).getParam(),((MySecDialog.MyLinearLayout)v).getUsde());
//               for(int i=0; i<((LinearLayout)v).getChildCount(); i++){
//                   for(Method m : ((LinearLayout)v).getChildAt(i).getClass().getMethods()){
//                       if(m.getName().equals("click")){
//
//                       }
//                   }
//                   if(((LinearLayout)v).getChildAt(i) instanceof MySecDialog.MyButton){
//                       mainA.setsendtoid(((MySecDialog.MyButton)((LinearLayout)v).getChildAt(i)).getParam(),((MySecDialog.MyButton)((LinearLayout)v).getChildAt(i)).getUsde());
//                       mainA.createMessagesVsId(((MySecDialog.MyButton)((LinearLayout)v).getChildAt(i)).getParam(),((MySecDialog.MyButton)((LinearLayout)v).getChildAt(i)).getUsde());
//                   }
//               }
           }else{
               ViewParent par = v.getParent();
               while(!(par instanceof MySecDialog.MyLinearLayout)){
                    par = v.getParent();
               }
               mainA.setsendtoid(((MySecDialog.MyLinearLayout)par).getParam(),((MySecDialog.MyLinearLayout)par).getUsde());
               mainA.createMessagesVsId(((MySecDialog.MyLinearLayout)par).getParam(),((MySecDialog.MyLinearLayout)par).getUsde());
           }
        }
    }

    private VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            //

            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(authorizationError.errorMessage)
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(MainActivity.this, sTokenKey);
            // Intent i = new Intent(LoginActivity.this, MainActivity.class);
            // startActivity(i);
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            //Intent i = new Intent(LoginActivity.this, MainActivity.class);
            // startActivity(i);
        }
    };


}
