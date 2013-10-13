package com.commonsware.empublite.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;
import com.commonsware.empublite.DatabaseHelper;
import com.commonsware.empublite.R;

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
        editor = (EditText)result.findViewById(R.id.editor);
        DatabaseHelper.getInstance(getActivity()).getNoteAync(position, this);
        return result;
    }

    public void setNote(String note) {
        editor.setText(note);
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        int position = getArguments().getInt(KEY_POSITION,-1);
        DatabaseHelper.getInstance(getActivity()).saveNoteAync(position, editor.getText().toString());
    }
}
