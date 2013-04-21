package com.commonsware.empublite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class EmPubLiteActivity extends SherlockFragmentActivity {
    private ViewPager pager = null;
    private ContentsAdapter adapter = null;
    private static final String MODEL = "model";
    private SharedPreferences prefs = null;

    private static final String PREF_LAST_POSITION = "lastPosition";
    private static final String PREF_SAVE_LAST_POSITION = "saveLastPosition";
    private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";

    @Override
    protected void onPause() {
        if (prefs != null) {
            int position = pager.getCurrentItem();
            Log.e("empublite", "on pause about to save current position " + position);
            prefs.edit().putInt(PREF_LAST_POSITION, position).apply();
        } else {
            Log.e("empublite", "prefs are null");
        }
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs != null) {
            pager.setKeepScreenOn(prefs.getBoolean(PREF_KEEP_SCREEN_ON, false));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (getSupportFragmentManager().findFragmentByTag(MODEL) == null) {
            getSupportFragmentManager().beginTransaction().add(new ModelFragment(), MODEL).commit();
        }
        pager = (ViewPager) findViewById(R.id.pager);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.options, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings:
                startActivity(new Intent(this, Preferences.class));
                return true;
            case android.R.id.home:
                pager.setCurrentItem(0, false);
                return (true);

            case R.id.about:
                Intent i = new Intent(this, SimpleContentActivity.class);

                i.putExtra(SimpleContentActivity.EXTRA_FILE,
                        "file:///android_asset/misc/about.html");
                startActivity(i);

                return (true);

            case R.id.help:
                i = new Intent(this, SimpleContentActivity.class);
                i.putExtra(SimpleContentActivity.EXTRA_FILE,
                        "file:///android_asset/misc/help.html");

                startActivity(i);

                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    public void setupPager(SharedPreferences prefs, BookContents contents) {
        this.prefs = prefs;
        adapter = new ContentsAdapter(this, contents);
        pager.setAdapter(adapter);
        Log.e("empublite", "prefs.getBoolean(PREF_SAVE_LAST_POSITION, false) = " + prefs.getBoolean(PREF_SAVE_LAST_POSITION, false));
        if (prefs.getBoolean(PREF_SAVE_LAST_POSITION, false)) {
            Log.e("empublite", "prefs.getInt(PREF_LAST_POSITION,0) = " + prefs.getInt(PREF_LAST_POSITION, 0));
            pager.setCurrentItem(prefs.getInt(PREF_LAST_POSITION, 0));
        }
        findViewById(R.id.progressBar1).setVisibility(View.GONE);
        findViewById(R.id.pager).setVisibility(View.VISIBLE);
    }
}
