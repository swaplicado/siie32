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
import erp.mtrn.form.SDialogUpdateAllDpsItem;
import erp.mtrn.form.SDialogUpdateDpsAccountCenterCost;
import erp.mtrn.form.SDialogUpdateDpsItemRefConcept;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterFunctionalArea;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import sa.lib.SLibUtils;

/**
 *
 * @author Claudio Peña
 */
public class SViewDpsTaxRustic extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.mitm.form.SPanelFilterItem moPanelFilterItem;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.mtrn.form.SDialogUpdateDpsAccountCenterCost moDialogUpdateDpsAccountCostCenter;
    private erp.mtrn.form.SDialogUpdateDpsItemRefConcept moDialogUpdateDpsItemRefConcept;
    private erp.mtrn.form.SDialogUpdateAllDpsItem moDialogUpdateAllDpsItem;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
   /**
     * Constructor for querying all documents at once.
     * 
     * @param client Client interface
     * @param tabTitle Title of the tab
     * @param auxType01 Type of document view
     */
    public SViewDpsTaxRustic(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        this(client, tabTitle, auxType01, 0);
    }

  /**
     * Constructor for querying documents by specific business partner and/or item.
     * 
     * @param client Client interface
     * @param tabTitle Title of the tab
     * @param auxType01 Type of document view
     * @
     * */
    public SViewDpsTaxRustic(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        maoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moPanelFilterItem = new SPanelFilterItem(miClient, this, true);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogUpdateDpsAccountCostCenter = new SDialogUpdateDpsAccountCenterCost(miClient);
        moDialogUpdateDpsItemRefConcept = new SDialogUpdateDpsItemRefConcept(miClient);
        moDialogUpdateAllDpsItem = new SDialogUpdateAllDpsItem(miClient);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

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

    private void renderTableColumns() {
        int i;

        moTablePane.reset();
        maoTableColumns = new STableColumn[18];
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "FECHA", "Fecha doc.", STableConstants.WIDTH_DATE);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "NUM", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "BP", "RFC", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "BP", "Proveedor", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Munucipio", 100);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "CONCEPT", "Producto(Ítem)", 300);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "MASS", "Volumen", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "PRICE_U", "P.U.", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "PRICE_U", "Unidad", STableConstants.WIDTH_CODE_COB);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "STOT_CUR_R", "Sub total", STableConstants.WIDTH_VALUE_UNITARY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "IMPUESTOPREDIAL", "Imp predial", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "PRODEPORTE", "Prod-deporte", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "PIAFES", "Pieafes", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ISR", "ISR retenido", STableConstants.WIDTH_VALUE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ISR", "Total ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ISR", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "TOT_CUR_R", "Precio unitario $", STableConstants.WIDTH_VALUE_2X);

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }
    
    @Override
    public void createSqlQuery() {
        String sqlWherePeriod = "";
        for (int i = 0; i < mvTableSettings.size(); i++) {
            STableSetting setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                java.util.Date[] range = (java.util.Date[]) setting.getSetting();
                sqlWherePeriod += "AND d.dt BETWEEN " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "' AND " +
                        "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
        }

        
        try {
            msSql = "SELECT D.DT AS FECHA, " +
                    "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, 6)) as f_rnum, " +
                    "D.NUM, BP.BP, AD.locality, E.CONCEPT, E.MASS, E.PRICE_U, E.STOT_CUR_R, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = 7 AND TAX.ID_TAX = 1 AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS IMPUESTOPREDIAL, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = 7 AND TAX.ID_TAX = 2 AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS PRODEPORTE, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = 7 AND TAX.ID_TAX = 3 AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS PIAFES, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = 2 AND TAX.ID_TAX = 10 AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS ISR, " +
                    "D.TOT_CUR_R " +
                    "FROM TRN_DPS AS D " +
                    "INNER JOIN TRN_DPS_ETY AS E ON E.ID_YEAR = D.ID_YEAR and E.ID_DOC = D.ID_DOC " +
                    "INNER JOIN TRN_DPS_ETY_TAX AS DET ON DET.ID_YEAR = D.ID_YEAR and DET.ID_DOC = D.ID_DOC " +
                    "INNER JOIN ERP.BPSU_BP AS BP ON BP.ID_BP = D.fid_bp_r " +
                    "INNER JOIN ERP.BPSU_BPB AS BPB ON BP.ID_BP = BPB.ID_BPB " +
                    "INNER JOIN ERP.BPSU_BPB_ADD AS AD ON BPB.ID_BPB = AD.ID_BPB " +
                    "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                    "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                    "WHERE DET.ID_TAX_BAS = " + SLibUtils.parseInt(erp.mcfg.data.SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_DPS_RUS_TAX)) + " " + sqlWherePeriod ;
        } catch (Exception ex) {
            Logger.getLogger(SViewDpsTaxRustic.class.getName()).log(Level.SEVERE, null, ex);
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
            JButton button = (JButton) e.getSource();

        }
    }
}
