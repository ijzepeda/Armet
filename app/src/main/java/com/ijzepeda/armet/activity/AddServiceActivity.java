package com.ijzepeda.armet.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ijzepeda.armet.R;

public class AddServiceActivity extends AppCompatActivity {
    private static final String TAG = "AddServiceActivity";
    Context context;
    Button addNewProduct;
    Button addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        context=this;
        initComponents();
    }

    public void initComponents(){
        addNewProduct=findViewById(R.id.addNewProductBtn);
        addProduct=findViewById(R.id.addProductBtn);
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar u nbundle con el id de este servicio, y al final de product, se agrega a la lista de servicio. en caso que no exista un serivcio, solo se registra a la base de datos [sin relacion]

                startActivity(new Intent(context,AddProductActivity.class));
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will add a new field with dropdown and quantity [new element on array for recyclerview]
            }
        });

    }
}
