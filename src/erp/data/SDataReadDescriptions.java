/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.sql.ResultSet;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataReadDescriptions {

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param pk Primary key.
     * @param fieldType Constants defined in erp.lib.SLibConstants.
     */
    public static java.lang.String createQueryForField(int catalogue, java.lang.Object pk, int fieldType) {
        String sql = "";

        switch (catalogue) {
            case SDataConstants.MFGS_ST_ORD:
                switch (fieldType) {
                    case SLibConstants.FIELD_DEL:
                        sql = "SELECT b_del FROM erp.mfgs_st_ord WHERE id_st = " + ((int[]) pk)[0] + " ";
                    default:
                }
                break;
            case SDataConstants.TRNU_TP_PAY_SYS:
                switch (fieldType) {
                    case SLibConstants.FIELD_TYPE_TEXT:
                        sql = "SELECT tp_pay_sys FROM erp.trnu_tp_pay_sys WHERE id_tp_pay_sys = " + ((int[]) pk)[0] + " ";
                    default:
                }
                break;
            default:
        }

        return sql;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param pk Primary key.
     * @param descriptionType Constants defined in erp.lib.SLibConstants.
     */
    public static java.lang.String createQueryForCatalogue(int catalogue, java.lang.Object pk, int descriptionType) {
        String sql = "";

        switch (catalogue) {
            case SDataConstants.USRU_USR:
                sql = "SELECT bp.bp AS descrip FROM erp.usru_usr AS u LEFT OUTER JOIN erp.bpsu_bp AS bp ON " +
                        "u.fid_bp_n = bp.id_bp WHERE u.id_usr = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.CFGS_CT_ENT:
                sql = "SELECT ct_ent AS descrip FROM erp.cfgs_ct_ent WHERE id_ct_ent = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.CFGS_TP_ENT:
                sql = "SELECT tp_ent AS descrip FROM erp.cfgs_tp_ent WHERE id_ct_ent = " + ((int[]) pk)[0] + " AND id_tp_ent = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.CFGU_CUR:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "cur_key" : "cur") + " AS descrip FROM erp.cfgu_cur WHERE id_cur = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.CFGU_COB_ENT:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "ent") + " AS descrip FROM erp.cfgu_cob_ent WHERE id_cob = " + ((int[]) pk)[0] + " AND id_ent = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.LOCU_CTY:
                switch (descriptionType) {
                    case SLibConstants.DESCRIPTION_CODE:
                        sql = "cty_code";
                        break;
                    case SLibConstants.DESCRIPTION_NAME:
                        sql = "cty";
                        break;
                    case SLibConstants.DESCRIPTION_NAME_ABBR:
                        sql = "cty_abbr";
                        break;
                    case SLibConstants.DESCRIPTION_NAME_LAN:
                        sql = "cty_lan";
                        break;
                    default:
                        sql = "cty";
                }
                    
                sql = "SELECT " + sql + " AS descrip FROM erp.locu_cty WHERE " + (pk instanceof int[] ? "id_cty = " + ((int[]) pk)[0] : "cty_code = '" + ((String) pk) + "'") + " ";
                break;
            case SDataConstants.BPSS_CT_BP:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "ct_bp") + " AS descrip FROM erp.bpss_ct_bp WHERE id_ct_bp = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSS_TP_CON:
                sql = "SELECT tp_con AS descrip FROM erp.bpss_tp_con WHERE id_tp_con = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSS_TP_TEL:
                sql = "SELECT tp_tel AS descrip FROM erp.bpss_tp_tel WHERE id_tp_tel = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSS_TP_CRED:
                sql = "SELECT tp_cred AS descrip FROM erp.bpss_tp_cred WHERE id_tp_cred = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSU_BP:
                sql = "SELECT bp AS descrip FROM erp.bpsu_bp WHERE id_bp = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSU_BPB:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "bpb") + " AS descrip FROM erp.bpsu_bpb WHERE id_bpb = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.BPSU_BANK_ACC:
                sql = "SELECT CONCAT(a.bank_acc, ' (', b.bp_comm, ')') AS descrip " +
                        "FROM erp.bpsu_bank_acc AS a " +
                        "INNER JOIN erp.bpsu_bp AS b ON a.fid_bank = b.id_bp " +
                        "WHERE a.b_del = FALSE AND a.id_bpb = " + ((int[]) pk)[0] + " AND a.id_bank_acc = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.ITMS_CT_ITEM:
                sql = "SELECT ct_item AS descrip FROM erp.itms_ct_item WHERE id_ct_item = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.ITMS_CL_ITEM:
                sql = "SELECT cl_item AS descrip FROM erp.itms_cl_item WHERE id_ct_item = " + ((int[]) pk)[0] + " AND id_cl_item = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.ITMS_TP_ITEM:
                sql = "SELECT tp_item AS descrip FROM erp.itms_tp_item WHERE id_ct_item = " + ((int[]) pk)[0] + " AND id_cl_item = " + ((int[]) pk)[1] + " AND id_tp_item = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.ITMU_IGEN:
                sql = "SELECT igen AS descrip FROM erp.itmu_igen WHERE id_igen = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.ITMU_ITEM:
                sql = "SELECT item AS descrip FROM erp.itmu_item WHERE id_item = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.ITMU_TP_UNIT:
                sql = "SELECT "+ (descriptionType == SLibConstants.DESCRIPTION_CODE ? "unit_base" : "tp_unit") + " AS descrip FROM erp.itmu_tp_unit WHERE id_tp_unit = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.ITMU_UNIT:
                sql = "SELECT "+ (descriptionType == SLibConstants.DESCRIPTION_CODE ? "symbol" : "unit") + " AS descrip FROM erp.itmu_unit WHERE id_unit = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_TP_BKR:
                sql = "SELECT tp_bkr AS descrip FROM erp.fins_tp_bkr WHERE id_tp_bkr = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_TP_ACC:
                sql = "SELECT tp_acc AS descrip FROM erp.fins_tp_acc WHERE id_tp_acc = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_CL_ACC:
                sql = "SELECT cl_acc AS descrip FROM erp.fins_cl_acc WHERE id_tp_acc = " + ((int[]) pk)[0] + " AND id_cl_acc = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.FINS_CLS_ACC:
                sql = "SELECT cls_acc AS descrip FROM erp.fins_cls_acc WHERE id_tp_acc = " + ((int[]) pk)[0] + " AND id_cl_acc = " + ((int[]) pk)[1] + " AND id_cls_acc = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.FINS_TP_ACC_MOV:
                sql = "SELECT tp_acc_mov AS descrip FROM erp.fins_tp_acc_mov WHERE id_tp_acc_mov = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_CL_ACC_MOV:
                sql = "SELECT cl_acc_mov AS descrip FROM erp.fins_cl_acc_mov WHERE id_tp_acc_mov = " + ((int[]) pk)[0] + " AND id_cl_acc_mov = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.FINS_CLS_ACC_MOV:
                sql = "SELECT cls_acc_mov AS descrip FROM erp.fins_cls_acc_mov WHERE id_tp_acc_mov = " + ((int[]) pk)[0] + " AND id_cl_acc_mov = " + ((int[]) pk)[1] + " AND id_cls_acc_mov = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.FINS_TP_ACC_SYS:
                sql = "SELECT tp_acc_sys AS descrip FROM erp.fins_tp_acc_sys WHERE id_tp_acc_sys = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_TP_ACC_BP:
                sql = "SELECT tp_acc_bp AS descrip FROM erp.fins_tp_acc_bp WHERE id_tp_acc_bp = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_TP_ACC_ITEM:
                sql = "SELECT tp_acc_item AS descrip FROM erp.fins_tp_acc_item WHERE id_tp_acc_item = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINS_TP_CARD:
                sql = "SELECT tp_card AS descrip FROM erp.fins_tp_card WHERE id_tp_card = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINU_TAX_IDY:
                sql = "SELECT tax_idy AS descrip FROM erp.finu_tax_idy WHERE id_tax_idy = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINU_TAX_REG:
                sql = "SELECT tax_reg AS descrip FROM erp.finu_tax_reg WHERE id_tax_reg = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINU_TAX_BAS:
                sql = "SELECT tax_bas AS descrip FROM erp.finu_tax_bas WHERE id_tax_bas = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINU_TAX:
                sql = "SELECT tax AS descrip FROM erp.finu_tax WHERE id_tax_bas = " + ((int[]) pk)[0] + " AND id_tax = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.FINU_TP_ACC_USR:
                sql = "SELECT tp_acc_usr AS descrip FROM erp.finu_tp_acc_usr WHERE id_tp_acc_usr = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FINU_CL_ACC_USR:
                sql = "SELECT cl_acc_usr AS descrip FROM erp.finu_cl_acc_usr WHERE id_tp_acc_usr = " + ((int[]) pk)[0] + " AND id_cl_acc_usr = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.FINU_CLS_ACC_USR:
                sql = "SELECT cls_acc_usr AS descrip FROM erp.finu_cls_acc_usr WHERE id_tp_acc_usr = " + ((int[]) pk)[0] + " AND id_cl_acc_usr = " + ((int[]) pk)[1] + " AND id_cls_acc_usr = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.FINU_TP_LAY_BANK:
                sql = "SELECT tp_lay_bank AS descrip FROM erp.finu_tp_lay_bank WHERE id_tp_lay_bank = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.FIN_ACC:
                sql = "SELECT acc AS descrip FROM fin_acc WHERE id_acc = '" + ((Object[]) pk)[0] + "' ";
                break;
            case SDataConstants.FIN_CC:
                sql = "SELECT cc AS descrip FROM fin_cc WHERE id_cc = '" + ((Object[]) pk)[0] + "' ";
                break;
            case SDataConstants.FIN_BKC:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "bkc") + " AS descrip FROM fin_bkc WHERE id_bkc = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_TP_PAY:
                sql = "SELECT tp_pay AS descrip FROM erp.trns_tp_pay WHERE id_tp_pay = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_ST_DPS:
                sql = "SELECT st_dps AS descrip FROM erp.trns_st_dps WHERE id_st_dps = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_ST_DPS_VAL:
                sql = "SELECT st_dps_val AS descrip FROM erp.trns_st_dps_val WHERE id_st_dps_val = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_ST_DPS_AUTHORN:
                sql = "SELECT st_dps_authorn AS descrip FROM erp.trns_st_dps_authorn WHERE id_st_dps_authorn = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_CT_DPS:
                sql = "SELECT ct_dps AS descrip FROM erp.trns_ct_dps WHERE id_ct_dps = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_CT_IOG:
                sql = "SELECT ct_iog AS descrip FROM erp.trns_ct_iog WHERE id_ct_iog = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_CL_DPS:
                sql = "SELECT cl_dps AS descrip FROM erp.trns_cl_dps WHERE id_ct_dps = " + ((int[]) pk)[0] + " AND id_cl_dps = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.TRNS_CL_IOG:
                sql = "SELECT cl_iog AS descrip FROM erp.trns_cl_iog WHERE id_ct_iog = " + ((int[]) pk)[0] + " AND id_cl_iog = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.TRNS_TP_DPS_ADJ:
                sql = "SELECT tp_dps_adj AS descrip FROM erp.trns_tp_dps_adj WHERE id_tp_dps_adj = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_STP_DPS_ADJ:
                sql = "SELECT stp_dps_adj AS descrip FROM erp.trns_stp_dps_adj WHERE id_tp_dps_adj = " + ((int[]) pk)[0] + " AND id_stp_dps_adj = " + ((int[]) pk)[1] + " ";
                break;
            case SDataConstants.TRNS_TP_DPS_ETY:
                sql = "SELECT tp_dps_ety AS descrip FROM erp.trns_tp_dps_ety WHERE id_tp_dps_ety = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNS_TP_CFD:
                sql = "SELECT tp_cfd AS descrip FROM erp.trns_tp_cfd WHERE id_tp_cfd = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNU_DPS_NAT:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "dps_nat") + " AS descrip FROM erp.trnu_dps_nat WHERE id_dps_nat = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.TRNU_TP_DPS:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "tp_dps") + " AS descrip FROM erp.trnu_tp_dps WHERE id_ct_dps = " + ((int[]) pk)[0] + " AND id_cl_dps = " + ((int[]) pk)[1] + " AND id_tp_dps = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.TRNS_TP_IOG:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "tp_iog") + " AS descrip FROM erp.trns_tp_iog WHERE id_ct_iog = " + ((int[]) pk)[0] + " AND id_cl_iog = " + ((int[]) pk)[1] + " AND id_tp_iog = " + ((int[]) pk)[2] + " ";
                break;
            case SDataConstants.TRN_DPS:
                sql = "SELECT CONCAT(td.code,' ',CONCAT(num_ser, IF(length(num_ser) = 0, '', '-'), num)) AS descrip FROM trn_dps AS d INNER JOIN erp.TRNU_TP_DPS AS td ON d.fid_ct_dps = td.id_ct_dps " +
                       "AND d.fid_cl_dps = td.id_cl_dps AND d.fid_tp_dps = td.id_tp_dps WHERE id_year = " + ((int[]) pk)[0] +" AND id_doc = " + ((int[]) pk)[1] + " ";
                break;    
            case SDataConstants.TRN_DPS_ADD:
                sql = "SELECT tp_cfd_add FROM erp.bpss_tp_cfd_add WHERE id_tp_cfd_add = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstants.MFGU_TP_ORD:
                sql = "SELECT tp AS descrip FROM erp.mfgu_tp_ord WHERE id_tp = " + ((int[]) pk)[0] + " ";
                break;
            case SModConsts.CFGU_FUNC:
                sql = "SELECT " + (descriptionType == SLibConstants.DESCRIPTION_CODE ? "code" : "name") + " AS descrip FROM cfgu_func WHERE id_func = " + ((int[]) pk)[0] + " ";
                break;
            case SModConsts.LOGS_TP_MOT:
                sql = "SELECT name AS descrip FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE id_tp_mot = " + ((int[]) pk)[0] + " ";
                break;
            case SModConsts.LOGS_TP_CAR:
                sql = "SELECT name AS descrip FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE id_tp_car = " + ((int[]) pk)[0] + " ";
                break;
            case SModConsts.LOGS_INC:
                sql = "SELECT name AS descrip FROM " + SModConsts.TablesMap.get(catalogue) + " WHERE id_inc = " + ((int[]) pk)[0] + " ";
                break;
            default:
        }

        return sql;
    }

    /**
     * @param linkType Constants defined in erp.data.SDataConstantsSys.
     * @param pk Primary key.
     */
    public static java.lang.String createQueryForLinkReference(int linkType, java.lang.Object pk) {
        String sql = "";

        switch (linkType) {
            case SDataConstantsSys.TRNS_TP_LINK_ALL:
                sql = "SELECT '" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "' AS descrip ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                sql = "SELECT ct_item AS descrip FROM erp.itms_ct_item WHERE ct_idx = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                sql = "SELECT cl_item AS descrip FROM erp.itms_cl_item WHERE cl_idx = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                sql = "SELECT tp_item AS descrip FROM erp.itms_tp_item WHERE tp_idx = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                sql = "SELECT ifam AS descrip FROM erp.itmu_ifam WHERE id_ifam = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                sql = "SELECT igrp AS descrip FROM erp.itmu_igrp WHERE id_igrp = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                sql = "SELECT igen AS descrip FROM erp.itmu_igen WHERE id_igen = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_LINE:
                sql = "SELECT line AS descrip FROM erp.itmu_line WHERE id_line = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_BRD:
                sql = "SELECT brd AS descrip FROM erp.itmu_brd WHERE id_brd = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_MFR:
                sql = "SELECT mfr AS descrip FROM erp.itmu_mfr WHERE id_mfr = " + ((int[]) pk)[0] + " ";
                break;
            case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                sql = "SELECT item AS descrip FROM erp.itmu_item WHERE id_item = " + ((int[]) pk)[0] + " ";
                break;
            default:
                break;
        }

        return sql;
    }

    /**
     * @param client Client interface.
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param pk Primary key.
     * @param fieldType Constants defined in erp.lib.SLibConstants.
     */
    @SuppressWarnings("unchecked")
    public static java.lang.Object getField(erp.client.SClientInterface client, int catalogue, java.lang.Object pk, int fieldType) {
        Object field = null;
        ResultSet resulSet = null;

        try {
            resulSet = client.getSession().getStatement().executeQuery(createQueryForField(catalogue, pk, fieldType));
            while (resulSet.next()) {
                field = resulSet.getObject(1);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SDataReadDescriptions.class.getName(), e);
        }

        return field;
    }

    /**
     * @param client Client interface.
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param pk Primary key.
     */
    public static java.lang.String getCatalogueDescription(erp.client.SClientInterface client, int catalogue, java.lang.Object pk) {
        return getCatalogueDescription(client, catalogue, pk, SLibConstants.DESCRIPTION_NAME);
    }

    /**
     * @param client Client interface.
     * @param catalogue Constants defined in erp.data.SDataConstants.
     * @param pk Primary key.
     * @param descriptionType Constants defined in erp.lib.SLibConstants.
     */
    public static java.lang.String getCatalogueDescription(erp.client.SClientInterface client, int catalogue, java.lang.Object pk, int descriptionType) {
        String descrip = "";
        SServerRequest request = null;
        SServerResponse response = null;

        try {
            request = new SServerRequest(SServerConstants.REQ_DB_CATALOGUE_DESCRIPTION, createQueryForCatalogue(catalogue, pk, descriptionType));
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                descrip = (String) response.getPacket();
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SDataReadDescriptions.class.getName(), e);
        }

        return descrip;
    }

    /**
     * @param linkType Constants defined in erp.data.SDataConstantsSys.
     * @param pk Primary key.
     */
    public static java.lang.String getLinkReferenceDescription(erp.client.SClientInterface client, int linkType, java.lang.Object pk) {
        String description = "";
        SServerRequest request = null;
        SServerResponse response = null;

        try {
            request = new SServerRequest(SServerConstants.REQ_DB_CATALOGUE_DESCRIPTION, createQueryForLinkReference(linkType, pk));
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                description = (String) response.getPacket();
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SDataReadDescriptions.class.getName(), e);
        }

        return description;
    }
}
