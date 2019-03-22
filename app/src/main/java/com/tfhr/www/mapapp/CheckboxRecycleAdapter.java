package com.tfhr.www.mapapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckboxRecycleAdapter extends RecyclerView.Adapter<CheckboxRecycleAdapter.MyViewHolder> {
    private ArrayList<String> data;
    private Context context;
    private RecyclerViewItemClickListener listener;

    public CheckboxRecycleAdapter(Context c, ArrayList<String> list) {
        this.data = list;
        this.context = c;
        this.listener = (RecyclerViewItemClickListener) c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view=
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.checkbox_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv.setText(data.get(i) + "");
        myViewHolder.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("tfhr","click item "+i);
                    listener.recyclerItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != data) {
            return data.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        CheckBox ck;
        LinearLayout l;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("tfhr","click item "+p1);
//                    listener.recyclerItemClick(p1);
                }
            });
            tv = itemView.findViewById(R.id.item_checkbox_tv);
            ck = itemView.findViewById(R.id.item_checkbox);
            l=itemView.findViewById(R.id.item_checkbox_l);
        }
    }

    interface RecyclerViewItemClickListener {
        void recyclerItemClick(int position);
    }
}
