package ushale.entities;

import android.app.Activity;
import android.view.View;

/**
 * Created by ushale on 04.03.15.
 */
public interface Viewable {
    public View draw(Activity activity, View.OnClickListener list);
}
