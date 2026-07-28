/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Adrian Aviles
 */
public class SImportProcurementFacility implements SGridRow, Serializable {
    private final int COL_TO_ACCOUNT = 5;
    
    public int WeekMonthNumber;
    public Date StartDate;
    public Date EndDate;
    public int FacilitySeasonWeekId;
    public String ProcurementFacilityName;
    public int StatusId;
    public int Year;
    public int MonthNumber;
    public boolean ToAccount;
    
    public SImportProcurementFacility() {
        WeekMonthNumber = 0;
        StartDate = null;
        EndDate = null;
        FacilitySeasonWeekId = 0;
        ProcurementFacilityName = "";
        StatusId = 0;
        ToAccount = false;
    }
    
    public SImportProcurementFacility(final int year, final int monthNumber, final int weekMonthNumber, final String startDate, final String endDate, final JsonNode docNode) throws ParseException {
        WeekMonthNumber = weekMonthNumber;
        Year = year;
        MonthNumber = monthNumber;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dStartDate= sdf.parse(startDate);
        Date dEndDate= sdf.parse(endDate);
        StartDate = dStartDate;
        EndDate = dEndDate;
        
        JsonNode status = docNode.path("status");
        FacilitySeasonWeekId = docNode.get("facility_season_week_id").asInt();
        ProcurementFacilityName = docNode.get("name").asText();
        StatusId = status.get("id").asInt();
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = WeekMonthNumber;
                break;
            case 1:
                value = StartDate;
                break;
            case 2:
                value = EndDate;
                break;
            case 3:
                value = ProcurementFacilityName;
                break;
            case 4:
                value = StatusId == 1 ? "Nueva" : "";
                break;
            case COL_TO_ACCOUNT:
                value = ToAccount;
                break;
            default:
            // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case COL_TO_ACCOUNT:
                ToAccount = (boolean) value;
                break;
            default:
                // nothing
        }
    }
    
}
