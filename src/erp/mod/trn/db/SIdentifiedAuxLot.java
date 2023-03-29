/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SIdentifiedAuxLot {
    protected int idLot;
    protected int idItem;
    protected int idUnit;
    protected String lot;
    protected Date expiration;
    protected int idItemExternal;
    protected int idUnitExternal;
    protected int idLotExternal;

    public int getIdLot() {
        return idLot;
    }

    public void setIdLot(int idLot) {
        this.idLot = idLot;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public int getIdItemExternal() {
        return idItemExternal;
    }

    public void setIdItemExternal(int idItemExternal) {
        this.idItemExternal = idItemExternal;
    }

    public int getIdUnitExternal() {
        return idUnitExternal;
    }

    public void setIdUnitExternal(int idUnitExternal) {
        this.idUnitExternal = idUnitExternal;
    }

    public int getIdLotExternal() {
        return idLotExternal;
    }

    public void setIdLotExternal(int idLotExternal) {
        this.idLotExternal = idLotExternal;
    }
    
}
