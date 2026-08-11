/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import erp.mfin.data.SDataFacilityRec;
import erp.mfin.data.SDataRecord;
import java.io.Serializable;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Adrian Aviles
 */
public class SImportProcurementFacility implements SGridRow, Serializable {
    private final int COL_TO_ACCOUNT = 6;
    private final int STATUS_APPROVAL = 22;
    private static final Map<Integer, String> STATES = new HashMap<>();
    
    static {
        STATES.put(1, "nuevo");
        STATES.put(2, "borrador");
        STATES.put(3, "capturando");
        STATES.put(11, "pendiente revisión");
        STATES.put(12, "en revisión");
        STATES.put(16, "pendiente corrección");
        STATES.put(17, "en corrección");
        STATES.put(21, "pendiente aprobación");
        STATES.put(22, "aprobado");
        STATES.put(26, "rechazado");
        STATES.put(31, "contabilizado");
        STATES.put(32, "parcialmente contabilizado");
        STATES.put(36, "cerrado");
        STATES.put(51, "en espera");
        STATES.put(56, "cancelado");
    }
    
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
    public SDataRecord moRecord;
    
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
        moRecord = null;
    }
    
    public SImportProcurementFacility(final int year, final int monthNumber, final int weekMonthNumber, final String startDate, final String endDate, final JsonNode docNode, Statement statement) throws ParseException {
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
        
        SDataFacilityRec oDataFacilityRec = new SDataFacilityRec();
        oDataFacilityRec.findByExtDataId(FacilitySeasonWeekId, statement);
        
        SDataRecord record = new SDataRecord();
        record.read(new Object[] { 
            oDataFacilityRec.getMnFkRecYear(),
            oDataFacilityRec.getMnFkRecPer(),
            oDataFacilityRec.getMnFkRecBkc(),
            oDataFacilityRec.getMsFkRecTpRec(),
            oDataFacilityRec.getMnFkRecNum()
        }, statement);
        
        moRecord = record;
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
                value = STATES.get(StatusId);
                break;
            case 5:
                value = isAccountedFor;
                break;
            case COL_TO_ACCOUNT:
                value = ToAccount;
                break;
            case 7:
                if (moRecord != null) {
                    value = moRecord.getRecordPeriod();
                } else {
                    value = "";
                }
                break;
            case 8:
                if (moRecord != null) {
                    value = moRecord.getDbmsBookkeepingCenterCode();
                } else {
                    value = "";
                }
                break;
            case 9:
                if (moRecord != null) {
                    value = moRecord.getDbmsCompanyBranchCode();
                } else {
                    value = "";
                }
                break;
            case 10:
                if (moRecord != null) {
                    value = moRecord.getRecordNumber();
                } else {
                    value = "";
                }
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
    public int getStatusApproval() { return STATUS_APPROVAL; }
    public void setMoRecord(SDataRecord moRecord) { this.moRecord = moRecord; }
    public SDataRecord getMoRecord() { return moRecord; }
}
