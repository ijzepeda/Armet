package com.ijzepeda.armet.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.ijzepeda.armet.R;

public class AddProductActivity extends BaseActivity {
    private static final String TAG = "AddProductActivity";
    Context context;
    Button createProductBtn;
    ImageButton photoButton;
    EditText itemSerialNumberTextView;
    EditText itemNameTextView;
    EditText itemDescriptionTextView;
    int CAMERA_CODE = 111;
    FirebaseVisionBarcodeDetectorOptions barcodeOptions;


//    @Override
//    public String scanBarcode() {
//        return super.scanBarcode();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = this;
        initComponents();

    }

    public void initComponents() {
        createProductBtn = findViewById(R.id.createProductBtn);
        photoButton = findViewById(R.id.cameraButton);
        itemSerialNumberTextView = findViewById(R.id.productIdTextView);
        itemNameTextView = findViewById(R.id.productNameTextView);
        itemDescriptionTextView = findViewById(R.id.productDescriptionTextView);

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

    @Override
    public void makeUseOfBarcode() {
        super.makeUseOfBarcode();
        Log.e(TAG, "makeUseOfBarcode: getBarcode():"+getBarcode() );
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
        Toast.makeText(context, "Producto Agregado", Toast.LENGTH_SHORT).show();
    }


}
