package com.commonsware.empublite;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceFragment;
/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 4/21/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
@TargetApi(11)
public class StockPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int res = getActivity()
                    .getResources()
                    .getIdentifier(getArguments().getString("resource"),
                        "xml",
                        getActivity().getPackageName());
        addPreferencesFromResource(res);
    }
}
