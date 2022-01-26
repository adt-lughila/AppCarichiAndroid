package model;

import java.io.Serializable;

public class Nota implements Serializable {

    int idnota,idordine,codicenota;
    String descrizione,commento;

    public Nota(int idordine, int codicenota, String descrizione, String commento) {
        this.idordine = idordine;
        this.codicenota = codicenota;
        this.descrizione = descrizione;
        this.commento = commento;
    }

    public Nota(int idnota, int idordine, int codicenota, String descrizione, String commento) {
        this.idnota=idnota;
        this.idordine = idordine;
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
        return idordine;
    }

    public void setIdordine(int idordine) {
        this.idordine = idordine;
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
