package com.okbiometry.okbiometry.enums;

import com.okbiometry.okbiometry.clases.clsTiposNip;

import java.util.ArrayList;

public enum DocumentType {

    CEDULA_CIUDADANIA(1,"Cédula de Ciudadania"),
    CEDULA_EXTRANJERIA(2,"Cédula de Extranjeria"),
    REGISTRO_CIVIL(3,"Registro Civil"),
    TARJETA_IDENTIDAD(4,"Tarjeta de Identidad"),
    PASAPORTE(4,"Pasaporte"),
    ADULTO_SIN_IDENTIDAD(5,"Adulto Sin Identidad"),
    MENOR_SIN_IDENTIDAD(6,"Menor Sin Identidad");

    private  int Intvalue;
    private String strDescripcion;
    private static int IdDoc;

    DocumentType(int value, String Descripcion) {
    this.Intvalue = value;
    this.strDescripcion = Descripcion;
    }

    public int getIntvalue() {
        return Intvalue;
    }

    public void setIntvalue(int intvalue) {
        Intvalue = intvalue;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public static int BuscarTipoNipDescri(String Descripcion, ArrayList<clsTiposNip> List){

        for(int i= 0; i<List.size();i++){
            if (List.get(i).getDescripcion().equals(Descripcion)){
                IdDoc = List.get(i).getIdTipoNip();
            }
        }
       return IdDoc;
    }


}
