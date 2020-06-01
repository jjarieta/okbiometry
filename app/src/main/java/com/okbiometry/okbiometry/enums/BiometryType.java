package com.okbiometry.okbiometry.enums;

import com.okbiometry.okbiometry.clases.clsTiposNip;

import java.util.ArrayList;

public enum BiometryType {

    FingerPrint(1,"FingerPrint Biometry"),
    Signature(2,"Signature Biometry"),
    Face(3,"Face Biometry "),
    Voice(4," Voice Biometry");


    private  int Intvalue;
    private String strDescripcion;
    private static int IdDoc;

    BiometryType(int value, String Descripcion) {
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
