package com.alex.appa.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.appa.R;
import com.alex.appa.adapters.MyRecyclerAdapter;
import com.alex.appa.utils.Validator;

/**
 * Created by alex on 12.03.18.
 */

public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    final Uri LINK_URI = Uri.parse("content://com.alex.appa.providers.dbA/links");
    final int TEST_TAB_ID = 1;

    MyRecyclerAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Cursor cursor;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {}

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;

        switch (getArguments().getInt(ARG_SECTION_NUMBER)){
            case 1:
                rootView = inflater.inflate(R.layout.fragment_test, container, false);

                Button button = (Button) rootView.findViewById(R.id.btnOK);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editText = (EditText) getActivity().findViewById(R.id.etLink);

                        if (new Validator().isValid(editText.getText().toString())){
                            Intent intent = new Intent("android.intent.action.APP_B");
                            intent.putExtra("link", editText.getText().toString());
                            intent.putExtra("id", TEST_TAB_ID);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "WRONG LINK!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;

            case 2:

                rootView = inflater.inflate(R.layout.fragment_history, container, false);
                recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHistory);
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);


                getActivity().getSupportLoaderManager().initLoader(0, null, this);

                break;
        }

        return rootView;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getContext(), LINK_URI);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        adapter = new MyRecyclerAdapter(getContext(), cursor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader {

        Context mContext;
        Uri uri;

        public MyCursorLoader(Context context, Uri uri) {
            super(context);
            this.mContext = context;
            this.uri = uri;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor mCursor = mContext.getContentResolver().query(uri, null, null, null, null);

            return mCursor;
        }
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }
}
