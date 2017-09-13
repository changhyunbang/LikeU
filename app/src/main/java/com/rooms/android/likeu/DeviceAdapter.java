package com.rooms.android.likeu;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by changhyun on 2017. 9. 6..
 */

public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Action1 action1;
    private ArrayList<DeviceItem> items;

    public DeviceAdapter(Action1<DeviceItem> action1) {
        items = new ArrayList<>();
        this.action1 = action1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (items == null || items.size() == 0) {
            return 0;
        }

        return items.size();
    }
}
