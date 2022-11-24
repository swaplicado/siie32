/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItem;
import erp.mtrn.form.SDialogUpdateDpsItemRefConcept;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Claudio Peña
 */
public class SViewQueryDpsByItemHistory extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.mitm.form.SPanelFilterItem moPanelFilterItem;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.mtrn.form.SDialogUpdateDpsItemRefConcept moDialogUpdateDpsItemRefConcept;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    private javax.swing.JButton jbChangeItemConcept;

    private boolean mbHasRightAuthor = false;
    
    private int mnRegistryType;
    
    /**
     * Query view of all documents at once.
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_CHANGE_ITEM_CONCEPT.
     */
    public SViewQueryDpsByItemHistory(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        this(client, tabTitle, auxType01, 0);
    }

    /**
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_CHANGE_ITEM_CONCEPT.
     * @param auxType02 SDataConstantsSys.TRNX_TP_DPS_DOC or SDataConstantsSys.TRNX_TP_DPS_ADJ.
     */
    public SViewQueryDpsByItemHistory(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int levelDoc;
        maoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moPanelFilterItem = new SPanelFilterItem(miClient, this, true);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogUpdateDpsItemRefConcept = new SDialogUpdateDpsItemRefConcept(miClient);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
     
        jbChangeItemConcept = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_rev.gif")));
        jbChangeItemConcept.setPreferredSize(new Dimension(23, 23));
        jbChangeItemConcept.addActionListener(this);
        jbChangeItemConcept.setToolTipText("Modificar ítem/concepto");

        if (isViewForPurchase()) {
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
        }
        else {
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
        }

        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbChangeItemConcept);
        
        if (isViewForItemBizPartnerOne()) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moPanelFilterItem);
            addTaskBarUpperComponent(moTabFilterBizPartner);
        }
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }

    private boolean isViewForPurchase() {
        boolean isPurchase = false;

        if (SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ALL, SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ONE })) {
            isPurchase = true;
        }
        return isPurchase;
    }
    
    private boolean isViewForItemBizPartnerAll() {
        return SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ALL, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_N_BP_ALL });
    }

    private boolean isViewForItemBizPartnerOne() {
        return SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ONE, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_N_BP_ONE });
    }

    private void renderTableColumns() {
        int i;

        moTablePane.reset();

        STableField[] aoKeyFields = new STableField[3];
        
        if (mnTabTypeAux02 == 216) {
            maoTableColumns = new STableColumn[32]; // - 2
        }
        else {
            maoTableColumns = new STableColumn[29];
        }
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_doc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_ety");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dps.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_dnum", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_key_old", "Clave antigua", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_key_new", "Clave nueva", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_old", "Concepto antiguo", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_new", "Concepto nuevo", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Ítem", 250);
        if (mnTabTypeAux02 == 216) {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ito.item", "Ítem referencia antiguo", 200);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "itn.item", "Ítem referencia nuevo", 200);
        }
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.usr", "Usr modificación", 70);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "his.ts_edit", "Fecha modicación", 110);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        if (mnTabTypeAux02 == 216) {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uo.symbol", "Unidad", STableConstants.WIDTH_CODE_COB);
        }
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_price_u_cur", "Precio unitario mon $", STableConstants.WIDTH_VALUE_UNITARY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stot_cur_r", "Subtotal mon $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tax_charged_cur_r", "Imp tras mon $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tax_retained_cur_r", "Imp ret mon $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_price_u", "Precio unitario $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tax_charged_r", "Imp tras $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tax_retained_r", "Imp ret $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cur_key", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_rper", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rbkc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rcob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "ety.sort_pos", "Renglón", 75);

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }
  
    private void actionChangeItemConcept() {
        if (jbChangeItemConcept.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else if (!moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    int gui = isViewForPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                    moDialogUpdateDpsItemRefConcept.formReset();
                    moDialogUpdateDpsItemRefConcept.setValue(SDataConstants.TRN_DPS, new int[] { ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0], ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1] });
                    moDialogUpdateDpsItemRefConcept.setValue(SDataConstants.TRN_DPS_ETY, moTablePane.getSelectedTableRow().getPrimaryKey());
                    moDialogUpdateDpsItemRefConcept.setRegistryType(mnRegistryType);
                    moDialogUpdateDpsItemRefConcept.setFormVisible(true);

                    if (moDialogUpdateDpsItemRefConcept.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWherePeriod = "";
        String sqlWhereFuncArea = "";
        String sqlWhereItemBizPartnerOne = "";

        for (int i = 0; i < mvTableSettings.size(); i++) {
            STableSetting setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                java.util.Date[] range = (java.util.Date[]) setting.getSetting();
                sqlWherePeriod += "AND dps.dt BETWEEN " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "' AND " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhereFuncArea += "AND dps.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            
           if (isViewForItemBizPartnerOne()) {
                if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM) {
                    if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                        sqlWhereItemBizPartnerOne += "AND ety.fid_item = " + ((int[]) setting.getSetting())[0] + " ";
                    }
                }
                else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                    if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                        sqlWhereItemBizPartnerOne += "AND bp.id_bp = " + (Integer) setting.getSetting() + " ";
                    }
                }
            }
        }

        msSql = "SELECT ety.id_year, ety.id_doc, ety.id_ety, dps.dt, dt.code, CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS _dnum, bp.bp, his.concept_key_old, his.concept_key_new, " +
                "his.concept_old, his.concept_new, it.item, ";
                if (mnTabTypeAux02 == 216) {
                    msSql += "ito.item, itn.item, ";
                }
                msSql += "his.fk_fid_item_ref_n_old, his.fk_fid_item_ref_n_new, " +
                "@factor := " + (isViewForItemBizPartnerAll() ? "1.0" : "IF(dps.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] + ", -1.0, 1.0)") + " AS _factor, " +
                "ety.id_ety, ety.concept_key, ety.sort_pos, it.item, it.item_key,  ";
                if (mnTabTypeAux02 == 216) {        
                    msSql += "uo.symbol, "; 
                }
                msSql += "c.cur_key, '"  + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS _cur_key, " +
                "ety.orig_qty * @factor AS _orig_qty, " +
                "ety.price_u_cur * @factor AS _price_u_cur, ety.stot_cur_r * @factor AS _stot_cur_r, ety.tax_charged_cur_r * @factor AS _tax_charged_cur_r, ety.tax_retained_cur_r * @factor AS _tax_retained_cur_r, ety.tot_cur_r * @factor AS _tot_cur_r, " +
                "ety.price_u * @factor AS _price_u, ety.stot_r * @factor AS _stot_r, ety.tax_charged_r * @factor AS _tax_charged_r, ety.tax_retained_r * @factor AS _tax_retained_r, ety.tot_r * @factor AS _tot_r, us.usr, his.ts_edit, " +
                "rbkc.code, rcob.code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as _rper, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as _rnum " +
                "FROM trn_dps AS dps " +
                "INNER JOIN trn_dps_ety AS ety ON dps.id_year = ety.id_year AND dps.id_doc = ety.id_doc " +
                "INNER JOIN trn_dps_ety_hist AS his ON his.id_year = ety.id_year AND his.id_doc = ety.id_doc AND his.id_ety = ety.id_ety " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON dps.fid_ct_dps = dt.id_ct_dps AND dps.fid_cl_dps = dt.id_cl_dps AND dps.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.bpsu_bp AS bp ON dps.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.itmu_item AS it ON ety.fid_item = it.id_item " ;
                if (mnTabTypeAux02 == 216) {
                    msSql += "INNER JOIN erp.itmu_item AS ito ON his.fk_fid_item_ref_n_old = ito.id_item " + // 
                    "INNER JOIN erp.itmu_item AS itn ON his.fk_fid_item_ref_n_new = itn.id_item " + //
                    "INNER JOIN erp.itmu_unit AS uo ON uo.id_unit = ety.fid_orig_unit ";
                }
                msSql += "INNER JOIN erp.usru_usr AS us ON us.id_usr = his.fid_usr_edit " +
                "INNER JOIN erp.cfgu_cur AS c ON dps.fid_cur = c.id_cur " +
                "LEFT OUTER JOIN trn_dps_rec AS dr ON dps.id_year = dr.id_dps_year AND dps.id_doc = dr.id_dps_doc " +
                "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "LEFT OUTER JOIN fin_bkc AS rbkc ON r.id_bkc = rbkc.id_bkc " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS rcob ON r.fid_cob = rcob.id_bpb " +
                "WHERE NOT dps.b_del AND NOT ety.b_del " + 
                sqlWherePeriod + sqlWhereFuncArea + sqlWhereItemBizPartnerOne + 
//                "AND " +
//                "dps.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND dps.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (mbHasRightAuthor ? "AND dps.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() : "") + ";";
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
            JButton button = (JButton) e.getSource();
            
            if (button == jbChangeItemConcept) {
                actionChangeItemConcept();
            }
        }
    }
}
