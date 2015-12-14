/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItem;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SViewQueryDpsByItemBizPartner extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.mitm.form.SPanelFilterItem moPanelFilterItem;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private boolean mbHasRightAuthor = false;

    public SViewQueryDpsByItemBizPartner(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01);
        initComponents();
    }

    private void initComponents() {
        int levelDoc = SDataConstantsSys.UNDEFINED;
        maoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moPanelFilterItem = new SPanelFilterItem(miClient, this, true);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);

        if (isPurchase()) {
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
        if (SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL })) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moPanelFilterItem);
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterBizPartner);
        }

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }

    private void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        maoTableColumns = new STableColumn[28];

        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "folio", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave AN", 50);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "de.concept_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Ítem", 300);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.price_u_cur", "Precio unitario mon $", STableConstants.WIDTH_VALUE_UNITARY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.stot_cur_r", "Subtotal mon $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_charged_cur_r", "Imp tras mon $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_retained_cur_r", "Imp ret mon $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.price_u", "Precio unitario $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_charged_r", "Imp tras $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_retained_r", "Imp ret $", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++].setSumApplying(true);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rper", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rbc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rcb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "de.id_ety", "Renglón", 75);

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }

    private boolean isPurchase() {
        boolean isPurchase = false;

        if (SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_ALL, SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL })) {
            isPurchase = true;
        }
        return isPurchase;
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlWhere = "";
        String sqlBizPartner = "";
        String sqlDatePeriod = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[]) setting.getSetting();
                sqlDatePeriod += " AND d.dt BETWEEN " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "' AND " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
            
           if (SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL })) {
                if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM) {
                    if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += " AND de.fid_item = " + ((int[]) setting.getSetting())[0] + " ";
                    }
                }
                else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                    if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                        sqlBizPartner += " AND bp.id_bp = " + (Integer) setting.getSetting() + " ";
                    }
                }
            }
        }

        if (sqlWhere.length() == 0 && sqlBizPartner.length() == 0 && SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL })) {
            sqlWhere = " AND de.fid_item = " + SLibConstants.UNDEFINED + " ";
        }

//        renderTableColumns();

        msSql = "SELECT d.dt, dt.code, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS folio, cob.code, bp.bp, bpc.bp_key, bpb.bpb, de.concept_key, it.item, de.orig_qty, u.symbol, de.price_u_cur, de.stot_cur_r, de.tax_charged_cur_r, de.tax_retained_cur_r,  de.tot_cur_r, c.cur_key, " +
                "de.price_u, de.stot_r, de.tax_charged_r, de.tax_retained_r,  de.tot_r, '"  + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS f_cur_key, " +
                "de.id_ety , rbc.code, rcb.code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_rper, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rnum " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " + sqlBizPartner +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.itmu_item AS it ON de.fid_item = it.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON u.id_unit = de.fid_unit " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "LEFT OUTER JOIN fin_bkc AS rbc ON r.id_bkc = rbc.id_bkc " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS rcb ON r.fid_cob = rcb.id_bpb " +
                "WHERE  d.b_del = 0 AND de.b_del = 0 AND d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                sqlDatePeriod + sqlWhere + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : " ");
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

        }
    }
}
