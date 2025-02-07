/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import erp.mod.hrs.link.db.SAbsence;
import erp.mod.hrs.link.db.SDepartment;
import erp.mod.hrs.link.db.SEmployee;
import erp.mod.hrs.link.db.SEmployeeVacations;
import erp.mod.hrs.link.db.SFirstDayYear;
import erp.mod.hrs.link.db.SHoliday;
import erp.mod.hrs.link.db.SPosition;
import erp.mod.hrs.link.db.SPrepayCutCalendar;
import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SRootJSON {
    
    String last_sync_date;
    ArrayList<SDepartment> departments;
    ArrayList<SPosition> positions;
    ArrayList<SEmployee> employees;
    ArrayList<SHoliday> holidays;
    ArrayList<SFirstDayYear> fdys;
    ArrayList<SAbsence> absences;
    ArrayList<SPrepayCutCalendar> cuts;
    ArrayList<SEmployeeVacations> vacations;
}
