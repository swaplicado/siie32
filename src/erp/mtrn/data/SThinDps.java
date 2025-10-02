package erp.mtrn.data;

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
 * Se usa para agilizar el procesamiento de CFDI de recepción de pagos.
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
    protected String msDbmsCurrencyKey;
    protected Object moDbmsRecordKey;
    protected int mnDbmsCfdId;
    
    protected SThinDpsCfd moThinDpsCfd;
    protected SThinCfd moThinCfd;
    
    public SThinDps() {
        reset();
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
        msDbmsCurrencyKey = "";
        moDbmsRecordKey = null;
        mnDbmsCfdId = 0;
        
        moThinDpsCfd = null;
        moThinCfd = null;
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

    public String getDbmsCurrencyKey() {
        return msDbmsCurrencyKey;
    }
    
    public Object getDbmsRecordKey() {
        return moDbmsRecordKey;
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
    public void read(Object primaryKey, Statement statement) throws Exception {
        reset();
        
        int[] key = (int[]) primaryKey;
        String sql = "SELECT d.id_year, d.id_doc, d.num_ser, d.num, d.dt, d.dt_start_cred, d.days_cred, d.tot_cur_r, d.tot_r, "
                + "d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.fid_tp_pay, "
                + "d.fid_bp_r, d.fid_bpb, d.fid_func, d.fid_func_sub, d.fid_cur, c.cur, c.cur_key, "
                + "dr.fid_rec_year, dr.fid_rec_per, dr.fid_rec_bkc, dr.fid_rec_tp_rec, dr.fid_rec_num "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = d.fid_cur "
                + "LEFT OUTER JOIN trn_dps_rec AS dr ON dr.id_dps_year = d.id_year AND dr.id_dps_doc = d.id_doc "
                + "WHERE d.id_year = " + key[0] + " AND d.id_doc = " + key[1] + ";";
        
        try (ResultSet resultSetDps = statement.executeQuery(sql)) {
            if (!resultSetDps.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento (ID " + SLibUtils.textKey(key) + ").");
            }
            else {
                mnPkYearId = resultSetDps.getInt("d.id_year");
                mnPkDocId = resultSetDps.getInt("d.id_doc");
                msNumberSeries = resultSetDps.getString("d.num_ser");
                msNumber = resultSetDps.getString("d.num");
                mtDate = resultSetDps.getDate("d.dt");
                mtDateStartOfCredit = resultSetDps.getDate("d.dt_start_cred");
                mnDaysOfCredit = resultSetDps.getInt("d.days_cred");
                mdTotalCy_r = resultSetDps.getDouble("d.tot_cur_r");
                mdTotal_r = resultSetDps.getDouble("d.tot_r");
                mnFkDpsCategoryId = resultSetDps.getInt("d.fid_ct_dps");
                mnFkDpsClassId = resultSetDps.getInt("d.fid_cl_dps");
                mnFkDpsTypeId = resultSetDps.getInt("d.fid_tp_dps");
                mnFkPaymentTypeId = resultSetDps.getInt("d.fid_tp_pay");
                mnFkBizPartnerId_r = resultSetDps.getInt("d.fid_bp_r");
                mnFkBizPartnerBranchId = resultSetDps.getInt("d.fid_bpb");
                mnFkFunctionalAreaId = resultSetDps.getInt("d.fid_func");
                mnFkFunctionalSubAreaId = resultSetDps.getInt("d.fid_func_sub");
                mnFkCurrencyId = resultSetDps.getInt("d.fid_cur");
                
                msDbmsCurrency = resultSetDps.getString("c.cur");
                msDbmsCurrencyKey = resultSetDps.getString("c.cur_key");
                
                int recYearId = resultSetDps.getInt("dr.fid_rec_year");
                int recPeriodId = resultSetDps.getInt("dr.fid_rec_per");
                int recBokkeepingCenterId = resultSetDps.getInt("dr.fid_rec_bkc");
                String recRecordTypeId = resultSetDps.getString("dr.fid_rec_tp_rec");
                int recNumberId = resultSetDps.getInt("dr.fid_rec_num");
                
                if (recYearId != 0 && recPeriodId != 0) {
                    moDbmsRecordKey = new Object[] { recYearId, recPeriodId, recBokkeepingCenterId, recRecordTypeId, recNumberId };
                }
                
                sql = "SELECT id_cfd "
                        + "FROM trn_cfd "
                        + "WHERE fid_dps_year_n = " + mnPkYearId + " AND fid_dps_doc_n = " + mnPkDocId + ";";
                try (ResultSet resultSetCfd = statement.executeQuery(sql)) {
                    if (resultSetCfd.next()) {
                        mnDbmsCfdId = resultSetCfd.getInt("id_cfd");
                    }
                }
                
                if (mnDbmsCfdId != 0) {
                    boolean exists = false;
                    
                    // read document CFD, if exists:
                    
                    sql = "SELECT COUNT(*) "
                            + "FROM trn_dps_cfd "
                            + "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + ";";
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            exists = true;
                        }
                    }
                    
                    if (exists) {
                        moThinDpsCfd = new SThinDpsCfd();
                        moThinDpsCfd.read(new int[] { mnPkYearId, mnPkDocId}, statement);
                    }

                    // read CFD, if exists:
                    
                    sql = "SELECT COUNT(*) "
                            + "FROM trn_cfd "
                            + "WHERE id_cfd = " + mnDbmsCfdId + ";";
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            exists = true;
                        }
                    }

                    if (exists) {
                        moThinCfd = new SThinCfd();
                        moThinCfd.read(new int[] { mnDbmsCfdId }, statement);
                    }
                }
            }
        }
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }
}
