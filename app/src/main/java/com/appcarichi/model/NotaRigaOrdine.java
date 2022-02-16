package com.appcarichi.model;

import java.io.Serializable;

public class NotaRigaOrdine implements Serializable {

        int idNotaRigaOrdine;
        int idRigaOrdine;
        Rigaordine ro;
        Nota nota;
        String utente,commento;

    public NotaRigaOrdine(int idRigaOrdine, Rigaordine ro, Nota nota, String commento) {
        this.idRigaOrdine = idRigaOrdine;
        this.ro = ro;
        this.nota = nota;
        this.commento = commento;
    }

    public int getIdNotaRigaOrdine(){
        return idNotaRigaOrdine;
    }

    public int getIdRigaOrdine() {
        return idRigaOrdine;
    }

    public void setIdRigaOrdine(int idRigaOrdine) {
        this.idRigaOrdine = idRigaOrdine;
    }

    public Rigaordine getRo() {
        return ro;
    }

    public void setRo(Rigaordine ro) {
        this.ro = ro;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }
}
