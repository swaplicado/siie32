/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItemGeneric;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import javax.swing.JButton;
import sa.lib.SLibUtils;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewBackorder extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private boolean mbIsByDoc;
    private boolean mbIsByItem;
    private boolean mbIsByItemBp;
    private boolean mbIsByItemBpBra;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.mitm.form.SPanelFilterItemGeneric moPanelFilterItemGeneric;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    public SViewBackorder(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_BACKORDER, auxType01, auxType02);
        mnTabTypeAux01 = auxType01;
        mnTabTypeAux02 = auxType02;
        
        mbIsByDoc = SLibUtils.belongsTo(mnTabTypeAux01, new int[] { 
            SDataConstantsSys.TRNX_PUR_BACKORDER_CON, 
            SDataConstantsSys.TRNX_PUR_BACKORDER_ORD, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_CON, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_ORD });
        mbIsByItem = SLibUtils.belongsTo(mnTabTypeAux01, new int[] { 
            SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM, 
            SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM });
        mbIsByItemBp = SLibUtils.belongsTo(mnTabTypeAux01, new int[] { 
            SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP, 
            SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP });
        mbIsByItemBpBra = SLibUtils.belongsTo(mnTabTypeAux01, new int[] { 
            SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP_BRA, 
            SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP_BRA, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP_BRA, 
            SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP_BRA });
        
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableColumn[] aoTableColumns = null;

        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.addActionListener(this);
        mjbViewNotes.setToolTipText("Ver notas");

        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.addActionListener(this);
        mjbViewLinks.setToolTipText("Ver vínculos del documento");

        moTabFilterDate = new STabFilterDate(miClient, this);
        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moPanelFilterItemGeneric = new SPanelFilterItemGeneric(miClient, this);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterItemGeneric);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDocumentNature);
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        mjbViewNotes.setEnabled(true);
        mjbViewLinks.setEnabled(true);

        STableField[] aoKeyFields = new STableField[2];

        if (mbIsByDoc) {
            aoTableColumns = new STableColumn[27];
        }
        else if (mbIsByItem) {
            aoTableColumns = new STableColumn[5];
        }
        else if (mbIsByItemBp) {
            aoTableColumns = new STableColumn[7];
        }
        else if (mbIsByItemBpBra) {
            aoTableColumns = new STableColumn[8];
        }

        i = 0;
        if (mbIsByDoc) {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_doc");
        }
        else {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        }
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (mbIsByDoc) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Suc. empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_status", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "seg.mkt_segm", "Segmento mercado", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sseg.mkt_segm_sub", "Subsegmento mercado", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agt.bp", "Agente venta", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.locality", "Localidad", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.county", "Municipio", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.state", "Estado", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cty_abbr", "País", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "de.sort_pos", "No.", STableConstants.WIDTH_NUM_TINYINT);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Un.", STableConstants.WIDTH_UNIT_SYMBOL);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.price_u", "Precio u. $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitaryFixed4());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.stot_r", "Importe doc. $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.mass", "Peso neto", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMass());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_supply", "Cant. surt.", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_pend", "Cant. pend.", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_pend", "Importe pendiente $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dn.code", "Naturaleza doc.", STableConstants.WIDTH_CODE_DOC);
        }
        else if (mbIsByItem) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Un.", STableConstants.WIDTH_UNIT_SYMBOL);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_pend", "Cant. pend.", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_pend", "Importe pendiente $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Un.", STableConstants.WIDTH_UNIT_SYMBOL);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
            if (mbIsByItemBpBra) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal", 75);
            }
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_pend", "Cant. pend.", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_pend", "Importe pendiente $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        }

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(SDataConstants.TRN_DPS);

        populateTable();
    }

    private boolean isPurchases() {
        boolean bIsPurchase = false;

        if (SLibUtils.belongsTo(mnTabTypeAux01, new int[] {
            SDataConstantsSys.TRNX_PUR_BACKORDER_CON, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP_BRA, 
            SDataConstantsSys.TRNX_PUR_BACKORDER_ORD, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP_BRA })) {
            bIsPurchase = true;
        }

        return bIsPurchase;
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlWhereDate = "";
        String sqlWhereDateSubquery = "";
        String sqlCompanyBranch = "";
        String sqlItemGeneric = "";
        String sqlFunctAreas = "";
        String sqlWhere = "";
        java.util.Date date = null;
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                date = (java.util.Date) setting.getSetting();
                sqlWhereDate += (sqlWhereDate.length() == 0 ? "AND " : "") + "d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' ";
                sqlWhereDateSubquery += (sqlWhereDateSubquery.length() == 0 ? "AND " : "") + "t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM_GENERIC) {
                if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                    sqlItemGeneric = "AND i.fid_igen = " + ((int[]) setting.getSetting())[0] + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.isEmpty() ? "AND " : "") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        if (mbIsByDoc) {
            msSql = "SELECT de.id_year, de.id_doc, de.id_ety, i.item_key, i.item, de.qty, de.price_u, de.stot_r, de.mass, de.sort_pos, " +
                "d.dt, d.dt_doc_delivery_n, d.num_ser, d.num, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, dn.code, " +
                "CASE d.fid_st_dps_authorn " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING + " THEN " + STableConstants.ICON_ST_WAIT + " " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + " THEN " + STableConstants.ICON_ST_THUMBS_UP + " " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + " THEN " + STableConstants.ICON_ST_THUMBS_DOWN + " " +
                "ELSE " + STableConstants.ICON_NULL + " END AS f_status, dt.code, bp.bp, " +
                "cob.code, ad.locality, ad.county, ad.state, COALESCE(cty.cty_abbr, '" + miClient.getSession().getSessionCustom().getLocalCountryCode() + "') AS f_cty_abbr, u.symbol, d.num_ref, agt.bp, seg.mkt_segm, sseg.mkt_segm_sub, " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ") AS f_qty_supply, " +
                "de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ") AS f_qty_pend, " +
                "((de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ")) * de.price_u) AS f_stot_pend " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND d.fid_ct_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[0] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) +
                " AND d.fid_cl_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[1] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) +
                " AND d.fid_tp_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +
                "INNER JOIN erp.trnu_dps_nat AS dn ON d.fid_dps_nat = dn.id_dps_nat " +
                "INNER JOIN erp.itmu_item AS i ON " +
                "de.fid_item = i.id_item " + sqlItemGeneric +
                "INNER JOIN erp.itmu_unit AS u ON " +
                "de.fid_unit = u.id_unit " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "d.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.bpsu_bpb_add AS ad ON " +
                "d.fid_bpb = ad.id_bpb AND d.fid_add = ad.id_add " +
                "LEFT OUTER JOIN erp.locu_cty AS cty ON " +
                "ad.fid_cty_n = cty.id_cty " +
                "LEFT OUTER JOIN erp.bpsu_bp AS agt ON " +
                "d.fid_sal_agt_n = agt.id_bp " +
                "LEFT OUTER JOIN mkt_cfg_cus AS mk ON " +
                "d.fid_bp_r = mk.id_cus " +
                "LEFT OUTER JOIN mktu_mkt_segm AS seg ON " +
                "mk.fid_mkt_segm = seg.id_mkt_segm " +
                "LEFT OUTER JOIN mktu_mkt_segm_sub AS sseg ON " +
                "mk.fid_mkt_segm = sseg.id_mkt_segm AND mk.fid_mkt_sub = sseg.id_mkt_sub " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " +
                SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_link = 0 " + sqlWhereDate + " " +
                (sqlCompanyBranch.length() == 0 ? "" : " AND " + sqlCompanyBranch) + " " +
                (sqlWhere.length() == 0 ? "" : sqlWhere) +
                "HAVING f_qty_pend > 0 " +
                "ORDER BY d.dt, dt.code, d.num_ser, d.num ";
        }
        else if (mbIsByItem) {
            msSql = "SELECT id_year, id_doc, id_ety, item_key, item, symbol, SUM(f_qty_pend) AS f_qty_pend, SUM(f_stot_pend) AS f_stot_pend " +
                "FROM(" +
                "SELECT de.id_year, de.id_doc, de.id_ety, i.item_key, i.item, u.symbol, " +
                "(de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ")) AS f_qty_pend, " +
                "(((de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ")) * de.price_u)) AS f_stot_pend "+
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND d.fid_ct_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[0] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) +
                " AND d.fid_cl_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[1] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) +
                " AND d.fid_tp_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +
                "INNER JOIN erp.itmu_item AS i ON " +
                "de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON " +
                "de.fid_unit = u.id_unit " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " +
                SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_link = 0 " + sqlWhereDate + " " +
                (sqlCompanyBranch.length() == 0 ? "" : " AND " + sqlCompanyBranch) + " " +
                (sqlWhere.length() == 0 ? "" : sqlWhere) +
                "HAVING f_qty_pend > 0 " +
                "ORDER BY i.item_key) AS t " +
                "GROUP BY item_key " +
                "ORDER BY item_key; ";
        }
        else {
            msSql = "SELECT id_year, id_doc, id_ety, item_key, item, symbol, bp_key, bp, " + (!mbIsByItemBpBra ? "" : "bpb, ") +
                "SUM(f_qty_pend) AS f_qty_pend, SUM(f_stot_pend) AS f_stot_pend " +
                "FROM(" +
                "SELECT de.id_year, de.id_doc, de.id_ety, i.item_key, i.item, u.symbol, ct.bp_key, bp.bp, " + (!mbIsByItemBpBra ? "" : "bpb.bpb, ") +
                "(de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ")) AS f_qty_pend, " +
                "(((de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) FROM trn_dps_dps_supply AS s INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND " +
                "s.id_des_doc = t.id_doc INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND " +
                "s.id_des_ety = te.id_ety WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = 0 AND te.b_del = 0 " + sqlWhereDateSubquery +
                ")) * de.price_u)) AS f_stot_pend "+
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND d.fid_ct_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[0] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) +
                " AND d.fid_cl_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[1] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) +
                " AND d.fid_tp_dps = " +
                (isPurchases() ?
                    mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] : SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] :
                        mnTabTypeAux02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                (!mbIsByItemBpBra ? "" : "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb ") +
                "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                "ct.id_bp = bp.id_bp AND ct.id_ct_bp = " + (isPurchases() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " +
                SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_link = 0 " + sqlWhereDate + " " +
                (sqlCompanyBranch.length() == 0 ? "" : " AND " + sqlCompanyBranch) + " " +
                (sqlWhere.length() == 0 ? "" : sqlWhere) +
                "HAVING f_qty_pend > 0 " +
                "ORDER BY i.item_key, ct.bp_key ) AS t " +
                "GROUP BY item_key, bp_key" + (!mbIsByItemBpBra ? "" : ", bpb ") + " " +
                "ORDER BY item_key, bp_key" + (!mbIsByItemBpBra ? "" : ", bpb ") + "; ";
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
        }
    }
}
