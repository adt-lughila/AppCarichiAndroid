package model;

import java.io.Serializable;

public class Rigaordine implements Serializable {

    int idrigarodine;
    String campo1;
    String campo2;
    String campo3;
    String campo4;
    String campo5;
    String campo6;
    int idordine;

    public Rigaordine(int idrigarodine, String campo1, String campo2, String campo3, String campo4, String campo5, String campo6, int idordine) {
        this.idrigarodine = idrigarodine;
        this.campo1 = campo1;
        this.campo2 = campo2;
        this.campo3 = campo3;
        this.campo4 = campo4;
        this.campo5 = campo5;
        this.campo6 = campo6;
        this.idordine = idordine;
    }

    public int getIdrigarodine() {
        return idrigarodine;
    }

    public void setIdrigarodine(int idrigarodine) {
        this.idrigarodine = idrigarodine;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    public String getCampo4() {
        return campo4;
    }

    public void setCampo4(String campo4) {
        this.campo4 = campo4;
    }

    public String getCampo5() {
        return campo5;
    }

    public void setCampo5(String campo5) {
        this.campo5 = campo5;
    }

    public String getCampo6() {
        return campo6;
    }

    public void setCampo6(String campo6) {
        this.campo6 = campo6;
    }

    public int getIdordine() {
        return idordine;
    }

    public void setIdordine(int idordine) {
        this.idordine = idordine;
    }
}
