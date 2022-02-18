package com.appcarichi.model;

import java.io.Serializable;

public class Nota implements Serializable {

    int idNota,codicenota;
    String descrizione;

    public Nota(int codicenota, String descrizione) {
        this.codicenota = codicenota;
        this.descrizione = descrizione;

    }

    public int getIdNota() {return idNota;}

    public int getCodicenota() {
        return codicenota;
    }

    public void setCodicenota(int codicenota) {
        this.codicenota = codicenota;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
