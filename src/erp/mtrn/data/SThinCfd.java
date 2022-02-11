package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Versión "delgada" del registro SDataDps (tabla trn_cfd).
 * Se usa para agilizar el procesamiento de CFDI de recepción de pagos.
 * @author Sergio Flores
 */
public class SThinCfd implements Serializable, SThinData {
    
    protected int mnPkCfdId;
    protected String msUuid;
    protected int mnFkXmlTypeId;
    protected int mnFkXmlStatusId;
    
    public SThinCfd() {
        reset();
    }
    
    public int getPkCfdId() {
        return mnPkCfdId;
    }
    
    public String getUuid() {
        return msUuid;
    }
    
    public int getFkXmlTypeId() {
        return mnFkXmlTypeId;
    }
    
    public int getFkXmlStatusId() {
        return mnFkXmlStatusId;
    }
    
    public boolean isStamped() {
        return !msUuid.isEmpty();
    }
    
    @Override
    public void reset() {
        mnPkCfdId = 0;
        msUuid = "";
        mnFkXmlTypeId = 0;
        mnFkXmlStatusId = 0;
    }
    
    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();
        
        int[] key = (int[]) primaryKey;
        String sql = "SELECT uuid, fid_tp_xml, fid_st_xml "
                + "FROM trn_cfd "
                + "WHERE id_cfd = " + key[0] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nCFD.");
            }
            else {
                mnPkCfdId = key[0];
                msUuid = resultSet.getString("uuid");
                mnFkXmlTypeId = resultSet.getInt("fid_tp_xml");
                mnFkXmlStatusId = resultSet.getInt("fid_st_xml");
            }
        }
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkCfdId };
    }
}
