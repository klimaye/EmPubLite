package com.commonsware.empublite.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;
import com.commonsware.empublite.DatabaseHelper;
import com.commonsware.empublite.R;
import com.commonsware.empublite.providers.NotesProvider;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 10/13/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoteFragment extends SherlockFragment implements DatabaseHelper.NoteListener {

    private static final String KEY_POSITION = "position";
    private EditText editor;

    private boolean noteExists = false;
    private int noteId = -1;

    private static final String[] projection = new String[]{
            NotesProvider.Constants.ID,
            NotesProvider.Constants.PROSE
    };

    public static NoteFragment newInstance(int position) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.editor, container, false);
        int position = getArguments().getInt(KEY_POSITION);
        editor = (EditText) result.findViewById(R.id.editor);
        //DatabaseHelper.getInstance(getActivity()).getNoteAync(position, this);
        setEditorText(position);
        Log.e("empublite", "notefragment end of onCreateView");
        return result;
    }

    private void setEditorText(final int position) {
        Cursor cursor = getActivity()
                .getContentResolver()
                .query(
                        NotesProvider.Constants.NOTES_URI,
                        projection,
                        NotesProvider.Constants.POSITION + "=?",
                        new String[]{String.valueOf(position)},
                        null
                );
        if (cursor != null) {
            if (cursor.moveToNext()) {
                noteId = cursor.getInt(cursor.getColumnIndex(NotesProvider.Constants.ID));
                editor.setText(cursor.getString(cursor.getColumnIndex(NotesProvider.Constants.PROSE)));
            }
            cursor.close();
        }
    }

    public void setNote(String note) {
        editor.setText(note);
    }

    @Override
    public void onPause() {
        Log.e("empublite", "notefragment onPause called");
        super.onPause();
        Log.e("empublite", "notefragment after super.onPause");
        int position = getArguments().getInt(KEY_POSITION, -1);
        //DatabaseHelper.getInstance(getActivity()).saveNoteAync(position, editor.getText().toString());
        saveNote(position);
    }

    private void saveNote(int position) {
        ContentValues values = new ContentValues(2);
        values.put(NotesProvider.Constants.POSITION, position);
        values.put(NotesProvider.Constants.PROSE, editor.getText().toString());
        if (noteId != -1) {
            Uri updateUri = ContentUris.withAppendedId(NotesProvider.Constants.NOTES_URI, noteId);
            getActivity().getContentResolver().update(
                    updateUri,
                    values,
                    null,
                    null);
            //,"_id=?", new String[]{String.valueOf(position)});
        } else {
            getActivity().getContentResolver().insert(
                    NotesProvider.Constants.NOTES_URI,
                    values);
        }
    }
}
