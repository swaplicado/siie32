/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.bps.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SBpsUtils {

    /**
     * @param idCategory Business partner category, constants defined in <code>erp.mod.SModSysConsts.BPSS_CT_BP_...</code>.
     * @param number Number noun, constants defined in <code>sa.lib.SUtilConsts.NUM_...</code>.
     */
    public static String getBizPartnerCategoryName(final int idCategory, final int number) {
        String name = "";

        switch (idCategory) {
            case SModSysConsts.BPSS_CT_BP_CO:
                name = number == SUtilConsts.NUM_SNG ? "Empresa" : "Empresas";
                break;
            case SModSysConsts.BPSS_CT_BP_CUS:
                name = number == SUtilConsts.NUM_SNG ? "Cliente" : "Clientes";
                break;
            case SModSysConsts.BPSS_CT_BP_SUP:
                name = number == SUtilConsts.NUM_SNG ? "Proveedor" : "Proveedores";
                break;
            case SModSysConsts.BPSS_CT_BP_DBR:
                name = number == SUtilConsts.NUM_SNG ? "Deudor diverso" : "Deudores diversos";
                break;
            case SModSysConsts.BPSS_CT_BP_CDR:
                name = number == SUtilConsts.NUM_SNG ? "Acreedor diverso" : "Acreedores diversos";
                break;
            default:
                name = number == SUtilConsts.NUM_SNG ? "Asociado de negocios" : "Asociados de negocios";
        }

        return name;
    }

    public static int[] getBizPartnerType(final SGuiSession session, final int idBizPartner, final int idBizPartnerCategory) {
        int[] type = null;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT fid_ct_bp, fid_tp_bp "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " "
                    + "WHERE id_bp = " + idBizPartner + " AND id_ct_bp = " + idBizPartnerCategory + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                type = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(SBpsUtils.class.getName(), e);
        }
        catch (Exception e) {
            SLibUtils.showException(SBpsUtils.class.getName(), e);
        }

        return type;
    }

    public static int getBizPartnerTypeIndex(final SGuiSession session, final int idBizPartner, final int idBizPartnerCategory) {
        int index = SLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT tp_idx "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " "
                    + "WHERE id_bp = " + idBizPartner + " AND id_ct_bp = " + idBizPartnerCategory + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                index = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(SBpsUtils.class.getName(), e);
        }
        catch (Exception e) {
            SLibUtils.showException(SBpsUtils.class.getName(), e);
        }

        return index;
    }

    public static int getHeadquartersId(SGuiSession session, int idBizPartner) {
        int idHeadquarters = SLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_bpb FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " "
                    + "WHERE fid_bp = " + idBizPartner + " AND fid_tp_bpb = " + SModSysConsts.BPSS_TP_BPB_HQ + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                idHeadquarters = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SBpsUtils.class.getName(), e);
        }

        return idHeadquarters;
    }
    
    public static String bankAccountNumberFormat(final String bankAccountNumber) {
        int index = 0;
        int len = 0;
        String bankAccountNumberFormat = "";
        ArrayList<String> abankAccountNumberFormat = new ArrayList<String>();
        
        index = bankAccountNumber.length();
        while (index > 0) {
            if (index > 4) {
                len = 4;
            }
            else {
                len = index;
            }
            abankAccountNumberFormat.add(bankAccountNumber.substring(index - len, index));
            index -= len;
        }
        
        for (int i = abankAccountNumberFormat.size() - 1; i >= 0; i--) {
            bankAccountNumberFormat += abankAccountNumberFormat.get(i) + " ";
        }
        
        return bankAccountNumberFormat;
    }
    
    public static String bankAccountClaBeFormat(final String bankAccountClaBe) {
        String bankAccountClaBeFormat = "";
        
        if (bankAccountClaBe.length() == 18) {
            bankAccountClaBeFormat = bankAccountClaBe.substring(0, 3) + " " + bankAccountClaBe.substring(3, 6) + " " + bankAccountClaBe.substring(6, 9) + " " +
                     bankAccountClaBe.substring(9, 13) + " " + bankAccountClaBe.substring(13, 17) + " " + bankAccountClaBe.substring(17);
        }
        else {
            bankAccountClaBeFormat = bankAccountClaBe.isEmpty() ? "" : "(ClaBE inválida)";
        }
        
        return bankAccountClaBeFormat;
    }
}
