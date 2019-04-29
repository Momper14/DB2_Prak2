package entitätsklassen;

import java.math.BigDecimal;

public class BestA {

    private Integer artnr, menge;
    private BigDecimal wert;

    public BestA() {
    }

    public BestA(Integer artnr, Integer menge, BigDecimal wert) {
        this.artnr = artnr;
        this.menge = menge;
        this.wert = wert;
    }

    public Integer getArtnr() {
        return artnr;
    }

    public void setArtnr(Integer artnr) {
        this.artnr = artnr;
    }

    public Integer getMenge() {
        return menge;
    }

    public void setMenge(Integer menge) {
        this.menge = menge;
    }

    public BigDecimal getWert() {
        return wert;
    }

    public void setWert(BigDecimal wert) {
        this.wert = wert;
    }

}
