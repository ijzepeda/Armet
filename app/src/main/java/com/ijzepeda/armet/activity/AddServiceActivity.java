package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.adapter.ProductBaseAdapter;
import com.ijzepeda.armet.adapter.ProductsAdapter;
import com.ijzepeda.armet.model.Client;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;
import com.ijzepeda.armet.model.Servicio;

import java.util.ArrayList;
import java.util.List;

import static com.ijzepeda.armet.util.Constants.PRODUCT_ID;

public class AddServiceActivity extends BaseActivity {
    private static final String TAG = "AddServiceActivity";
    Servicio service;
    Client client;

    Context context;
    Button addNewProduct;
    Button addServiceBtn;
    TextView clientIdTextView;
    TextView clientNameTextView;
    ImageButton photoButton;
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

        //todo get data from bundle, and receive client id
        client = new Client();
        client.setId(123456);
        client.setName("Coral&Marina");

        getData();
        initComponents();
    }

    public void initComponents() {
        selectQtyProduct = findViewById(R.id.selectQtyProduct);
        addServiceBtn=findViewById(R.id.addServiceBtn);
        addNewProduct = findViewById(R.id.addNewProductBtn);
        clientIdTextView=findViewById(R.id.clientIdTextView);
        clientNameTextView=findViewById(R.id.clientNameTextView);
//        addProduct = findViewById(R.id.addProductBtn);
        addProductButton = findViewById(R.id.addProductButton);
//        productsListView = findViewById(R.id.productsListView);
        photoButton =findViewById(R.id.photoButton);
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

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clientIdTextView.getText().equals("")){
                    saveService();
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();

            }
        });
    }


    @Override
    public void makeUseOfBarcode() {
        super.makeUseOfBarcode();
        clientIdTextView.setText(getBarcode());

    }

    public void getData() {

        //todo, after creating a service, when creating a new service the previous localQty keeps prevs values
        totalProductsList = new ArrayList<>();
        productsOnService = new ArrayList<>();
        totalProductsList.addAll(singleton.getProductsList());
    }

    public void saveService(){
        createService();
        Toast.makeText(context, "Servicio saved", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("serviceId",service.getId());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void createService(){
        service = new Servicio();
        service.setClientId(client.getId());
        service.setName(clientNameTextView.getText().toString());
        service.setId(Integer.parseInt(clientIdTextView.getText().toString()));


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
