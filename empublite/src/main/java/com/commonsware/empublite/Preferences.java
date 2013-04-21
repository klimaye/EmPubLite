package com.commonsware.empublite;
import java.util.List;
import android.os.Build;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 4/21/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Preferences extends SherlockPreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            addPreferencesFromResource(R.xml.pref_display);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
}