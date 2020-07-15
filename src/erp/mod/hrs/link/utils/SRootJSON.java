/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import erp.mod.hrs.link.db.*;
import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SRootJSON {
    String last_sync_date;
    ArrayList<SDepartment> departments;
    ArrayList<SEmployee> employees;
    ArrayList<SHoliday> holidays;
    ArrayList<SFirstDayYear> fdys;
    ArrayList<SAbsence> absences;
    ArrayList<SPrepayCutCalendar> cuts;
}
