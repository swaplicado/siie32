/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SSessionUtils {

    /**
     * Gets reference ID of desired link of provided item.
     * @param session GUI session.
     * @param idItem Item ID.
     * @param idLink Link ID, constants defined in <code>SModSysConsts</code> (SModSysConsts.ITMS_LINK_...).
     */
    public static int getItemReferenceId(final SGuiSession session, final int idItem, final int idLink) throws SQLException, Exception {
        int idReference = SLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;

        switch (idLink) {
            case SModSysConsts.ITMS_LINK_ITEM:
                idReference = idItem;
                break;
            case SModSysConsts.ITMS_LINK_MFR:
                sql = "SELECT fid_mfr FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " WHERE id_item = " + idItem + " ";
                break;
            case SModSysConsts.ITMS_LINK_BRD:
                sql = "SELECT fid_brd FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " WHERE id_item = " + idItem + " ";
                break;
            case SModSysConsts.ITMS_LINK_LINE:
                sql = "SELECT fid_line_n FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " WHERE id_item = " + idItem + " ";
                break;
            case SModSysConsts.ITMS_LINK_IGEN:
                sql = "SELECT fid_igen FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " WHERE id_item = " + idItem + " ";
                break;
            case SModSysConsts.ITMS_LINK_IGRP:
                sql = "SELECT igen.fid_igrp FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " ";
                break;
            case SModSysConsts.ITMS_LINK_IFAM:
                sql = "SELECT igrp.fid_ifam FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGRP) + " AS igrp ON igen.fid_igrp = igrp.id_igrp ";
                break;
            case SModSysConsts.ITMS_LINK_TP_ITEM:
                sql = "SELECT tp.tp_idx FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS tp ON igen.fid_ct_item = tp.id_ct_item AND igen.fid_cl_item = tp.id_cl_item AND igen.fid_tp_item = tp.id_tp_item ";
                break;
            case SModSysConsts.ITMS_LINK_CL_ITEM:
                sql = "SELECT cl.cl_idx FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CL_ITEM) + " AS cl ON igen.fid_ct_item = cl.id_ct_item AND igen.fid_cl_item = cl.id_cl_item ";
                break;
            case SModSysConsts.ITMS_LINK_CT_ITEM:
                sql = "SELECT ct.ct_idx FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CT_ITEM) + " AS ct ON igen.fid_ct_item = ct.id_ct_item ";
                break;
            case SModSysConsts.ITMS_LINK_ALL:
                idReference = SLibConsts.UNDEFINED;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (!sql.isEmpty()) {
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                idReference = resultSet.getInt(1);
            }
        }

        return idReference;
    }
}
