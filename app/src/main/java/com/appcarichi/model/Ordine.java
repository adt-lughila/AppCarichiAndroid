package com.appcarichi.model;

import java.io.Serializable;

public class Ordine implements Serializable {
    int idordine,idcarico,tot_colli,colli_consegnati;
    String fornitore,matricola,barcode,luogo_consegna;

    public Ordine(int idordine, int idcarico, int tot_colli, int colli_consegnati, String fornitore, String matricola, String barcode, String luogo_consegna) {
        this.idordine = idordine;
        this.idcarico = idcarico;
        this.tot_colli = tot_colli;
        this.colli_consegnati = colli_consegnati;
        this.fornitore = fornitore;
        this.matricola = matricola;
        this.barcode = barcode;
        this.luogo_consegna = luogo_consegna;
    }

    public int getIdordine() {
        return idordine;
    }

    public void setIdordine(int idordine) {
        this.idordine = idordine;
    }

    public int getIdcarico() {
        return idcarico;
    }

    public void setIdcarico(int idcarico) {
        this.idcarico = idcarico;
    }

    public int getTot_colli() {
        return tot_colli;
    }

    public void setTot_colli(int tot_colli) {
        this.tot_colli = tot_colli;
    }

    public int getColli_consegnati() {
        return colli_consegnati;
    }

    public void setColli_consegnati(int colli_consegnati) {
        this.colli_consegnati = colli_consegnati;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLuogo_consegna() {
        return luogo_consegna;
    }

    public void setLuogo_consegna(String luogo_consegna) {
        this.luogo_consegna = luogo_consegna;
    }
}
