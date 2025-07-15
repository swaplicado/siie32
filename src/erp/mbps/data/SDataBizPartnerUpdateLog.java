package erp.mbps.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Claudio Peña
 */
public class SDataBizPartnerUpdateLog {
    private int supplierMatrix = 1;
    private int supplierType = 2;    
    
    public void saveFromForm(erp.client.SClientInterface miClient, int idBp, String BizPartnerCommercial, String email, String taxRegime, String leadTime, int userId) throws Exception {
        String sql;
        
        if(!BizPartnerCommercial.trim().isEmpty()){
            sql = "UPDATE erp.BPSU_BP SET bp_comm = ? " +
                  "WHERE id_bp = ? ";
            try (PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
                ps.setString(1, BizPartnerCommercial);
                ps.setInt(2, idBp);
                ps.executeUpdate();
            }
        }

        
        sql = "UPDATE erp.BPSU_BPB_CON SET email_01 = ? " +
              "WHERE id_bpb = (SELECT id_bpb FROM erp.bpsu_bpb WHERE fid_bp = ? AND fid_tp_bpb = " + supplierMatrix + " LIMIT 1) " +
              "AND id_con = " + supplierMatrix + " ";
        try (PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, idBp);
            ps.executeUpdate();
        }
        
        
        sql = "UPDATE erp.BPSU_BP_CT SET tax_regime = ?, lead_time = ? " +
              "WHERE id_bp = ? AND id_ct_bp = " + supplierType + " ";
        try (PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
            ps.setString(1, taxRegime);
            ps.setInt(2, Integer.parseInt(leadTime));
            ps.setInt(3, idBp);
            ps.executeUpdate();
        }

        int logId = 0;
        sql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM erp.bpsu_bp_upd_log WHERE id_bp = ?";
        try (PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idBp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logId = rs.getInt(1);
                }
            }
        }

        sql = "INSERT INTO erp.bpsu_bp_upd_log (id_bp, id_log, bp_comm, email_01, tax_regime, lead_time, fk_usr_upd, ts_usr_upd) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idBp);
            ps.setInt(2, logId);
            ps.setString(3, BizPartnerCommercial);
            ps.setString(4, email);
            ps.setString(5, taxRegime);
            ps.setInt(6, Integer.parseInt(leadTime));
            ps.setInt(7, userId);
            ps.executeUpdate();
        }
    }
}