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
    private final int COL_TO_ACCOUNT = 6;
    
    public int WeekMonthNumber;
    public Date StartDate;
    public Date EndDate;
    public int FacilitySeasonWeekId;
    public int ProcurementFacilityId;
    public String ProcurementFacilityName;
    public int StatusId;
    public int Year;
    public int MonthNumber;
    public boolean ToAccount;
    public boolean isAccountedFor;
    public int mnSortingPosition;
    
    public SImportProcurementFacility() {
        WeekMonthNumber = 0;
        StartDate = null;
        EndDate = null;
        FacilitySeasonWeekId = 0;
        ProcurementFacilityName = "";
        StatusId = 0;
        ToAccount = false;
        ProcurementFacilityId = 0;
        isAccountedFor = false;
        mnSortingPosition = 0;
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
        ProcurementFacilityId = docNode.get("id").asInt();
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
            case 5:
                value = isAccountedFor;
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

    public int getWeekMonthNumber() { return WeekMonthNumber; }
    public void setWeekMonthNumber(int WeekMonthNumber) { this.WeekMonthNumber = WeekMonthNumber; }
    public Date getStartDate() { return StartDate; }
    public void setStartDate(Date StartDate) { this.StartDate = StartDate; }
    public Date getEndDate() { return EndDate; }
    public void setEndDate(Date EndDate) { this.EndDate = EndDate; }
    public int getFacilitySeasonWeekId() { return FacilitySeasonWeekId; }
    public void setFacilitySeasonWeekId(int FacilitySeasonWeekId) { this.FacilitySeasonWeekId = FacilitySeasonWeekId; }
    public int getProcurementFacilityId() { return ProcurementFacilityId; }
    public void setProcurementFacilityId(int ProcurementFacilityId) { this.ProcurementFacilityId = ProcurementFacilityId; }
    public String getProcurementFacilityName() { return ProcurementFacilityName; }
    public void setProcurementFacilityName(String ProcurementFacilityName) { this.ProcurementFacilityName = ProcurementFacilityName; }
    public int getStatusId() { return StatusId; }
    public void setStatusId(int StatusId) { this.StatusId = StatusId; }
    public int getYear() { return Year; }
    public void setYear(int Year) { this.Year = Year; }
    public int getMonthNumber() { return MonthNumber; }
    public void setMonthNumber(int MonthNumber) { this.MonthNumber = MonthNumber; }
    public boolean isToAccount() { return ToAccount; }
    public void setToAccount(boolean ToAccount) { this.ToAccount = ToAccount; }
    public boolean isIsAccountedFor() { return isAccountedFor; }
    public void setIsAccountedFor(boolean isAccountedFor) { this.isAccountedFor = isAccountedFor; }
    public int getMnSortingPosition() { return mnSortingPosition; }
    public void setMnSortingPosition(int mnSortingPosition) { this.mnSortingPosition = mnSortingPosition; }
}
