package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;

import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_PRODUCT;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_PRODUCT;

public class AddProductActivity extends BaseActivity {
    private static final String TAG = "AddProductActivity";
    Context context;
    Product newProduct;
    Button createProductBtn;
    ImageButton photoButton;
    EditText itemSerialNumberTextView;
    EditText itemNameTextView;
    EditText itemDescriptionTextView;
    EditText itemFacturaTextView;
    int CAMERA_CODE = 111;
    FirebaseVisionBarcodeDetectorOptions barcodeOptions;
    DataSingleton singleton;
    boolean editingProduct;

//    @Override
//    public String scanBarcode() {
//        return super.scanBarcode();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = this;

        singleton = DataSingleton.getInstance();
        initFirebase();
        initComponents();
        Intent intent = getIntent();
        editingProduct = intent.getBooleanExtra(EXTRA_EDITING_PRODUCT, false);
        if (editingProduct) {
            LoadEditProduct(intent.getStringExtra(EXTRA_EDIT_PRODUCT));
        }

    }


    FirebaseApp app;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    DatabaseReference databaseRef;

    public void initFirebase() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
        databaseRef = database.getReference("products");
    }


    public void initComponents() {
        createProductBtn = findViewById(R.id.createProductBtn);
        photoButton = findViewById(R.id.cameraButton);
        itemSerialNumberTextView = findViewById(R.id.productIdTextView);
        itemNameTextView = findViewById(R.id.productNameTextView);
        itemDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        itemFacturaTextView = findViewById(R.id.itemFacturaTextView);

        createProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();

            }
        });

    }

    public void LoadEditProduct(String id) {

        DatabaseReference childRef = databaseRef.child(id);
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    fillFields(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void fillFields(Product product) {
        itemSerialNumberTextView.setText(product.getId());
        itemSerialNumberTextView.setClickable(false);
        itemSerialNumberTextView.setFocusable(false);
        itemNameTextView.setText(product.getName());
        itemNameTextView.setClickable(false);
        itemNameTextView.setFocusable(false);
        itemDescriptionTextView.setText(product.getDescription());
    }


    @Override
    public void makeUseOfBarcode() {
        super.makeUseOfBarcode();
        Log.e(TAG, "makeUseOfBarcode: getBarcode():" + getBarcode());
        itemSerialNumberTextView.setText(getBarcode());

    }

    /*
    public void getBarCode() {

        barcodeOptions =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE,
                                FirebaseVisionBarcode.FORMAT_AZTEC,
                                FirebaseVisionBarcode.FORMAT_CODE_128,
                                FirebaseVisionBarcode.FORMAT_CODE_39,
                                FirebaseVisionBarcode.FORMAT_UPC_A,
                                FirebaseVisionBarcode.FORMAT_UPC_E,
                                FirebaseVisionBarcode.FORMAT_EAN_8,
                                FirebaseVisionBarcode.FORMAT_EAN_13,
                                FirebaseVisionBarcode.FORMAT_ITF
                        )
                        .build();

        //FirebaseVisionImage image= FirebaseVisionImage.fromBitmap()
        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//            ImageView image = (ImageView) findViewById(R.id.imageView1);
//            image.setImageBitmap(picture);
            FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector();
// Or, to specify the formats to recognize:
// FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
//        .getVisionBarcodeDetector(options);
            Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            // Task completed successfully
                            // ...

                            for (FirebaseVisionBarcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                itemSerialNumberTextView.setText(rawValue);
                                verifyItemExists();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });
        } else {
            Toast.makeText(context, "No picture Taken", Toast.LENGTH_SHORT).show();
        }

    }

    public void verifyItemExists() {
//        check online if exists, or in local database
    }*/


    public void verifyData() {
        EditText[] fields = {itemSerialNumberTextView,
                itemNameTextView,
                itemDescriptionTextView};
        boolean anyError = false;
        for (EditText field : fields) {
            if (field.getText().equals("")) {
                field.setError(getResources().getString(R.string.error_empty_field));
                anyError = true;
            }
        }
        if (!anyError) {
            sendData();
        }
    }


    public void sendData() {

        //TODO: Remember that singleton it WILL be different than what is going to be uploaded to JSON firebase
        // , as it will hold local quantity, while JSONFirebase will hold Absolute quantity.
        //Although Product.class will work fine with the rest of the app. Only on AddProduct is going to be used differently. wont upload, so that structure is not going to exists online.
//        but it will upload a custom version of it, that holds only totalqty, but not local.

//        itemFacturaTextView
//                (String id, String name, String description, String factura, int qty, int localQty)
//
//        newProduct = new Product(itemSerialNumberTextView.getText().toString(),itemNameTextView.getText().toString(), itemDescriptionTextView.getText().toString(),
        newProduct = new Product(itemSerialNumberTextView.getText().toString(),itemNameTextView.getText().toString(),
                itemDescriptionTextView.getText().toString(),
                itemFacturaTextView.getText().toString());
        singleton.setProducts(newProduct);


        DatabaseReference mref = databaseRef.child(newProduct.getId());
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !editingProduct) {
                    Toast.makeText(context, "Ya existe ese numero de serie", Toast.LENGTH_LONG).show();
                } else {
                    databaseRef.child(newProduct.getId()).setValue(newProduct);

                    if(editingProduct)
                    Toast.makeText(context, singleton.getProduct(newProduct.getId()).getName() + " Editado", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, singleton.getProduct(newProduct.getId()).getName() + " Agregado", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("productId", newProduct.getId());
                    setResult(Activity.RESULT_OK, returnIntent);
                    singleton.update(context);
                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        Log.e(TAG, "sendData: databaseProductRef.child(newProduct.getId()).getKey"+databaseProductRef.child("0000"). );
//      if(databaseProductRef.child(newProduct.getId()).equals(newProduct.getId())){
//          Toast.makeText(context, "Ya existe ese producto", Toast.LENGTH_SHORT).show();
//      }
//      else {
        /*  databaseProductRef.child(newProduct.getId()).setValue(newProduct);

        Toast.makeText(context, singleton.getProduct(newProduct.getId()).getId() + " Agregado", Toast.LENGTH_SHORT).show();

//        [if everything is aok, then return]/
        Intent returnIntent = new Intent();
        returnIntent.putExtra("productId", newProduct.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        singleton.update(context);
        finish();*/
        //}
        // Toast.makeText(context, "Producto Agregado", Toast.LENGTH_SHORT).show();
        //save on Firebase
//        check if any value on bundle, then add to
    }


}
