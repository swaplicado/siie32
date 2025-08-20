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
import erp.mtrn.form.SDialogUpdateAllDpsItem;
import erp.mtrn.form.SDialogUpdateDpsAccountCenterCost;
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
public class SViewQueryDpsByItemBizPartnerLocality extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    private boolean mbHasRightAuthor = false;
    
    private boolean mbIsDpsDoc = false;
    private boolean mbIsDpsAdj = false;

    private int mnRegistryType;
    
    /**
     * Query view of all documents at once.
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_SAL.
     */
    public SViewQueryDpsByItemBizPartnerLocality(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        this(client, tabTitle, auxType01, 0);
    }

    /**
     * Query view of documents from one business partner and/or item at a time.
     * @param client
     * @param tabTitle
     * @param auxType01 SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_N_BP_SAL.
     * @param auxType02 SDataConstantsSys.TRNX_TP_DPS_DOC or SDataConstantsSys.TRNX_TP_DPS_ADJ.
     */
    public SViewQueryDpsByItemBizPartnerLocality(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        maoTableColumns = null;
        
        mbIsDpsDoc = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
        mbIsDpsAdj = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);


        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }


    private void renderTableColumns() { 
        int i;

        moTablePane.reset();

        STableField[] aoKeyFields = new STableField[3];
        maoTableColumns = new STableColumn[20];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_doc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_ety");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_dnum", "Folio doc.", STableConstants.WIDTH_DOC_NUM); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "de.sort_pos", "Renglón", 75); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "country", "País", 100); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Ítem", 300); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Tipo producto", 300); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uo.symbol", "Unidad", STableConstants.WIDTH_CODE_COB); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "de.id_year", "Año", 75); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mesLocal", "Mes", 75); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_price_u_cur", "Precio unitario mon $", STableConstants.WIDTH_VALUE_UNITARY); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "Tipo de cambio$", STableConstants.WIDTH_VALUE_2X); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ku", "Kilo moneda extranjera", STableConstants.WIDTH_VALUE_2X); 
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "km", "Kilo moneda MXN", STableConstants.WIDTH_VALUE_2X); 
        
        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }
        moTablePane.createTable(this); 
    }
    
    @Override
    public void createSqlQuery() {
        String sqlWherePeriod = "";
        String sqlWhereFuncArea = "";

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
            
        }
        
        int[][] dpsTypeKeys = null;
        dpsTypeKeys = new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_INV };        
        
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
        
        msSql = "SELECT de.id_year, de.id_doc, de.id_ety, dt.code, " +
            "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS _dnum, " +
            "de.sort_pos, bp.bp, " +
            "(SELECT COALESCE(ct.cty, (SELECT cty FROM erp.LOCU_CTY WHERE id_cty = 1)) " +
            " FROM trn_dps AS d2 " +
            " INNER JOIN erp.BPSU_BPB AS bpb2 ON bpb2.fid_bp = d2.fid_bp_r " +
            " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb2.id_bpb " +
            " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
            " WHERE d2.id_year = de.id_year AND d2.id_doc = de.id_doc LIMIT 1) AS country, " +
            "it.item, ig.igen, de.orig_qty AS _orig_qty, uo.symbol, " +
            "de.id_year, " +
            "(SELECT CASE MONTH(d.dt) " +
            " WHEN 1 THEN 'Enero' WHEN 2 THEN 'Febrero' WHEN 3 THEN 'Marzo' WHEN 4 THEN 'Abril' WHEN 5 THEN 'Mayo' WHEN 6 THEN 'Junio' " +
            " WHEN 7 THEN 'Julio' WHEN 8 THEN 'Agosto' WHEN 9 THEN 'Septiembre' WHEN 10 THEN 'Octubre' WHEN 11 THEN 'Noviembre' WHEN 12 THEN 'Diciembre' END) AS mesLocal, " +
            "de.price_u_cur AS _price_u_cur, c.cur_key, de.tot_cur_r AS _tot_cur_r, de.stot_r AS _stot_r, " +
            "'MXN' AS _cur_key, d.exc_rate, " +
            "IF(d.fid_cur <> 1, (de.stot_prov_cur_r / de.qty), 0) AS ku, (de.stot_prov_r / de.qty) AS km " +
            "FROM trn_dps AS d " +
            "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
            "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
            "INNER JOIN erp.itmu_item AS it ON de.fid_item = it.id_item " +
            "INNER JOIN erp.itmu_igen AS ig ON ig.id_igen = it.fid_igen " +
            "INNER JOIN erp.itmu_unit AS uo ON uo.id_unit = de.fid_orig_unit " +
            "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
            "WHERE NOT d.b_del AND NOT de.b_del " + sqlWhereDpsTypes + sqlWherePeriod + sqlWhereFuncArea +
            "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + ";";
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
