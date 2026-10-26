/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import java.util.ArrayList;

/**
 *
 * @author Adrian Aviles
 */
public class SImportedBatchWeekMovProcurementFacility {
    private int mnFacilitySeasonWeekId;
    private int mnProcurementId;
    private String msProcurementName;
    private int mnWeekNumebr;
    private int mnCashHoldingIdCob;
    private int mnCashHoldingIdEnt;
    private int mnAccountingTypeId;
    protected ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility;
    
    public SImportedBatchWeekMovProcurementFacility() {
        this.mnFacilitySeasonWeekId = 0;
        this.mnProcurementId = 0;
        this.msProcurementName = "";
        this.mnWeekNumebr = 0;
        this.maWeekMovProcurementFacility = new ArrayList<>();
        this.mnCashHoldingIdCob = 0;
        this.mnCashHoldingIdEnt = 0;
        this.mnAccountingTypeId = 0;
    }

    public int getMnFacilitySeasonWeekId() { return mnFacilitySeasonWeekId; }
    public void setMnFacilitySeasonWeekId(int mnFacilitySeasonWeekId) { this.mnFacilitySeasonWeekId = mnFacilitySeasonWeekId; }
    public ArrayList<SImportWeekMovProcurementFacility> getMaWeekMovProcurementFacility() { return maWeekMovProcurementFacility; }
    public void setMaWeekMovProcurementFacility(ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility) { this.maWeekMovProcurementFacility = maWeekMovProcurementFacility; }
    public int getMnProcurementId() { return mnProcurementId; }
    public void setMnProcurementId(int mnProcurementId) { this.mnProcurementId = mnProcurementId; }
    public String getMsProcurementName() { return msProcurementName; }
    public void setMsProcurementName(String msProcurementName) { this.msProcurementName = msProcurementName; }
    public int getMnWeekNumebr() { return mnWeekNumebr; }
    public void setMnWeekNumebr(int mnWeekNumebr) { this.mnWeekNumebr = mnWeekNumebr; }
    public int getMnCashHoldingIdCob() { return mnCashHoldingIdCob; }
    public void setMnCashHoldingIdCob(int mnCashHoldingIdCob) { this.mnCashHoldingIdCob = mnCashHoldingIdCob; }
    public int getMnCashHoldingIdEnt() { return mnCashHoldingIdEnt; }
    public void setMnCashHoldingIdEnt(int mnCashHoldingIdEnt) { this.mnCashHoldingIdEnt = mnCashHoldingIdEnt; }
    public int getMnAccountingTypeId() { return mnAccountingTypeId; }
    public void setMnAccountingTypeId(int mnAccountingTypeId) { this.mnAccountingTypeId = mnAccountingTypeId; }
    
    public void addToMaWeekMovProcurementFacility(SImportWeekMovProcurementFacility oWeekMovProcurementFacility) {
        this.maWeekMovProcurementFacility.add(oWeekMovProcurementFacility);
    }
}
