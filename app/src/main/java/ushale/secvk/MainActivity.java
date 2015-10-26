package ushale.secvk;

import ushale.core.*;
import ushale.entities.*;

import android.app.*;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.*;
import com.vk.sdk.api.*;
import com.vk.sdk.dialogs.*;
import com.vk.sdk.util.*;


public class MainActivity extends ActionBarActivity {

    private static String[] sMyScope = new String[]{VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS, VKScope.NOHTTPS, VKScope.MESSAGES};

    private static String sTokenKey = "Nse3bnAwwSYBA45Ce2P7";

    private View.OnClickListener clicklist;

    public void addViewOnPanel(View vi){
        ((LinearLayout)findViewById(R.id.lo1)).addView(vi);
    }

    public View.OnClickListener getClicklist(){
        return this.clicklist;
    }

    public void setTextOnTextView(String text){
        ((TextView)findViewById(R.id.textView)).setText(text);
    }

    private void authar(){
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, "ushale.secvk");
        VKSdk.initialize(sdkListener, "4797929", VKAccessToken.tokenFromSharedPreferences(this, sTokenKey));
        VKSdk.authorize(sMyScope);
    }

    public void startDialog(String param){
        Intent intent = new Intent(this, DialogActivity.class);

        intent.putExtra("dialogParam",param);
        startActivity(intent);
    }

    public void getHui(View view){
        VKRequest request = new VKRequest("messages.send", VKParameters.from("user_id","19887456", "message",((EditText)findViewById(R.id.editText2)).getText().toString()));//account.getAppPermissions
        //VKRequest request = new VKRequest("account.getAppPermissions");//, VKParameters.from(VKApiConst.USER_ID,"13171599",VKApiConst.MESSAGE,"TestOneTwo"));
        setTitle("hui_"+request.toString());
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                setTitle("win"+response.responseString);
            }
            @Override
            public void onError(VKError error) {

                //tv1.setText(fingerprints[0]);
                setTitle("fail"+error.toString());
//Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//I don't really believe in progrerrores
// s
                //tv1.setText(fingerprints[0]);
                setTitle("Attemt Failed_"+request.toString());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       /* if (id == R.id.setsBut) {
            Intent intent = new Intent(this, Sets.class);
            startActivity(intent);
            return true;
        }*/
        if(id == R.id.setsBut){
            VkCore.getInstance().getDialogs10();
            return true;
        }
        if (id == R.id.authBut) {
            this.authar();
            return true;
        }
        if (id == R.id.testBut) {
            VKRequest request = new VKRequest("messages.getHistory ", VKParameters.from("user_id","19887456"));
//            Core.getInstance().getMainActivity().setTextOnTextView(param+" "+id);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyListener implements View.OnClickListener{
        private MainActivity mainA;
        public MyListener(MainActivity ma){//(MainActivity ma
            this.mainA = ma;
        }
        @Override
        public void onClick(View v){
           //mainA.setTextOnTextView(v.toString());
           mainA.startDialog(v.toString());
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
