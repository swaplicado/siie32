/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Sergio Flores, Claudio Peña
 */
public abstract class SDataReadComponentItems {

    private static java.lang.Object[] getSettingsCatCfg(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        int category = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.CFGS_CT_ENT:
                lenPk = 1;
                sql = "SELECT id_ct_ent AS f_id_1, ct_ent AS f_item FROM erp.cfgs_ct_ent WHERE b_del = 0 ORDER BY id_ct_ent ";
                text = "categoría de entidad";
                break;
            case SDataConstants.CFGX_CT_ENT_CFG:
                lenPk = 1;
                sql = "SELECT id_ct_ent AS f_id_1, ct_ent AS f_item FROM erp.cfgs_ct_ent " +
                        "WHERE b_del = 0 AND (id_ct_ent = " + SDataConstantsSys.CFGS_CT_ENT_WH + " OR id_ct_ent = " + SDataConstantsSys.CFGS_CT_ENT_PLANT + ") ORDER BY id_ct_ent ";
                text = "categoría de entidad";
                break;
            case SDataConstants.CFGS_TP_ENT:
                lenPk = 2;
                sql = "SELECT id_ct_ent AS f_id_1, id_tp_ent AS f_id_2, tp_ent AS f_item FROM erp.cfgs_tp_ent WHERE b_del = 0 " +
                        (pk == null ? "" : "AND id_ct_ent = " + ((int[]) pk)[0] + " ") + " ORDER by id_ct_ent, id_tp_ent ";
                text = "tipo de entidad";
                break;
            case SDataConstants.CFGS_TP_SORT:
                lenPk = 1;
                sql = "SELECT id_tp_sort AS f_id_1, tp_sort AS f_item FROM erp.cfgs_tp_sort WHERE b_del = 0 ORDER BY id_tp_sort ";
                text = "tipo de ordenamiento";
                break;
            case SDataConstants.CFGS_TP_BAL:
                lenPk = 1;
                sql = "SELECT id_tp_bal AS f_id_1, tp_bal AS f_item FROM erp.cfgs_tp_bal WHERE b_del = 0 ORDER BY id_tp_bal ";
                text = "tipo de saldo";
                break;
            case SDataConstants.CFGS_TP_REL:
                lenPk = 1;
                sql = "SELECT id_tp_rel AS f_id_1, tp_rel AS f_item FROM erp.cfgs_tp_rel WHERE b_del = 0 ORDER BY id_tp_rel ";
                text = "tipo de relación";
                break;
            case SDataConstants.CFGS_TP_FMT_D:
                lenPk = 1;
                sql = "SELECT id_tp_fmt_d AS f_id_1, tp_fmt_d AS f_item FROM erp.cfgs_tp_fmt_d WHERE b_del = 0 ORDER BY id_tp_fmt_d ";
                text = "tipo de formato fecha";
                break;
            case SDataConstants.CFGS_TP_FMT_DT:
                lenPk = 1;
                sql = "SELECT id_tp_fmt_dt AS f_id_1, tp_fmt_dt AS f_item FROM erp.cfgs_tp_fmt_dt WHERE b_del = 0 ORDER BY id_tp_fmt_dt ";
                text = "tipo de formato fecha-hora";
                break;
            case SDataConstants.CFGS_TP_FMT_T:
                lenPk = 1;
                sql = "SELECT id_tp_fmt_t AS f_id_1, tp_fmt_t AS f_item FROM erp.cfgs_tp_fmt_t WHERE b_del = 0 ORDER BY id_tp_fmt_t ";
                text = "tipo de formato hora";
                break;
            case SDataConstants.CFGS_TP_DBMS:
                lenPk = 1;
                sql = "SELECT id_tp_dbms AS f_id_1, tp_dbms AS f_item FROM erp.cfgs_tp_dbms WHERE b_del = 0 ORDER BY id_tp_dbms ";
                text = "tipo de DBMS";
                break;
            case SDataConstants.CFGS_TP_MOD:
                lenPk = 1;
                sql = "SELECT id_tp_mod AS f_id_1, tp_mod AS f_item FROM erp.cfgs_tp_mod WHERE b_del = 0 ORDER BY id_tp_mod ";
                text = "tipo de módulo";
                break;
            case SDataConstants.CFGU_CUR:
                lenPk = 1;
                sql = "SELECT id_cur AS f_id_1, cur AS f_item, cur_key AS f_comp FROM erp.cfgu_cur WHERE b_del = 0 ORDER BY cur, id_cur ";
                text = "moneda";
                isComplementApplying = true;
                break;
            case SDataConstants.CFGU_CO:
                lenPk = 1;
                sql = "SELECT id_co AS f_id_1, co AS f_item FROM erp.cfgu_co WHERE b_del = 0 ORDER BY co, id_co ";
                text = "empresa";
                break;
            case SDataConstants.CFGU_COB_ENT:
                lenPk = 2;
                lenFk = 1;
                
                if (pk != null && pk instanceof int[]) {
                    switch (((int[]) pk).length) {
                        case 1:
                            // entity category:
                            sql = "AND fid_ct_ent = " + ((int[]) pk)[0] + " ";
                            break;
                        case 2:
                            // filter company branch and entity category:
                            sql = "AND id_cob = " + ((int[]) pk)[0] + " AND fid_ct_ent = " + ((int[]) pk)[1] + " ";
                            break;
                        default:
                    }
                }
                
                sql = "SELECT id_cob AS f_id_1, id_ent AS f_id_2, ent AS f_item, id_cob AS f_fid_1 " +
                        "FROM erp.cfgu_cob_ent " +
                        "WHERE b_del = 0 " + sql +
                        "ORDER BY id_cob, ent, id_ent ";
                text = "entidad";
                break;
            case SDataConstants.CFGU_LAN:
                lenPk = 1;
                sql = "SELECT id_lan AS f_id_1, lan AS f_item FROM erp.cfgu_lan WHERE b_del = 0 ORDER BY lan, id_lan ";
                text = "idioma";
                break;
            case SDataConstants.CFGU_SHIFT:
                lenPk = 1;
                sql = "SELECT id_shift AS f_id_1, CONCAT(code, ' - ', name, ': ', time_sta, ' - ', time_end) AS f_item FROM cfgu_shift WHERE b_del = 0 ORDER BY code, id_shift ";
                text = "turno";
                break;
            case SDataConstants.CFGX_COB_ENT_CASH:
            case SDataConstants.CFGX_COB_ENT_WH:
            case SDataConstants.CFGX_COB_ENT_POS:
            case SDataConstants.CFGX_COB_ENT_PLANT:
                int[] an = null;
                boolean bool = false;

                lenPk = 2;

                switch (catalogue) {
                    case SDataConstants.CFGX_COB_ENT_CASH:
                        text = "cuenta de efectivo";
                        category = SDataConstantsSys.CFGS_CT_ENT_CASH;
                        break;
                    case SDataConstants.CFGX_COB_ENT_WH:
                        text = "almacén";
                        category = SDataConstantsSys.CFGS_CT_ENT_WH;
                        break;
                    case SDataConstants.CFGX_COB_ENT_POS:
                        text = "punto de venta";
                        category = SDataConstantsSys.CFGS_CT_ENT_POS;
                        break;
                    case SDataConstants.CFGX_COB_ENT_PLANT:
                        text = "planta";
                        category = SDataConstantsSys.CFGS_CT_ENT_PLANT;
                        break;
                    default:
                }

                sql = "SELECT e.id_cob AS f_id_1, e.id_ent AS f_id_2, CONCAT(e.ent, ' - ', e.code) AS f_item " +
                        "FROM erp.cfgu_cob_ent AS e INNER JOIN erp.bpsu_bpb AS b ON " +
                        "e.id_cob = b.id_bpb ";

                if (pk instanceof int[]) {
                    an = (int[]) pk;
                }
                else if (pk instanceof Object[]) {
                    if (((Object[]) pk)[0] instanceof Boolean) {
                        bool = (Boolean) ((Object[]) pk)[0];
                        sql += "AND e.id_cob = " + ((Object[]) pk)[1] + " ";
                        sql += "AND e.fid_ct_ent = " + SDataConstantsSys.CFGS_CT_ENT_WH + " AND e.fid_tp_ent IN(" + (String) ((Object[]) pk)[2] + ") ";
                    }
                    else {
                        an = (int[]) ((Object[]) pk)[0];
                        sql += "INNER JOIN fin_acc_cash AS ac ON " +
                                "e.id_cob = ac.id_cob AND e.id_ent = ac.id_acc_cash AND " +
                                "ac.fid_acc = '" + ((Object[]) pk)[1] + "' ";
                    }
                }

                if (!bool) {
                    switch (an.length) {
                        case 1:
                            sql += "AND e.id_cob = " + an[0] + " ";
                            break;
                        case 2:
                            sql += "AND e.fid_ct_ent = " + an[0] + " AND e.fid_tp_ent = " + an[1] + " ";
                            break;
                        case 3:
                            sql += "AND e.id_cob = " + an[0] + " AND e.fid_ct_ent = " + an[1] + " AND e.fid_tp_ent = " + an[2] + " ";
                            break;
                    }
                }

                sql += "WHERE e.b_del = 0 AND e.fid_ct_ent = " + category + " " +
                        "ORDER BY b.bpb, e.ent, e.id_cob, e.id_ent ";
                break;
            case SModConsts.CFGU_FUNC:
                /* Use of Object pk:
                 * pk[0] = Filter: Array integer; user ID. It's optional, if is null inner join statement is unnecesary.
                 */
                lenPk = 1;
                sql = "SELECT fa.id_func AS f_id_1, name AS f_item "
                        + "FROM cfgu_func AS fa "
                        + (pk == null ? "" : "INNER JOIN usr_usr_func AS fau ON fau.id_func = fa.id_func AND fau.id_usr = " + ((int[]) pk)[0] + " ")
                        + "WHERE b_del = 0 ORDER BY fa.name, fa.id_func ";
                text = "área funcional";
                break;

            default:
                break;
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatUsr(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.USRS_TP_LEV:
                lenPk = 1;
                sql = "SELECT id_tp_lev AS f_id_1, tp_lev AS f_item FROM erp.usrs_tp_lev WHERE b_del = 0 ORDER BY id_tp_lev ";
                text = "nivel de acceso";
                break;
            case SDataConstants.USRS_TP_PRV:
                lenPk = 1;
                sql = "SELECT id_tp_prv AS f_id_1, tp_prv AS f_item FROM erp.usrs_tp_prv WHERE b_del = 0 ORDER BY id_tp_prv ";
                text = "tipo de privilegio";
                break;
            case SDataConstants.USRS_TP_ROL:
            case SDataConstants.USRX_TP_ROL_ALL:
                lenPk = 1;
                sql = "SELECT id_tp_rol AS f_id_1, tp_rol AS f_item FROM erp.usrs_tp_rol WHERE b_del = 0 " +
                        (catalogue == SDataConstants.USRX_TP_ROL_ALL ? "" : "AND id_tp_rol < " + SDataConstantsSys.PRV_SPE + " ") +
                        "ORDER BY id_tp_rol ";
                text = "tipo de rol";
                break;
            case SDataConstants.USRS_PRV:
                lenPk = 1;
                sql = "SELECT id_prv AS f_id_1, prv AS f_item, CAST(b_tp_lev AS UNSIGNED INTEGER) AS f_comp FROM erp.usrs_prv " +
                        "WHERE b_del = 0 " + (pk == null ? "" : " AND fid_tp_prv = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY id_prv ";
                text = "privilegio";
                isComplementApplying = true;
                break;
            case SDataConstants.USRS_ROL:
                lenPk = 1;
                sql = "SELECT r.id_rol AS f_id_1, r.rol AS f_item, " +
                        "CAST((SELECT count(*) FROM erp.usrs_rol_prv AS rp, erp.usrs_prv AS p WHERE " +
                        "rp.id_rol = r.id_rol AND rp.id_prv = p.id_prv AND p.b_tp_lev = 1) >= 1 AS UNSIGNED INTEGER) AS f_comp " +
                        "FROM erp.usrs_rol AS r " +
                        "WHERE b_del = 0 " + (pk == null ? "" : " AND fid_tp_rol = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY r.id_rol ";
                text = "rol";
                isComplementApplying = true;
                break;
            case SDataConstants.USRU_USR:
                lenPk = 1;
                sql = "SELECT id_usr AS f_id_1, usr AS f_item FROM erp.usru_usr WHERE b_del = 0 ORDER BY usr, id_usr ";
                text = "usuario";
                break;
            default:
                break;
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatLoc(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.LOCU_CTY:
                lenPk = 1;
                sql = "SELECT id_cty AS f_id_1, " +
                        (!paramsErp.getIsKeyLocalityApplying() ? "cty" :
                            (paramsErp.getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(cty_key, ' - ', cty)" : "CONCAT(cty, ' - ', cty_key)")) +
                            " AS f_item " +
                        "FROM erp.locu_cty WHERE b_del = 0 " +
                        "ORDER BY " + (paramsErp.getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "cty_key, cty, " : "cty, cty_key, ") + "id_cty ";
                text = "país";
                break;
            case SDataConstants.LOCU_STA:
                lenPk = 1;
                sql = "SELECT id_sta AS f_id_1, " +
                        (!paramsErp.getIsKeyLocalityApplying() ? "sta" :
                            (paramsErp.getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(sta_code, ' - ', sta)" : "CONCAT(sta, ' - ', sta_code)")) +
                            " AS f_item " +
                        "FROM erp.locu_sta " +
                        "WHERE b_del = 0 " + (pk == null ? "" : " AND fid_cty = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY " + (paramsErp.getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "sta_code, sta, " : "sta, sta_code, ") + "id_sta ";
                text = "estado";
                break;
            default:
                break;
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, false };
    }

    private static java.lang.Object[] getSettingsCatBps(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        int category = 0;
        int sortingType = 0;
        boolean isKeyApplying = false;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";
        java.lang.String field = "";

        switch (catalogue) {
            case SDataConstants.BPSS_CT_BP:
                lenPk = 1;
                sql = "SELECT id_ct_bp AS f_id_1, ct_bp AS f_item FROM erp.bpss_ct_bp WHERE b_del = 0 AND id_ct_bp <> " + SDataConstantsSys.BPSS_CT_BP_CO + " " +
                        "ORDER BY id_ct_bp ";
                text = "categoría de AN";
                break;
            case SDataConstants.BPSS_TP_BP_IDY:
                lenPk = 1;
                sql = "SELECT id_tp_bp_idy AS f_id_1, tp_bp_idy AS f_item FROM erp.bpss_tp_bp_idy WHERE b_del = 0 ORDER BY id_tp_bp_idy ";
                text = "tipo de identidad de AN";
                break;
            case SDataConstants.BPSS_TP_BP_ATT:
                lenPk = 1;
                sql = "SELECT id_tp_bp_att AS f_id_1, tp_bp_att AS f_item FROM erp.bpss_tp_bp_att WHERE b_del = 0 ORDER BY id_tp_bp_att ";
                text = "tipo de atributo de AN";
                break;
            case SDataConstants.BPSS_TP_BPB:
                lenPk = 1;
                sql = "SELECT id_tp_bpb AS f_id_1, tp_bpb AS f_item FROM erp.bpss_tp_bpb WHERE b_del = 0 ORDER BY id_tp_bpb ";
                text = "tipo de sucursal";
                break;
            case SDataConstants.BPSS_TP_ADD:
                lenPk = 1;
                sql = "SELECT id_tp_add AS f_id_1, tp_add AS f_item FROM erp.bpss_tp_add WHERE b_del = 0 ORDER BY id_tp_add ";
                text = "tipo de domicilio";
                break;
            case SDataConstants.BPSS_TP_ADD_FMT:
                lenPk = 1;
                sql = "SELECT id_tp_add_fmt AS f_id_1, tp_add_fmt AS f_item FROM erp.bpss_tp_add_fmt WHERE b_del = 0 ORDER BY id_tp_add_fmt ";
                text = "tipo de formato de domicilio";
                break;
            case SDataConstants.BPSS_TP_CON:
                lenPk = 1;
                sql = "SELECT id_tp_con AS f_id_1, tp_con AS f_item FROM erp.bpss_tp_con WHERE b_del = 0 ORDER BY id_tp_con ";
                text = "tipo de contacto";
                break;
            case SDataConstants.BPSS_TP_TEL:
                lenPk = 1;
                sql = "SELECT id_tp_tel AS f_id_1, tp_tel AS f_item FROM erp.bpss_tp_tel WHERE b_del = 0 ORDER BY id_tp_tel ";
                text = "tipo de teléfono";
                break;
            case SDataConstants.BPSS_TP_CRED:
                lenPk = 1;
                sql = "SELECT id_tp_cred AS f_id_1, tp_cred AS f_item FROM erp.bpss_tp_cred WHERE b_del = 0 ORDER BY id_tp_cred ";
                text = "tipo de crédito";
                break;
            case SDataConstants.BPSS_TP_CFD_ADD:
                lenPk = 1;
                sql = "SELECT id_tp_cfd_add AS f_id_1, tp_cfd_add AS f_item FROM erp.bpss_tp_cfd_add WHERE b_del = 0 ORDER BY id_tp_cfd_add ";
                text = "tipo de addenda para CFD";
                break;
            case SDataConstants.BPSS_RISK:
                lenPk = 1;
                sql = "SELECT id_risk AS f_id_1, name AS f_item FROM erp.bpss_risk WHERE b_del = 0 ORDER BY id_risk ";
                text = "riesgo";
                break;
            case SDataConstants.BPSU_BP:
                lenPk = 1;
                sql = "SELECT id_bp AS f_id_1, bp AS f_item FROM erp.bpsu_bp WHERE b_del = 0 ORDER BY bp, id_bp ";
                text = "asociado de negocios";
                break;
            case SDataConstants.BPSX_BP_CO:
            case SDataConstants.BPSX_BP_SUP:
            case SDataConstants.BPSX_BP_CUS:
            case SDataConstants.BPSX_BP_CDR:
            case SDataConstants.BPSX_BP_DBR:
            case SDataConstants.BPSX_BP_EMP:
                lenPk = 1;

                switch (catalogue) {
                    case SDataConstants.BPSX_BP_CO:
                        text = "empresa";
                        category = SDataConstantsSys.BPSS_CT_BP_CO;
                        sortingType = paramsErp.getFkSortingSupplierTypeId();
                        isKeyApplying = paramsErp.getIsKeySupplierApplying();
                        break;
                    case SDataConstants.BPSX_BP_SUP:
                        text = "proveedor";
                        category = SDataConstantsSys.BPSS_CT_BP_SUP;
                        sortingType = paramsErp.getFkSortingSupplierTypeId();
                        isKeyApplying = paramsErp.getIsKeySupplierApplying();
                        break;
                    case SDataConstants.BPSX_BP_CUS:
                        text = "cliente";
                        category = SDataConstantsSys.BPSS_CT_BP_CUS;
                        sortingType = paramsErp.getFkSortingCustomerTypeId();
                        isKeyApplying = paramsErp.getIsKeyCustomerApplying();
                        break;
                    case SDataConstants.BPSX_BP_CDR:
                        text = "acreedor diverso";
                        category = SDataConstantsSys.BPSS_CT_BP_CDR;
                        sortingType = paramsErp.getFkSortingCreditorTypeId();
                        isKeyApplying = paramsErp.getIsKeyCreditorApplying();
                        break;
                    case SDataConstants.BPSX_BP_DBR:
                        text = "deudor diverso";
                        category = SDataConstantsSys.BPSS_CT_BP_DBR;
                        sortingType = paramsErp.getFkSortingDebtorTypeId();
                        isKeyApplying = paramsErp.getIsKeyDebtorApplying();
                        break;
                    case SDataConstants.BPSX_BP_EMP:
                        text = "empleado";
                        sortingType = paramsErp.getFkSortingEmployeeTypeId();
                        isKeyApplying = paramsErp.getIsKeyEmployeeApplying();
                        break;
                    default:
                }

                sql = "SELECT bp.id_bp AS f_id_1, " + (!isKeyApplying ? "bp.bp " :
                        (sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ||sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME_COMM ? "CONCAT(ct.bp_key, ' - ', bp.bp) " : "CONCAT(bp.bp, ' - ', ct.bp_key) ")) + "AS f_item " +
                        "FROM erp.bpsu_bp AS bp " +
                        (catalogue == SDataConstants.BPSX_BP_EMP ? "WHERE bp.b_del = 0 AND bp.b_att_emp = 1 " :
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                        "bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + category + " AND bp.b_del = 0 AND ct.b_del = 0 ") +
                        "ORDER BY " +
                        (catalogue == SDataConstants.BPSX_BP_EMP ? "bp.bp, " :
                        (sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME || sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME_COMM ? "ct.bp_key, bp.bp, " : "bp.bp, ct.bp_key, ")) + "bp.id_bp ";
                break;
            case SDataConstants.BPSX_BP_X_SUP_CUS:
            case SDataConstants.BPSX_BP_X_CDR_DBR:
                lenPk = 1;

                switch (catalogue) {
                    case SDataConstants.BPSX_BP_X_SUP_CUS:
                        field = "AND (bp.b_sup = 1 OR bp.b_cus = 1) ";
                        break;
                    case SDataConstants.BPSX_BP_X_CDR_DBR:
                        field = "AND (bp.b_cdr = 1 OR bp.b_dbr = 1) ";
                        break;
                    default:
                }

                sql = "SELECT bp.id_bp AS f_id_1, bp.bp AS f_item " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                        "bp.id_bp = ct.id_bp AND ct.id_ct_bp IN(" + (catalogue == SDataConstants.BPSX_BP_X_SUP_CUS ? SDataConstantsSys.BPSS_CT_BP_SUP + ", " + SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_CDR + ", " + SDataConstantsSys.BPSS_CT_BP_DBR) + ") " +
                        "WHERE bp.b_del = 0 AND ct.b_del = 0 " + field +
                        "ORDER BY bp.bp, bp.id_bp ";
                text = "asociado de negocios";
                break;
            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                lenPk = 1;
                sql = "SELECT DISTINCT id_bp AS f_id_1, bp AS f_item " +
                        "FROM erp.bpsu_bp " +
                        "WHERE b_del = 0 AND b_att_sal_agt = 1 " +
                        "ORDER BY bp, id_bp ";
                text = "agente de ventas";
                break;
            case SDataConstants.BPSX_BP_ATT_EMP_MFG:
                lenPk = 1;
                sql = "SELECT DISTINCT bp.id_bp AS f_id_1, bp.bp AS f_item " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND bp.b_del = 0 " +
                        "INNER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp AND e.b_mfg_ope = 1 " +
                        "ORDER BY bp.bp, bp.id_bp ";
                text = "operador";
                break;
            case SDataConstants.BPSU_BPB:
            case SDataConstants.BPSX_BPB_EMP:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_bpb AS f_id_1, " + (catalogue == SDataConstants.BPSU_BPB ? "bpb" : "CONCAT(bpb, ' - ', code)") + " AS f_item, fid_bp AS f_fid_1 " +
                        "FROM erp.bpsu_bpb " +
                        "WHERE b_del = 0 " +
                        (pk == null ? "" : "AND fid_bp = " + ((int[]) pk)[0] + " " ) +
                        "ORDER BY fid_tp_bpb, bpb, id_bpb ";
                text = "sucursal";
                break;
            case SDataConstants.BPSU_BPB_ADD:
                lenPk = 2;
                lenFk = 1;
                isComplementApplying = true;
                sql = "SELECT a.id_bpb AS f_id_1, a.id_add AS f_id_2, " +
                        "CONCAT(a.bpb_add, IF(a.b_def, ' (P)', ''), ' - ', a.street, ' ', RTRIM(CONCAT(a.street_num_ext, ' ', a.street_num_int)), '; CP ', a.zip_code, '; ', a.locality, ', ', a.state) AS f_item, " +
                        "a.id_bpb AS f_fid_1, a.b_def AS f_comp " +
                        "FROM erp.bpsu_bpb_add AS a " +
                        "WHERE a.b_del = 0 " + (pk == null ? "" : "AND a.id_bpb = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY a.fid_tp_add, a.bpb_add, a.street, a.street_num_ext, a.street_num_int, a.id_bpb, a.id_add ";
                text = "domicilio";
                break;
            case SDataConstants.BPSU_BPB_CON:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_bpb AS f_id_1, id_con AS f_id_2, bpb_con AS f_item, fid_tp_con AS f_fid_1 " +
                        "FROM erp.bpsu_bpb_con " +
                        "WHERE b_del = 0 " +
                        (pk == null ? "" : "AND id_bpb = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY id_bpb, bpb_con, id_con ";
                text = "contacto";
                break;
            case SDataConstants.BPSU_BANK_ACC:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT a.id_bpb AS f_id_1, a.id_bank_acc AS f_id_2, CONCAT(a.bank_acc, ' (', b.bp_comm, ')') AS f_item, " +
                        "a.id_bpb AS f_fid_1 " +
                        "FROM erp.bpsu_bank_acc AS a " +
                        "INNER JOIN erp.bpsu_bp AS b ON a.fid_bank = b.id_bp " +
                        "WHERE a.b_del = 0 " +
                        (pk == null ? "" : "AND a.id_bpb = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY a.id_bpb, a.bank_acc, a.id_bank_acc ";
                text = "cuenta bancaria";
                break;
            case SDataConstants.BPSU_BANK_ACC_CARD:
                lenPk = 3;
                lenFk = 2;
                sql = "SELECT id_bpb AS f_id_1, id_bank_acc AS f_id_2, id_card AS f_id_3, CONCAT(card_num, ' - ', holder) AS f_item, id_bpb AS f_fid_1, id_bank_acc AS f_fid_2 " +
                        "FROM erp.bpsu_bank_acc_card " +
                        "WHERE b_del = 0 " +
                        "ORDER BY id_bpb, id_bank_acc, card_num, holder, id_card ";
                text = "tarjeta de cuenta bancaria";
                break;
            case SDataConstants.BPSU_TP_BP:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_ct_bp AS f_id_1, id_tp_bp AS f_id_2, tp_bp AS f_item, id_ct_bp AS f_fid_1 " +
                        "FROM erp.bpsu_tp_bp " +
                        "WHERE b_del = 0 " +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                text = "tipo de asociados de negocios";
                break;
            case SDataConstants.BPSX_TP_BP_CUS:
                lenPk = 2;
                sql = "SELECT id_ct_bp AS f_id_1, id_tp_bp AS f_id_2, tp_bp AS f_item FROM erp.bpsu_tp_bp " +
                        "WHERE id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " AND b_del = 0 " +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                text = "tipo de cliente";
                break;
            case SDataConstants.BPSX_TP_BP_SUP:
                lenPk = 2;
                sql = "SELECT id_ct_bp AS f_id_1, id_tp_bp AS f_id_2, tp_bp AS f_item FROM erp.bpsu_tp_bp " +
                        "WHERE id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " AND b_del = 0 " +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                text = "tipo de proveedor";
                break;
            case SDataConstants.BPSX_TP_BP_DBR:
                lenPk = 2;
                sql = "SELECT id_ct_bp AS f_id_1, id_tp_bp AS f_id_2, tp_bp AS f_item FROM erp.bpsu_tp_bp " +
                        "WHERE id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_DBR + " AND b_del = 0 " +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                text = "tipo de deudor diverso";
                break;
            case SDataConstants.BPSX_TP_BP_CDR:
                lenPk = 2;
                sql = "SELECT id_ct_bp AS f_id_1, id_tp_bp AS f_id_2, tp_bp AS f_item FROM erp.bpsu_tp_bp " +
                        "WHERE id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CDR + " AND b_del = 0 " +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                text = "tipo de acreedor diverso";
                break;
            case SDataConstants.BPSU_BA:
                lenPk = 1;
                sql = "SELECT id_ba AS f_id_1, ba AS f_item FROM erp.bpsu_ba WHERE b_del = 0 ORDER BY ba, id_ba ";
                text = "área de negocios";
                break;
            case SDataConstants.BPSX_BP_CT:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT c.id_bp AS f_id_1, c.id_ct_bp AS f_id_2, b.bp AS f_item, c.id_ct_bp as f_fid_1 " +
                        "FROM erp.bpsu_bp AS b " +
                        "INNER JOIN erp.bpsu_bp_ct AS c ON " +
                        "b.id_bp = c.id_bp " +
                        "WHERE b.b_del = 0 " +
                        "ORDER BY c.id_ct_bp, b.bp, b.id_bp ";
                text = "asociado de negocios";
                break;
            case SDataConstants.BPSX_BP_ATT_BANK:
                lenPk = 1;
                sql = "SELECT b.id_bp AS f_id_1, b.bp AS f_item " +
                        "FROM erp.bpsu_bp AS b " +
                        "WHERE b.b_del = 0 AND b.b_att_bank = 1 " +
                        "ORDER BY b.bp, b.id_bp ";
                text = "banco";
                break;
            case SDataConstants.BPSX_BP_ATT_CARR:
                lenPk = 1;
                sql = "SELECT b.id_bp AS f_id_1, b.bp AS f_item " +
                        "FROM erp.bpsu_bp AS b " +
                        "WHERE b.b_del = 0 AND b.b_att_car = 1 " +
                        "ORDER BY b.bp, b.id_bp ";
                text = "transportista";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatItm(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.ITMS_CT_ITEM:
                lenPk = 1;
                lenFk = 0;
                isComplementApplying = true;
                sql = "SELECT id_ct_item AS f_id_1, ct_item AS f_item, " +
                        "ct_idx AS f_comp " +
                        "FROM erp.itms_ct_item WHERE b_del = 0 ORDER BY id_ct_item ";
                text = "categoría de ítem";
                break;
            case SDataConstants.ITMS_CL_ITEM:
                lenPk = 2;
                lenFk = 1;
                isComplementApplying = true;
                sql = "SELECT id_ct_item AS f_id_1, id_cl_item AS f_id_2, cl_item AS f_item, " +
                        "id_ct_item AS f_fid_1, cl_idx AS f_comp " +
                        "FROM erp.itms_cl_item WHERE b_del = 0 ORDER BY id_ct_item, id_cl_item ";
                text = "clase de ítem";
                break;
            case SDataConstants.ITMS_TP_ITEM:
                lenPk = 3;
                lenFk = 2;
                isComplementApplying = true;
                sql = "SELECT id_ct_item AS f_id_1, id_cl_item AS f_id_2, id_tp_item AS f_id_3, tp_item AS f_item, " +
                        "id_ct_item AS f_fid_1, id_cl_item AS f_fid_2, tp_idx AS f_comp " +
                        "FROM erp.itms_tp_item WHERE b_del = 0 ORDER BY id_ct_item, id_cl_item, id_tp_item ";
                text = "tipo de ítem";
                break;
            case SDataConstants.ITMS_TP_SNR:
                lenPk = 1;
                sql = "SELECT id_tp_snr AS f_id_1, tp_snr AS f_item FROM erp.itms_tp_snr WHERE b_del = 0 ORDER BY id_tp_snr ";
                text = "tipo de número de serie";
                break;
            case SDataConstants.ITMS_ST_ITEM:
                lenPk = 1;
                sql = "SELECT id_st_item AS f_id_1, name AS f_item FROM erp.itms_st_item WHERE b_del = 0 ORDER BY sort ";
                text = "estatus de ítem";
                break;
            case SDataConstants.ITMU_IFAM:
                lenPk = 1;
                sql = "SELECT id_ifam AS f_id_1, ifam AS f_item FROM erp.itmu_ifam WHERE b_del = 0 ORDER BY ifam, id_ifam ";
                text = "familia de ítems";
                break;
            case SDataConstants.ITMU_IGRP:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_igrp AS f_id_1, igrp AS f_item, fid_ifam AS f_fid_1 FROM erp.itmu_igrp WHERE b_del = 0 " +
                        "ORDER BY igrp, id_igrp ";
                text = "grupo de ítems";
                break;
            case SDataConstants.ITMU_IGEN:
            case SDataConstants.ITMX_IGEN_LINE:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_igen AS f_id_1, CONCAT(igen, ' (', code, ')') AS f_item, fid_igrp AS f_fid_1 FROM erp.itmu_igen " +
                        "WHERE b_del = 0 " + (catalogue != SDataConstants.ITMX_IGEN_LINE ? "" : "AND b_line = 1 ") +
                        (pk == null ? "" :
                        ((int[]) pk).length == 1 ? "AND fid_ct_item = " + ((int[]) pk)[0] :
                        "AND fid_ct_item = " + ((int[]) pk)[0] + " AND (fid_cl_item = " + ((int[]) pk)[1]  +
                        (SLibUtilities.compareKeys(pk, SDataConstantsSys.ITMS_CL_ITEM_EXP_MFG) ? " OR fid_cl_item = " +
                        SDataConstantsSys.ITMS_CL_ITEM_EXP_OPE[1] : "") + ")") + " " +
                        "ORDER BY igen, id_igen ";
                text = "ítem genérico";
                break;
            case SDataConstants.ITMU_LINE:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_line AS f_id_1, CONCAT(line, ' (', code, ')') AS f_item, fid_igen AS f_fid_1 FROM erp.itmu_line " +
                        "WHERE b_del = 0 " + (pk == null ? "": " AND fid_igen = " + ((int[]) pk)[0] +  " ") +
                        "ORDER BY line, id_line ";
                text = "línea de ítems";
                break;
            case SDataConstants.ITMU_ITEM:
            case SDataConstants.ITMX_ITEM_BOM_ITEM:
            case SDataConstants.ITMX_ITEM_BOM_LEVEL:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT i.id_item AS f_id_1, CONCAT(" + (!paramsErp.getIsItemKeyApplying() ? "i.item " :
                        (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "CONCAT(i.item_key, ' - ', i.item) " : "CONCAT(i.item, ' - ', i.item_key) ")) + ", CASE WHEN i.fid_st_item = " + SModSysConsts.ITMS_ST_ITEM_RES + " THEN ' (!)' WHEN i.fid_st_item = " + SModSysConsts.ITMS_ST_ITEM_INA + " THEN ' (/)' ELSE '' END) AS f_item, " +
                        "i.fid_igen AS f_fid_1, u.symbol AS f_comp " +
                        "FROM erp.itmu_item AS i " +
                        "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " + (pk == null ? "" : "AND ig.fid_ct_item = " + ((int[]) pk)[0]) + " " +
                        "WHERE i.b_del = 0 ";

                switch (catalogue) {
                    case SDataConstants.ITMX_ITEM_BOM_ITEM:
                        sql += "AND ((" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) ";
                        break;
                    case SDataConstants.ITMX_ITEM_BOM_LEVEL:
                        sql += "AND ((" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) ";
                        break;
                    default:
                }

                if (pk != null) {
                    switch (((int[]) pk).length) {
                        case 1:
                            sql += "AND ig.fid_ct_item = " + ((int[]) pk)[0] + " ";
                            break;
                        case 2:
                            sql += "AND ig.fid_ct_item = " + ((int[]) pk)[0] + " ";
                            sql += "AND ig.fid_cl_item = " + ((int[]) pk)[1] + " ";
                            break;
                        case 3:
                            sql += "AND ig.fid_ct_item = " + ((int[]) pk)[0] + " ";
                            sql += "AND ig.fid_cl_item = " + ((int[]) pk)[1] + " ";
                            sql += "AND ig.fid_tp_item = " + ((int[]) pk)[2] + " ";
                            break;
                        default:
                    }
                }

                sql += "ORDER BY " + (!paramsErp.getIsItemKeyApplying() ? "i.item, " :
                    paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
                text = "ítem";
                isComplementApplying = true;
                break;
            case SDataConstants.ITMX_ITEM_IOG:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT item.id_item AS f_id_1, " + (!paramsErp.getIsItemKeyApplying() ? "item" :
                    (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item_key, ' - ', item)" :
                        "CONCAT(item, ' - ', item_key)")) + " AS f_item, fid_igen AS f_fid_1 " +
                    "FROM erp.itmu_item AS item " +
                    "WHERE item.b_del = 0 " +
                    ((pk == null || pk instanceof int[]) ? "" : (((Boolean) ((Object[]) pk)[0] ? " AND EXISTS(SELECT * FROM erp.itmu_item_pack AS item_pack WHERE item_pack.id_item_pack = item.id_item)":"" ) + " ")) +
                    ((pk == null || pk instanceof int[]) ? "" : (((Boolean) ((Object[]) pk)[1] ? " AND item.b_inv = 1 ":"" ) + " ")) +
                    "ORDER BY " + (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") + " id_item ";
                text = "ítem";
                break;
            /* XXX 2011-09
            case SDataConstants.ITMX_ITEM_IOG_GOOD_CONS:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT item.id_item AS f_id_1, " + (!paramsErp.getIsItemKeyApplying() ? "item" :
                    (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item_key, ' - ', item)" :
                        "CONCAT(item, ' - ', item_key)")) + " AS f_item, fid_igen AS f_fid_1 " +
                    "FROM erp.itmu_item AS item " +
                    "INNER JOIN erp.itmu_igen AS ig ON " +
                    "item.fid_igen = ig.id_igen " +
                    "WHERE item.b_del = 0 AND ig.fid_cl_item = " + SDataConstantsSys.ITMS_CL_ITEM_GOOD_CONS + " " +
                    (pk == null ? "" : (((Boolean) pk ? " AND EXISTS(SELECT * FROM erp.itmu_item_pack AS item_pack WHERE item_pack.id_item_pack = item.id_item)":"" ) + " ")) +
                    "ORDER BY " + (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") + " id_item ";
                text = "ítem";
                break;
            case SDataConstants.ITMX_ITEM_IOG_GOOD_WORK_F:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT item.id_item AS f_id_1, " + (!paramsErp.getIsItemKeyApplying() ? "item" :
                    (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item_key, ' - ', item)" :
                        "CONCAT(item, ' - ', item_key)")) + " AS f_item, fid_igen AS f_fid_1 " +
                    "FROM erp.itmu_item AS item " +
                    "INNER JOIN erp.itmu_igen AS ig ON " +
                    "item.fid_igen = ig.id_igen " +
                    "WHERE item.b_del = 0 AND ig.fid_cl_item = " + SDataConstantsSys.ITMS_CL_ITEM_GOOD_WORK_F + " " +
                    (pk == null ? "" : (((Boolean) pk ? " AND EXISTS(SELECT * FROM erp.itmu_item_pack AS item_pack WHERE item_pack.id_item_pack = item.id_item)":"" ) + " ")) +
                    "ORDER BY " + (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") + " id_item ";
                text = "ítem";
                break;
            */
            case SDataConstants.ITMU_ITEM_BARC:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_item AS f_id_1, id_barc AS f_id_2, barc AS f_item , id_item AS f_fid_1 FROM erp.itmu_item_barc " +
                        "WHERE b_del = 0 " + (pk == null ? "" : "AND id_item = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY id_item, barc, id_barc ";
                text = "ítem";
                break;
            /* XXX 2011-09
            case SDataConstants.ITMU_ITEM_PACK:
                lenPk = 1;
                sql = "SELECT ipack.id_item AS f_id_1, " + (!paramsErp.getIsItemKeyApplying() ? "i.item " :
                        (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(i.item_key, ' - ', i.item)" :
                            "CONCAT(i.item, ' - ', i.item_key)")) + " AS f_item " +
                        "FROM erp.itmu_item_pack AS ipack " +
                        "INNER JOIN erp.itmu_item AS i ON " +
                        "ipack.id_item = i.id_item " +
                        (pk == null ? "": "WHERE id_item_pack = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY " + (!paramsErp.getIsItemKeyApplying() ? "i.item, " :
                            paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " :
                                "i.item, ipack.item_key, ") + " i.id_item ";
                text = "ítem";
                break;
            */
            case SDataConstants.ITMU_TP_UNIT:
                lenPk = 1;
                sql = "SELECT id_tp_unit AS f_id_1, CONCAT(tp_unit, ' (', unit_base, ')') AS f_item " +
                        "FROM erp.itmu_tp_unit WHERE b_del = 0 " +
                        "ORDER BY tp_unit, id_tp_unit ";
                text = "tipo de unidad";
                break;
            case SDataConstants.ITMU_TP_LEV:
                lenPk = 1;
                sql = "SELECT id_tp_lev AS f_id_1, tp_lev AS f_item " +
                        "FROM erp.itmu_tp_lev WHERE b_del = 0 " +
                        "ORDER BY tp_lev, id_tp_lev ";
                text = "tipo de nivel";
                break;
            case SDataConstants.ITMU_UNIT:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_unit AS f_id_1, CONCAT(unit, ' (', symbol, ')') AS f_item, " +
                        "fid_tp_unit AS f_fid_1, symbol as f_comp " +
                        "FROM erp.itmu_unit " +
                        "WHERE b_del = 0 " + (pk == null ? "" : pk.getClass() == int[].class ? "AND fid_tp_unit = " + ((int[]) pk)[0] + " " :
                            "AND (fid_tp_unit = " + (Integer) ((Object[]) pk)[0] + " OR fid_tp_unit = " + (Integer) ((Object[]) pk)[1] + ") ") +
                        "ORDER BY fid_tp_unit, sort_pos, unit, id_unit ";
                text = "unidad";
                isComplementApplying = true;
                break;
            case SDataConstants.ITMU_TP_VAR:
                lenPk = 1;
                sql = "SELECT id_tp_var AS f_id_1, tp_var AS f_item FROM erp.itmu_tp_var WHERE b_del = 0 ORDER BY tp_var, id_tp_var ";
                text = "tipo de variedad";
                break;
            case SDataConstants.ITMU_TP_BRD:
                lenPk = 1;
                sql = "SELECT id_tp_brd AS f_id_1, tp_brd AS f_item FROM erp.itmu_tp_brd WHERE b_del = 0 ORDER BY tp_brd, id_tp_brd ";
                text = "tipo de marca";
                break;
            case SDataConstants.ITMU_TP_EMT:
                lenPk = 1;
                sql = "SELECT id_tp_emt AS f_id_1, tp_emt AS f_item FROM erp.itmu_tp_emt WHERE b_del = 0 ORDER BY tp_emt, id_tp_emt ";
                text = "tipo de elemento";
                break;
            case SDataConstants.ITMU_TP_MFR:
                lenPk = 1;
                sql = "SELECT id_tp_mfr AS f_id_1, tp_mfr AS f_item FROM erp.itmu_tp_mfr WHERE b_del = 0 ORDER BY tp_mfr, id_tp_mfr ";
                text = "tipo de fabricante";
                break;
            case SDataConstants.ITMU_VAR:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT v.id_var AS f_id_1, t.id_tp_var, t.tp_var, CONCAT(v.var, ' (', v.code, ')') AS f_item, v.fid_tp_var AS f_fid_1 " +
                        "FROM erp.itmu_var AS v INNER JOIN erp.itmu_tp_var AS t ON v.fid_tp_var = t.id_tp_var " +
                        "WHERE v.b_del = 0 " + (pk == null ? "" : "AND v.fid_tp_var = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY t.tp_var, t.id_tp_var, v.var, v.id_var ";
                text = "variedad";
                break;
            case SDataConstants.ITMU_BRD:
                lenPk = 1;
                sql = "SELECT id_brd AS f_id_1, CONCAT(brd, ' (', code, ')') AS f_item FROM erp.itmu_brd WHERE b_del = 0 ORDER BY fid_tp_brd, brd, id_brd ";
                text = "marca";
                break;
            case SDataConstants.ITMU_MFR:
                lenPk = 1;
                sql = "SELECT id_mfr AS f_id_1, CONCAT(mfr, ' (', code, ')') AS f_item FROM erp.itmu_mfr WHERE b_del = 0 ORDER BY fid_tp_mfr, mfr, id_mfr ";
                text = "fabricante";
                break;
            case SDataConstants.ITMU_EMT:
                lenPk = 1;
                sql = "SELECT id_emt AS f_id_1, CONCAT(emt, ' (', code, ')') AS f_item FROM erp.itmu_emt WHERE b_del = 0 ORDER BY fid_tp_emt, emt, id_emt ";
                text = "elemento";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatFin(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.FINS_TP_TAX:
                lenPk = 1;
                sql = "SELECT id_tp_tax AS f_id_1, tp_tax AS f_item FROM erp.fins_tp_tax WHERE b_del = 0 ORDER BY id_tp_tax ";
                text = "tipo de impuesto";
                break;
            case SDataConstants.FINS_TP_TAX_CAL:
                lenPk = 1;
                    sql = "SELECT id_tp_tax_cal AS f_id_1, tp_tax_cal AS f_item FROM erp.fins_tp_tax_cal WHERE b_del = 0 ORDER BY id_tp_tax_cal ";
                text = "tipo de cálculo de impuesto";
                break;
            case SDataConstants.FINS_TP_TAX_APP:
                lenPk = 1;
                sql = "SELECT id_tp_tax_app AS f_id_1, tp_tax_app AS f_item FROM erp.fins_tp_tax_app WHERE b_del = 0 ORDER BY id_tp_tax_app ";
                text = "tipo de aplicación de impuesto";
                break;
            case SDataConstants.FINS_TP_BKR:
                lenPk = 1;
                sql = "SELECT id_tp_bkr AS f_id_1, tp_bkr AS f_item FROM erp.fins_tp_bkr WHERE b_del = 0 ORDER BY id_tp_bkr ";
                text = "tipo de asiento contable";
                break;
            case SDataConstants.FINS_TP_ACC_MOV:
                lenPk = 1;
                sql = "SELECT id_tp_acc_mov AS f_id_1, tp_acc_mov AS f_item " +
                        "FROM erp.fins_tp_acc_mov " +
                        "WHERE b_del = 0 AND id_tp_acc_mov <> " +  SDataConstantsSys.FINS_TP_ACC_MOV_NA + " ORDER BY id_tp_acc_mov ";
                text = "tipo de movimiento contable";
                break;
            case SDataConstants.FINS_CL_ACC_MOV:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_tp_acc_mov AS f_id_1, id_cl_acc_mov AS f_id_2, cl_acc_mov AS f_item, id_tp_acc_mov as f_fid_1 " +
                        "FROM erp.fins_cl_acc_mov WHERE b_del = 0 AND id_tp_acc_mov <> " +  SDataConstantsSys.FINS_TP_ACC_MOV_NA + " " +
                        "ORDER BY id_tp_acc_mov, id_cl_acc_mov ";
                text = "clase de movimiento contable";
                break;
            case SDataConstants.FINS_CLS_ACC_MOV:
                lenPk = 3;
                lenFk = 2;
                sql = "SELECT id_tp_acc_mov AS f_id_1, id_cl_acc_mov AS f_id_2, id_cls_acc_mov AS f_id_3, cls_acc_mov AS f_item, id_tp_acc_mov as f_fid_1, id_cl_acc_mov as f_fid_2 " +
                        "FROM erp.fins_cls_acc_mov WHERE b_del = 0 AND id_tp_acc_mov <> " +  SDataConstantsSys.FINS_TP_ACC_MOV_NA + " " +
                        "ORDER BY id_tp_acc_mov, id_cl_acc_mov, id_cls_acc_mov ";
                text = "subclase de movimiento contable";
                break;
            case SDataConstants.FINS_TP_ACC:
                lenPk = 1;
                sql = "SELECT id_tp_acc AS f_id_1, tp_acc AS f_item " +
                        "FROM erp.fins_tp_acc WHERE b_del = 0 AND id_tp_acc <> " +  SDataConstantsSys.FINS_TP_ACC_NA + " " +
                        "ORDER BY id_tp_acc ";
                text = "tipo de cuenta contable";
                break;
            case SDataConstants.FINS_CL_ACC:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_tp_acc AS f_id_1, id_cl_acc AS f_id_2, cl_acc AS f_item, id_tp_acc AS f_fid_1 " +
                        "FROM erp.fins_cl_acc WHERE b_del = 0 AND id_tp_acc <> " +  SDataConstantsSys.FINS_TP_ACC_NA + " " +
                        "ORDER BY id_tp_acc, id_cl_acc ";
                text = "clase de cuenta contable";
                break;
            case SDataConstants.FINS_CLS_ACC:
                lenPk = 3;
                lenFk = 2;
                sql = "SELECT id_tp_acc AS f_id_1, id_cl_acc AS f_id_2, id_cls_acc AS f_id_3, cls_acc AS f_item, id_tp_acc AS f_fid_1, id_cl_acc as f_fid_2 " +
                        "FROM erp.fins_cls_acc WHERE b_del = 0 AND id_tp_acc <> " +  SDataConstantsSys.FINS_TP_ACC_NA + " " +
                        "ORDER BY id_tp_acc, id_cl_acc, id_cls_acc ";
                text = "subclase de cuenta contable";
                break;
            case SDataConstants.FINS_TP_ACC_SPE:
                lenPk = 1;
                sql = "SELECT id_tp_acc_spe AS f_id_1, name AS f_item FROM erp.fins_tp_acc_spe WHERE b_del = 0 " +
                        "ORDER BY sort ";
                text = "tipo de cuenta especializada";
                break;
            case SDataConstants.FINS_TP_ACC_SYS:
                lenPk = 1;
                sql = "SELECT id_tp_acc_sys AS f_id_1, tp_acc_sys AS f_item, deep AS f_comp FROM erp.fins_tp_acc_sys WHERE b_del = 0 " +
                        "ORDER BY id_tp_acc_sys ";
                text = "tipo de cuenta contable de sistema";
                isComplementApplying = true;
                break;
            case SDataConstants.FINS_CT_ACC_CASH:
                lenPk = 1;
                sql = "SELECT id_ct_acc_cash AS f_id_1, ct_acc_cash AS f_item FROM erp.fins_ct_acc_cash WHERE b_del = 0 " +
                        "ORDER BY id_ct_acc_cash ";
                text = "categoría de cuenta de efectivo";
                break;
            case SDataConstants.FINS_TP_ACC_CASH:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_ct_acc_cash AS f_id_1, id_tp_acc_cash AS f_id_2, tp_acc_cash AS f_item, id_ct_acc_cash AS f_fid_1 " +
                        "FROM erp.fins_tp_acc_cash WHERE b_del = 0 " + (pk == null ? "" : "AND id_ct_acc_cash = " + ((int[]) pk)[0] +  " ") + " " +
                        "ORDER BY id_ct_acc_cash, id_tp_acc_cash ";
                text = "tipo de cuenta de efectivo";
                break;
            case SDataConstants.FINS_TP_ACC_BP:
                lenPk = 1;
                sql = "SELECT id_tp_acc_bp AS f_id_1, tp_acc_bp AS f_item FROM erp.fins_tp_acc_bp WHERE b_del = 0 ORDER BY id_tp_acc_bp ";
                text = "tipo de cuenta contable para AN";
                break;
            case SDataConstants.FINS_TP_ACC_ITEM:
                lenPk = 1;
                sql = "SELECT id_tp_acc_item AS f_id_1, tp_acc_item AS f_item FROM erp.fins_tp_acc_item WHERE b_del = 0 ORDER BY id_tp_acc_item ";
                text = "tipo de cuenta contable para ítems";
                break;
            case SDataConstants.FINS_TP_CARD:
                lenPk = 1;
                sql = "SELECT id_tp_card AS f_id_1, tp_card AS f_item FROM erp.fins_tp_card WHERE b_del = 0 ORDER BY id_tp_card ";
                text = "tipo de tarjeta";
                break;
            case SDataConstants.FINS_TP_PAY_BANK:
                lenPk = 1;
                sql = "SELECT id_tp_pay_bank AS f_id_1, tp_pay_bank AS f_item FROM erp.fins_tp_pay_bank WHERE b_del = 0 ORDER BY id_tp_pay_bank ";
                text = "tipo de pago";
                break;
            case SDataConstants.FINS_ST_FIN_MOV:
                lenPk = 1;
                sql = "SELECT id_st_fin_mov AS f_id_1, st_fin_mov AS f_item FROM erp.fins_st_fin_mov WHERE b_del = 0 ORDER BY id_st_fin_mov ";
                text = "status de movimiento financiero";
                break;
            case SDataConstants.FINS_FISCAL_ACC:
                lenPk = 1;
                sql = "SELECT id_fiscal_acc AS f_id_1, CONCAT(code, ' - ', name) AS f_item FROM erp.fins_fiscal_acc WHERE b_del = 0 ORDER BY code, name, id_fiscal_acc";
                text = "código agrupador SAT";
                break;
            case SDataConstants.FINS_FISCAL_CUR:
                lenPk = 1;
                sql = "SELECT id_fiscal_cur AS f_id_1, CONCAT(code, ' - ', name) AS f_item FROM erp.fins_fiscal_cur WHERE b_del = 0 ORDER BY code, name, id_fiscal_cur ";
                text = "moneda SAT";
                break;
            case SDataConstants.FINS_FISCAL_BANK:
                lenPk = 1;
                sql = "SELECT id_fiscal_bank AS f_id_1, CONCAT(code, ' - ', name) AS f_item FROM erp.fins_fiscal_bank WHERE b_del = 0 ORDER BY code, name, id_fiscal_bank ";
                text = "banco SAT";
                break;
            case SDataConstants.FINS_FISCAL_PAY_MET:
                lenPk = 1;
                sql = "SELECT id_fiscal_pay_met AS f_id_1, CONCAT(code, ' - ', name) AS f_item FROM erp.fins_fiscal_pay_met WHERE b_del = 0 ORDER BY code, name, id_fiscal_pay_met ";
                text = "método pago SAT";
                break;
            case SDataConstants.FINU_TAX_REG:
                lenPk = 1;
                sql = "SELECT id_tax_reg AS f_id_1, tax_reg AS f_item FROM erp.finu_tax_reg WHERE b_del = 0 ORDER BY tax_reg, id_tax_reg ";
                text = "región de impuesto";
                break;
            case SDataConstants.FINU_TAX_IDY:
                lenPk = 1;
                sql = "SELECT id_tax_idy AS f_id_1, tax_idy AS f_item, fid_tp_bp_idy AS f_fid_1 FROM erp.finu_tax_idy WHERE b_del = 0 " + (pk == null ? "" : "AND fid_tp_bp_idy = " + ((int[]) pk)[0] +  " ") + " ORDER BY tax_idy, id_tax_idy ";
                text = "identidad de impuesto";
                break;
            case SDataConstants.FINU_TAX_BAS:
                lenPk = 1;
                sql = "SELECT id_tax_bas AS f_id_1, tax_bas AS f_item FROM erp.finu_tax_bas WHERE b_del = 0 ORDER BY tax_bas, id_tax_bas ";
                text = "impuesto básico";
                break;
            case SDataConstants.FINU_TAX:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_tax_bas AS f_id_1, id_tax AS f_id_2, tax AS f_item, id_tax_bas AS f_fid_1 FROM erp.finu_tax WHERE b_del = 0 ORDER BY tax, id_tax_bas, id_tax ";
                text = "impuesto";
                break;
            case SDataConstants.FINU_CARD_ISS:
                lenPk = 1;
                sql = "SELECT id_card_iss AS f_id_1, card_iss AS f_item FROM erp.finu_card_iss WHERE b_del = 0 ORDER BY card_iss, id_card_iss ";
                text = "emisor de tarjetas";
                break;
            case SDataConstants.FINU_CHECK_FMT:
                lenPk = 1;
                sql = "SELECT id_check_fmt AS f_id_1, check_fmt AS f_item FROM erp.finu_check_fmt WHERE b_del = 0 ORDER BY check_fmt, id_check_fmt ";
                text = "formato de impresión de cheques";
                break;
            case SDataConstants.FINU_CHECK_FMT_GP:
                lenPk = 1;
                sql = "SELECT id_check_fmt_gp AS f_id_1, check_fmt_gp AS f_item FROM erp.finu_check_fmt_gp WHERE b_del = 0 ORDER BY check_fmt_gp, id_check_fmt_gp ";
                text = "formato de impresión gráfica de cheques";
                break;
            case SDataConstants.FINU_TP_REC:
            case SDataConstants.FINX_TP_REC_ALL:
            case SDataConstants.FINX_TP_REC_USER:
                lenPk = 1;
                isPkOnlyInts = false;
                sql = "SELECT id_tp_rec AS f_id_1, CONCAT(id_tp_rec, ' - ', tp_rec) AS f_item, b_acc_cash AS f_comp FROM erp.finu_tp_rec ";

                switch (catalogue) {
                    case SDataConstants.FINU_TP_REC:
                        sql += "WHERE b_del = 0 ";
                        break;
                    case SDataConstants.FINX_TP_REC_ALL:
                        sql += "";
                        break;
                    case SDataConstants.FINX_TP_REC_USER:
                        sql += "WHERE b_sys = 0 AND b_del = 0 ";
                        break;
                    default:
                }

                sql += "ORDER BY id_tp_rec ";

                text = "tipo de póliza contable";
                isComplementApplying = true;
                break;
            case SDataConstants.FINU_TP_ACC_USR:
                lenPk = 1;
                sql = "SELECT id_tp_acc_usr AS f_id_1, tp_acc_usr AS f_item " +
                        "FROM erp.finu_tp_acc_usr " +
                        "WHERE b_apply = 1 AND b_del = 0 AND id_tp_acc_usr <> " + SDataConstantsSys.FINU_TP_ACC_USR_NA + " " +
                        "ORDER BY fid_tp_acc, tp_acc_usr ";
                text = "tipo de cuenta contable de la empresa";
                break;
            case SDataConstants.FINU_CL_ACC_USR:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_tp_acc_usr AS f_id_1, id_cl_acc_usr AS f_id_2, cl_acc_usr AS f_item, id_tp_acc_usr AS f_fid_1 " +
                        "FROM erp.finu_cl_acc_usr " +
                        "WHERE b_apply = 1 AND b_del = 0 AND id_tp_acc_usr <> " + SDataConstantsSys.FINU_TP_ACC_USR_NA + " " +
                        "ORDER BY fid_tp_acc, fid_cl_acc, cl_acc_usr ";
                text = "clase de cuenta contable de la empresa";
                break;
            case SDataConstants.FINU_CLS_ACC_USR:
                lenPk = 3;
                lenFk = 2;
                sql = "SELECT id_tp_acc_usr AS f_id_1, id_cl_acc_usr AS f_id_2, id_cls_acc_usr AS f_id_3, cls_acc_usr AS f_item, id_tp_acc_usr AS f_fid_1, id_cl_acc_usr as f_fid_2 " +
                        "FROM erp.finu_cls_acc_usr " +
                        "WHERE b_apply = 1 AND b_del = 0 AND id_tp_acc_usr <> " + SDataConstantsSys.FINU_TP_ACC_USR_NA + " " +
                        "ORDER BY fid_tp_acc, fid_cl_acc, fid_cls_acc, cls_acc_usr ";
                text = "subclase de cuenta contable de la empresa";
                break;
            case SDataConstants.FINU_TP_ACC_LEDGER:
                lenPk = 1;
                lenFk = 3;
                sql = "SELECT aled.id_tp_acc_ledger AS f_id_1, aled.tp_acc_ledger AS f_item, aled.fid_tp_acc_usr AS f_fid_1, aled.fid_cl_acc_usr as f_fid_2, aled.fid_cls_acc_usr as f_fid_3, " +
                        "clsa.fid_tp_acc, clsa.fid_cl_acc, clsa.fid_cls_acc " +
                        "FROM erp.finu_tp_acc_ledger AS aled " +
                        "INNER JOIN erp.finu_cls_acc_usr AS clsa ON " +
                        "aled.fid_tp_acc_usr = clsa.id_tp_acc_usr AND aled.fid_cl_acc_usr = clsa.id_cl_acc_usr AND aled.fid_cls_acc_usr = clsa.id_cls_acc_usr " +
                        "WHERE aled.b_del = 0 " +
                        "ORDER BY clsa.fid_tp_acc, clsa.fid_cl_acc, clsa.fid_cls_acc, clsa.num_start ";
                text = "tipo de cuenta de libro mayor";
                break;
            case SDataConstants.FINU_TP_ACC_EBITDA:
                lenPk = 1;
                sql = "SELECT id_tp_acc_ebitda AS f_id_1, tp_acc_ebitda AS f_item FROM erp.finu_tp_acc_ebitda WHERE NOT b_del ORDER BY sort_pos ";
                text = "tipo de cuenta EBITDA";
                break;
            case SDataConstants.FINU_TP_ASSET_FIX:
                lenPk = 1;
                sql = "SELECT id_tp_asset_fix AS f_id_1, tp_asset_fix AS f_item FROM erp.finu_tp_asset_fix WHERE b_del = 0 " +
                        "ORDER BY tp_asset_fix, id_tp_asset_fix ";
                text = "tipo de activo fijo";
                break;
            case SDataConstants.FINU_TP_ASSET_DIF:
                lenPk = 1;
                sql = "SELECT id_tp_asset_dif AS f_id_1, tp_asset_dif AS f_item FROM erp.finu_tp_asset_dif WHERE b_del = 0 " +
                        "ORDER BY tp_asset_dif, id_tp_asset_dif ";
                text = "tipo de activo diferido";
                break;
            case SDataConstants.FINU_TP_LIABTY_DIF:
                lenPk = 1;
                sql = "SELECT id_tp_liabty_dif AS f_id_1, tp_liabty_dif AS f_item FROM erp.finu_tp_liabty_dif WHERE b_del = 0 " +
                        "ORDER BY tp_liabty_dif, id_tp_liabty_dif ";
                text = "tipo de pasivo diferido";
                break;
            case SDataConstants.FINU_TP_EXPEN_OP:
                lenPk = 1;
                sql = "SELECT id_tp_expen_op AS f_id_1, tp_expen_op AS f_item FROM erp.finu_tp_expen_op WHERE b_del = 0 " +
                        "ORDER BY tp_expen_op, id_tp_expen_op ";
                text = "tipo de gasto de operación";
                break;
            case SDataConstants.FINU_TP_ADM_CPT:
                lenPk = 1;
                sql = "SELECT id_tp_adm_cpt AS f_id_1, tp_adm_cpt AS f_item FROM erp.finu_tp_adm_cpt WHERE b_del = 0 ORDER BY tp_adm_cpt, id_tp_adm_cpt ";
                text = "tipo de concepto administrativo";
                break;
            case SDataConstants.FINU_TP_TAX_CPT:
                lenPk = 1;
                sql = "SELECT id_tp_tax_cpt AS f_id_1, tp_tax_cpt AS f_item FROM erp.finu_tp_tax_cpt WHERE b_del = 0 ORDER BY tp_tax_cpt, id_tp_tax_cpt ";
                text = "tipo de concepto de impuestos";
                break;
            case SDataConstants.FINU_TP_LAY_BANK:
                lenPk = 1;
                lenFk = 2;
                sql = "SELECT id_tp_lay_bank AS f_id_1, tp_lay_bank AS f_item, fid_tp_pay_bank AS f_fid_1, fid_bank AS f_fid_2 " +
                        "FROM erp.finu_tp_lay_bank " +
                        "WHERE b_del = 0 " + (pk == null ? "" : " AND lay_bank = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY tp_lay_bank, id_tp_lay_bank ";
                text = "tipo layout";
                break;
            case SDataConstants.FIN_YEAR:
                lenPk = 1;
                sql = "SELECT id_year AS f_id_1, id_year AS f_item FROM fin_year WHERE b_del = 0 ORDER BY id_year ";
                text = "año contable";
                break;
            case SDataConstants.FIN_ACC_CASH:
            case SDataConstants.FINX_ACC_CASH_CASH:
            case SDataConstants.FINX_ACC_CASH_BANK:
            case SDataConstants.FINX_ACC_CASH_BANK_CHECK:
                lenPk = 2;
                sql = "SELECT a.id_cob AS f_id_1, a.id_acc_cash AS f_id_2, CONCAT(e.ent, ' (', e.code, ')') AS f_item " +
                        "FROM fin_acc_cash AS a " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent AND " +
                        "a.b_del = 0 " + (pk == null ? "" : " AND a.id_cob = " + ((int[]) pk)[0] + " ");

                switch (catalogue) {
                    case SDataConstants.FINX_ACC_CASH_CASH:
                        text = "caja";
                        sql += "AND e.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[0] + " AND " +
                                "e.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[1] + " ";
                        break;
                    case SDataConstants.FINX_ACC_CASH_BANK:
                        text = "cuenta bancaria";
                        sql += "AND e.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[0] + " AND " +
                                "e.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1] + " ";
                        break;
                    case SDataConstants.FINX_ACC_CASH_BANK_CHECK:
                        text = "cuenta bancaria cheques";
                        sql += "AND e.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[0] + " AND " +
                                "e.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1] + " AND " +
                                "a.b_check_wal = 1 ";
                        break;
                    default:
                        text = "cuenta de efectivo";
                }
                sql += "ORDER BY a.id_cob, e.ent, e.code, a.id_acc_cash ";
                break;
            case SDataConstants.FIN_CHECK_WAL:
                lenPk = 1;
                sql = "SELECT id_check_wal AS f_id_1, CONCAT(num_start, ' - ', IF(num_end_n IS NULL, '...', num_end_n)) AS f_item " +
                        "FROM fin_check_wal WHERE b_del = 0 AND b_act = 1 " +
                        (pk == null ? "" : "AND fid_cob = " + ((int[]) pk)[0] + " AND fid_acc_cash = " + ((int[]) pk)[1] + " ") +
                        "ORDER BY f_item, id_check_wal ";
                text = "chequera";
                break;
            case SDataConstants.FIN_TAX_GRP:
                lenPk = 1;
                sql = "SELECT id_tax_grp AS f_id_1, tax_grp AS f_item FROM fin_tax_grp WHERE b_del = 0 ORDER BY tax_grp ";
                text = "grupo de impuestos";
                break;
            case SDataConstants.FINX_TAX_BAS_TAX:
                lenPk = 2;
                sql = "SELECT t.id_tax_bas AS f_id_1, t.id_tax AS f_id_2, CONCAT(tb.tax_bas, ' - ', t.tax) AS f_item " +
                        "FROM erp.finu_tax_bas AS tb INNER JOIN erp.finu_tax AS t ON " +
                        "tb.id_tax_bas = t.id_tax_bas WHERE t.b_del = 0 " +
                        "ORDER BY tb.tax_bas, t.tax, t.id_tax_bas, t.id_tax ";
                text = "impuesto";
                break;
            case SDataConstants.FIN_BKC:
                lenPk = 1;
                sql = "SELECT id_bkc AS f_id_1, bkc AS f_item FROM fin_bkc WHERE b_del = 0 ORDER BY bkc, id_bkc ";
                text = "centro contable";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatTrn(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        java.lang.String sql = "";
        java.lang.String text = "";
        java.lang.String filter = "";

        switch (catalogue) {
            case SDataConstants.TRNS_CT_DPS:
                lenPk = 1;
                sql = "SELECT id_ct_dps AS f_id_1, ct_dps AS f_item FROM erp.trns_ct_dps ORDER BY id_ct_dps ";
                text = "categoría de doc. compras-ventas";
                break;
            case SDataConstants.TRNS_CL_DPS:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_ct_dps AS f_id_1, id_cl_dps AS f_id_2, cl_dps AS f_item, id_ct_dps AS f_fid_1 " +
                        "FROM erp.trns_cl_dps ORDER BY id_ct_dps, id_cl_dps ";
                text = "clase de doc. compras-ventas";
                break;
            case SDataConstants.TRNS_TP_DPS_ADJ:
                lenPk = 1;
                sql = "SELECT id_tp_dps_adj AS f_id_1, tp_dps_adj AS f_item FROM erp.trns_tp_dps_adj WHERE b_del = 0 " +
                        "ORDER BY id_tp_dps_adj ";
                text = "tipo de ajuste de doc. compras-ventas";
                break;
            case SDataConstants.TRNS_STP_DPS_ADJ:
                lenPk = 2;
                sql = "SELECT id_tp_dps_adj AS f_id_1, id_stp_dps_adj AS f_id_2, stp_dps_adj AS f_item FROM erp.trns_stp_dps_adj WHERE b_del = 0 " +
                        "ORDER BY id_tp_dps_adj, id_stp_dps_adj ";
                text = "subtipo de ajuste de doc. compras-ventas";
                break;
            case SDataConstants.TRNS_CT_IOG:
                lenPk = 1;
                sql = "SELECT id_ct_iog AS f_id_1, ct_iog AS f_item FROM erp.trns_ct_iog ORDER BY id_ct_iog ";
                text = "categoría de doc. inventarios";
                break;
            case SDataConstants.TRNS_CL_IOG:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_ct_iog AS f_id_1, id_cl_iog AS f_id_2, cl_iog AS f_item, id_ct_iog AS f_fid_1 " +
                        "FROM erp.trns_cl_iog ORDER BY id_ct_iog, id_cl_iog ";
                text = "clase de doc. inventarios";
                break;
            case SDataConstants.TRNS_TP_IOG:
                lenPk = 3;
                lenFk = 2;
                if (pk != null) {
                    switch (((int[]) pk).length) {
                        case 1:
                            filter = "id_ct_iog = " + ((int[]) pk)[0] + " ";
                            break;
                        case 2:
                            filter = "id_ct_iog = " + ((int[]) pk)[0] + " AND id_cl_iog = " + ((int[]) pk)[1] + " ";
                            break;
                        case 3:
                            filter = "id_ct_iog = " + ((int[]) pk)[0] + " AND id_cl_iog = " + ((int[]) pk)[1] + " AND id_tp_iog = " + ((int[]) pk)[2] + " ";
                            break;
                        default:
                    }
                }
                sql = "SELECT id_ct_iog f_id_1, id_cl_iog AS f_id_2, id_tp_iog AS f_id_3, CONCAT(tp_iog, ' (', code, ')') AS f_item, id_ct_iog AS f_fid_1, id_cl_iog AS f_fid_2 " +
                        "FROM erp.trns_tp_iog " +
                        "WHERE b_del = 0 " + (filter.length() == 0 ? "" : "AND " + filter) +
                        "ORDER BY id_ct_iog, id_cl_iog, id_tp_iog ";
                text = "tipo de doc. inventarios";
                break;
            case SDataConstants.TRNU_DPS_NAT:
                lenPk = 1;
                sql = "SELECT id_dps_nat f_id_1, dps_nat AS f_item " +
                        "FROM erp.trnu_dps_nat " +
                        "WHERE b_del = 0 " + (filter.length() == 0 ? "" : "AND " + "id_dps_nat = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY id_dps_nat ";
                text = "nuturaleza de doc. compras-ventas";
                break;
            case SDataConstants.TRNU_TP_DPS:
                lenPk = 3;
                lenFk = 2;
                if (pk != null) {
                    switch (((int[]) pk).length) {
                        case 1:
                            filter = "id_ct_dps = " + ((int[]) pk)[0] + " ";
                            break;
                        case 2:
                            filter = "id_ct_dps = " + ((int[]) pk)[0] + " AND id_cl_dps = " + ((int[]) pk)[1] + " ";
                            break;
                        case 3:
                            filter = "id_ct_dps = " + ((int[]) pk)[0] + " AND id_cl_dps = " + ((int[]) pk)[1] + " AND id_tp_dps = " + ((int[]) pk)[2] + " ";
                            break;
                        default:
                    }
                }
                sql = "SELECT id_ct_dps f_id_1, id_cl_dps AS f_id_2, id_tp_dps AS f_id_3, CONCAT(tp_dps, ' (', code, ')') AS f_item, id_ct_dps AS f_fid_1, id_cl_dps AS f_fid_2 " +
                        "FROM erp.trnu_tp_dps " +
                        "WHERE b_del = 0 " + (filter.length() == 0 ? "" : "AND " + filter) +
                        "ORDER BY id_ct_dps, id_cl_dps, id_tp_dps ";
                text = "tipo de doc. compras-ventas";
                break;
            case SDataConstants.TRNU_TP_IOG_ADJ:
                lenPk = 1;
                sql = "SELECT id_tp_iog_adj AS f_id_1, tp_iog_adj AS f_item " +
                        "FROM erp.trnu_tp_iog_adj " +
                        "WHERE b_del = 0 " +
                        "ORDER BY id_tp_iog_adj ";
                text = "tipo de ajuste de doc. inventarios";
                break;
            case SDataConstants.TRN_PAC:
                lenPk = 1;
                sql = "SELECT id_pac AS f_id_1, pac AS f_item " +
                        "FROM trn_pac " +
                        "WHERE b_del = 0 AND b_pre_pay = 1 " +
                        "ORDER BY id_pac ";
                text = "pac";
                break;
            case SDataConstants.TRNS_TP_PAY:
                lenPk = 1;
                sql = "SELECT id_tp_pay AS f_id_1, tp_pay AS f_item FROM erp.trns_tp_pay ORDER BY id_tp_pay ";
                text = "tipo de pago";
                break;
            case SDataConstants.TRNU_TP_PAY_SYS:
                lenPk = 1;

                if (pk != null) {
                    sql = "(SELECT 0 AS f_id_1, pay_method AS f_item FROM trn_dps " +
                            "WHERE b_del = 0 AND pay_method <> '' AND pay_method <> '" + SUtilConsts.NON_APPLYING + "' AND fid_bp_r = " + ((int[]) pk)[0] + " AND id_year = " + ((int[]) pk)[1] + " AND fid_tp_pay_sys = " + SDataConstantsSys.TRNU_TP_PAY_SYS_NA + " " +
                            "AND fid_ct_dps = " + ((int[]) pk)[2] + ") " +
                            "UNION (";
                }

                sql += "SELECT id_tp_pay_sys AS f_id_1, tp_pay_sys AS f_item " +
                        "FROM erp.trnu_tp_pay_sys " +
                        "WHERE b_del = 0 ";

                if (pk != null) {
                    sql += ") ORDER BY f_item  ";
                }
                else {
                    sql += " ORDER BY id_tp_pay_sys  ";
                }
                text = "forma de pago";
                break;
            case SDataConstants.TRNS_TP_LINK:
                lenPk = 1;
                sql = "SELECT id_tp_link AS f_id_1, tp_link AS f_item FROM erp.trns_tp_link WHERE b_del = 0 " +
                        "ORDER BY id_tp_link ";
                text = "tipo de referencia";
                break;
            case SDataConstants.TRNX_DPS_BAL:
                lenPk = 2;

                sql = "SELECT d.id_year AS f_id_1, d.id_doc AS f_id_2, " +
                    "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num, ', saldo: ', " +
                    (pk == null ? "0.00 " : ("SUM(IF(d.fid_cur = re.fid_cur, IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1), 0) * (re.debit_cur - re.credit_cur))")) + ", ' ', c.cur_key) AS f_item, " +
                    "SUM(IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1) * (re.debit - re.credit)) AS f_balance " +
                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.b_del = 0 AND re.b_del = 0 " + (pk == null ? "" :
                    " AND re.fid_ct_sys_mov_xxx = " + ((int[])((Object[]) pk)[0])[0] + " " +
                    " AND re.fid_tp_sys_mov_xxx = " + ((int[])((Object[]) pk)[0])[1]) + " " +
                    "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " + (pk == null ? "" : "AND b.id_bp = " + (Integer)((Object[]) pk)[1]) + " " +
                    "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp " + (pk == null ? "" : "AND bc.id_ct_bp =  " +
                    (SLibUtilities.compareKeys(((Object[]) pk)[0], SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) ?
                    SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS)) + " " +
                    "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = 0 " +
                    "INNER JOIN erp.trnu_tp_dps AS td ON d.fid_ct_dps = td.id_ct_dps AND d.fid_cl_dps = td.id_cl_dps AND d.fid_tp_dps = td.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "GROUP BY d.id_year, d.id_doc, d.fid_cur " +
                    //(pk == null ? "" : ((Integer)((Object[]) pk)[2] == SDataConstantsSys.TRNX_DPS_BAL_PAY_PEND ? "HAVING f_balance_cur <> 0 AND f_balance <> 0 " : "")) + " " +
                    "ORDER BY d.dt, d.id_year, d.id_doc, f_item ";
                text = "doc. compras-ventas";
                break;
            case SDataConstants.TRN_DNC_DPS:
                lenPk = 1;
                sql = "SELECT id_dnc AS f_id_1, dnc AS f_item FROM trn_dnc_dps ORDER BY dnc, id_dnc ";
                text = "centro de foliado de compras-ventas";
                break;
            case SDataConstants.TRN_DNC_DIOG:
                lenPk = 1;
                sql = "SELECT id_dnc AS f_id_1, dnc AS f_item FROM trn_dnc_diog ORDER BY dnc, id_dnc ";
                text = "centro de foliado de inventarios";
                break;
            case SDataConstants.TRN_DNS_DPS:
                lenPk = 1;
                sql = "SELECT s.id_dns AS f_id_1, CONCAT(s.dns, ' (', t.tp_dps, ')') AS f_item " +
                        "FROM trn_dns_dps AS s " +
                        "INNER JOIN erp.trnu_tp_dps AS t ON " +
                        "s.fid_ct_dps = t.id_ct_dps AND s.fid_cl_dps = t.id_cl_dps AND s.fid_tp_dps = t.id_tp_dps " +
                        (pk == null ? "" : "AND t.id_ct_dps = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY s.dns, s.id_dns, t.id_ct_dps, t.id_cl_dps, t.tp_dps ";

                if (pk != null) {
                    switch(((int[]) pk)[0]) {
                        case SDataConstantsSys.TRNS_CT_DPS_SAL:
                            text = "serie folios docs. de ventas";
                            break;
                        case SDataConstantsSys.TRNS_CT_DPS_PUR:
                            text = "serie folios docs. de compras";
                            break;
                        default:
                            text = "serie folios de compras-ventas";
                            break;
                    }
                }

                break;
            case SDataConstants.TRN_DNS_DIOG:
                lenPk = 1;
                sql = "SELECT s.id_dns AS f_id_1, CONCAT(s.dns, ' (', cl.cl_iog, ')') AS f_item " +
                        "FROM trn_dns_diog AS s " +
                        "INNER JOIN erp.trns_cl_iog AS cl ON " +
                        "s.fid_ct_iog = cl.id_ct_iog AND s.fid_cl_iog = cl.id_cl_iog " +
                        (pk == null ? "" : "AND cl.id_ct_iog = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY s.dns, s.id_dns, cl.id_ct_iog, cl.cl_iog ";
                text = "serie folios de inventarios";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, false };
    }

    private static java.lang.Object[] getSettingsCatMkt(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.MKTS_TP_DISC_APP:
                lenPk = 1;
                sql = "SELECT id_tp_disc_app AS f_id_1, tp_disc_app AS f_item FROM erp.mkts_tp_disc_app WHERE b_del = 0 " +
                        "ORDER BY tp_disc_app ";
                text = "tipo de descuento";
                break;
            case SDataConstants.MKTU_TP_CUS:
                lenPk = 1;
                sql = "SELECT id_tp_cus AS f_id_1, tp_cus AS f_item FROM mktu_tp_cus ORDER BY tp_cus, id_tp_cus ";
                text = "tipo de cliente";
                break;
            case SDataConstants.MKT_PLIST:
                lenPk = 1;
                sql = "SELECT id_plist AS f_id_1, plist AS f_item " +
                    "FROM mkt_plist " +
                    "WHERE b_del = 0 " + (pk == null ? "" : "AND fid_ct_dps = " + ((Integer) pk == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstantsSys.TRNS_CT_DPS_SAL : SDataConstantsSys.TRNS_CT_DPS_PUR)) + " " +
                    "ORDER BY plist, id_plist ";
                text = "lista de precios";
                break;
            case SDataConstants.MKTU_SAL_ROUTE:
                lenPk = 1;
                sql = "SELECT id_sal_route AS f_id_1, sal_route AS f_item FROM mktu_sal_route WHERE b_del = 0 ORDER BY sal_route, id_sal_route ";
                text = "ruta de ventas";
                break;
            case SDataConstants.MKTU_MKT_SEGM:
                lenPk = 1;
                sql = "SELECT id_mkt_segm AS f_id_1, mkt_segm AS f_item FROM mktu_mkt_segm WHERE b_del = 0 ORDER BY mkt_segm, id_mkt_segm ";
                text = "segmento de mercado";
                break;
            case SDataConstants.MKTU_MKT_SEGM_SUB:
                lenPk = 2;
                lenFk = 1;
                sql = "SELECT id_mkt_segm AS f_id_1, id_mkt_sub AS f_id_2, mkt_segm_sub AS f_item, id_mkt_segm AS f_fid_1 " +
                        "FROM mktu_mkt_segm_sub " +
                        "WHERE b_del = 0 " +
                        "ORDER BY mkt_segm_sub ";
                text = "subsegmento de mercado";
                break;
            case SDataConstants.MKTU_DIST_CHAN:
                lenPk = 1;
                sql = "SELECT id_dist_chan AS f_id_1, dist_chan AS f_item FROM mktu_dist_chan WHERE b_del = 0 ORDER BY dist_chan, id_dist_chan ";
                text = "canal de distribución";
                break;
            case SDataConstants.MKTU_TP_SAL_AGT:
                lenPk = 1;
                sql = "SELECT id_tp_sal_agt AS f_id_1, tp_sal_agt AS f_item FROM mktu_tp_sal_agt WHERE b_del = 0 ORDER BY tp_sal_agt ";
                text = "tipo de agente ventas";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, false };
    }

    private static java.lang.Object[] getSettingsCatLog(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isComplementApplying = false;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        java.lang.String sql = "";
        java.lang.String text = "";
        java.lang.String filter = "";

        switch (catalogue) {
            case SModConsts.LOGS_TP_MOT:
                lenPk = 1;
                sql = "SELECT id_tp_mot AS f_id_1, name AS f_item FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE b_del = 0 ORDER BY sort ";
                text = "tipo de modo de transportación";
                break;
            case SModConsts.LOGS_TP_CAR:
                lenPk = 1;
                sql = "SELECT id_tp_car AS f_id_1, name AS f_item FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE b_del = 0 ORDER BY sort ";
                text = "tipo de transportista";
                break;
            case SModConsts.LOGS_INC:
                lenPk = 1;
                sql = "SELECT id_inc AS f_id_1, CONCAT(code, ' - ', name) AS f_item, code AS f_comp, sort " +
                        "FROM " + SModConsts.TablesMap.get(catalogue) +  " " +
                        "WHERE b_del = 0 AND (id_inc =  " + SModSysConsts.LOGS_INC_NA + (pk == null ? "" : " OR fid_tp_dly = " + ((int[]) pk)[0]) + ") " +
                        "ORDER BY sort ";
                text = "INCOTERM";
                isComplementApplying = true;
                break;
            case SModConsts.LOGU_TP_VEH:
                lenPk = 1;
                sql = "SELECT id_tp_veh AS f_id_1, name AS f_item, cap_mass AS f_comp FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE b_del = 0 ORDER BY name, id_tp_veh ";
                text = "tipo vehículo";
                isComplementApplying = true;
                break;
            case SModConsts.LOG_VEH:
                lenPk = 1;
                sql = "SELECT id_veh AS f_id_1, name AS f_item FROM " + SModConsts.TablesMap.get(catalogue) + " " +
                        "WHERE b_del = 0 " + (pk == null ? "" : " AND fk_tp_veh = " + ((int[]) pk)[0] + " ") +
                        "ORDER BY name, id_veh ";
                text = "vehículo";
                break;
            case SModConsts.LOGU_SPOT:
                /* Use of Object pk:
                 * pk[0] = Filter: Array integer; types spot ID. It's optional.
                 * pk[1] = Filter: Integer; type spot ID. It's optional.
                 */
                lenPk = 1;

                if (pk != null) {
                    if (((Object[]) pk)[0] instanceof int[]) {
                        for(int n : (int[])((Object[]) pk)[0]) {
                            filter += (filter.isEmpty() ? "" : ", ") + n;
                        }
                    }
                }

                sql = "SELECT id_spot AS f_id_1, name AS f_item " +
                        "FROM erp.logu_spot " +
                        "WHERE b_del = 0 " + (filter.isEmpty() ? "" : " AND fk_tp_spot IN(" + filter + ") ") + (pk == null || (int)((Object[]) pk)[1] == SLibConstants.UNDEFINED ? "" : " AND fk_tp_dly = " + (int)((Object[]) pk)[1] + " ") +
                        "ORDER BY name, id_spot ";
                text = "destino";
                break;
            case SModConsts.LOGU_SPOT_COB:
                lenPk = 1;
                sql = "SELECT s.id_spot AS f_id_1, s.name AS f_item " +
                        "FROM erp.logu_spot AS s " +
                        "INNER JOIN erp.logu_spot_cob AS sc ON s.id_spot = sc.fk_spot " + (pk == null ? "" : " AND id_cob = " + ((int[]) pk)[0] + " ") +
                        "WHERE s.b_del = 0 AND sc.b_del = 0 " +
                        "ORDER BY s.name, s.id_spot ";
                text = "origen";
                break;
            default:
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatMfg(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
            case SDataConstants.MFGS_PTY_ORD:
                lenPk = 1;
                sql = "SELECT id_pty AS f_id_1, pty AS f_item FROM erp.mfgs_pty_ord ORDER BY val ";
                text = "prioridad";
                break;
            case SDataConstants.MFGS_ST_ORD:
                lenPk = 1;
                sql = "SELECT id_st AS f_id_1, st AS f_item FROM erp.mfgs_st_ord ORDER BY id_st ";
                text = "estado";
                break;
            case SDataConstants.MFGS_TP_REQ:
                lenPk = 1;
                sql = "SELECT id_tp AS f_id_1, tp AS f_item FROM erp.mfgs_tp_req ORDER BY id_tp ";
                text = "tipo";
                break;
            case SDataConstants.MFGS_TP_COST_OBJ:
                lenPk = 1;
                sql = "SELECT id_tp_cost_obj AS f_id_1, tp_cost_obj AS f_item FROM erp.mfgs_tp_cost_obj WHERE b_del = 0 ORDER BY id_tp_cost_obj ";
                text = "tipo objeto costo";
                break;
            case SDataConstants.MFGU_TP_ORD:
                lenPk = 1;
                sql = "SELECT id_tp AS f_id_1, tp AS f_item, b_req_fat AS f_comp FROM erp.mfgu_tp_ord ORDER BY id_tp ";
                text = "tipo";
                isComplementApplying = true;
                break;
            case SDataConstants.MFGU_TURN:
                lenPk = 1;
                sql = "SELECT id_turn AS f_id_1, turn AS f_item FROM erp.mfgu_turn ORDER BY id_turn ";
                text = "turno";
                break;
            case SDataConstants.MFGU_GANG:
                lenPk = 1;
                sql = "SELECT id_gang AS f_id_1, des AS f_item FROM mfgu_gang WHERE b_del = 0 ORDER BY des ";
                text = "cuadrilla";
                break;
            case SDataConstants.MFGU_LINE:
                lenPk = 1;
                sql = "SELECT id_line AS f_id_1, line AS f_item FROM mfgu_line WHERE b_del = 0 ORDER BY line ";
                text = "línea producción";
                break;

            case SDataConstants.MFG_BOM:
                lenPk = 1;
                lenFk = 1;
                sql = "SELECT id_bom AS f_id_1, bom AS f_item, fid_item AS f_fid_1, qty AS f_comp " +
                        "FROM mfg_bom " +
                        "WHERE b_del = 0 AND fid_item_n IS NULL " +
                        (pk == null ? "" : "AND fid_item = " + ((int[]) pk)[0] + " AND (ts_end_n IS NULL OR ts_end_n >= NOW()) " +
                        "ORDER BY bom, id_bom ");
                text = "fórmula";
                isComplementApplying = true;
                break;
            case SDataConstants.MFG_BOM_SUB:
                lenPk = 4;
                lenFk = 2;
                sql = "SELECT bs.id_bom AS f_id_1, bs.id_item AS f_id_2, bs.id_unit AS f_id_3, bs.id_bom_sub AS f_id_4, " +
                        (!paramsErp.getIsItemKeyApplying() ? "i.item " :
                        (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "CONCAT(i.item_key, ' - ', i.item)" : "CONCAT(i.item, ' - ', i.item_key) ")) + "AS f_item, " +
                        "bs.fid_item_sub AS f_fid_1, bs.fid_unit_sub AS f_fid_2, bs.per AS f_comp " +
                        "FROM mfg_bom AS b " +
                        "INNER JOIN mfg_bom_sub AS bs ON b.id_bom = bs.id_bom " +
                        (pk == null ? "" : "AND b.fid_item_n = " + ((int[]) pk)[2] + " AND b.fid_unit_n = " + ((int[]) pk)[3]) + " " +
                        "INNER JOIN erp.itmu_item AS i ON bs.fid_item_sub = i.id_item " +
                        "INNER JOIN erp.itmu_unit AS u ON bs.fid_unit_sub = u.id_unit " +
                        "WHERE bs.b_del = 0 " +
                        (pk == null ? "" : "AND bs.id_item = " + ((int[]) pk)[0] + " AND bs.id_unit = " + ((int[]) pk)[1]) + " " +
                        "ORDER BY " + (!paramsErp.getIsItemKeyApplying() ? "i.item " : (paramsErp.getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "i.item_key, i.item" : "i.item, i.item_key "));
                text = "ítem sustituto";
                isComplementApplying = true;
                break;
            case SDataConstants.MFG_ORD:
                lenPk = 2;
                sql = "SELECT o.id_year AS f_id_1, o.id_ord AS f_id_2, CONCAT(o.id_year, '-', o.num, ', ', ref) AS f_item, o.fid_st_ord AS f_comp " +
                    "FROM mfg_ord AS o " +
                    "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                    "WHERE o.b_del = 0 " +
                    (pk == null ? "" : "AND o.fid_st_ord IN (" + (String) ((Object[]) pk)[0] + ") " +
                    ((Integer)((Object[]) pk)[1] == SDataConstants.MFGX_ORD_MAIN_NA ? "" :
                    (Integer)((Object[]) pk)[1] == SDataConstants.MFGX_ORD_MAIN_FA ? "AND t.b_req_fat = 0 AND t.id_tp > 1 " :
                    (Integer)((Object[]) pk)[1] == SDataConstants.MFGX_ORD_MAIN_CH ? "AND t.b_req_fat = 1 AND t.id_tp > 1 " : "") +
                    " AND o.b_for = " + ((Boolean)((Object[]) pk)[2] == false ? "0 " : "1 ")) +
                    (((Object[]) pk).length <= 3 ? "" :((Integer)((Object[]) pk)[3] == SDataConstants.UNDEFINED ? "" : "AND o.fid_item_r IN (SELECT fid_item FROM mfg_bom WHERE root = " +(Integer)((Object[]) pk)[3] + " )")) +    
                    "ORDER BY f_id_1, f_id_2, f_item ";
                text = "orden de producción";
                isComplementApplying = true;
                break;
            case SDataConstants.MFGX_ORD:
                lenPk = 2;
                sql = "SELECT id_year AS f_id_1, id_ord AS f_id_2, CONCAT(id_year, '-', num, ', ', ref) AS f_item, fid_st_ord AS f_comp " +
                        "FROM mfg_ord " +
                        "WHERE b_del = 0 " +
                        (pk == null ? "" : "AND fid_st_ord IN (" + (String) ((Object[]) pk)[0] + ") " +
                        (((Object[]) pk).length == 1 ? "" : "AND fid_ord_year_n = " + ((int[]) ((Object[]) pk)[1])[0] + " AND fid_ord_n = " + ((int[]) ((Object[]) pk)[1])[1] + " ")) +
                        "ORDER BY f_id_1, f_id_2, f_item ";
                text = "orden de producción";
                isComplementApplying = true;
                break;
            case SDataConstants.MFG_LINE:
                lenPk = 3;
                sql = "SELECT id_cob AS f_id_1, id_ent AS f_id_2, id_line AS f_id_3, line AS f_item " +
                        "FROM mfg_line " +
                        "WHERE b_del = 0 " + (pk == null ? "" : "AND id_cob = " + ((int[]) pk)[0] + " AND id_ent = " + ((int[]) pk)[1]) + " " +
                        "ORDER BY line ";
                text = "línea";
                break;
            default:
                break;
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.lang.Object[] getSettingsCatHrs(int catalogue, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        int lenPk = 0;
        int lenFk = 0;
        boolean isPkOnlyInts = true;
        boolean isFkOnlyInts = true;
        boolean isComplementApplying = false;
        java.lang.String sql = "";
        java.lang.String text = "";

        switch (catalogue) {
           // Eployee of the human resources module:

            case SModConsts.HRSS_CL_HRS_CAT:
                lenPk = 1;
                sql = "SELECT id_cl_hrs_cat AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_CL_HRS_CAT) + " WHERE b_del = 0 ORDER BY sort ";
                text = "clase catálogo recursos humanos";
                break;
            case SModConsts.HRSS_TP_HRS_CAT:
                lenPk = 2;
                //lenFk = 1;
                sql = "SELECT id_cl_hrs_cat AS " + SDbConsts.FIELD_ID + "1, id_tp_hrs_cat AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + " "
                        //+ "id_cl_hrs_cat AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_HRS_CAT) + " "
                        + "WHERE b_del = 0 "
                        + (pk != null ? " AND id_cl_hrs_cat = " + ((int) pk) : "") + " " 
                        + "ORDER BY sort ";
                text = "tipo catálogo recursos humanos";
                break;
            case SModConsts.HRSS_TP_PAY:
                lenPk = 1;
                sql = "SELECT id_tp_pay AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " WHERE b_del = 0 ORDER BY sort ";
                text = "periodo pago";
                break;
            case SModConsts.HRSS_TP_SAL:
                lenPk = 1;
                sql = "SELECT id_tp_sal AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_SAL) + " WHERE b_del = 0 ORDER BY sort ";
                text = "tipo salario";
                break;
            case SModConsts.HRSS_TP_CON:
                lenPk = 1;
                sql = "SELECT id_tp_con AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_CON) + " WHERE b_del = 0 ORDER BY name, id_tp_con ";
                text = "contrato";
                break;
            case SModConsts.HRSS_TP_REC_SCHE:
                lenPk = 1;
                sql = "SELECT id_tp_rec_sche AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " WHERE b_del = 0 ORDER BY sort ";
                text = "régimen contratación";
                break;
            case SModConsts.HRSS_TP_POS_RISK:
                lenPk = 1;
                sql = "SELECT id_tp_pos_risk AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_POS_RISK) + " WHERE b_del = 0 ORDER BY sort ";
                text = "riesgo trabajo";
                break;
            case SModConsts.HRSS_TP_WORK_DAY:
                lenPk = 1;
                sql = "SELECT id_tp_work_day AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_WORK_DAY) + " WHERE b_del = 0 ORDER BY sort ";
                text = "jornada";
                break;
            case SModConsts.HRSS_BANK:
                lenPk = 1;
                sql = "SELECT id_bank AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_BANK) + " WHERE b_del = 0 ORDER BY sort ";
                text = "banco";
                break;
            case SModConsts.HRSU_TP_EMP:
                lenPk = 1;
                sql = "SELECT id_tp_emp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EMP) + " WHERE b_del = 0 ORDER BY name, id_tp_emp ";
                text = "tipo empleado";
                break;
            case SModConsts.HRSU_TP_WRK:
                lenPk = 1;
                sql = "SELECT id_tp_wrk AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_WRK) + " WHERE b_del = 0 ORDER BY name, id_tp_wrk ";
                text = "tipo obrero";
                break;
            case SModConsts.HRSU_TP_MWZ:
                lenPk = 1;
                sql = "SELECT id_tp_mwz AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " WHERE b_del = 0 ORDER BY name, id_tp_mwz ";
                text = "área geográfica";
                break;
            case SModConsts.HRSU_DEP:
                lenPk = 1;
                sql = "SELECT id_dep AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " WHERE b_del = 0 ORDER BY name, id_dep ";
                text = "departamento";
                break;
            case SModConsts.HRSU_POS:
                lenPk = 1;
                sql = "SELECT id_pos AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " "
                        + "WHERE b_del = 0 "
                        + (pk != null ? " AND fk_dep = " + ((int) pk) : "") + " " 
                        + "ORDER BY name, id_pos ";
                text = "puesto";
                break;
            case SModConsts.HRSU_SHT:
                lenPk = 1;
                sql = "SELECT id_sht AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_SHT) + " WHERE b_del = 0 ORDER BY name, id_sht ";
                text = "turno";
                break;

            default:
                break;
        }

        return new Object[] { lenPk, isPkOnlyInts, lenFk, isFkOnlyInts, sql, text, isComplementApplying };
    }

    private static java.util.Vector<erp.lib.form.SFormComponentItem> getComponentItems(int pnCatalogue, java.sql.Statement poStatement, erp.mcfg.data.SDataParamsErp poParamsErp, java.lang.Object poPk, boolean pbAddFirstItem) {
        int i = 0;
        int j = 0;
        int nLenPk = 0;
        int nLenFk = 0;
        int[] anItemPk = null;
        int[] anItemFk = null;
        Object oItemPk = null;
        Object oItemFk = null;
        Object[] aoItemPk = null;
        Object[] aoItemFk = null;
        boolean bIsPkOnlyInts = true;
        boolean bIsFkOnlyInts = true;
        boolean bIsComplementApplying = false;
        String sSql = "";
        String sText = "";
        ResultSet oResultSet = null;
        Object[] aoSettings = null;
        SFormComponentItem oComponentItem = null;
        Vector<SFormComponentItem> vVector = new Vector<SFormComponentItem>();

        if (SDataUtilities.isCatalogueCfg(pnCatalogue)) {
            aoSettings = getSettingsCatCfg(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueUsr(pnCatalogue)) {
            aoSettings = getSettingsCatUsr(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueLoc(pnCatalogue)) {
            aoSettings = getSettingsCatLoc(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueBps(pnCatalogue)) {
            aoSettings = getSettingsCatBps(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueItm(pnCatalogue)) {
            aoSettings = getSettingsCatItm(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueFin(pnCatalogue)) {
            aoSettings = getSettingsCatFin(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueTrn(pnCatalogue)) {
            aoSettings = getSettingsCatTrn(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueTrnPur(pnCatalogue)) {

        }
        else if (SDataUtilities.isCatalogueTrnSal(pnCatalogue)) {

        }
        else if (SDataUtilities.isCatalogueTrnInv(pnCatalogue)) {

        }
        else if (SDataUtilities.isCatalogueMkt(pnCatalogue)) {
            aoSettings = getSettingsCatMkt(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueLog(pnCatalogue)) {
            aoSettings = getSettingsCatLog(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueMfg(pnCatalogue)) {
            aoSettings = getSettingsCatMfg(pnCatalogue, poParamsErp, poPk);
        }
        else if (SDataUtilities.isCatalogueHrs(pnCatalogue)) {
            aoSettings = getSettingsCatHrs(pnCatalogue, poParamsErp, poPk);
        }

        if (aoSettings != null) {
            nLenPk = (Integer) aoSettings[0];
            bIsPkOnlyInts = (Boolean) aoSettings[1];
            nLenFk = (Integer) aoSettings[2];
            bIsFkOnlyInts = (Boolean) aoSettings[3];
            sSql = (String) aoSettings[4];
            sText = (String) aoSettings[5];
            bIsComplementApplying = (Boolean) aoSettings[6];

            if (pbAddFirstItem) {
                vVector.add(new SFormComponentItem(new int[nLenPk], "(Seleccionar " + (sText.length() == 0 ? "opción" : sText) + ")", nLenFk == 0 ? null : new int[nLenFk]));
            }

            try {
                oResultSet = poStatement.executeQuery(sSql);
                while (oResultSet.next()) {
                    if (bIsPkOnlyInts) {
                        anItemPk = new int[nLenPk];
                        for (i = 0; i < anItemPk.length; i++) {
                            anItemPk[i] = oResultSet.getInt("f_id_" + (i + 1));
                        }
                        oItemPk = anItemPk;
                    }
                    else {
                        aoItemPk = new Object[nLenPk];
                        for (i = 0; i < aoItemPk.length; i++) {
                            aoItemPk[i] = oResultSet.getObject("f_id_" + (i + 1));
                        }
                        oItemPk = aoItemPk;
                    }

                    if (nLenFk == 0) {
                        oItemFk = null;
                    }
                    else {
                        if (bIsFkOnlyInts) {
                            anItemFk = new int[nLenFk];
                            for (i = 0; i < anItemFk.length; i++) {
                                anItemFk[i] = oResultSet.getInt("f_fid_" + (i + 1));
                            }
                            oItemFk = anItemFk;
                        }
                        else {
                            aoItemFk = new Object[nLenFk];
                            for (i = 0; i < aoItemFk.length; i++) {
                                aoItemFk[i] = oResultSet.getObject("f_fid_" + (i + 1));
                            }
                            oItemFk = aoItemFk;
                        }
                    }

                    oComponentItem = new SFormComponentItem(oItemPk, oResultSet.getString("f_item"), oItemFk);
                    if (bIsComplementApplying) {
                        oComponentItem.setComplement(oResultSet.getObject("f_comp"));
                    }

                    vVector.add(oComponentItem);
                }
            }
            catch (SQLException e) {
                SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
            }
        }

        return vVector;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param statement Database connection statement.
     * @param paramsErp ERP parameters object.
     * @param pk Primary key to be used as a filter. Can be null, it only applies in certain catalogues.
     * @return java.lang.Object[] Array of objects: primary key (PK) length [int], is PK of integers [boolean], foreign key (FK) length [int], is FK of integers [boolean], array of PK + item + FK [java.util.Vector].
     */
    public static java.util.Vector<erp.lib.form.SFormComponentItem> getComponentItemsForComboBox(int catalogue, java.sql.Statement statement, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        return getComponentItems(catalogue, statement, paramsErp, pk, true);
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param statement Database connection statement.
     * @param paramsErp ERP parameters object.
     * @param pk Primary key to be used as a filter. Can be null, it only applies in certain catalogues.
     * @return java.lang.Object[] Array of objects: primary key (PK) length [int], is PK of integers [boolean], foreign key (FK) length [int], is FK of integers [boolean], array of PK + item + FK [java.util.Vector].
     */
    public static java.util.Vector<erp.lib.form.SFormComponentItem> getComponentItemsForList(int catalogue, java.sql.Statement statement, erp.mcfg.data.SDataParamsErp paramsErp, java.lang.Object pk) {
        return getComponentItems(catalogue, statement, paramsErp, pk, false);
    }
}
