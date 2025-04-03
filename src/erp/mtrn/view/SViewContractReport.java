package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.form.SDialogContractAnalysis;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterDnsDps;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.joda.time.LocalDate;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Claudio Peña
 */
public class SViewContractReport extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjbViewContractAnalysis;
    private javax.swing.JButton mjbPrintContractMoves;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.table.STabFilterDnsDps moTabFilterDnsDps;

    private boolean mbHasRightAuthor = false;

    /**
     * View for documents linked or pending to be linked to other documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param type Constants defined in SDataConstants (TRNX_DPS_LINK_PEND..., TRNX_DPS_LINKED...).
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewContractReport(erp.client.SClientInterface client, java.lang.String tabTitle, int type, int auxType01, int auxType02) {
        super(client, tabTitle, type, auxType01, auxType02);
        initComponents();
    }
    
    public SViewContractReport(erp.client.SClientInterface client, java.lang.String tabTitle, int type) {
        this(client, tabTitle, type, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
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
        
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjbViewContractAnalysis = new JButton(miClient.getImageIcon(SLibConstants.ICON_CONTRACT_ANALYSIS));
        mjbPrintContractMoves = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_moves.gif")));
        
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjbViewContractAnalysis.setPreferredSize(new Dimension(23, 23));
        mjbPrintContractMoves.setPreferredSize(new Dimension(23, 23));

        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        mjbViewContractAnalysis.addActionListener(this);
        mjbPrintContractMoves.addActionListener(this);

        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        mjbViewContractAnalysis.setToolTipText("Ver detalles de vínculos");
        mjbPrintContractMoves.setToolTipText("Imprimir movimientos de contrato");

        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogContractAnalysis = new SDialogContractAnalysis(miClient);

        // Usamos solo moTabFilterDatePeriod con el formato adecuado
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, isViewForEstimateCon() ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewContractAnalysis);
        addTaskBarUpperComponent(mjbPrintContractMoves);
        
        addTaskBarLowerComponent(moTabFilterDnsDps);

        mjbViewNotes.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        mjbViewContractAnalysis.setEnabled(isViewForEstimateCon());
        mjbPrintContractMoves.setEnabled(isViewForEstimateCon());
        moTabFilterDnsDps.setVisible(mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[11];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Num. Contrato", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha contrato.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Producto", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "orig_qty", "Cantidad ton.", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega inicial", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_lapsing_n", "Entrega final", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "orig_qty", "Entrega mensual", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "Estimacion", "Estimacion entr. acum", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cant_procesada", "Entrega real", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "porcentaje_surtido", "Estatus %", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());


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

    private boolean isViewForEstimateEst() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST;
    }

    private boolean isViewForEstimateCon() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON;
    }

    private boolean isViewForOrder() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
    }

    private int[] getDpsTypeKey() {
        int[] dpsTypeKey = null;

        if (isViewForCategoryPur()) {
            if (isViewForEstimateEst()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_EST;
            }
            else if (isViewForEstimateCon()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_CON;
            }
            else if (isViewForOrder()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_ORD;
            }
        }
        else {
            if (isViewForEstimateEst()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
            }
            else if (isViewForEstimateCon()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
            }
            else if (isViewForOrder()) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
            }
        }

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

    private void actionViewContractAnalysis() {
        if (mjbViewContractAnalysis.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                moDialogContractAnalysis.formReset();
                moDialogContractAnalysis.setValue(SDataConstants.TRN_DPS, dps);
                moDialogContractAnalysis.setFormVisible(true);
            }
        }
    }
    
    private void actionPrintContractMoves() {
        if (mjbPrintContractMoves.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null && moTablePane.getSelectedTableRow().getPrimaryKey() != null) {
                STrnUtilities.createReportContractAnalysis(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] dpsTypeKey = getDpsTypeKey();
        LocalDate today = LocalDate.now();

        int sqlDateYear = today.getYear();
        int sqlDatePeriod = today.getMonthOfYear();
        String sqlDpsType = ""; 
        String sqlBizPartner = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (setting.getSetting() != null) {
                    int[] period = (int[]) setting.getSetting();
                    if (period.length >= 2) { 
                        sqlDateYear = period[0];
                        sqlDatePeriod =  period[1];
                    }
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND b.id_bp = " + (Integer) setting.getSetting() + " ");
            }
        }
        
        sqlDpsType = "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " ";
        
        msSql = "SELECT "
                + "d.id_year, d.id_doc, "
                + "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, "
                + "d.dt, b.bp, i.item, de.orig_qty, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, "
                + "(SELECT orig_qty FROM TRN_DPS_ETY_PRC "
                + "WHERE id_year = d.id_year "
                + "AND id_doc = d.id_doc "
                + "AND id_ety = de.id_ety "
                + "AND con_prc_mon = ( "
                + "    SELECT IFNULL( "
                + "        (SELECT con_prc_mon FROM TRN_DPS_ETY_PRC "
                + "        WHERE id_year = d.id_year "
                + "        AND id_doc = d.id_doc "
                + "        AND id_ety = de.id_ety "
                + "        AND con_prc_mon = " + sqlDatePeriod + " "
                + "        AND b_del = 0 LIMIT 1), "
                + "        (SELECT MIN(con_prc_mon) FROM TRN_DPS_ETY_PRC "
                + "        WHERE id_year = d.id_year "
                + "        AND id_doc = d.id_doc "
                + "        AND id_ety = de.id_ety "
                + "        AND con_prc_mon > " + sqlDatePeriod + " "
                + "        AND b_del = 0) "
                + "    ) "
                + ") "
                + "AND b_del = 0 LIMIT 1) AS orig_qty, "
                + "(SELECT COALESCE(SUM(orig_qty), 0) AS total_orig_qty "
                + "FROM trn_dps_ety_prc "
                + "WHERE id_year = d.id_year "
                + "AND id_doc = d.id_doc "
                + "AND id_ety = de.id_ety "
                + "AND con_prc_year <= " + sqlDateYear + " "
                + "AND con_prc_mon <= IF(d.id_year < " + sqlDateYear + " , "
                + "(SELECT MAX(con_prc_mon) FROM trn_dps_ety_prc WHERE id_year = d.id_year AND id_doc = d.id_doc AND id_ety = de.id_ety), " + sqlDatePeriod + ")) "
                + "AS Estimacion, "
                + "COALESCE((SELECT SUM(ds.orig_qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd "
                + "WHERE ds.id_src_year = de.id_year "
                + "AND ds.id_src_doc = de.id_doc "
                + "AND ds.id_src_ety = de.id_ety "
                + "AND ds.id_des_year = xde.id_year "
                + "AND ds.id_des_doc = xde.id_doc "
                + "AND ds.id_des_ety = xde.id_ety "
                + "AND xde.id_year = xd.id_year "
                + "AND xde.id_doc = xd.id_doc "
                + "AND xde.b_del = 0 "
                + "AND xd.b_del = 0 "
                + "AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0) AS cant_procesada, "
                + "ROUND(IFNULL(((COALESCE((SELECT SUM(ds.orig_qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd "
                + "WHERE ds.id_src_year = de.id_year "
                + "AND ds.id_src_doc = de.id_doc "
                + "AND ds.id_src_ety = de.id_ety "
                + "AND ds.id_des_year = xde.id_year "
                + "AND ds.id_des_doc = xde.id_doc "
                + "AND ds.id_des_ety = xde.id_ety "
                + "AND xde.id_year = xd.id_year "
                + "AND xde.id_doc = xd.id_doc "
                + "AND xde.b_del = 0 "
                + "AND xd.b_del = 0 "
                + "AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0)) / NULLIF(de.orig_qty, 0)) * 100, 0), 2) AS porcentaje_surtido "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "AND d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " + sqlDpsType
                + "AND d.fid_cob != " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN erp.trnu_dps_nat AS dn ON d.fid_dps_nat = dn.id_dps_nat "
                + "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb "
                + "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb "
                + "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp "
                + "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = 3 "
                + "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur "
                + "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr "
                + "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item "
                + "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen "
                + "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit "
                + "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit "
                + "WHERE NOT d.b_del " + sqlBizPartner
                + "GROUP BY d.dt, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, d.num_ref, d.b_link, d.ts_link, d.num_ser, d.num, "
                + "d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, dt.code, dn.code, cob.code, bb.bpb, b.bp, bc.bp_key, "
                + "c.cur_key, ul.usr, de.fid_item, de.fid_unit, de.fid_orig_unit, de.surplus_per, de.qty, de.orig_qty, "
                + "de.stot_cur_r, i.item_key, i.item, ig.igen, u.symbol, uo.symbol "
                + "HAVING cant_procesada < de.orig_qty AND d.b_link = 0 "
                + "ORDER BY de.id_year, de.id_doc;";
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
            else if (button == mjbViewContractAnalysis) {
                actionViewContractAnalysis();
            }
            else if (button == mjbPrintContractMoves) {
                actionPrintContractMoves();
            }
        }
    }
}