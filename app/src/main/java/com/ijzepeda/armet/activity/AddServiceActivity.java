package com.ijzepeda.armet.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.adapter.ProductBaseAdapter;
import com.ijzepeda.armet.adapter.ProductsAdapter;
import com.ijzepeda.armet.model.Client;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;
import com.ijzepeda.armet.model.Servicio;

import java.util.ArrayList;

import static com.ijzepeda.armet.util.Constants.EXTRA_CLIENT_ID;
import static com.ijzepeda.armet.util.Constants.EXTRA_CLIENT_NAME;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_SERVICE;
import static com.ijzepeda.armet.util.Constants.PRODUCT_ID;
import static com.ijzepeda.armet.util.Constants.SERVICE_ID;

public class AddServiceActivity extends BaseActivity {
    private static final String TAG = "AddServiceActivity";
    Servicio service;
    Client client;

    Context context;
    Button addNewProduct;
    Button addServiceBtn;
    TextView serviceIdTextView;
    TextView serviceNameTextView;
    ImageButton photoButton;
    ImageButton addClientButton;
    Spinner clientSpinner;

    RecyclerView selectedProductsRecyclerView;
    LinearLayoutManager layoutManager;
    ProductsAdapter productsAdapter;

    Spinner spinner;
    ProductBaseAdapter baseSpinnerAdapter;

    Service currentService;
    ArrayList<Product> productsOnService;//>how to hold qty? on object, but neber sent
    ArrayList<Product> totalProductsList;
//    ArrayList<String> productsNames;

    ImageButton addProductButton;
    EditText selectQtyProduct;
    boolean editingService;
    DataSingleton singleton;

    /**
     * Hacer un solo dropdown, con boton de agregar
     * abajo se llena la lista de los agregados: boton de eliminar y cantidad:
     * <p>
     * <p>
     * El totalProductos se obtiene de la basede datos,
     * mientras que el local del on result.
     * el onlocal, se usa solo para armar el objeto que se va a enviar.
     */

    private static int PRODUCT_ADDED = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        context = this;
        singleton = DataSingleton.getInstance();
        Intent intent = getIntent();
        editingService = intent.getBooleanExtra(EXTRA_EDITING_SERVICE, false);

        //todo get data from bundle, and receive client id
//todo: ----------------if I receive a client id, then work from it. else, search from list
        client = new Client();
        client.setId(intent.getStringExtra(EXTRA_CLIENT_ID));
        client.setName(intent.getStringExtra(EXTRA_CLIENT_NAME));

        Log.e(TAG, "onCreate client.getName(): " + client.getName());

        initFirebase();
        getData();
        //todo loadDAta() > I receive from bundle the client Id
        initComponents();
        if (editingService) {
            LoadEditService(intent.getStringExtra(EXTRA_EDIT_SERVICE));
        }
    }

    public void initComponents() {
        TextView title = findViewById(R.id.titleTextView);
        title.setText("Servicio para " + ((client.getName() == null) ? " nuevo cliente" : client.getName()));
        selectQtyProduct = findViewById(R.id.selectQtyProduct);
        addServiceBtn = findViewById(R.id.addServiceBtn);
        addNewProduct = findViewById(R.id.addNewProductBtn);
        serviceIdTextView = findViewById(R.id.serviceIdTextView);
        serviceNameTextView = findViewById(R.id.serviceNameTextView);
//        addProduct = findViewById(R.id.addProductBtn);
        addProductButton = findViewById(R.id.addProductButton);
//        productsListView = findViewById(R.id.productsListView);
        photoButton = findViewById(R.id.photoButton);
        //recyclerview
        spinner = findViewById(R.id.productsSpinner);
        //For spinner
//        ArrayAdapter dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, totalProductsList);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //        spinner.setAdapter(dataAdapter1);
        baseSpinnerAdapter = new ProductBaseAdapter(this, totalProductsList); //total products
        spinner.setAdapter(baseSpinnerAdapter);
//productsListView.setAdapter(baseSpinnerAdapter);

        //        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        selectedProductsRecyclerView = findViewById(R.id.selectedProductsRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectedProductsRecyclerView.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(context, productsOnService);//<< on recycler [added to service]
        selectedProductsRecyclerView.setAdapter(productsAdapter);


        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get spinner item id/object and put it on listview, then onstate change para el recyclerview
///if element is already on productsOnService [from edition] then spinner is different
                Product temp = (Product) spinner.getSelectedItem();
                int qty = (selectQtyProduct.getText().toString().equals("") ? 1 : Integer.parseInt(selectQtyProduct.getText().toString()));
//                if(qty==0)
//                    temp.setLocalQty(0);
//                else
                temp.addLocalQty(qty); //increase qty
//aun cuando haya borrado el elemento, se le queda guardado el valor de local qty, hay que checar como borrar bien el objeto de productsOnservices
                //to avoid repeating element, just replace it/update it

                //todo: print all elements from edition, and check why it doesnt match element in spinner as added!!!!!!!
                //todo: qty is different, therefore, is not going to be the same.
                // BUT it checks if exists


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
                if (!serviceIdTextView.getText().equals("")) {
                    verifyData();
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();

            }
        });

        addClientButton=findViewById(R.id.addClientButton);
        clientSpinner=findViewById(R.id.clientSpinner);

        addClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreateClientActivity.class));
            }
        });

    }


    FirebaseApp app;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    DatabaseReference databaseProductRef;
    DatabaseReference databaseServiceRef;
