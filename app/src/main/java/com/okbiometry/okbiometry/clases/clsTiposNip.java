package com.okbiometry.okbiometry.clases;

public class clsTiposNip {
   int IdTipoNip;
    String Descripcion;

    public clsTiposNip(int idTipoNip, String descripcion) {
        IdTipoNip = idTipoNip;
        Descripcion = descripcion;
    }

    public int getIdTipoNip() {
        return IdTipoNip;
    }

    public void setIdTipoNip(int idTipoNip) {
        IdTipoNip = idTipoNip;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return
                 Descripcion;

    }
}
