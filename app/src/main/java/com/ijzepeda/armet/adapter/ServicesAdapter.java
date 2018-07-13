package com.ijzepeda.armet.adapter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.activity.AddProductActivity;
import com.ijzepeda.armet.activity.AddServiceActivity;
import com.ijzepeda.armet.model.Product;
import com.ijzepeda.armet.model.Servicio;

import java.util.ArrayList;
import java.util.List;

import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_SERVICE;

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
        final Servicio servicio=mData.get(position);
//        final String itemName=mData.get(position).getName();
        holder.itemNameTextView.setText(servicio.getName());
        holder.clientNameTextView.setText(servicio.getClientName());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleting "+servicio.getName(), Toast.LENGTH_SHORT).show();
                removeAt(holder.getAdapterPosition());
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(context, AddServiceActivity.class);
                productIntent.putExtra(EXTRA_EDIT_SERVICE,servicio.getId() );
                productIntent.putExtra(EXTRA_EDITING_SERVICE,true);
                context.startActivity(productIntent);
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
        TextView clientNameTextView;
//        TextView qtyTextView;
        ImageButton deleteBtn;
        ImageButton editBtn;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            clientNameTextView = itemView.findViewById(R.id.clientNameTextView);

//            qtyTextView = itemView.findViewById(R.id.itemQtyTextView);
            deleteBtn=itemView.findViewById(R.id.editServiceBtn);
            editBtn=itemView.findViewById(R.id.editServiceBtn);
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