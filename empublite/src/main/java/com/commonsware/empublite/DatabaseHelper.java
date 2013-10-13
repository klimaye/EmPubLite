package com.commonsware.empublite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 10/13/13
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int SCHEMA_VERSION = 1;
    private static final String DATABASE_NAME = "empublite.db";
    private static DatabaseHelper singleton = null;
    private Context context;

    public interface NoteListener {
        void setNote(String note);
    }

    private class GetNoteTask extends AsyncTask<Integer, Void, String> {

        private NoteListener listener;

        GetNoteTask(NoteListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String[] positions = new String[] {integers[0].toString()};

            Cursor c =
                getReadableDatabase().rawQuery("SELECT prose from notes where position=?",positions);
            c.moveToFirst();
            if (c.isAfterLast()) {
                return null;
            }
            String result = c.getString(0);
            c.close();

            return result;
        }

        @Override
        protected void onPostExecute(String prose) {
            listener.setNote(prose);
        }
    }

    public void getNoteAync(int position, NoteListener listener) {
        ModelFragment.executeAsyncTask(new GetNoteTask(listener), position);
    }

    private class SaveNoteTask extends AsyncTask<Void, Void, Void> {

        private int position;
        private String prose;

        private SaveNoteTask(int position, String prose) {
            this.position = position;
            this.prose = prose;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String[] args = {String.valueOf(position), prose};
            getWritableDatabase()
                    .execSQL("INSERT OR REPLACE INTO notes (position, prose) values (?,?)", args);
            return null;
        }
    }

    public void saveNoteAync(int position, String prose) {
        ModelFragment.executeAsyncTask(new SaveNoteTask(position, prose));
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        this.context = context;
    }

    synchronized public static DatabaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new DatabaseHelper(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL("CREATE TABLE notes (position INTEGER PRIMARY_KEY, prose TEXT)");
            sqLiteDatabase.setTransactionSuccessful();
        }
        finally {
             sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        throw new RuntimeException(context.getString(R.string.on_upgrade_error));
    }
}
