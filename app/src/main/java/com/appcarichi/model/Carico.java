package com.appcarichi.model;

import java.io.Serializable;

public class Carico implements Serializable {

    int idcarico, codice, num_sedute, numColliSpuntati, numTotColli;
    String destinazione, stato_spedizione, statoCarico;


    public Carico(int idcarico, int codice, int num_sedute, String destinazione, String stato_spedizione, String statoCarico, int numColliSpuntati, int numTotColli) {
        this.idcarico = idcarico;
        this.codice = codice;
        this.num_sedute = num_sedute;
        this.destinazione = destinazione;
        this.stato_spedizione = stato_spedizione;
        this.statoCarico = statoCarico;
        this.numColliSpuntati = numColliSpuntati;
        this.numTotColli = numTotColli;
    }

    public int getIdcarico() {return idcarico; }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public int getNum_sedute() {
        return num_sedute;
    }

    public void setNum_sedute(int num_sedute) {
        this.num_sedute = num_sedute;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getStato_spedizione() {
        return stato_spedizione;
    }

    public void setStato_spedizione(String stato_spedizione) {
        this.stato_spedizione = stato_spedizione;
    }

    public String getStatoCarico() {
        return statoCarico;
    }

    public void setStatoCarico(String statoCarico) {
        this.statoCarico = statoCarico;
    }

    public int getNumColliSpuntati() {
        return numColliSpuntati;
    }

    public int getNumTotColli() {
        return numTotColli;
    }
}


