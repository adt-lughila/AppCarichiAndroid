package com.appcarichi.model;

import java.io.Serializable;

public class Nota implements Serializable {

    int idnota,idrigaordine,codicenota;
    String descrizione,commento;

    public Nota(int idrigaordine, int codicenota, String descrizione, String commento) {
        this.idrigaordine = idrigaordine;
        this.codicenota = codicenota;
        this.descrizione = descrizione;
        this.commento = commento;
    }

    public Nota(int idnota, int idrigaordine, int codicenota, String descrizione, String commento) {
        this.idnota=idnota;
        this.idrigaordine = idrigaordine;
        this.codicenota = codicenota;
        this.descrizione = descrizione;
        this.commento = commento;
    }

        public int getIdnota() {
        return idnota;
    }

    public void setIdnota(int idnota) {
        this.idnota = idnota;
    }

    public int getIdordine() {
        return idrigaordine;
    }

    public void setIdordine(int idordine) {
        this.idrigaordine = idordine;
    }

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

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }
}
