package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.adapter.ProductsAdapter;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;

import java.util.ArrayList;

import static com.ijzepeda.armet.util.Constants.PRODUCT_ID;

public class AddServiceActivity extends AppCompatActivity {
    private static final String TAG = "AddServiceActivity";
    Context context;
    Button addNewProduct;
    Button addProduct;
    ListView productsListView;
    RecyclerView productsRecyclerView;
    LinearLayoutManager layoutManager;
    ProductsAdapter productsAdapter;

    Service currentService;
    ArrayList<Product> productsOnService;//>how to hold qty? on object, but neber sent
    ArrayList<Product> totalProductsList;
//    ArrayList<String> productsNames;

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
        addNewProduct = findViewById(R.id.addNewProductBtn);
        addProduct = findViewById(R.id.addProductBtn);
        productsListView = findViewById(R.id.productsListView);

        //recyclerview
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productsAdapter = new ProductsAdapter(totalProductsList);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setAdapter(productsAdapter);

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar u nbundle con el id de este servicio, y al final de product, se agrega a la lista de servicio. en caso que no exista un serivcio, solo se registra a la base de datos [sin relacion]
                Intent productIntent = new Intent(context, AddProductActivity.class);
                startActivityForResult(productIntent, PRODUCT_ADDED);
//                startActivity(new Intent(context,AddProductActivity.class));
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will add a new field with dropdown and quantity [new element on array for recyclerview]
            }
        });

    }

    public void getData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRODUCT_ADDED) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Product productTemp = singleton.getProduct(data.getStringExtra(PRODUCT_ID));
                    productsOnService.add(productTemp);
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }
}
