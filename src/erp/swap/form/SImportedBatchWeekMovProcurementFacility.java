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
    protected ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility;
    
    public SImportedBatchWeekMovProcurementFacility() {
        this.mnFacilitySeasonWeekId = 0;
        this.maWeekMovProcurementFacility = new ArrayList<>();
    }

    public int getMnFacilitySeasonWeekId() { return mnFacilitySeasonWeekId; }
    public void setMnFacilitySeasonWeekId(int mnFacilitySeasonWeekId) { this.mnFacilitySeasonWeekId = mnFacilitySeasonWeekId; }
    public ArrayList<SImportWeekMovProcurementFacility> getMaWeekMovProcurementFacility() { return maWeekMovProcurementFacility; }
    public void setMaWeekMovProcurementFacility(ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility) { this.maWeekMovProcurementFacility = maWeekMovProcurementFacility; }
    
    public void addToMaWeekMovProcurementFacility(SImportWeekMovProcurementFacility oWeekMovProcurementFacility) {
        this.maWeekMovProcurementFacility.add(oWeekMovProcurementFacility);
    }
}
