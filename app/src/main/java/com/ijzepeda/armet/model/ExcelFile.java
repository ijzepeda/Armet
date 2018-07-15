package com.ijzepeda.armet.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelFile {
    private static final String TAG = "ExcelFile";
    Day day;
    String path;
    String name;
    String email;
    Context context;
Uri uriPath;
    public ExcelFile(Day day) {
        this.day = day;
    }

    public ExcelFile(Day day, String path, String name) {
        this.day = day;
        this.path = path;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void createExcel(){

    }

    public ExcelFile(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name+".xls";
    }


    public void exportToExcel(Day saveDay) {
        //final String fileName = "TodoList.xls";
        //String fileName = name+".xls";


        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        Log.e("ESPORTEXCEL", "exportToExcel: path:"+Environment.getExternalStorageDirectory().toString() );
        File directory = new File(sdCard.getAbsolutePath() + "/armet");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }
        Log.e("exportExcel", "+++++exportToExcel: "+directory.toString() );
        //file path
        File file = new File(directory, name);
            uriPath=Uri.fromFile(file);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("es", "MX"));
        WritableWorkbook workbook;


        try {
            workbook = Workbook.createWorkbook(file, wbSettings);

            //tasks
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Actividades", 0);

            try {
                sheet.addCell(new Label(0, 0, "ID")); // column and row
                sheet.addCell(new Label(1, 0, "Usuario"));
                sheet.addCell(new Label(2, 0, "Fecha"));
                sheet.addCell(new Label(3, 0, "Tarea/Accion"));
                sheet.addCell(new Label(4, 0, "Hora Inicio"));
                sheet.addCell(new Label(5, 0, "Lugar"));
                sheet.addCell(new Label(6, 0, "Cliente"));
                sheet.addCell(new Label(7, 0, "Duracion (minutos)"));
                sheet.addCell(new Label(8, 0, "Tecnico1"));
                sheet.addCell(new Label(9, 0, "Tecnico2"));
                sheet.addCell(new Label(10, 0, "Tecnico3"));

                int i=1;
                for(Task task: day.getTasks()){
                    sheet.addCell(new Label(0, i, i+""));
                    sheet.addCell(new Label(1, i, day.getUserName()));
                    sheet.addCell(new Label(2, i, day.getDate()));
                    sheet.addCell(new Label(3, i, task.getAction()));
                    sheet.addCell(new Label(4, i, task.getStartingTime()));
                    sheet.addCell(new Label(5, i, task.getAddress()));
                    sheet.addCell(new Label(6, i, task.getClient()));
                    sheet.addCell(new Label(7, i, task.getFinalTime()));
                    sheet.addCell(new Label(8, i, task.getTec1Name()));
                    sheet.addCell(new Label(9, i, task.getTec2Name()));
                    sheet.addCell(new Label(10, i, task.getTec3Name()));
                    i++;
                }


            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            //Products
            WritableSheet sheet2 = workbook.createSheet("Productos", 1);

            try {

                sheet2.addCell(new Label(0, 0, "No. de Serie")); // column and row
                sheet2.addCell(new Label(1, 0, "Producto"));
                sheet2.addCell(new Label(2, 0, "Factura"));
                sheet2.addCell(new Label(3, 0, "Cantidad"));
                sheet2.addCell(new Label(4, 0, "Usuario"));
                sheet2.addCell(new Label(5, 0, "Fecha"));
                Log.e(TAG, "exportToExcel: about startpage2 for ");
                int i=1;
                for(Servicio service: day.getServices()){
                    Log.e(TAG, "exportToExcel: about startpage2 for{1} ");

                    for(Product product: service.getProducts()){
                        Log.e(TAG, "exportToExcel: about startpage2 for {2}");

                        sheet2.addCell(new Label(0, i, product.getId()));
                        sheet2.addCell(new Label(1, i, product.getName()));
                        sheet2.addCell(new Label(2, i, product.getFactura()));
                        sheet2.addCell(new Label(3, i, product.getLocalQty()+""));
                        sheet2.addCell(new Label(4, i, day.getUserName()));
                        sheet2.addCell(new Label(5, i, day.getDate()));
                    }
                    i++;
                }



            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            //--------------------------
            workbook.write();
            try {
                workbook.close();
                Log.e(TAG, "exportToExcel: about to send email" );
                sendExcelTo();
            } catch (WriteException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




    }



    public void sendExcelTo(Context context, String email){
//        Toast.makeText(context, "your excel file is ready"+email, Toast.LENGTH_SHORT).show();
        Intent emailIntent=new Intent(Intent.ACTION_SEND);
//        emailInten.
//        emailIntent .setType("vnd.android.cursor.dir/email");
        emailIntent.setType("text/plain");

        String to[] = {email};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, uriPath);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Actividades de "+day.getUserName()+" del dia "+day.getDate());
        context.startActivity(Intent.createChooser(emailIntent , "Enviando Datos..."));
    }


    public void sendExcelTo(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        Toast.makeText(context, "your excel file is ready"+email, Toast.LENGTH_SHORT).show();
        Intent emailIntent=new Intent(Intent.ACTION_SEND);
//        emailInten.
//        emailIntent .setType("vnd.android.cursor.dir/email");
        emailIntent.setType("text/plain");
        String to[] = {email};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, uriPath);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Actividades de "+day.getUserName()+" del dia "+day.getDate());
        context.startActivity(Intent.createChooser(emailIntent , "Selecciona App de correo."));
    }

}
