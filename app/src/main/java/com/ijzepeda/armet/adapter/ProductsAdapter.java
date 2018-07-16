package com.ijzepeda.armet.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.activity.AddProductActivity;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;

import java.util.List;

import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_PRODUCT;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_PRODUCT;

public class ProductsAdapter  extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Product> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public ProductsAdapter(Context context, List<Product> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//       final String itemName=mData.get(position).getName();
//        int itemQty=mData.get(position).getLocalQty();
        final Product product = mData.get(position);

        holder.itemNameTextView.setText(product.getName());
        holder.qtyTextView.setText(""+product.getLocalQty());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Deleting "+product.getName(), Toast.LENGTH_SHORT).show();
                removeAt(holder.getAdapterPosition());
                DataSingleton.getInstance().update(context);
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(context, AddProductActivity.class);
                productIntent.putExtra(EXTRA_EDIT_PRODUCT,product.getId() );
                productIntent.putExtra(EXTRA_EDITING_PRODUCT,true);
                context.startActivity(productIntent);

            }
        });
    }

    public void removeAt(int position) {
        mData.get(position).setLocalQty(0);
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
        TextView qtyTextView;
        ImageButton deleteBtn;
        ImageButton editBtn;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            qtyTextView = itemView.findViewById(R.id.itemQtyTextView);
            deleteBtn=itemView.findViewById(R.id.deleteProductBtn);
            editBtn=itemView.findViewById(R.id.editProductBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Product getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
