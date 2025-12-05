package erp.mtrn.data;

import erp.SClientUtils;
import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import erp.mod.trn.db.STrnUtils;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 * Versión "delgada" del registro SDataDps (tabla trn_dps).
 * Se usa para agilizar la lectura de datos de DPS,
 * p. ej., en el procesamiento de CFDI de recepción de pagos o la importación de documentos desde SWAP Services.
 * @author Sergio Flores
 */
public class SThinDps implements Serializable, SThinData {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msNumberSeries;
    protected String msNumber;
    protected Date mtDate;
    protected Date mtDateStartOfCredit;
    protected int mnDaysOfCredit;
    protected double mdTotalCy_r;
    protected double mdTotal_r;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkPaymentTypeId;
    protected int mnFkBizPartnerId_r;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkFunctionalSubAreaId;
    protected int mnFkCurrencyId;
    
    protected String msDbmsCurrency;
    protected String msDbmsCurrencyCode;
    protected Object moDbmsRecordKey;
    
    protected int mnFkUserNewId;
    protected String msDbmsUserNew;
    
    protected int mnDbmsCfdId;
    protected SThinDpsCfd moThinDpsCfd;
    protected SThinCfd moThinCfd;
    protected SThinPdf moThinPdf;
    
    public SThinDps() {
        reset();
    }

    public int getPkYearId() {
        return mnPkYearId;
    }
    
    public int getPkDocId() {
        return mnPkDocId;
    }

    public String getNumberSeries() {
        return msNumberSeries;
    }

    public String getNumber() {
        return msNumber;
    }

    public Date getDate() {
        return mtDate;
    }
    
    public Date getDateStartOfCredit() {
        return mtDateStartOfCredit;
    }
    
    public int getDaysOfCredit() {
        return mnDaysOfCredit;
    }
    
    public double getTotalCy_r() {
        return mdTotalCy_r;
    }

    public double getTotal_r() {
        return mdTotal_r;
    }
    
    public int getFkDpsCategoryId() {
        return mnFkDpsCategoryId;
    }

    public int getFkDpsClassId() {
        return mnFkDpsClassId;
    }

    public int getFkDpsTypeId() {
        return mnFkDpsTypeId;
    }
    
    public int getFkPaymentTypeId() {
        return mnFkPaymentTypeId;
    }

    public int getFkBizPartnerId_r() {
        return mnFkBizPartnerId_r;
    }
    
    public int getFkBizPartnerBranchId() {
        return mnFkBizPartnerBranchId;
    }
    
    public int getFkFunctionalAreaId() {
        return mnFkFunctionalAreaId;
    }
    
    public int getFkFunctionalSubAreaId() {
        return mnFkFunctionalSubAreaId;
    }
    
    public int getFkCurrencyId() {
        return mnFkCurrencyId;
    }

    public String getDbmsCurrency() {
        return msDbmsCurrency;
    }

    public String getDbmsCurrencyCode() {
        return msDbmsCurrencyCode;
    }
    
    public Object getDbmsRecordKey() {
        return moDbmsRecordKey;
    }

    public int getFkUserNewId() {
        return mnFkUserNewId;
    }
    
    public String getDbmsUserNew() {
        return msDbmsUserNew;
    }
    
    public int getDbmsCfdId() {
        return mnDbmsCfdId;
    }
    
    public SThinDpsCfd getThinDpsCfd() {
        return moThinDpsCfd;
    }
    
    public SThinCfd getThinCfd() {
        return moThinCfd;
    }
    
    public SThinPdf getThinPdf() {
        return moThinPdf;
    }
    
    public String getDpsNumber() {
        return STrnUtils.formatDocNumber(msNumberSeries, msNumber);
    }

    public int[] getDpsCategoryKey() {
        return new int[] { mnFkDpsCategoryId };
    }
    
    public int[] getDpsClassKey() {
        return new int[] { mnFkDpsCategoryId, mnFkDpsClassId };
    }
    
