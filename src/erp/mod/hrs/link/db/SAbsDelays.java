/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SAbsDelays {
    private String startDate;
    private String endDate;
    private ArrayList<SDataRow> rows;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<SDataRow> getRows() {
        return rows;
    }

    public void setRows(ArrayList<SDataRow> rows) {
        this.rows = rows;
    }
    
}
