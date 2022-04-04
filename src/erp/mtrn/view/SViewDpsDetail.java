/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.SClientUtils;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItem;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import java.awt.Dimension;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import erp.lib.table.STabFilterDatePeriod;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Claudio Peña
 */
public class SViewDpsDetail extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.mitm.form.SPanelFilterItem moPanelFilterItem;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    private boolean mbHasRightAuthor = false;

    /**
     * View detailed quote.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param type Constants defined in SDataConstants (TRNX_DPS_LINK_PEND..., TRNX_DPS_LINKED...).
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsDetail(erp.client.SClientInterface client, java.lang.String tabTitle, int type, int auxType01, int auxType02) {
        super(client, tabTitle, type, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        boolean hasRightToClose = false;
        boolean hasRightToOpen = false;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mbHasRightAuthor = false;

        if (isViewForCategoryPur()) {
            if (isViewForEstimate()) {
                hasRightToClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level >= SUtilConsts.LEV_EDITOR ||
                        miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_DOC_EST_CLO);
                hasRightToOpen = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
            else if (isViewForOrder()) {
                hasRightToClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level >= SUtilConsts.LEV_EDITOR ||
                        miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_DOC_ORD_CLO);
                hasRightToOpen = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
            }
        }
        else {
            if (isViewForEstimate()) {
                hasRightToClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level >= SUtilConsts.LEV_EDITOR ||
                        miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_SAL_DOC_EST_CLO);
                hasRightToOpen = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
            else if (isViewForOrder()) {
                hasRightToClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level >= SUtilConsts.LEV_EDITOR ||
                        miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_SAL_DOC_ORD_CLO);
                hasRightToOpen = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
            }
        }
        
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));

        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);

        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");

        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moPanelFilterItem = new SPanelFilterItem(miClient, this);
         
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperComponent(moPanelFilterItem);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();

        mjbViewNotes.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewLinks.setEnabled(true);
            
        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[38];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio documento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 40);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "bp_key", "Cantidad", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dety.id_ety", "#", 20);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dety.concept_key", "Clave", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dety.concept", "Concepto", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "NoParte", "No. parte", 100);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.qty", "Cantidad partida ", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit", "Unidad", 30);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.price_u", "Precio u $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.stot_prov_r", "Subtotal $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.tax_charged_r", "Impto tras $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.tax_retained_r", "Impto ret $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dety.tot_r", "Total partida $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ItemRef", "Ítem referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "Cuenta", "No. centro costo", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "NombreCuenta", "Centro costo", 100);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_r", "Subtotal Cot $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_r", "Impto Cot tras $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_r", "Impto Cot ret $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total Cot $", 80);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda Cot", 80);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.exc_rate", "T cambio", 80);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_fa_code", "Área funcional", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_link", "Vinculación", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. elimincación", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Elimincación", 100);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        setIsSummaryApplying(true);
        
        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DVY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINK_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINKED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private boolean isViewForCategoryPur() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }

    private boolean isViewForEstimate() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST || mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON;
    }

    private boolean isViewForOrder() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
    }

    private int[] getDpsTypeKey() {
        int[] dpsTypeKey = null;
        dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_EST;
         
        return dpsTypeKey;
    }
    
    private void actionViewDps() {
        int gui = isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
        int[] dpsTypeKey = getDpsTypeKey();

        miClient.getGuiModule(gui).setFormComplement(dpsTypeKey);
        miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, moTablePane.getSelectedTableRow().getPrimaryKey());
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
        try {
            int[] dpsTypeKey = getDpsTypeKey();
            String sqlDpsType = "";
            String sqlDate = "";
            String sqlBizPartner = "";
            String sqlItemGeneric = "";
            STableSetting setting = null;
            
            for (int i = 0; i < mvTableSettings.size(); i++) {
                setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
                
                if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                    sqlDate += (setting.getSetting() == null ?
                            ("AND " + SDataSqlUtilities.composePeriodFilter((int[]) SLibTimeUtilities.digestYearMonth(miClient.getSession().getCurrentDate()), "d.dt"))
                            : ("AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt")));
                }
                else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                    sqlBizPartner = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND bp.id_bp = " + (Integer) setting.getSetting() + " ");
                }
                else if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM) {
                    if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                        sqlItemGeneric = "AND dety.fid_item = " + ((int[]) setting.getSetting())[0] + " ";
                    }
                }
            }
            
            sqlDpsType = "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " ";
            
            msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, dety.id_ety, dety.concept_key, dety.concept, " +
                    "dety.qty, dety.fid_unit, dety.price_u, dety.stot_prov_r, dety.tax_charged_r, dety.tot_r, dety.fid_item_ref_n, dety.fid_cc_n, " +
                    "(SELECT it.item FROM erp.itmu_item AS it where it.id_item = dety.fid_item_ref_n) AS ItemRef, " +
                    "(SELECT it.part_num FROM erp.itmu_item AS it where it.id_item = dety.fid_item) AS NoParte, " +
                    "(SELECT cc.id_cc FROM FIN_CC AS cc WHERE cc.id_cc = dety.fid_cc_n ) AS Cuenta, " +
                    "(SELECT cc.cc FROM FIN_CC AS cc WHERE cc.id_cc = dety.fid_cc_n ) AS NombreCuenta, " +
                    "d.exc_rate, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, d.stot_cur_r, d.tax_charged_cur_r, " +
                    "d.tax_retained_cur_r, d.tot_cur_r, d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, " +
                    "d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
                    "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, dety.tax_retained_r, dety.fid_item, " +
                    "(SELECT symbol FROM erp.ITMU_UNIT AS uni WHERE uni.id_unit = dety.fid_unit) AS unit, " +
                    "(SELECT fa.code FROM cfgu_func AS fa WHERE d.fid_func = fa.id_func) AS f_fa_code, " +
                    "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, " +
                    "(SELECT CONCAT(dps_src.num_ser, IF(LENGTH(dps_src.num_ser) = 0, '', '-'), dps_src.num) FROM " +
                    "trn_dps AS dps_src INNER JOIN trn_dps_dps_supply AS spl ON dps_src.id_doc = spl.id_src_doc AND dps_src.id_year = spl.id_src_year " +
                    "WHERE spl.id_des_doc = d.id_doc AND dps_src.id_year = d.id_year AND dps_src.b_del = 0 LIMIT 1) AS f_ord_num, " +
                    "(SELECT de.concept FROM trn_dps_ety AS de WHERE de.id_doc = d.id_doc AND de.id_year = d.id_year " +
                    "AND NOT de.b_del ORDER BY de.id_ety LIMIT 1) AS f_concept, " +
                    "(SELECT CONCAT(mo.id_year, '-', mo.num) FROM mfg_ord AS mo " +
                    "WHERE d.fid_mfg_year_n = mo.id_year AND d.fid_mfg_ord_n = mo.id_ord) AS f_mfg_ord, " +
                    "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                    "IF((x.ts IS NULL OR doc_xml = '') AND p.doc_pdf_name IS NULL, " + STableConstants.ICON_NULL  + ", IF((x.ts IS NULL OR doc_xml = '') " +
                    "AND p.doc_pdf_name IS NOT NULL, " + STableConstants.ICON_PDF + ", " +
                    "IF((x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ") AND p.doc_pdf_name IS NULL, " + STableConstants.ICON_XML + ", " +
                    "IF((x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ") AND p.doc_pdf_name IS NOT NULL, " + STableConstants.ICON_XML_PDF + ", " +
                    "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " +
                    "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " +
                    "IF(LENGTH(xc.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", IF(xc.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " +
                    " " + STableConstants.ICON_XML_SIGN + ")))))))) AS f_ico_xml, " +
                    "x.ts_prc, x.can_st, bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                    "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, 'MXN' AS f_cur_key_local, " +
                    "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr " +
                    "FROM trn_dps AS d " +
                    "lEFT J"
                    + "OIN trn_dps_ety AS dety ON d.id_year = dety.id_year AND d.id_doc = dety.id_doc " +
                    "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = 2 " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";
            msSql += sqlDpsType;
            msSql += "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                    "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                    "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                    "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n " +
                    "LEFT OUTER JOIN " + SClientUtils.getComplementaryDbName(miClient.getSession().getStatement().getConnection()) + ".trn_cfd AS xc ON x.id_cfd = xc.id_cfd " +
                    "LEFT OUTER JOIN " + SClientUtils.getComplementaryDbName(miClient.getSession().getStatement().getConnection()) + ".trn_pdf AS p ON d.id_year = p.id_year AND d.id_doc = p.id_doc " +
                    "LEFT OUTER JOIN erp.usru_usr AS xu ON x.fid_usr_prc = xu.id_usr " +
                    "WHERE d.b_del = 0 " + sqlDate + sqlBizPartner + sqlItemGeneric +
                    "ORDER BY dt.code , d.num_ser , CAST(d.num AS UNSIGNED INTEGER), " +
                    "d.num , d.dt , bp.bp , bpc.bp_key , bp.id_bp , bpb.bpb , bpb.id_bpb, dety.id_ety";
            
        } catch (Exception ex) {
            Logger.getLogger(SViewDpsDetail.class.getName()).log(Level.SEVERE, null, ex);
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

    public void publicActionViewDps() {
        actionViewDps();
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
        }
    }
}
