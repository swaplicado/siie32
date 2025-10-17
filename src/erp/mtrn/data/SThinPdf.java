package erp.mtrn.data;

import erp.SClientUtils;
import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Versión "delgada" del registro SDataPdf (tabla trn_pdf).
 * Se usa para agilizar la lectura de datos de DPS,
 * p. ej., en el procesamiento de CFDI de recepción de pagos o la importación de documentos desde SWAP Services.
 * @author Sergio Flores
 */
public class SThinPdf implements Serializable, SThinData {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msDocPdfName;
    
    public SThinPdf() {
        reset();
    }
    
    public int getPkYearId() {
        return mnPkYearId;
    }
    
    public int getPkDocId() {
        return mnPkDocId;
    }

    public String getDocPdfName() {
        return msDocPdfName;
    }
    
    @Override
    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        msDocPdfName = "";
    }
    
    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();

        int[] key = (int[]) primaryKey;
        String sql = "SELECT id_year, id_doc, doc_pdf_name "
                + "FROM " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_pdf "
                + "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nPDF.");
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msDocPdfName = resultSet.getString("doc_pdf_name");
            }
        }
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }
}
