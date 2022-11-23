package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.data.SThinData;
import erp.mod.trn.db.STrnUtils;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

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
    protected double mdTotalCy;
    protected double mdTotal;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkBizPartnerId_r;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkCurrencyId;
    
    protected String msDbmsCurrency;
    protected String msDbmsCurrencyKey;
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
        mdTotalCy = 0;
        mdTotal = 0;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkBizPartnerId_r = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkCurrencyId = 0;
        
        msDbmsCurrency = "";
        msDbmsCurrencyKey = "";
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
    
    public double getTotalCy() {
        return mdTotalCy;
    }

    public double getTotal() {
        return mdTotal;
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

    public int getFkBizPartnerId_r() {
        return mnFkBizPartnerId_r;
    }
    
    public int getFkBizPartnerBranchId() {
        return mnFkBizPartnerBranchId;
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
        String sql = "SELECT d.id_year, d.id_doc, d.num_ser, d.num, d.dt, d.tot_cur_r, d.tot_r, "
                + "d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, "
                + "d.fid_bp_r, d.fid_bpb, d.fid_cur, c.cur, c.cur_key "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = d.fid_cur "
                + "WHERE d.id_year = " + key[0] + " AND d.id_doc = " + key[1] + ";";
        
        try (ResultSet resultSetDps = statement.executeQuery(sql)) {
            if (!resultSetDps.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nDocumento.");
            }
            else {
                mnPkYearId = resultSetDps.getInt("d.id_year");
                mnPkDocId = resultSetDps.getInt("d.id_doc");
                msNumberSeries = resultSetDps.getString("d.num_ser");
                msNumber = resultSetDps.getString("d.num");
                mtDate = resultSetDps.getDate("d.dt");
                mdTotalCy = resultSetDps.getDouble("d.tot_cur_r");
                mdTotal = resultSetDps.getDouble("d.tot_r");
                mnFkDpsCategoryId = resultSetDps.getInt("d.fid_ct_dps");
                mnFkDpsClassId = resultSetDps.getInt("d.fid_cl_dps");
                mnFkDpsTypeId = resultSetDps.getInt("d.fid_tp_dps");
                mnFkBizPartnerId_r = resultSetDps.getInt("d.fid_bp_r");
                mnFkBizPartnerBranchId = resultSetDps.getInt("d.fid_bpb");
                mnFkCurrencyId = resultSetDps.getInt("d.fid_cur");
                
                msDbmsCurrency = resultSetDps.getString("c.cur");
                msDbmsCurrencyKey = resultSetDps.getString("c.cur_key");
                
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
