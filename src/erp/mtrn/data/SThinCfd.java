package erp.mtrn.data;

import erp.lib.data.SThinData;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Versión "delgada" del registro SDataDps (tabla trn_cfd).
 * Se usa para agilizar el procesamiento de CFDI de recepción de pagos.
 * @author Sergio Flores
 */
public class SThinCfd implements SThinData {
    
    protected int mnCfdId;
    protected String msUuid;
    protected int mnXmlTypeId;
    protected int mnXmlStatusId;
    
    public SThinCfd() {
        reset();
    }
    
    public int getCfdId() {
        return mnCfdId;
    }
    
    public String getUuid() {
        return msUuid;
    }
    
    public int getXmlTypeId() {
        return mnXmlTypeId;
    }
    
    public int getXmlStatusId() {
        return mnXmlStatusId;
    }
    
    @Override
    public void reset() {
        mnCfdId = 0;
        msUuid = "";
        mnXmlTypeId = 0;
        mnXmlStatusId = 0;
    }
    
    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();
        
        int[] key = (int[]) primaryKey;
        String sql = "SELECT uuid, fid_tp_xml, fid_st_xml "
                + "FROM trn_cfd "
                + "WHERE id_cfd = " + key[0] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            mnCfdId = key[0];
            msUuid = resultSet.getString("uuid");
            mnXmlTypeId = resultSet.getInt("fid_tp_xml");
            mnXmlStatusId = resultSet.getInt("fid_st_xml");
        }
    }
}
