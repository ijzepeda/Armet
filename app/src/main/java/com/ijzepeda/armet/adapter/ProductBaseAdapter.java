package com.ijzepeda.armet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.Product;

import java.util.ArrayList;

public class ProductBaseAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Product> products;

    public ProductBaseAdapter(Context context, ArrayList<Product> products) {
//        products=new ArrayList<>();
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    /**
     * getitemid is id on adapter, not id of element
     * */
    @Override
    public long getItemId(int position) {
        return position;//position //products.get(position).getId()
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product currentItem = (Product) getItem(position);
        viewHolder.itemName.setText(currentItem.getName());
        viewHolder.itemDescription.setText(currentItem.getId());

        return convertView;
  /*      final View result;
        if(convertView==null){
            result = LayoutInflater.from(context).inflate(R.layout.spinner_item,
                    parent,false);
        }
        else{
            result=convertView;
        }

        Product product= getItem(position);
        ((TextView)result.findViewById(R.id.text_view_item_name)).setText(product.getName());
        return result ;
*/

        //inflate the layout for each listrow
//        if(convertView==null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item,
//                    parent, false);
//        }
            //get current  item to be displayed
//            Product currentProduct = (Product) getItem(position);

            //get the TextView for product name and item description
//            TextView textviewItemName= convertView.findViewById(R.id.text_view_item_name);

            //sets the text for item name from the current product
//            textviewItemName.setText(currentProduct.getName());

//            return convertView;
        }

    private class ViewHolder {
        TextView itemName;
        TextView itemDescription;

        public ViewHolder(View view) {
            itemName = view.findViewById(R.id.text_view_item_name);
            itemDescription =  view.findViewById(R.id.text_view_item_description);
        }
    }


}
