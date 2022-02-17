package com.appcarichi.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Rigaordine implements Serializable {

    int idrigarodine;
    String codiceArticolo;
    String matricola;
    String barcode;
    String descrizione;
    BigDecimal pezziordinati;
    BigDecimal pezzispediti;
    BigDecimal sconto;
    BigDecimal prezzo;
    Integer nroColli;
    Integer colliSpuntati;
    int idordine;

    public int getIdrigarodine() {
        return idrigarodine;
    }

    public void setIdrigarodine(int idrigarodine) {
        this.idrigarodine = idrigarodine;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPezziordinati() {
        return pezziordinati;
    }

    public void setPezziordinati(BigDecimal pezziordinati) {
        this.pezziordinati = pezziordinati;
    }

    public BigDecimal getPezzispediti() {
        return pezzispediti;
    }

    public void setPezzispediti(BigDecimal pezzispediti) {
        this.pezzispediti = pezzispediti;
    }

    public BigDecimal getSconto() {
        return sconto;
    }

    public void setSconto(BigDecimal sconto) {
        this.sconto = sconto;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getIdordine() {
        return idordine;
    }

    public void setIdordine(int idordine) {
        this.idordine = idordine;
    }

    public Integer getNroColli(){ return nroColli; }

    public void setNroColli(int nroColli){ this.nroColli = nroColli; }

    public Integer getColliSpuntati(){ return colliSpuntati; }

    public void setColliSpuntati(int colliSPuntati){ this.colliSpuntati = colliSpuntati; }

    public Rigaordine(int idrigarodine, String codiceArticolo, String matricola, String barcode, String descrizione,
                      BigDecimal pezziordinati, BigDecimal pezzispediti, BigDecimal sconto, BigDecimal prezzo, int idordine,
                      int nroColli, int colliSpuntati) {
        this.idrigarodine = idrigarodine;
        this.codiceArticolo = codiceArticolo;
        this.matricola = matricola;
        this.barcode = barcode;
        this.descrizione = descrizione;
        this.pezziordinati = pezziordinati;
        this.pezzispediti = pezzispediti;
        this.sconto = sconto;
        this.prezzo = prezzo;
        this.idordine = idordine;
        this.nroColli = nroColli;
        this.colliSpuntati = colliSpuntati;
    }




}
