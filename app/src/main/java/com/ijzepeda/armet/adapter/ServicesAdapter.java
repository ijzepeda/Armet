package com.ijzepeda.armet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.Product;
import com.ijzepeda.armet.model.Servicio;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Servicio> mData;
    private LayoutInflater mInflater;
    private ServicesAdapter.ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public ServicesAdapter(Context context, List<Servicio> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.service_list_item, parent, false);
        return new ServicesAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ServicesAdapter.ViewHolder holder, int position) {
        final String itemName=mData.get(position).getName();
//        int itemQty=mData.get(position).getLocalQty();
        //        holder.qtyTextView.setText(""+itemQty);

        holder.itemNameTextView.setText(itemName);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleting "+itemName, Toast.LENGTH_SHORT).show();
                removeAt(holder.getAdapterPosition());
            }
        });
    }

    public void removeAt(int position) {
//        mData.get(position).setLocalQty(0);
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemNameTextView;
//        TextView qtyTextView;
        ImageButton deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
//            qtyTextView = itemView.findViewById(R.id.itemQtyTextView);
            deleteBtn=itemView.findViewById(R.id.deleteProductBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Servicio getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ServicesAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}