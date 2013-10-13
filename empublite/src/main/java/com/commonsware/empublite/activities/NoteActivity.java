package com.commonsware.empublite.activities;

import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.commonsware.empublite.fragments.NoteFragment;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 10/13/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoteActivity extends SherlockFragmentActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        if (getSupportFragmentManager().findFragmentById(R.id.content) == null) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, -1);
            if (position >= 0) {
                Fragment fragment = NoteFragment.newInstance(position);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.content, fragment)
                        .commit();
            }
        }

    }
}
