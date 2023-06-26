package com.example.acn4av_mansilla_angel_gaston_tp2;

public class VotacionObjeto {

    private String idVotacion;
    private String idOrganizador;
    private String descripcion;
    private String opcion1 = "";
    private String opcion1Id = "";
    private String opcion2 = "";
    private String opcion2Id = "";
    private String opcion3 = "";
    private String opcion3Id = "";


    public VotacionObjeto(String idVotacion, String idOrganizador, String descripcion) {
        this.idVotacion = idVotacion;
        this.idOrganizador = idOrganizador;
        this.descripcion = descripcion;
    }
    public VotacionObjeto(String idVotacion, String idOrganizador, String descripcion,
                          String opcion1, String opcion2, String opcion3) {
        this.idVotacion = idVotacion;
        this.idOrganizador = idOrganizador;
        this.descripcion = descripcion;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
    }
    public String getIdVotacion() {
        return idVotacion;
    }

    public String getIdOrganizador() {
        return idOrganizador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public void setOpcion1Id(String opcion1Id) {
        this.opcion1Id = opcion1Id;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public void setOpcion2Id(String opcion2Id) {
        this.opcion2Id = opcion2Id;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public void setOpcion3Id(String opcion3Id) {
        this.opcion3Id = opcion3Id;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public String getOpcion1Id() {
        return opcion1Id;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public String getOpcion2Id() {
        return opcion2Id;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public String getOpcion3Id() {
        return opcion3Id;
    }
}
