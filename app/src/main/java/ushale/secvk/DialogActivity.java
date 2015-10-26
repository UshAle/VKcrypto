package ushale.secvk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import ushale.core.VkCore;


public class DialogActivity extends ActionBarActivity {

    private String[] params;


    public void addViewOnPanel(View vi){
        ((LinearLayout)findViewById(R.id.Dlo1)).addView(vi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        params = getIntent().getStringExtra("dialogParam").split("[/]");
        ((TextView) findViewById(R.id.dialogTestView)).setText("Dialog-monolog-"+getIntent().getStringExtra("dialogParam"));
        VkCore.getInstance().getHistory("user_id",params[1],this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMes(View v){
        VKRequest request = new VKRequest("messages.send", VKParameters.from("user_id", params[1], "message", ((EditText) findViewById(R.id.DeditText2)).getText().toString()));//account.getAppPermissions
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
                setTitle("Attemt Failed_"+request.toString());
            }
        });
    }
}
