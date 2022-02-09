package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Versión "delgada" del registro SDataDpsCfd (tabla trn_dps_cfd).
 * Se usa para agilizar el procesamiento de CFDI de recepción de pagos.
 * @author Sergio Flores
 */
public class SThinDpsCfd implements SThinData {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msPaymentMethod;
    
    public SThinDpsCfd() {
        reset();
    }
    
    public int getPkYearId() {
        return mnPkYearId;
    }
    
    public int getPkDocId() {
        return mnPkDocId;
    }
    
    public String getPaymentMethod() {
        return msPaymentMethod;
    }

    @Override
    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        msPaymentMethod = "";
    }

    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();
        
        int[] key = (int[]) primaryKey;
        String sql = "SELECT pay_met "
                + "FROM trn_dps_cfd "
                + "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
            }
            else {
                mnPkYearId = key[0];
                mnPkDocId = key[1];
                msPaymentMethod = resultSet.getString("pay_met");
            }
        }
    }
}
