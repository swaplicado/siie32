/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class SPrepayroll {
    
    private String start_date;
    private String end_date;
    private List<SPrepayrollRow> rows;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<SPrepayrollRow> getRows() {
        return rows;
    }

    public void setRows(List<SPrepayrollRow> rows) {
        this.rows = rows;
    }
 
    
}
