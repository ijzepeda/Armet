package com.ijzepeda.armet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.Client;
import com.ijzepeda.armet.model.Product;

import java.util.ArrayList;

public class ClientSpinnerAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Client> clients;

    public ClientSpinnerAdapter(Context context, ArrayList<Client> clients) {
//        clients=new ArrayList<>();
        this.context = context;
        this.clients = clients;
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Client getItem(int position) {
        return clients.get(position);
    }

    /**
     * getitemid is id on adapter, not id of element
     * */
    @Override
    public long getItemId(int position) {
        return position;//position //clients.get(position).getId()
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClientSpinnerAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            viewHolder = new ClientSpinnerAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ClientSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        Client currentItem = (Client) getItem(position);
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

        Client Client= getItem(position);
        ((TextView)result.findViewById(R.id.text_view_item_name)).setText(Client.getName());
        return result ;
*/

        //inflate the layout for each listrow
//        if(convertView==null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item,
//                    parent, false);
//        }
        //get current  item to be displayed
//            Client currentClient = (Client) getItem(position);

        //get the TextView for Client name and item description
//            TextView textviewItemName= convertView.findViewById(R.id.text_view_item_name);

        //sets the text for item name from the current Client
//            textviewItemName.setText(currentClient.getName());

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
