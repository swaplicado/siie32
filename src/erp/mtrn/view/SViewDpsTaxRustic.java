/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Claudio Peña
 */
public class SViewDpsTaxRustic extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private static final int[] tax_rus_pred = new int[] { 7, 1 };
    private static final int[] tax_rus_prod_dep = new int[] { 7, 2 };
    private static final int[] tax_rus_pia = new int[] { 7, 3 };
    private static final int[] tax_rus_isr = new int[] { 2, 10 };
    private int tax_reg_apsa = 14; 

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

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }   

    private void renderTableColumns() {
        int i;

        moTablePane.reset();
        maoTableColumns = new STableColumn[19];
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "FECHA", "Fecha doc.", STableConstants.WIDTH_DATE);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "NUM", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "BP.fiscal_id", "RFC", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "BP", "Proveedor", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Municipio", 100);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "AD.zip_code", "Código postal", 50);
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
                    "D.NUM, BP.BP, BP.fiscal_id, AD.zip_code, AD.county, E.CONCEPT, E.MASS, E.PRICE_U, E.STOT_CUR_R, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = " + tax_rus_pred[0] + "  AND TAX.ID_TAX = " + tax_rus_pred[1] + " AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS IMPUESTOPREDIAL, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = " + tax_rus_prod_dep[0] + " AND TAX.ID_TAX = " + tax_rus_prod_dep[1] + " AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS PRODEPORTE, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = " + tax_rus_pia[0] + " AND TAX.ID_TAX = " + tax_rus_pia[1] + " AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS PIAFES, " +
                    "(SELECT TAX FROM TRN_DPS_ETY_TAX AS TAX WHERE TAX.ID_TAX_BAS = " + tax_rus_isr[0] + " AND TAX.ID_TAX = " + tax_rus_isr[0] + " AND TAX.ID_YEAR = D.ID_YEAR AND TAX.ID_DOC = D.ID_DOC) AS ISR, " +
                    "D.TOT_CUR_R " +
                    "FROM TRN_DPS AS D " +
                    "INNER JOIN TRN_DPS_ETY AS E ON E.ID_YEAR = D.ID_YEAR and E.ID_DOC = D.ID_DOC " +
                    "INNER JOIN TRN_DPS_ETY_TAX AS DET ON DET.ID_YEAR = D.ID_YEAR and DET.ID_DOC = D.ID_DOC " +
                    "INNER JOIN ERP.BPSU_BP AS BP ON BP.ID_BP = D.fid_bp_r " +
                    "INNER JOIN ERP.BPSU_BPB AS BPB ON BP.ID_BP = BPB.fid_bp " +
                    "INNER JOIN ERP.BPSU_BPB_ADD AS AD ON BPB.ID_BPB = AD.ID_BPB " +
                    "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                    "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                    "WHERE E.FID_TAX_REG = " + tax_reg_apsa + " " + sqlWherePeriod + " GROUP BY D.ID_DOC; " ;
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
