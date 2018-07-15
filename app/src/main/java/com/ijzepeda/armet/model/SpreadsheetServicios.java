package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpreadsheetServicios implements Parcelable {

    String id;
    String noSerie;
    String nombreProducto;
    String cantidad;
    String personal;
    String factura;
    String cliente;//a donde lo llevo

    public SpreadsheetServicios(String id, String noSerie, String nombreProducto, String cantidad, String personal, String factura, String cliente) {
        this.id = id;
        this.noSerie = noSerie;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.personal = personal;
        this.factura = factura;
        this.cliente = cliente;
    }


    public SpreadsheetServicios() {
    }

    protected SpreadsheetServicios(Parcel in) {
        id = in.readString();
        noSerie = in.readString();
        nombreProducto = in.readString();
        cantidad = in.readString();
        personal = in.readString();
        factura = in.readString();
        cliente = in.readString();
    }

    public static final Creator<SpreadsheetServicios> CREATOR = new Creator<SpreadsheetServicios>() {
        @Override
        public SpreadsheetServicios createFromParcel(Parcel in) {
            return new SpreadsheetServicios(in);
        }

        @Override
        public SpreadsheetServicios[] newArray(int size) {
            return new SpreadsheetServicios[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(noSerie);
        dest.writeString(nombreProducto);
        dest.writeString(cantidad);
        dest.writeString(personal);
        dest.writeString(factura);
        dest.writeString(cliente);
    }
}
