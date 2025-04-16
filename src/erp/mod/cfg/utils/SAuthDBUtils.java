package erp.mod.cfg.utils;

import sa.lib.gui.SGuiSession;

public class SAuthDBUtils {

    /**
     * Obtener último usuario que insertó un registro en la tabla trn_dps_authorn
     * por el campo fid_usr_new filtrando por id_year y id_doc
     * 
     * @param session
     * @param idYear
     * @param idDoc
     * @return
     */
    public static int getLastUserInsert(SGuiSession session, final int idYear, final int idDoc) {
        String sql = "SELECT fid_usr_new FROM trn_dps_authorn WHERE NOT b_del AND id_year = " + idYear
                + " AND id_doc = " + idDoc + " ORDER BY id_authorn DESC, ts_edit DESC LIMIT 1";
        int lastUser = 0;
        try {
            java.sql.Statement statement = session.getStatement().getConnection().createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                lastUser = resultSet.getInt("fid_usr_new");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return lastUser;
    }
}
