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
import erp.mfin.data.SFinUtilities;
import erp.mitm.form.SPanelFilterItem;
import erp.mtrn.form.SDialogUpdateDpsAccountCenterCost;
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
 * @author Juan Barajas, Isabel Servín, Sergio Flores
 */
public class SViewQueryDpsByItemBizPartner extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.mitm.form.SPanelFilterItem moPanelFilterItem;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.mtrn.form.SDialogUpdateDpsAccountCenterCost moDialogUpdateDpsAccountCostCenter;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    private javax.swing.JButton jbAccountCostCenter;

    private boolean mbHasRightAuthor = false;
    
    private boolean mbIsDpsDoc = false;
    private boolean mbIsDpsAdj = false;

    private int mnRegistryType;
    
    /**
     * Query view of all documents at once.
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ALL or SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_N_BP_ALL.
     */
    public SViewQueryDpsByItemBizPartner(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        this(client, tabTitle, auxType01, 0);
    }

    /**
     * Query view of documents from one business partner and/or item at a time.
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ONE or SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_N_BP_ONE.
     * @param auxType02 SDataConstantsSys.TRNX_TP_DPS_DOC or SDataConstantsSys.TRNX_TP_DPS_ADJ.
     */
    public SViewQueryDpsByItemBizPartner(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int levelDoc;
        maoTableColumns = null;
        
        mbIsDpsDoc = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
        mbIsDpsAdj = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moPanelFilterItem = new SPanelFilterItem(miClient, this, true);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogUpdateDpsAccountCostCenter = new SDialogUpdateDpsAccountCenterCost(miClient);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        jbAccountCostCenter = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_bkk_csh.gif")));
        jbAccountCostCenter.setPreferredSize(new Dimension(23, 23));
        jbAccountCostCenter.addActionListener(this);
        jbAccountCostCenter.setToolTipText("Modificar contabilización");

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
        if (isViewForItemBizPartnerAll()) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jbAccountCostCenter);
        }
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
        maoTableColumns = new STableColumn[28];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_doc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_ety");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_dnum", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave AN", 50);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "de.concept_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Ítem", 300);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uo.symbol", "Unidad", STableConstants.WIDTH_CODE_COB);
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
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "de.sort_pos", "Renglón", 75);

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }

    private void actionAccountCostCenter() {
        String account = "";
        String costCenter = "";
        
        if (jbAccountCostCenter.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else if (!moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    int gui = isViewForPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                    account = SFinUtilities.getAccountForDpsEntry(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    costCenter = SFinUtilities.getCostCenterForDpsEntry(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());

                    moDialogUpdateDpsAccountCostCenter.formReset();
                    moDialogUpdateDpsAccountCostCenter.setValue(SDataConstants.TRN_DPS, new int[] { ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0], ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1] });
                    moDialogUpdateDpsAccountCostCenter.setValue(SDataConstants.TRN_DPS_ETY, moTablePane.getSelectedTableRow().getPrimaryKey());
                    moDialogUpdateDpsAccountCostCenter.setValue(SDataConstants.FIN_ACC, new String[] { account, costCenter }); 
                    moDialogUpdateDpsAccountCostCenter.setRegistryType(mnRegistryType);
                    moDialogUpdateDpsAccountCostCenter.setFormVisible(true);

                    if (moDialogUpdateDpsAccountCostCenter.getFormResult() == SLibConstants.FORM_RESULT_OK) {
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
                sqlWherePeriod += "AND d.dt BETWEEN " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "' AND " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhereFuncArea += "AND d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            
           if (isViewForItemBizPartnerOne()) {
                if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM) {
                    if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                        sqlWhereItemBizPartnerOne += "AND de.fid_item = " + ((int[]) setting.getSetting())[0] + " ";
                    }
                }
                else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                    if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                        sqlWhereItemBizPartnerOne += "AND bp.id_bp = " + (Integer) setting.getSetting() + " ";
                    }
                }
            }
        }

        if (isViewForItemBizPartnerOne() && sqlWhereItemBizPartnerOne.isEmpty()) {
            sqlWhereItemBizPartnerOne = "AND de.fid_item = " + SLibConstants.UNDEFINED + " "; // prevent from retrieving any data
        }
        
        int[][] dpsTypeKeys = null;
        
        if (mbIsDpsDoc) {
            dpsTypeKeys = new int[][] { isViewForPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV };
            mnRegistryType = SDataConstantsSys.TRNX_TP_DPS_DOC;
        }
        else if (mbIsDpsAdj) {
            dpsTypeKeys = new int[][] { isViewForPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN : SDataConstantsSys.TRNU_TP_DPS_SAL_CN };
            mnRegistryType = SDataConstantsSys.TRNX_TP_DPS_ADJ;
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_ONE:
                    dpsTypeKeys = new int[][] { SDataConstantsSys.TRNU_TP_DPS_PUR_INV, SDataConstantsSys.TRNU_TP_DPS_PUR_CN };
                    break;
                case SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_N_BP_ONE:
                    dpsTypeKeys = new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_INV, SDataConstantsSys.TRNU_TP_DPS_SAL_CN };
                    break;
                default:
                    // do nothing
            }
        }
        
        String sqlWhereDpsTypes = "";
        
        for (int[] dpsTypeKey : dpsTypeKeys) {
            sqlWhereDpsTypes += (sqlWhereDpsTypes.isEmpty() ? "" : " OR ") +
                    "(d.fid_ct_dps = " + dpsTypeKey[0] + " AND " +
                    "d.fid_cl_dps = " + dpsTypeKey[1] + " AND " +
                    "d.fid_tp_dps = " + dpsTypeKey[2] + ")";
        }
        
        if (!sqlWhereDpsTypes.isEmpty() && dpsTypeKeys != null) {
            sqlWhereDpsTypes = "AND " + (dpsTypeKeys.length == 1 ? sqlWhereDpsTypes : "(" + sqlWhereDpsTypes + ")") + " ";
        }
        
        msSql = "SELECT de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, dt.code, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS _dnum, cob.code, bp.bp, bpc.bp_key, bpb.bpb, " +
                "rbkc.code, rcob.code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as _rper, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as _rnum, " +
                "@factor := " + (isViewForItemBizPartnerAll() ? "1.0" : "IF(d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] + ", -1.0, 1.0)") + " AS _factor, " +
                "de.id_ety, de.concept_key, de.sort_pos, it.item, it.item_key, uo.symbol, c.cur_key, '"  + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS _cur_key, " +
                "de.orig_qty * @factor AS _orig_qty, " +
                "de.price_u_cur * @factor AS _price_u_cur, de.stot_cur_r * @factor AS _stot_cur_r, de.tax_charged_cur_r * @factor AS _tax_charged_cur_r, de.tax_retained_cur_r * @factor AS _tax_retained_cur_r, de.tot_cur_r * @factor AS _tot_cur_r, " +
                "de.price_u * @factor AS _price_u, de.stot_r * @factor AS _stot_r, de.tax_charged_r * @factor AS _tax_charged_r, de.tax_retained_r * @factor AS _tax_retained_r, de.tot_r * @factor AS _tot_r " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (isViewForPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.itmu_item AS it ON de.fid_item = it.id_item " +
                "INNER JOIN erp.itmu_unit AS uo ON uo.id_unit = de.fid_orig_unit " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "LEFT OUTER JOIN fin_bkc AS rbkc ON r.id_bkc = rbkc.id_bkc " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS rcob ON r.fid_cob = rcob.id_bpb " +
                "WHERE NOT d.b_del AND NOT de.b_del " + 
                sqlWhereDpsTypes + sqlWherePeriod + sqlWhereFuncArea + sqlWhereItemBizPartnerOne + "AND " +
                "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (mbHasRightAuthor ? "AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() : "") + ";";
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
                
            if (button == jbAccountCostCenter) {
                actionAccountCostCenter();
            }
        }
    }
}
