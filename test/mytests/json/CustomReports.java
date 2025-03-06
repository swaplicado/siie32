/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytests.json;

import erp.mod.fin.view.SCustomReportsParser;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class CustomReports {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SCustomReportsParser parser = new SCustomReportsParser();
            parser.readCustomReports(SCustomReportsParser.CUST_REPS_EXPENSES);
            
            for (SCustomReportsParser.Report report : parser.getCustomReports()) {
                System.out.println("Report: \"" + report.Report + "\" = " + report);
            }
        }
        catch (Exception e) {
            SLibUtils.showException("main()", e);
        }
    }
}
