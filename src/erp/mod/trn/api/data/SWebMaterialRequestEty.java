/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SWebMaterialRequestEty {
    /**
         * SELECT 
            mre.id_mat_req,
            mre.id_ety,
            mre.qty,
            mre.price_u_sys,
            mre.price_u,
            mre.tot_r,
            i.item_key,
            i.item,
            cc.pk_cc,
            cc.cc,
            cc.id_cc
        FROM
            erp_aeth.trn_mat_req_ety AS mre
                INNER JOIN
            erp.itmu_item AS i ON mre.fk_item = i.id_item
                LEFT JOIN
	        erp_aeth.fin_cc AS cc ON mre.fk_cc_n = cc.pk_cc
        WHERE
            NOT mre.b_del AND mre.id_mat_req = 8263;
         */
    private int idMaterialRequest;
    private int idEty;
    private double quantity;
    private double priceUnitarySystem;
    private double priceUnitary;
    private double total;
    private String itemKey;
    private String itemName;
    private int idCostCenter;
    private String costCenter;

    private ArrayList<SWebMatReqEtyNote> lEtyNotes;

    public SWebMaterialRequestEty() {
        this.itemKey = "";
        this.itemName = "";
        
        this.lEtyNotes = new ArrayList<>();
    }

    public int getIdMaterialRequest() {
        return idMaterialRequest;
    }

    public void setIdMaterialRequest(int idMaterialRequest) {
        this.idMaterialRequest = idMaterialRequest;
    }

    public int getIdEty() {
        return idEty;
    }

    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPriceUnitarySystem() {
        return priceUnitarySystem;
    }

    public void setPriceUnitarySystem(double priceUnitarySystem) {
        this.priceUnitarySystem = priceUnitarySystem;
    }

    public double getPriceUnitary() {
        return priceUnitary;
    }

    public void setPriceUnitary(double priceUnitary) {
        this.priceUnitary = priceUnitary;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIdCostCenter() {
        return idCostCenter;
    }

    public void setIdCostCenter(int idCostCenter) {
        this.idCostCenter = idCostCenter;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public ArrayList<SWebMatReqEtyNote> getlEtyNotes() {
        return lEtyNotes;
    }

    public void setlEtyNotes(ArrayList<SWebMatReqEtyNote> lEtyNotes) {
        this.lEtyNotes = lEtyNotes;
    }
}