//    DatabaseReference databaseCurrentServiceRef;

    public void initFirebase() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
        databaseServiceRef = database.getReference("service");
        databaseProductRef = database.getReference("products");
    }


    public void LoadEditService(String id) {
        DatabaseReference databaseCurrentServiceRef = databaseServiceRef.child(id);
        databaseCurrentServiceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Servicio servicio = dataSnapshot.getValue(Servicio.class);
                    //Product product = dataSnapshot.getValue(Product.class);
                    fillFields(servicio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillFields(Servicio serv) {

        serviceIdTextView.setText(serv.getId());
        serviceIdTextView.setClickable(false);
        serviceIdTextView.setFocusable(false);
        serviceNameTextView.setText(serv.getName());
        serviceNameTextView.setClickable(false);
        serviceNameTextView.setFocusable(false);


        //serv.getProducts()
        ArrayList<Product> templist = serv.getProducts();
        for (Product product : templist) {
            productsOnService.add(product);
        }
        productsAdapter.notifyDataSetChanged();
    }


    @Override
    public void makeUseOfBarcode() {
        super.makeUseOfBarcode();
        serviceIdTextView.setText(getBarcode());

    }

    public void getData() {
        getDataFromFirebase();
        //todo, after creating a service, when creating a new service the previous localQty keeps prevs values
//        totalProductsList = new ArrayList<>();
        productsOnService = new ArrayList<>();
//        totalProductsList.addAll(singleton.getProductsList());
    }

    public void getDataFromFirebase() {
        totalProductsList = new ArrayList<>(); //clear list
        Log.e(TAG, "getDataFromFirebase, loading data for first time, from onCreate: ");
//        totalProductsList.addAll(singleton.getProductsList());
        databaseProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e(TAG, "onDataChange: Loading data from fb to list after update on list ");
//                totalProductsList.clear();
//                totalProductsList = new ArrayList<>(); //clear list, it is a listener
                Log.e("productos Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//            <YourClass> post = postSnapshot.getValue(<YourClass>.class);
//                    Log.e("Get Data", post.<YourMethod>());
                    Product product = postSnapshot.getValue(Product.class);
                    if (!totalProductsList.contains(product))
                        totalProductsList.add(product);
                    Log.e(TAG, "onDataChange product loaded from database: " + product.getName());
                }
                baseSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });


    }

    public void saveService() {
        createService();
        Log.e(TAG, "saveService: just crated service object ");
        singleton.setService(service); //maybe the list on singleton hasnt been created yet

//        en ningun momento se hixo un servicio nuevo

//todo
/** Recuerda que los servicios si se pueden editar. asi que la edicion del snapshot si es posible
 * siempre y cuando se haya elegido antes. y no generado el numero de servicio*/

        //if(editingService) {
            DatabaseReference mref = databaseServiceRef.child(service.getId());
        Log.e(TAG, "saveService++1: " );
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG, "onDataChange: ++2" );
                    if (dataSnapshot.exists() && !editingService) {
                        Toast.makeText(context, "Ya existe ese numero de servicio", Toast.LENGTH_LONG).show();
                    } else {

                        databaseServiceRef.child(service.getId()).setValue(service);

                        Toast.makeText(context, "Servicio saved", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(SERVICE_ID, service.getId());
                        setResult(Activity.RESULT_OK, returnIntent);
                        productsOnService.clear();
                        productsOnService = null;
                        singleton.update(context);

                        finish();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//        }else{
//            databaseServiceRef.pus
//        }

    }

    public void verifyData() {
        if (serviceIdTextView.getText().toString().equals(""))
            serviceIdTextView.setError("Llena este dato");
        if (serviceNameTextView.getText().toString().equals(""))
            serviceNameTextView.setError("Llena este dato");
        Log.e(TAG, "verifyData: serviceId:" + serviceIdTextView.getText() + ", nameservice:" + serviceNameTextView.getText());
        if (serviceIdTextView.getText().toString().equals("") || serviceNameTextView.getText().toString().equals("")) {
            Toast.makeText(context, "Llena los datos", Toast.LENGTH_SHORT).show();
        } else {
            saveService();

        }


    }

    public void createService() {
        /**Remember that if I
         * upload this object it will save the products with every information on it,
         * rather than just the ID to fetch them later*/
        service = new Servicio();
        service.setClientId(client.getId());
        service.setClientName(client.getName());
        service.setName(serviceNameTextView.getText().toString());
        service.setId((serviceIdTextView.getText().toString()));
        service.setProducts(productsOnService);


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
                    baseSpinnerAdapter.notifyDataSetChanged();
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
