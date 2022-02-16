package com.appcarichi.model;

import java.io.Serializable;

public class NotaRigaOrdine implements Serializable {

        int idNotaRigaOrdine;
        Rigaordine ro;
        Nota nota;
        String utente,commento;

    public NotaRigaOrdine(int idNotaRigaOrdine, Rigaordine ro, Nota nota, String commento, String utente) {
        this.idNotaRigaOrdine = idNotaRigaOrdine;
        this.ro = ro;
        this.nota = nota;
        this.commento = commento;
        this.utente = utente;
    }

    public int getIdNotaRigaOrdine(){
        return idNotaRigaOrdine;
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
