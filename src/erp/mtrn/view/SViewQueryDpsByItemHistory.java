/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.form.SDialogUpdateDpsItemRefConcept;
import erp.table.SFilterConstants;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Claudio Peña, Rodrigo Ayala
 */
public class SViewQueryDpsByItemHistory extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mtrn.form.SDialogUpdateDpsItemRefConcept moDialogUpdateDpsItemRefConcept;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    private javax.swing.JButton jbChangeItemConcept;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;

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
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR);
        moDialogUpdateDpsItemRefConcept = new SDialogUpdateDpsItemRefConcept(miClient);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
     
        jbChangeItemConcept = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_rev.gif")));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        
        jbChangeItemConcept.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));

        jbChangeItemConcept.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);

        jbChangeItemConcept.setToolTipText("Modificar ítem o concepto");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas del documento");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        
        if (isViewForPurchase()) {
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
        }
        else {
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
        }

        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;
        mnRegistryType = SDataConstants.TRN_DPS;

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD) {
            addTaskBarUpperComponent(moTabFilterDatePeriod);
            addTaskBarUpperSeparator();
        }
        else{
            addTaskBarUpperComponent(moTabFilterDatePeriodRange);
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jbChangeItemConcept);
        }
                
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        
        jbChangeItemConcept.setEnabled(true);
        mjbViewNotes.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        
        
        renderTableColumns();
        if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_DPS_ORD) {
            setIsSummaryApplying(true);
        } else {
            setIsSummaryApplying(false);
        }

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");
        
        populateTable();
    }

    private boolean isViewForPurchase() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_DPS_BY_CHANGE_ITEM_CONCEPT; // Historial de cambios de items en modulo compras
    }
    
    private boolean isViewForOrder() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
    }
    
    private boolean isViewForInvoice() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
    }
    
    private boolean isViewForCreditNote() {
        return mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;
    }
    
    private int getCreditNoteClassId() {
        return isViewForPurchase() ? SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] : SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]; // Devuelve la clase de NC según el módulo
    }
    
    
    private int[] getDpsTypeKey() {
        int[] dpsTypeKey = null;

        if (isViewForPurchase()) {
            if (isViewForOrder()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_ORD;
            else if (isViewForInvoice()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_INV;
            else if (isViewForCreditNote()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_CN;
        }
        else { //SALE
            if (isViewForOrder()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
            else if (isViewForInvoice()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
            else if (isViewForCreditNote()) dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
        }

        return dpsTypeKey;
    }
    
    
    private void renderTableColumns() {
        int i;

        moTablePane.reset();

        STableField[] aoKeyFields = new STableField[3];
        
        if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD) {
            maoTableColumns = new STableColumn[28];
        }
        else {
            maoTableColumns = new STableColumn[34];
        }
        
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_doc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ety.id_ety");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_dnum", "Folio documento", STableConstants.WIDTH_DOC_NUM);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dps.dt", "Fecha documento", STableConstants.WIDTH_DATE);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa documento", STableConstants.WIDTH_CODE_COB);
            
        if (mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_DPS_BY_CHANGE_ITEM_CONCEPT) {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
        }
        else{
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Provedor", 200);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
        }
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "ety.sort_pos", "# Partida", STableConstants.WIDTH_NUM_TINYINT);
        
        if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD) {
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_qty_old", "Cantidad anterior", STableConstants.WIDTH_QUANTITY);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_qty_new", "Cantidad nueva", STableConstants.WIDTH_QUANTITY);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            //Añadir centro costo
        }
        else{
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_key_old", "Clave anterior", STableConstants.WIDTH_ITEM_KEY);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_key_new", "Clave nueva", STableConstants.WIDTH_ITEM_KEY);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_old", "Concepto anterior", STableConstants.WIDTH_ITEM_KEY);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "his.concept_new", "Concepto nuevo", STableConstants.WIDTH_ITEM_KEY); //10
        }
        
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Ítem", 250);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ito.item", "Ítem referencia anterior", 200);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "itn.item", "Ítem referencia nuevo", 200);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_orig_qty", "Cantidad actual", STableConstants.WIDTH_QUANTITY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
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
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cur_key", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY); //17
        
        if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_DPS_ORD){ 
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_rper", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rbkc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rcob.code", "Sucursal empresa póliza", STableConstants.WIDTH_CODE_COB);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
            maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark()); //4
        }
        
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.usr", "Usr. modificación", 70);
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "his.ts_edit", "Modificación", 110);

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
    
    private void actionViewDps() {
        if (mjbViewDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsSummary()) {
                    int gui = isViewForPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                    int[] dpsTypeKey = getDpsTypeKey();

                    // Extraemos sólo los primeros 2 elementos de la llave primaria para el DPS
                    int[] primaryKey = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                    int[] dpsKey = new int[] { primaryKey[0], primaryKey[1] };

                    miClient.getGuiModule(gui).setFormComplement(dpsTypeKey);
                    miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, dpsKey);

                }
            }
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsSummary()) {
                    SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
                }
            }
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsSummary()) {
                    SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
                }
            }
        }
    }
    
    @Override
    public void createSqlQuery() {
        String sqlWherePeriod = "";
        String sqlWhereFuncArea = "";

        for (int i = 0; i < mvTableSettings.size(); i++) {
            STableSetting setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if(setting.getSetting() instanceof int[]){
                    sqlWherePeriod += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "dps.dt") + " ";
                }
                else if (setting.getSetting() instanceof java.util.Date[]) {
                    java.util.Date[] range = (java.util.Date[]) setting.getSetting();
                    sqlWherePeriod += "AND dps.dt BETWEEN " +
                            "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "' AND " +
                            "'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhereFuncArea += "AND dps.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        int dpsCategory = isViewForPurchase() ? SDataConstantsSys.TRNS_CT_DPS_PUR : SDataConstantsSys.TRNS_CT_DPS_SAL;
        int dpsClass = 0;
        
        if (mnTabTypeAux02 ==  SDataConstantsSys.TRNX_TP_DPS_ORD) {
            dpsClass = SDataConstantsSys.TRNS_CL_DPS_ORD;
        } else if (mnTabTypeAux02 ==  SDataConstantsSys.TRNX_TP_DPS_DOC) {
            dpsClass = SDataConstantsSys.TRNS_CL_DPS_DOC;
        } else if (mnTabTypeAux02 ==  SDataConstantsSys.TRNX_TP_DPS_ADJ) {
            dpsClass = SDataConstantsSys.TRNS_CL_DPS_ADJ;
        }
        
        String logSubquery = 
                "(SELECT h.id_year, h.id_doc, h.id_ety, h.concept_key_old, h.concept_key_new, h.concept_old, h.concept_new, " +
                "h.fid_item_ref_old_n, h.fid_item_ref_new_n, 0.0 AS qty_old, 0.0 AS qty_new, " +
                "'Referencia' AS _log_type, h.fid_usr_edit AS _usr_id, h.ts_edit " +
                "FROM trn_dps_ety_hist AS h " +
                "UNION ALL " +
                "SELECT q.id_year, q.id_doc, q.id_ety, '' AS concept_key_old, '' AS concept_key_new, '' AS concept_old, '' AS concept_new, " +
                "NULL AS fid_item_ref_old_n, NULL AS fid_item_ref_new_n, q.orig_qty_old AS qty_old, q.orig_qty_new AS qty_new, " +
                "'Cantidad' AS _log_type, q.fid_usr_new AS _usr_id, q.ts_new " +
                "FROM trn_dps_ety_qty_chg AS q WHERE q.b_del = 0) ";
        
        msSql = "SELECT " +
                "ety.id_year, ety.id_doc, ety.id_ety, " +
                "dt.code, " +
                "CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS _dnum, " +
                "dps.dt, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE dps.fid_cob = cob.id_bpb) AS f_cob_code, " +
                "bp.bp, " +
                "bpb.bpb, " +
                "ety.sort_pos, " +

                "his.concept_key_old, " +
                "his.concept_key_new, " +
                "his.concept_old, " +
                "his.concept_new, " +

                "it.item, " +
                "ito.item, " +
                "itn.item, " +

                "his.fid_item_ref_old_n, " +
                "his.fid_item_ref_new_n, " +
                "his.qty_old AS _qty_old, " +
                "his.qty_new AS _qty_new, " +
                "his._log_type, " +

                "@factor := IF(dps.fid_cl_dps = " + getCreditNoteClassId() + ", -1.0, 1.0) AS _factor, " +

                "ety.concept_key, " +///////
                "it.item_key, " +
                "uo.symbol, " +

                "c.cur_key, " +
                "'" + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS _cur_key, " +

                "ety.orig_qty * @factor AS _orig_qty, " +

                "ety.price_u_cur * @factor AS _price_u_cur, " +
                "ety.stot_cur_r * @factor AS _stot_cur_r, " +
                "ety.tax_charged_cur_r * @factor AS _tax_charged_cur_r, " +
                "ety.tax_retained_cur_r * @factor AS _tax_retained_cur_r, " +
                "ety.tot_cur_r * @factor AS _tot_cur_r, " +

                "ety.price_u * @factor AS _price_u, " +
                "ety.stot_r * @factor AS _stot_r, " +
                "ety.tax_charged_r * @factor AS _tax_charged_r, " +
                "ety.tax_retained_r * @factor AS _tax_retained_r, " +
                "ety.tot_r * @factor AS _tot_r, " +

                "rbkc.code, " +
                "rcob.code, " +
                "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) AS _rper, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) AS _rnum, " +

                "us.usr, " +
                "his.ts_edit " +

                "FROM trn_dps AS dps " +

                "INNER JOIN trn_dps_ety AS ety ON dps.id_year = ety.id_year AND dps.id_doc = ety.id_doc " +

                "INNER JOIN " + logSubquery + " AS his ON " +
                "his.id_year = ety.id_year AND " +
                "his.id_doc = ety.id_doc AND " +
                "his.id_ety = ety.id_ety " +

                "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                "dps.fid_ct_dps = dt.id_ct_dps AND " +
                "dps.fid_cl_dps = dt.id_cl_dps AND " +
                "dps.fid_tp_dps = dt.id_tp_dps " +

                "INNER JOIN erp.bpsu_bp AS bp ON dps.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON dps.fid_bpb = bpb.id_bpb " +

                "INNER JOIN erp.itmu_item AS it ON ety.fid_item = it.id_item " +

                "LEFT OUTER JOIN erp.itmu_item AS ito ON his.fid_item_ref_old_n = ito.id_item " +
                "LEFT OUTER JOIN erp.itmu_item AS itn ON his.fid_item_ref_new_n = itn.id_item " +

                "LEFT OUTER JOIN erp.itmu_unit AS uo ON uo.id_unit = ety.fid_orig_unit " +

                "INNER JOIN erp.usru_usr AS us ON us.id_usr = his._usr_id " +
                "INNER JOIN erp.cfgu_cur AS c ON dps.fid_cur = c.id_cur " +

                "LEFT OUTER JOIN trn_dps_rec AS dr ON " +
                "dps.id_year = dr.id_dps_year AND dps.id_doc = dr.id_dps_doc " +

                "LEFT OUTER JOIN fin_rec AS r ON " +
                "dr.fid_rec_year = r.id_year AND " +
                "dr.fid_rec_per = r.id_per AND " +
                "dr.fid_rec_bkc = r.id_bkc AND " +
                "dr.fid_rec_tp_rec = r.id_tp_rec AND " +
                "dr.fid_rec_num = r.id_num " +

                "LEFT OUTER JOIN fin_bkc AS rbkc ON r.id_bkc = rbkc.id_bkc " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS rcob ON r.fid_cob = rcob.id_bpb " +

                "WHERE NOT dps.b_del " +
                "AND NOT ety.b_del " +

                "AND dps.fid_ct_dps = " + dpsCategory + " " +
                "AND dps.fid_cl_dps = " + dpsClass + " " +

                sqlWherePeriod +
                sqlWhereFuncArea +

                (mbHasRightAuthor ?
                    "AND dps.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " "
                    : "") +

                "ORDER BY dps.num_ser, CAST(dps.num AS UNSIGNED INTEGER), bp.bp, his.ts_edit ASC;";
        msSql += "";
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
            JButton button = (JButton) e.getSource();
            
            if (button == jbChangeItemConcept) {
                actionChangeItemConcept();
            }
            else if (button == mjbViewDps) {
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
