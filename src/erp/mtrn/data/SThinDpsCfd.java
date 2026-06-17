package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Versión "delgada" del registro SDataDpsCfd (tabla trn_dps_cfd).
 * Se usa para agilizar la lectura de datos de DPS,
 * p. ej., en el procesamiento de CFDI de recepción de pagos o la importación de documentos desde SWAP Services.
 * @author Sergio Flores, Edwin Carmona
 */
public class SThinDpsCfd implements Serializable, SThinData {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msPaymentMethod;
    protected String msCfdUse;
    
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

    public String getCfdUse() {
        return msCfdUse;
    }

    @Override
    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        msPaymentMethod = "";
        msCfdUse = "";
    }

    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();
        
        int[] key = (int[]) primaryKey;
        String sql = "SELECT pay_met, COALESCE(dcfd.cfd_use, '') AS _cfd_use "
                + "FROM trn_dps_cfd "
                + "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento CFD.");
            }
            else {
                mnPkYearId = key[0];
                mnPkDocId = key[1];
                msPaymentMethod = resultSet.getString("pay_met");
                msCfdUse = resultSet.getString("_cfd_use");
            }
        }
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }
}