    public int[] getDpsTypeKey() {
        return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId };
    }
    
    @Override
    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        msNumberSeries = "";
        msNumber = "";
        mtDate = null;
        mtDateStartOfCredit = null;
        mnDaysOfCredit = 0;
        mdTotalCy_r = 0;
        mdTotal_r = 0;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkPaymentTypeId = 0;
        mnFkBizPartnerId_r = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkFunctionalSubAreaId = 0;
        mnFkCurrencyId = 0;
        
        msDbmsCurrency = "";
        msDbmsCurrencyCode = "";
        moDbmsRecordKey = null;
        
        mnFkUserNewId = 0;
        msDbmsUserNew = "";
        
        mnDbmsCfdId = 0;
        moThinDpsCfd = null;
        moThinCfd = null;
        moThinPdf = null;
    }
    
    @Override
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();

        int[] key = (int[]) primaryKey;
        String sql = "SELECT d.id_year, d.id_doc, d.num_ser, d.num, d.dt, d.dt_start_cred, d.days_cred, d.tot_cur_r, d.tot_r, "
                + "d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.fid_tp_pay, "
                + "d.fid_bp_r, d.fid_bpb, d.fid_func, d.fid_func_sub, d.fid_cur, c.cur, c.cur_key, un.id_usr, un.usr, "
                + "dr.fid_rec_year, dr.fid_rec_per, dr.fid_rec_bkc, dr.fid_rec_tp_rec, dr.fid_rec_num, "
                + "dcfd.id_year, dcfd.id_doc, cfd.id_cfd, pdf.doc_pdf_name "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = d.fid_cur "
                + "INNER JOIN erp.usru_usr AS un ON un.id_usr = d.fid_usr_new "
                + "LEFT OUTER JOIN trn_dps_rec AS dr ON dr.id_dps_year = d.id_year AND dr.id_dps_doc = d.id_doc "
                + "LEFT OUTER JOIN trn_dps_cfd AS dcfd ON dcfd.id_year = d.id_year AND dcfd.id_doc = d.id_doc "
                + "LEFT OUTER JOIN trn_cfd AS cfd ON cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc "
                + "LEFT OUTER JOIN " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_pdf AS pdf ON pdf.id_year = d.id_year AND pdf.id_doc = d.id_doc "
                + "WHERE d.id_year = " + key[0] + " AND d.id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento (ID " + SLibUtils.textKey(key) + ").");
            }
            else {
                mnPkYearId = resultSet.getInt("d.id_year");
                mnPkDocId = resultSet.getInt("d.id_doc");
                msNumberSeries = resultSet.getString("d.num_ser");
                msNumber = resultSet.getString("d.num");
                mtDate = resultSet.getDate("d.dt");
                mtDateStartOfCredit = resultSet.getDate("d.dt_start_cred");
                mnDaysOfCredit = resultSet.getInt("d.days_cred");
                mdTotalCy_r = resultSet.getDouble("d.tot_cur_r");
                mdTotal_r = resultSet.getDouble("d.tot_r");
                mnFkDpsCategoryId = resultSet.getInt("d.fid_ct_dps");
                mnFkDpsClassId = resultSet.getInt("d.fid_cl_dps");
                mnFkDpsTypeId = resultSet.getInt("d.fid_tp_dps");
                mnFkPaymentTypeId = resultSet.getInt("d.fid_tp_pay");
                mnFkBizPartnerId_r = resultSet.getInt("d.fid_bp_r");
                mnFkBizPartnerBranchId = resultSet.getInt("d.fid_bpb");
                mnFkFunctionalAreaId = resultSet.getInt("d.fid_func");
                mnFkFunctionalSubAreaId = resultSet.getInt("d.fid_func_sub");
                mnFkCurrencyId = resultSet.getInt("d.fid_cur");
                
                msDbmsCurrency = resultSet.getString("c.cur");
                msDbmsCurrencyCode = resultSet.getString("c.cur_key");
                
                int recYearId = resultSet.getInt("dr.fid_rec_year");
                int recPeriodId = resultSet.getInt("dr.fid_rec_per");
                int recBokkeepingCenterId = resultSet.getInt("dr.fid_rec_bkc");
                String recRecordTypeId = resultSet.getString("dr.fid_rec_tp_rec");
                int recNumberId = resultSet.getInt("dr.fid_rec_num");
                
                if (recYearId != 0 && recPeriodId != 0) {
                    moDbmsRecordKey = new Object[] { recYearId, recPeriodId, recBokkeepingCenterId, recRecordTypeId, recNumberId };
                }
                
                // Recover all related CFD data:
                
                mnFkUserNewId = resultSet.getInt("un.id_usr");
                msDbmsUserNew = resultSet.getString("un.usr");
                
                mnDbmsCfdId = resultSet.getInt("cfd.id_cfd"); // means "has CFD"
                boolean hasPdfCfd = resultSet.getInt("dcfd.id_year") != 0 && resultSet.getInt("dcfd.id_doc") != 0;
                boolean hasPdf = resultSet.getString("pdf.doc_pdf_name") != null;

                if (mnDbmsCfdId != 0) {
                    // read CFD:
                    moThinCfd = new SThinCfd();
                    moThinCfd.read(new int[] { mnDbmsCfdId }, statement);
                }
                
                if (hasPdfCfd) {
                    // read CFD data:
                    moThinDpsCfd = new SThinDpsCfd();
                    moThinDpsCfd.read(new int[] { mnPkYearId, mnPkDocId}, statement);
                }

                if (hasPdf) {
                    // read PDF:
                    moThinPdf = new SThinPdf();
                    moThinPdf.read(new int[] { mnPkYearId, mnPkDocId}, statement);
                }
            }
        }
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }
    
    /**
     * Read dps folio number in format series-number.
     * @param primaryKey DPS primary key.
     * @param statement DB statement.
     * @return
     * @throws Exception 
     */
    public static String readDpsNumber(final Object primaryKey, final Statement statement) throws Exception {
        String dpsNumber = "";
        int[] key = (int[]) primaryKey;
        String sql = "SELECT CONCAT(num_ser, IF(num_ser = '', '', '-'), num) AS _dps_num "
                + "FROM trn_dps "
                + "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento (ID " + SLibUtils.textKey(key) + ").");
            }
            else {
                dpsNumber = resultSet.getString("_dps_num");
            }
        }
        
        return dpsNumber;
    }
    
    /**
     * Read dps folio number in format series-number.
     * @param primaryKey DPS primary key.
     * @param statement DB statement.
     * @return
     * @throws Exception 
     */
    public static String readDpsBizPartner(final Object primaryKey, final Statement statement) throws Exception {
        String bizPartner = "";
        int[] key = (int[]) primaryKey;
        String sql = "SELECT b.bp "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = d.fid_bp_r "
                + "WHERE d.id_year = " + key[0] + " AND d.id_doc = " + key[1] + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento (ID " + SLibUtils.textKey(key) + ").");
            }
            else {
                bizPartner = resultSet.getString("b.bp");
            }
        }
        
        return bizPartner;
    }
}
