package ushale.entities;

import android.app.Activity;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by ushale on 04.03.15.
 */
public interface Viewable {
    public View draw(Activity activity, View.OnClickListener list);
    public void respondOfVK(Object respon);
    public void click();
}
