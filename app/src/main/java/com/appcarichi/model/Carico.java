package com.appcarichi.model;

import java.io.Serializable;

public class Carico implements Serializable {

    int idcarico, codice, tot_colli, colli_censiti, num_sedute, numColliSpuntati, numTotColli;
    String destinazione, stato_spedizione, statoCarico;


    public Carico(int idcarico, int codice, int tot_colli, int colli_censiti, int num_sedute, String destinazione, String stato_spedizione, String statoCarico, int numColliSpuntati, int numTotColli) {
        this.idcarico = idcarico;
        this.codice = codice;
        this.tot_colli = tot_colli;
        this.colli_censiti = colli_censiti;
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

    public int getTot_colli() {
        return tot_colli;
    }

    public void setTot_colli(int tot_colli) {
        this.tot_colli = tot_colli;
    }

    public int getColli_censiti() {
        return colli_censiti;
    }

    public void setColli_censiti(int colli_censiti) {
        this.colli_censiti = colli_censiti;
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


