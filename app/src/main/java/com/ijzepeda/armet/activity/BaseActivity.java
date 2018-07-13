package com.ijzepeda.armet.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    private static int CAMERA_CODE = 111;
    public String barcodeStr;

    public void scanBarcode() {
//        barcodeOptions =
//                new FirebaseVisionBarcodeDetectorOptions.Builder()
//                        .setBarcodeFormats(
//                                FirebaseVisionBarcode.FORMAT_QR_CODE,
//                                FirebaseVisionBarcode.FORMAT_AZTEC,
//                                FirebaseVisionBarcode.FORMAT_CODE_128,
//                                FirebaseVisionBarcode.FORMAT_CODE_39,
//                                FirebaseVisionBarcode.FORMAT_UPC_A,
//                                FirebaseVisionBarcode.FORMAT_UPC_E,
//                                FirebaseVisionBarcode.FORMAT_EAN_8,
//                                FirebaseVisionBarcode.FORMAT_EAN_13,
//                                FirebaseVisionBarcode.FORMAT_ITF
//                        )
//                        .build();

        //FirebaseVisionImage image= FirebaseVisionImage.fromBitmap()
        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_CODE);

    }

    public String getBarcode() {
        return barcodeStr;
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                                barcodeStr = rawValue;
                                Log.e(TAG, "onSuccess: barcodeStr" + barcodeStr + ", rawvalue:" + rawValue);
                                //itemSerialNumberTextView.setText(rawValue);
                                makeUseOfBarcode();
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
        }
//        else {
//            Toast.makeText(context, "No picture Taken", Toast.LENGTH_SHORT).show();
//        }

    }

    public void makeUseOfBarcode(){


    }


    public void verifyItemExists() {
//        check online if exists, or in local database
    }


}
