package com.commonsware.empublite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

//    private class GetNoteTask extends AsyncTask<Integer, Void, String> {
//
//        private NoteListener listener;
//
//        GetNoteTask(NoteListener listener) {
//            this.listener = listener;
//        }
//
//        @Override
//        protected String doInBackground(Integer... integers) {
//            String[] _ids = new String[] {integers[0].toString()};
//
//            Cursor c =
//                getReadableDatabase().rawQuery("SELECT prose from notes where _id=?",_ids);
//            c.moveToFirst();
//            if (c.isAfterLast()) {
//                return null;
//            }
//            String result = c.getString(0);
//            c.close();
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String prose) {
//            listener.setNote(prose);
//        }
//    }
//
//    public void getNoteAync(int _id, NoteListener listener) {
//        ModelFragment.executeAsyncTask(new GetNoteTask(listener), _id);
//    }

//    private class SaveNoteTask extends AsyncTask<Void, Void, Void> {
//
//        private int _id;
//        private String prose;
//
//        private SaveNoteTask(int _id, String prose) {
//            this._id = _id;
//            this.prose = prose;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            String[] args = {String.valueOf(_id), prose};
//            Log.e("empublite", "saveNoteAsync - about to insert or replace note at " + _id + " with " + prose);
//            ContentValues cv = new ContentValues();
//            cv.put("_id", _id);
//            cv.put("prose", prose);
//            getWritableDatabase()
//                    .replace("notes", null, cv);
//                    //.execSQL("INSERT OR REPLACE INTO notes (_id, prose) values (?,?)", args);
//            Log.e("empublite", "saveNoteAsync - after insert or replace");
//            return null;
//        }
//    }

//    public void saveNoteAync(int _id, String prose) {
//        Log.e("empublite", "inside saveNoteAsync");
//        ModelFragment.executeAsyncTask(new SaveNoteTask(_id, prose));
//    }

    public DatabaseHelper(Context context) {
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
            sqLiteDatabase.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY, position INTEGER, prose TEXT)");
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
