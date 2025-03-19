/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterUsers;
import java.awt.event.KeyEvent;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Edwin Carmona, Isabel Servín, Sergio Flores
 *
 */
public class SViewDpsAnnulled extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    private boolean mbHasRightAuthor = false;

    /**
     * View annuled documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsAnnulled(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNU_TP_DPS_ANN, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        mbHasRightAuthor = false;

        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUser);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDocumentNature);
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        aoTableColumns = new STableColumn[25];
        

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }
            

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "XML", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_dps_ann", "Causa de cancelación", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_dps_ann", "Causa de cancelación", 200);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "CFD", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());

            if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDIT_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDITED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private int getDpsSortingType() {
        int type = SLibConstants.UNDEFINED;

        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId();
        }
        else {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId();
        }

        return type;
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        sqlWhere += "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " ";

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, d.exc_rate, " +
                "d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, " +
                "d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, " +
                "d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
                "(SELECT tp_dps_ann FROM erp.trnu_tp_dps_ann where id_tp_dps_ann = d.fid_tp_dps_ann) AS tp_dps_ann,  " +
                "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "(SELECT CONCAT(src.num_ser, IF(length(src.num_ser) = 0, '', '-'), src.num) AS id_ped " +
                "FROM trn_dps AS src INNER JOIN trn_dps_dps_supply AS spl ON src.id_doc = spl.id_src_doc AND src.id_year = spl.id_src_year " +
                "WHERE spl.id_des_doc = d.id_doc AND src.id_year = d.id_year AND src.b_del = 0 LIMIT 1) AS f_ord_num, " +
                "(SELECT CONCAT(ord.id_year, '-', ord.num) FROM mfg_ord AS ord WHERE d.fid_mfg_year_n = ord.id_year AND d.fid_mfg_ord_n = ord.id_ord) AS num_ord, " +
                "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                "IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL  + ", " + /* not is CFD not is CFDI */
                "IF(x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                "IF(LENGTH(xc.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                "IF(xc.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                ")))))) AS f_ico_xml, " +
                "bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr ";

        msSql +=
                "FROM trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " +
                (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";
        
        String complementaryDbName = "";
        
        try {
            complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        msSql +=
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n " +
                "LEFT OUTER JOIN " + complementaryDbName + ".trn_cfd AS xc ON x.id_cfd = xc.id_cfd ";
        
                if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC) {
                    msSql += "WHERE " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " " :
                                        "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ");
                }
                else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ) {
                    msSql += "WHERE " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] + " " :
                                        "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2] + " ");
                }
                msSql += (sqlWhere.length() == 0 ? "" : "AND " + sqlWhere);

        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            msSql += "ORDER BY ";
            if ((mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }
            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb, dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt ";
        }
        else {
            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";

            if ((mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }
            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb ";
        }
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}
