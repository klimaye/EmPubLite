package com.commonsware.empublite;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.actionbarsherlock.app.SherlockFragment;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 4/7/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelFragment extends SherlockFragment {

    private BookContents contents = null;
    private ContentsLoadTask contentsTask = null;

    private SharedPreferences prefs = null;
    private PrefsLoadTask prefsTask = null;

    private class PrefsLoadTask extends AsyncTask<Context, Void, Void> {
        SharedPreferences localPreferences = null;
        @Override
        protected Void doInBackground(Context... ctxt) {
            localPreferences = PreferenceManager.getDefaultSharedPreferences(ctxt[0]);
            localPreferences.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ModelFragment.this.prefs = localPreferences;
            ModelFragment.this.prefsTask = null;
            Log.e("empublite", "about to deliver model");
            deliverModel();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setRetainInstance(true);
        deliverModel();
    }

    synchronized private void deliverModel() {

        if (prefs != null && contents != null) {
            Log.e("empublite", "setupPager");
            ((EmPubLiteActivity)getActivity()).setupPager(prefs, contents);
        }
        else {
            if (prefs == null && prefsTask == null) {
                prefsTask = new PrefsLoadTask();
                Log.e("empublite", "about to execute async task");
                executeAsyncTask(prefsTask,getActivity().getApplicationContext());
            }

            if (contents == null && contentsTask == null) {
                contentsTask = new ContentsLoadTask();
                executeAsyncTask(contentsTask, getActivity().getApplicationContext());
            }
        }
    }

    @TargetApi(11)
    static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    private class ContentsLoadTask extends AsyncTask<Context, Void, Void> {
        private BookContents localContents = null;
        private Exception e = null;

        @Override
        protected Void doInBackground(Context... ctxt) {
            try {
                StringBuilder buff = new StringBuilder();
                InputStream json = ((Context) ctxt[0]).getAssets().open("book/contents.json");
                BufferedReader in = new BufferedReader(new InputStreamReader(json));
                String str;
                while ((str = in.readLine()) != null) {
                    buff.append(str);
                }
                in.close();
                localContents = new BookContents(new JSONObject(buff.toString()));

            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
            if (e == null) {
                ModelFragment.this.contents = localContents;
                ModelFragment.this.contentsTask = null;
                deliverModel();
            } else {
                Log.e(getClass().getSimpleName(), "Exception loading contents",
                        e);
            }
        }
    }
}
