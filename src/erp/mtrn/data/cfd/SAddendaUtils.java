/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.data.SDataConstantsSys;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public abstract class SAddendaUtils {
    
    public static final SimpleDateFormat DateFormatIso = new SimpleDateFormat("yyyy-MM-dd");
    public static final DecimalFormat DecimalFormatAmount = new DecimalFormat("0.00");
    
    /**
     * Normalize value.
     * @param value Value as String.
     * @return Empty String if value is <code>null</code>. Otherwise value as is.
     */
    public static String normalize(final String value) {
        return value == null ? "" : value;
    }
    
    /**
     * Retrieve last addenda JSON data strings from sales invoices for required customer and addenda type.
     * @param statement Database statement.
     * @param customerId Required customer (business partner) ID.
     * @param addendaTypeId Required CFD addenda type ID.
     * @return Array of addenda JSON data strings.
     * @throws Exception 
     */
    public static ArrayList<String> retriveAddendaJsonDataStrings(final Statement statement, final int customerId, final int addendaTypeId) throws Exception {
        ArrayList<String> addendaJsonDataStrings = new ArrayList<>();
        
        String sql = "SELECT DISTINCT da.json_data "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_dps_add AS da ON d.id_year = da.id_year and d.id_doc = da.id_doc "
                + "WHERE NOT d.b_del AND "
                + "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND "
                + "d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND "
                + "d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " AND "
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND "
                + "d.fid_bp_r = " + customerId + " AND "
                + "da.fid_tp_cfd_add = " + addendaTypeId + " AND "
                + "da.json_data <> '' "
                + "ORDER BY da.json_data;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                addendaJsonDataStrings.add(resultSet.getString(1));
            }
        }
        
        return addendaJsonDataStrings;
    }
}
