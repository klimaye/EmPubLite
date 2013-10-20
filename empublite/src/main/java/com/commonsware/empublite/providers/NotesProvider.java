package com.commonsware.empublite.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.commonsware.empublite.DatabaseHelper;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 10/20/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotesProvider extends ContentProvider {
    public static final String DIR_TYPE = "com.commonsware.empublite.cursor.dir/note";
    public static final String ITEM_TYPE = "com.commonsware.empublite.cursor.item/note";
    private DatabaseHelper dbHelper;

    public static final String AUTHORITY = "com.commonsware.empublite.providers.NotesProvider";
    public static final String TABLE = "notes";

    private static final UriMatcher Matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int NOTES = 1;

    private static final int NOTES_ID = 2;

    static {
        Matcher.addURI(AUTHORITY, "notes", NOTES);
        Matcher.addURI(AUTHORITY, "notes/#", NOTES_ID);
    }

    public static class Constants {
        public static Uri NOTES_URI = Uri.parse("content://" + AUTHORITY + "/notes");
        public static final String ID = "_id";
        public static final String POSITION = "position";
        public static final String PROSE = "prose";

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return dbHelper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE);
        String orderBy = TextUtils.isEmpty(sort) ? Constants.POSITION : sort;
        return qb.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, orderBy);
    }

    private boolean isCollectionUri(Uri uri) {
        return Matcher.match(uri) == NOTES;
    }

    @Override
    public String getType(Uri uri) {
        Log.i("empublite", "notesprovider.getType " + uri);
        return isCollectionUri(uri) ? DIR_TYPE : ITEM_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowId = dbHelper.getWritableDatabase().insert(TABLE, null, contentValues);
        if (rowId > 0) {
            Uri url = ContentUris.withAppendedId(Constants.NOTES_URI, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return url;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = dbHelper.getWritableDatabase().delete(TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = dbHelper.getWritableDatabase().update(TABLE, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
