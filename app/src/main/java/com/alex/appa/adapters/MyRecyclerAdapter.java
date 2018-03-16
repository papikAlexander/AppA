package com.alex.appa.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.alex.appa.R;

/**
 * Created by alex on 15.03.18.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    CursorAdapter mCursorAdapter;
    Context mContext;
    final int HISTORY_TAB_ID = 2;

    public MyRecyclerAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.mCursorAdapter = new CursorAdapter(mContext, cursor, true) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {

                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_history_item, parent, false);

            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textViewLink = (TextView) view.findViewById(R.id.textViewLink);
                TextView textViewData = (TextView) view.findViewById(R.id.textViewData);

                int linkIndex = cursor.getColumnIndex("link");
                int dataIndex = cursor.getColumnIndex("data");
                int statusIndex = cursor.getColumnIndex("status");

                textViewData.setText(cursor.getString(dataIndex));
                textViewLink.setText(cursor.getString(linkIndex));

                int status = cursor.getInt(statusIndex);

                switch (status){
                    case 1:
                        textViewLink.setTextColor(Color.GREEN);
                        break;
                    case 2:
                        textViewLink.setTextColor(Color.RED);
                        break;
                    case 3:
                        textViewLink.setTextColor(Color.GRAY);
                        break;
                }
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textViewLink);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView) v.findViewById(R.id.textViewLink);
                Intent intent = new Intent("android.intent.action.APP_B");
                intent.putExtra("link", textView.getText().toString());
                intent.putExtra("id", HISTORY_TAB_ID);

                switch (textView.getCurrentTextColor()){
                    case Color.GREEN:
                        intent.putExtra("status", 1);
                        break;
                    case Color.RED:
                        intent.putExtra("status", 2);
                        break;
                    case Color.GRAY:
                        intent.putExtra("status", 3);
                        break;
                }

                mContext.startActivity(intent);

            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

}
