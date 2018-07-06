package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.adapter.ProductBaseAdapter;
import com.ijzepeda.armet.adapter.ProductsAdapter;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;

import java.util.ArrayList;

import static com.ijzepeda.armet.util.Constants.PRODUCT_ID;

public class AddServiceActivity extends AppCompatActivity {
    private static final String TAG = "AddServiceActivity";
    Context context;
    Button addNewProduct;
    //    Button addProduct;
//    ListView productsListView;
//    RecyclerView productsRecyclerView;
    RecyclerView selectedProductsRecyclerView;
    LinearLayoutManager layoutManager;
    ProductsAdapter productsAdapter;

    Spinner spinner;
    ProductBaseAdapter baseAdapter;

    Service currentService;
    ArrayList<Product> productsOnService;//>how to hold qty? on object, but neber sent
    ArrayList<Product> totalProductsList;
//    ArrayList<String> productsNames;

    ImageButton addProductButton;
    EditText selectQtyProduct;

    DataSingleton singleton;

    /**
     * Hacer un solo dropdown, con boton de agregar
     * abajo se llena la lista de los agregados: boton de eliminar y cantidad:
     */


    private static int PRODUCT_ADDED = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        context = this;
        singleton = DataSingleton.getInstance();
        getData();
        initComponents();
    }

    public void initComponents() {
        selectQtyProduct = findViewById(R.id.selectQtyProduct);
        addNewProduct = findViewById(R.id.addNewProductBtn);
//        addProduct = findViewById(R.id.addProductBtn);
        addProductButton = findViewById(R.id.addProductButton);
//        productsListView = findViewById(R.id.productsListView);

        //recyclerview
        spinner = findViewById(R.id.productsSpinner);
        //For spinner
//        ArrayAdapter dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, totalProductsList);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //        spinner.setAdapter(dataAdapter1);
        baseAdapter = new ProductBaseAdapter(this, totalProductsList);
        spinner.setAdapter(baseAdapter);
//productsListView.setAdapter(baseAdapter);

        //        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        selectedProductsRecyclerView = findViewById(R.id.selectedProductsRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectedProductsRecyclerView.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(context, productsOnService);
        selectedProductsRecyclerView.setAdapter(productsAdapter);


       

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get spinner item id/object and put it on listview, then onstate change para el recyclerview

                Product temp = (Product) spinner.getSelectedItem();
                Toast.makeText(context, "spinner element:" + temp.getName(), Toast.LENGTH_SHORT).show();
                int qty = (selectQtyProduct.getText().toString().equals("")?1:Integer.parseInt(selectQtyProduct.getText().toString()));
//                if(qty==0)
//                    temp.setLocalQty(0);
//                else
                temp.addLocalQty(qty); //increase qty
//aun cuando haya borrado el elemento, se le queda guardado el valor de local qty, hay que checar como borrar bien el objeto de productsOnservices
                //to avoid repeating element, just replace it/update it
                if (productsOnService.contains(temp))
                    productsOnService.set(productsOnService.indexOf(temp), temp);
                else
                    productsOnService.add(temp);

                productsAdapter.notifyDataSetChanged();
                selectQtyProduct.setText(null);
            }
        });
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar u nbundle con el id de este servicio, y al final de product, se agrega a la lista de servicio. en caso que no exista un serivcio, solo se registra a la base de datos [sin relacion]
                Intent productIntent = new Intent(context, AddProductActivity.class);
                startActivityForResult(productIntent, PRODUCT_ADDED);
//                startActivity(new Intent(context,AddProductActivity.class));
            }
        });
    }

    public void getData() {
        totalProductsList = new ArrayList<>();
        productsOnService = new ArrayList<>();
        totalProductsList.addAll(singleton.getProductsList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRODUCT_ADDED) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Product productTemp = singleton.getProduct(data.getStringExtra(PRODUCT_ID));
                    //productsOnService.add(productTemp);
                    totalProductsList.add(productTemp); //todo checar: este se debe subir a internet, o mas bien actualizar los datos de internet
                    baseAdapter.notifyDataSetChanged();
                    // productsOnService.add(productTemp); //todo checar: este se debe subir a internet, o mas bien actualizar los datos de internet

                    //dataAdapter.notifyDataSetChanged();
                    //productsAdapter.notifyDataSetChanged();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }
}
