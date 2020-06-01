package com.okbiometry.okbiometry.clases;

public class clsUsuario {
    String Dni,TipoDni,Nombres, Apellidos,Clave;

    public clsUsuario(String dni, String tipoDni, String nombres, String apellidos, String clave) {
        Dni = dni;
        TipoDni = tipoDni;
        Nombres = nombres;
        Apellidos = apellidos;
        Clave = clave;
    }

    public clsUsuario() {

    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getTipoDni() {
        return TipoDni;
    }

    public void setTipoDni(String tipoDni) {
        TipoDni = tipoDni;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }
}
