package com.appcarichi.model;

import java.math.BigDecimal;

public class FlagCarico {

    private BigDecimal numTotColli;

    private BigDecimal numColliSpuntati;

    public BigDecimal getNumTotColli() {
        return numTotColli;
    }

    public void setNumTotColli(BigDecimal numTotColli) {
        this.numTotColli = numTotColli;
    }

    public BigDecimal getNumColliSpuntati() {
        return numColliSpuntati;
    }

    public void setNumColliSpuntati(BigDecimal numColliSpuntati) {
        this.numColliSpuntati = numColliSpuntati;
    }
}