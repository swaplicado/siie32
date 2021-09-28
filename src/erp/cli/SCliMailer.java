/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SParamsApp;
import java.sql.ResultSet;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 *
 * @author Sergio Flores
 */
public class SCliMailer {
    
    public static final String ERR_ARGS_INVALID = "Argumentos inválidos.";
    
    public static final int ARG_IDX_COMPANY_ID = 0;
    public static final int ARG_IDX_OPTION = 1;
    public static final String OPTION_REP_INV = "I";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //======================================================================
        // KEEP THIS SECTION COMMENTED WHEN CODE RELEASED!, they all are only for testing purpouses.
        // company ID, report type, 's'ales/'p'urchases, start, end, TO, [BCC]
        // Company ID: 1211 = Operadora Tron; 2852 = Aceites Especiales TH
        //args = new String[] { "2852", "i", "s", "today", "today", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "2020-08-01", "2020-08-31", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-2", "today-2", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today", "today", "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "gortiz@aeth.mx", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "gortiz@aeth.mx;sflores@swaplicado.com.mx", "floresgtz@hotmail.com;cesar.orozco@swaplicado.com.mx" };
        //args = new String[] { "1211", "i", "s", "2020-01-01", "2020-12-31", "sflores@swaplicado.com.mx;sflores@aeth.mx", "floresgtz@hotmail.com" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "2021-01-31", "2021-01-31", "sflores@swaplicado.com.mx" };
        //args = new String[] { "2852", "i", "s", "today-1", "today-1", "sflores@swaplicado.com.mx;sflores@aeth.mx", "floresgtz@hotmail.com" };
        //======================================================================
        
        SParamsApp paramsApp = null;
        SDbDatabase dbErp = null;
        SDbDatabase dbCompany = null;
        
        try {
            if (args.length <= ARG_IDX_COMPANY_ID) {
                throw new Exception(ERR_ARGS_INVALID);
            }

            // read ERP parameters:
            
            paramsApp = new SParamsApp();
            
            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }
            
            // create database connections:
            
            int result = 0;
            
            // connect to ERP database:
            
            dbErp = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            result = dbErp.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                    paramsApp.getDatabaseName(), paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // connect to company database:
            
            String companyDatabase = "";
            
            String sql = "SELECT bd "
                    + "FROM erp.cfgu_co "
                    + "WHERE id_co = " + args[ARG_IDX_COMPANY_ID] + ";";
            try (ResultSet resultSet = dbErp.getConnection().createStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    companyDatabase = resultSet.getString("bd");
                }
            }
            
            dbCompany = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            result = dbCompany.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                    companyDatabase, paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // process request:
            
            if (args.length <= ARG_IDX_OPTION) {
                throw new Exception(ERR_ARGS_INVALID);
            }
            
            switch (args[ARG_IDX_OPTION].toUpperCase()) {
                case OPTION_REP_INV:
                    new SCliRepInvoices(args, dbCompany).sendMail();
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            dbErp.disconnect();
            dbCompany.disconnect();
        }
        catch (Exception e) {
            SLibUtils.printException(SCliMailer.class.getName(), e);
            System.exit(-1);
        }
    }
}
