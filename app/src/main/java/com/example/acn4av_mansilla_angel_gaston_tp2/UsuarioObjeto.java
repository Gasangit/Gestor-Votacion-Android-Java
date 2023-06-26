package com.example.acn4av_mansilla_angel_gaston_tp2;

public class UsuarioObjeto {

    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String pass;
    private String calle;
    private String altura;
    private boolean admin;

    public UsuarioObjeto(String id, String nombre, String apellido, String dni,
                         String mail, String pass, String calle, String altura, boolean admin) {

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.pass = pass;
        this.calle = calle;
        this.altura = altura;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }

    public String getCalle() {
        return calle;
    }

    public String getAltura() {
        return altura;
    }

    public boolean getAdmin() { return admin;}

}

