package entitätsklassen;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BestellAt {

    private Integer bstnr, knr, status;
    private BigDecimal rsum;
    private Date bestdate, liefdate, rechdate;
    private List<BestA> artList;

    public BestellAt() {
    }

    public BestellAt(Integer bstnr, Integer knr, Integer status, BigDecimal rsum, Date bestdate, Date liefdate, Date rechdate, List<BestA> artList) {
        this.bstnr = bstnr;
        this.knr = knr;
        this.status = status;
        this.rsum = rsum;
        this.bestdate = bestdate;
        this.liefdate = liefdate;
        this.rechdate = rechdate;
        this.artList = artList;
    }

    public Integer getBstnr() {
        return bstnr;
    }

    public void setBstnr(Integer bstnr) {
        this.bstnr = bstnr;
    }

    public Integer getKnr() {
        return knr;
    }

    public void setKnr(Integer knr) {
        this.knr = knr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getRsum() {
        return rsum;
    }

    public void setRsum(BigDecimal rsum) {
        this.rsum = rsum;
    }

    public Date getBestdate() {
        return bestdate;
    }

    public void setBestdate(Date bestdate) {
        this.bestdate = bestdate;
    }

    public Date getLiefdate() {
        return liefdate;
    }

    public void setLiefdate(Date liefdate) {
        this.liefdate = liefdate;
    }

    public Date getRechdate() {
        return rechdate;
    }

    public void setRechdate(Date rechdate) {
        this.rechdate = rechdate;
    }

    public List<BestA> getArtList() {
        return artList;
    }

    public void setArtList(List<BestA> artList) {
        this.artList = artList;
    }

}
