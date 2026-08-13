/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.form.SDialogContractAnalysis;
import erp.mtrn.form.SDialogContractFilter;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterUsers;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 * Vista de detalle de vínculos de partidas de cotizaciones contratos por procesar.
 * Aunque la vista está diseñada para estos dos tipos de documento, sólo estará disponible para contratos.
 * @author Sergio Flores
 */
public class SViewContractLinkEntry extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    public static final String NO_ANALYST = "(SIN ANALISTA)";
    
    private static final int MAX_OPTIONS = 10;

    private javax.swing.JButton mjbSelectOptions;
    private javax.swing.JButton mjbViewSelectedOptions;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjbViewContractAnalysis;
    private javax.swing.JButton mjbPrintContractMoves;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.mtrn.form.SDialogContractFilter moDialogContractFilter;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private SDialogContractFilter.SelectedOptions moSelectedOptions;
    private boolean mbHasRightAuthor = false;

    /**
     * Vista de detalle de vínculos de partidas de contratos.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS_EST_EST | TRNX_TP_DPS_EST_CON).
     */
    public SViewContractLinkEntry(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_CON_LINK_ETY, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelDoc = SDataConstantsSys.UNDEFINED;

        mbHasRightAuthor = false;

        if (isViewForCategoryPur()) {
            if (isViewForEstimate()) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
        }
        else {
            if (isViewForEstimate()) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
        }
        
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        mjbSelectOptions = new JButton(miClient.getImageIcon(SLibConstants.ICON_FILTER_DOC));
        mjbViewSelectedOptions = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjbViewContractAnalysis = new JButton(miClient.getImageIcon(SLibConstants.ICON_CONTRACT_ANALYSIS));
        mjbPrintContractMoves = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_moves.gif")));
        
        mjbSelectOptions.setPreferredSize(new Dimension(23, 23));
        mjbViewSelectedOptions.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjbViewContractAnalysis.setPreferredSize(new Dimension(23, 23));
        mjbPrintContractMoves.setPreferredSize(new Dimension(23, 23));

        mjbSelectOptions.addActionListener(this);
        mjbViewSelectedOptions.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        mjbViewContractAnalysis.addActionListener(this);
        mjbPrintContractMoves.addActionListener(this);

        mjbSelectOptions.setToolTipText("Filtrar opciones");
        mjbViewSelectedOptions.setToolTipText("Ver opciones filtradas");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas del documento");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        mjbViewContractAnalysis.setToolTipText("Ver detalles de vínculos");
        mjbPrintContractMoves.setToolTipText("Imprimir movimientos de contrato");

        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moDialogContractAnalysis = new SDialogContractAnalysis(miClient);

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(mjbSelectOptions);
        addTaskBarUpperComponent(mjbViewSelectedOptions);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewContractAnalysis);
        addTaskBarUpperComponent(mjbPrintContractMoves);
        
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterFunctionalArea);

        mjbSelectOptions.setEnabled(true);
        mjbViewSelectedOptions.setEnabled(true);
        mjbViewNotes.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        mjbViewContractAnalysis.setEnabled(isViewForEstimateCon());
        mjbPrintContractMoves.setEnabled(isViewForEstimateCon());

        STableField[] keyFields = new STableField[3];

        i = 0;
        keyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cone.id_year");
        keyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cone.id_doc");
        keyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cone.id_ety");
        for (i = 0; i < keyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(keyFields[i]);
        }
        
        STableColumn[] tableColumns = new STableColumn[58];

        i = 0;
        if (isViewForCategoryPur()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
            }
            else {
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
            }
        }
        else {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
            }
            else {
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
                tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
            }
        }
        
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 100);
        
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", 50);
            tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
        }
        else {
            tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
            tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", 50);
        }
        
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tcon.code", "Tipo contrato", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_con_num", "Folio contrato", STableConstants.WIDTH_DOC_NUM);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "con.num_ref", "Referencia contrato", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "con.dt", "Fecha contrato", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bcon.code", "Sucursal empresa contrato", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "con.dt_doc_delivery_n", "Entrega inicial contrato", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "con.dt_doc_lapsing_n", "Entrega final contrato", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fcon.code", "Área funcional contrato", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fscon.code", "Subárea funcional contrato", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "cone.sort_pos", "Partida contrato", 50);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cone.orig_qty", "Cantidad partida contrato", STableConstants.WIDTH_QUANTITY_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_orig_unit", "Unidad partida contrato", STableConstants.WIDTH_UNIT_SYMBOL);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_cone_orig_price_u_real_cur_r", "Precio u. contrato $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ccon.cur_key", "Moneda contrato", STableConstants.WIDTH_CURRENCY_KEY);
        
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tdor.code", "Tipo pedido", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_dor_num", "Folio pedido", STableConstants.WIDTH_DOC_NUM);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dor.num_ref", "Referencia pedido", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dor.dt", "Fecha pedido", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bdor.code", "Sucursal empresa pedido", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fdor.code", "Área funcional pedido", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fsdor.code", "Subárea funcional pedido", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "dore.sort_pos", "Partida pedido", 50);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "dore.orig_qty", "Cantidad partida pedido", STableConstants.WIDTH_QUANTITY_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_orig_unit", "Unidad partida pedido", STableConstants.WIDTH_UNIT_SYMBOL);
        
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tinv.code", "Tipo factura", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_inv_num", "Folio factura", STableConstants.WIDTH_DOC_NUM);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "inv.num_ref", "Referencia factura", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "inv.comms_ref", "Referencia factura", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "inv.dt", "Fecha factura", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "binv.code", "Sucursal empresa factura", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "finv.code", "Área funcional factura", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fsinv.code", "Subárea funcional factura", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "inv.tot_cur_r", "Total factura $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_inv_bal", "Saldo factura $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cinv.cur_key", "Moneda factura", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "inve.sort_pos", "Partida factura", 50);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "inve.orig_qty", "Cantidad partida factura", STableConstants.WIDTH_QUANTITY_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_orig_unit", "Unidad partida factura", STableConstants.WIDTH_UNIT_SYMBOL);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "inve.cont_tank", "Remolque, tanq./cont. partida factura", 100);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "inve.tank_car", "Carrotanque(s) partida factura", 100);
        
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tcn.code", "Tipo nota crédito", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cn_num", "Folio nota crédito", STableConstants.WIDTH_DOC_NUM);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cn.num_ref", "Referencia nota crédito", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cn.comms_ref", "Referencia nota crédito", STableConstants.WIDTH_DOC_NUM_REF);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "cn.dt", "Fecha nota crédito", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bcn.code", "Sucursal empresa nota crédito", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fcn.code", "Área funcional nota crédito", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fscn.code", "Subárea funcional nota crédito", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cn.tot_cur_r", "Total nota crédito $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ccn.cur_key", "Moneda nota crédito", STableConstants.WIDTH_CODE_DOC);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "cne.sort_pos", "Partida nota crédito", 50);
        tableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cne.orig_qty", "Cantidad partida nota crédito", STableConstants.WIDTH_QUANTITY_2X);
        tableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_orig_unit", "Unidad partida nota crédito", STableConstants.WIDTH_UNIT_SYMBOL);

        for (i = 0; i < tableColumns.length; i++) {
            moTablePane.addTableColumn(tableColumns[i]);
        }
        
        setIsSummaryApplying(false);
        
        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.TRN_DPS);

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

    private int[] getDpsTypeKey() {
        int[] typeKey = null;

        if (isViewForCategoryPur()) {
            if (isViewForEstimateEst()) {
                typeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_EST;
            }
            else if (isViewForEstimateCon()) {
                typeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_CON;
            }
        }
        else {
            if (isViewForEstimateEst()) {
                typeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
            }
            else if (isViewForEstimateCon()) {
                typeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
            }
        }

        return typeKey;
    }

    private int[] getSysMovementTypeKey() {
        int[] typeKey = null;

        if (isViewForCategoryPur()) {
            typeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
        }
        else {
            typeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
        }

        return typeKey;
    }

    private int getBizPartnerCategory() {
        int category = 0;

        if (isViewForCategoryPur()) {
            category = SDataConstantsSys.BPSS_CT_BP_SUP;
        }
        else {
            category = SDataConstantsSys.BPSS_CT_BP_CUS;
        }

        return category;
    }

    private void actionSelectOptions(final boolean reload) {
        if (moDialogContractFilter == null) {
            moDialogContractFilter = new SDialogContractFilter(miClient, getDpsTypeKey());
        }

        moDialogContractFilter.formReset();
        moDialogContractFilter.setFormVisible(true);

        if (moDialogContractFilter.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moSelectedOptions = (SDialogContractFilter.SelectedOptions) moDialogContractFilter.getValue(SDialogContractFilter.VALUE_SELECTED_OPTIONS);
        }
        else {
            moSelectedOptions = new SDialogContractFilter.SelectedOptions(SDataConstants.BPSU_BP);
        }
        
        if (moSelectedOptions.Options.isEmpty()) {
            miClient.showMsgBoxWarning("¡No hay ningún " + moSelectedOptions.getOptionsTypeDescrip() + " seleccionado!");
        }
        
        if (reload) {
            actionReload();
        }
    }
    
    private void actionViewSelectedOptions() {
        if (moSelectedOptions.Options.isEmpty()) {
            miClient.showMsgBoxWarning("¡No hay ningún " + moSelectedOptions.getOptionsTypeDescrip() + " seleccionado!");
        }
        else {
            int options = 0;
            String message = "";
            
            for (String option : moSelectedOptions.Options) {
                message += (message.isEmpty() ? "" : "\n") + ++options + ". " + option;
                if (options == MAX_OPTIONS) {
                    break;
                }
            }
            
            message = (options == 1 ? "Opción seleccionada" : "Opciones seleccionadas") + " de " + moSelectedOptions.getOptionsTypeDescrip() + ":\n" + message;
            if (options < moSelectedOptions.Options.size()) {
                int remaining = moSelectedOptions.Options.size() - options;
                message += "\n(Entre " + (remaining == 1 ? "otro" : "otros " + remaining) + " más.)";
            }
            
            miClient.showMsgBoxInformation(message);
        }
    }
    
    private void actionViewDps() {
        if (mjbViewDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL; // GUI module
                int[] dpsTypeKey = getDpsTypeKey();

                miClient.getGuiModule(gui).setFormComplement(dpsTypeKey);
                miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, moTablePane.getSelectedTableRow().getPrimaryKey());
            }
        }
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
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (moTablePane.getSelectedTableRow() != null) {
                    SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                    moDialogContractAnalysis.formReset();
                    moDialogContractAnalysis.setValue(SDataConstants.TRN_DPS, dps);
                    moDialogContractAnalysis.setFormVisible(true);
                }
            }
        }
    }
    
    private void actionPrintContractMoves() {
        if (mjbPrintContractMoves.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (moTablePane.getSelectedTableRow() != null && moTablePane.getSelectedTableRow().getPrimaryKey() != null) {
                    STrnUtilities.createReportContractAnalysis(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        if (moSelectedOptions == null) {
            actionSelectOptions(false);
        }
        
        int bizPartnerCategory = getBizPartnerCategory();
        int[] sysMovementTypeKey = getSysMovementTypeKey();
        String sqlWhere = "";
        String sqlOrderByBizPartner = "";
        String sqlOrderByItem = "";
        
        if (isViewForCategoryPur()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderByBizPartner = "bc.bp_key, b.bp,";
            }
            else {
                sqlOrderByBizPartner = "b.bp, bc.bp_key,";
            }
        }
        else {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderByBizPartner = "bc.bp_key, b.bp,";
            }
            else {
                sqlOrderByBizPartner = "b.bp, bc.bp_key,";
            }
        }
        
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            sqlOrderByItem = "i.item, i.item_key,";
        }
        else {
            sqlOrderByItem = "i.item_key, i.item,";
        }

        for (int i = 0; i < mvTableSettings.size(); i++) {
            STableSetting setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "con.fid_cob = " + (Integer) setting.getSetting() + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "con.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        String contracts = moSelectedOptions.composeSqlFilter("con");
        sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + (contracts.isEmpty() ? "FALSE " : contracts);
        
        msSql = "SELECT STRAIGHT_JOIN "
                // # contratos:
                + "b.bp, b.id_bp, bc.bp_key, "
                + "ig.igen, ig.id_igen, i.item_key, i.item, i.id_item, cone.id_year, cone.id_doc, cone.id_ety, "
                + "tcon.code, IF(con.num_ser <> '', CONCAT(con.num_ser, '-', con.num), con.num) AS _con_num, con.dt, con.num_ref, bcon.code, "
                + "fcon.id_func, fcon.code, fscon.id_func_sub, fscon.code, con.dt_doc_delivery_n, con.dt_doc_lapsing_n, "
                + "cone.sort_pos, cone.qty, u.symbol AS _unit, cone.orig_qty, ou.symbol AS _orig_unit, "
                + "cone.price_u_real_cur_r, ROUND(cone.stot_cur_r / cone.orig_qty, " + SErpConsts.VAL_QTY_MAX_DECS + ") AS _cone_orig_price_u_real_cur_r, ccon.cur_key, "
                // # pedidos:
                + "tdor.code, IF(dor.num_ser <> '', CONCAT(dor.num_ser, '-', dor.num), dor.num) AS _dor_num, dor.dt, dor.num_ref, bdor.code, "
                + "fdor.id_func, fdor.code, fsdor.id_func_sub, fsdor.code, "
                + "dore.sort_pos, dore.qty, u.symbol AS _unit, dore.orig_qty, ou.symbol AS _orig_unit, "
                // # facturas:
                + "tinv.code, IF(inv.num_ser <> '', CONCAT(inv.num_ser, '-', inv.num), inv.num) AS _inv_num, inv.dt, inv.num_ref, inv.comms_ref, binv.code, "
                + "finv.id_func, finv.code, fsinv.id_func_sub, fsinv.code, "
                + "inv.tot_cur_r, cinv.cur_key, "
                + "COALESCE((SELECT SUM(re.debit_cur - re.credit_cur) "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND r.id_tp_rec <> '" + SDataConstantsSys.FINU_TP_REC_FY_OPEN + "' AND re.fid_ct_sys_mov_xxx = " + sysMovementTypeKey[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMovementTypeKey[1] + " "
                + "AND re.fid_dps_year_n = inv.id_year AND re.fid_dps_doc_n = inv.id_doc), 0.0) AS _inv_bal, "
                + "inve.sort_pos, inve.qty, u.symbol AS _unit, inve.orig_qty, ou.symbol AS _orig_unit, inve.cont_tank, inve.tank_car, "
                // # notas de crédito:
                + "tcn.code, IF(cn.num_ser <> '', CONCAT(cn.num_ser, '-', cn.num), cn.num) AS _cn_num, cn.dt, cn.num_ref, cn.comms_ref, bcn.code, "
                + "fcn.id_func, fcn.code, fscn.id_func_sub, fscn.code, "
                + "cn.tot_cur_r, ccn.cur_key, "
                + "cne.sort_pos, cne.qty, u.symbol AS _unit, cne.orig_qty, ou.symbol AS _orig_unit "
                + ""
                + "FROM "
                // # contratos:
                + "trn_dps AS con "
                + "INNER JOIN erp.trnu_tp_dps AS tcon ON tcon.id_ct_dps = con.fid_ct_dps AND tcon.id_cl_dps = con.fid_cl_dps AND tcon.id_tp_dps = con.fid_tp_dps "
                + "INNER JOIN erp.bpsu_bpb AS bcon ON bcon.id_bpb = con.fid_cob "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = con.fid_bp_r "
                + "INNER JOIN erp.bpsu_bp_ct AS bc ON bc.id_bp = con.fid_bp_r AND bc.id_ct_bp = " + bizPartnerCategory + " "
                + "INNER JOIN cfgu_func AS fcon ON fcon.id_func = con.fid_func "
                + "INNER JOIN cfgu_func_sub AS fscon ON fscon.id_func_sub = con.fid_func_sub "
                + "INNER JOIN erp.cfgu_cur AS ccon ON ccon.id_cur = con.fid_cur "
                + "INNER JOIN trn_dps_ety AS cone ON cone.id_year = con.id_year AND cone.id_doc = con.id_doc "
                + "INNER JOIN erp.itmu_item AS i ON i.id_item = cone.fid_item "
                + "INNER JOIN erp.itmu_unit AS u ON u.id_unit = cone.fid_unit "
                + "INNER JOIN erp.itmu_unit AS ou ON ou.id_unit = cone.fid_orig_unit "
                + "INNER JOIN erp.itmu_igen AS ig ON ig.id_igen = i.fid_igen "
                + "LEFT OUTER JOIN trn_dps_dps_supply AS con_sup ON con_sup.id_src_year = cone.id_year AND con_sup.id_src_doc = cone.id_doc AND con_sup.id_src_ety = cone.id_ety "
                // # pedidos:
                + "LEFT OUTER JOIN trn_dps_ety AS dore ON dore.id_year = con_sup.id_des_year AND dore.id_doc = con_sup.id_des_doc AND dore.id_ety = con_sup.id_des_ety "
                + "LEFT OUTER JOIN trn_dps AS dor ON dor.id_year = dore.id_year AND dor.id_doc = dore.id_doc "
                + "LEFT OUTER JOIN erp.trnu_tp_dps AS tdor ON tdor.id_ct_dps = dor.fid_ct_dps AND tdor.id_cl_dps = dor.fid_cl_dps AND tdor.id_tp_dps = dor.fid_tp_dps "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS bdor ON bdor.id_bpb = dor.fid_cob "
                + "LEFT OUTER JOIN cfgu_func AS fdor ON fdor.id_func = dor.fid_func "
                + "LEFT OUTER JOIN cfgu_func_sub AS fsdor ON fsdor.id_func_sub = dor.fid_func_sub "
                + "LEFT OUTER JOIN trn_dps_dps_supply AS dor_sup ON dor_sup.id_src_year = dore.id_year AND dor_sup.id_src_doc = dore.id_doc AND dor_sup.id_src_ety = dore.id_ety "
                // # facturas:
                + "LEFT OUTER JOIN trn_dps_ety AS inve ON inve.id_year = dor_sup.id_des_year AND inve.id_doc = dor_sup.id_des_doc AND inve.id_ety = dor_sup.id_des_ety "
                + "LEFT OUTER JOIN trn_dps AS inv ON inv.id_year = inve.id_year AND inv.id_doc = inve.id_doc AND inv.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " "
                + "LEFT OUTER JOIN erp.trnu_tp_dps AS tinv ON tinv.id_ct_dps = inv.fid_ct_dps AND tinv.id_cl_dps = inv.fid_cl_dps AND tinv.id_tp_dps = inv.fid_tp_dps "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS binv ON binv.id_bpb = inv.fid_cob "
                + "LEFT OUTER JOIN cfgu_func AS finv ON finv.id_func = inv.fid_func "
                + "LEFT OUTER JOIN cfgu_func_sub AS fsinv ON fsinv.id_func_sub = inv.fid_func_sub "
                + "LEFT OUTER JOIN erp.cfgu_cur AS cinv ON cinv.id_cur = inv.fid_cur "
                + "LEFT OUTER JOIN trn_dps_dps_adj AS inv_adj ON inv_adj.id_dps_year = inve.id_year AND inv_adj.id_dps_doc = inve.id_doc AND inv_adj.id_dps_ety = inve.id_ety "
                // # notas de crédito:
                + "LEFT OUTER JOIN trn_dps_ety AS cne ON cne.id_year = inv_adj.id_adj_year AND cne.id_doc = inv_adj.id_adj_doc AND cne.id_ety = inv_adj.id_adj_ety "
                + "LEFT OUTER JOIN trn_dps AS cn ON cn.id_year = cne.id_year AND cn.id_doc = cne.id_doc AND cn.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " "
                + "LEFT OUTER JOIN erp.trnu_tp_dps AS tcn ON tcn.id_ct_dps = cn.fid_ct_dps AND tcn.id_cl_dps = cn.fid_cl_dps AND tcn.id_tp_dps = cn.fid_tp_dps "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS bcn ON bcn.id_bpb = cn.fid_cob "
                + "LEFT OUTER JOIN cfgu_func AS fcn ON fcn.id_func = cn.fid_func "
                + "LEFT OUTER JOIN cfgu_func_sub AS fscn ON fscn.id_func_sub = cn.fid_func_sub "
                + "LEFT OUTER JOIN erp.cfgu_cur AS ccn ON ccn.id_cur = cn.fid_cur "
                + ""
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + ""
                + "ORDER BY "
                + sqlOrderByBizPartner + " b.id_bp, "
                + "ig.igen, ig.id_igen, "
                + sqlOrderByItem + " i.id_item, "
                + "tcon.code, con.num_ser, LPAD(con.num, " + SDataConstantsSys.NUM_LEN_DPS + ", '0'), con.id_year, con.id_doc, cone.sort_pos, "
                + "tdor.code, dor.num_ser, LPAD(dor.num, " + SDataConstantsSys.NUM_LEN_DPS + ", '0'), dor.id_year, dor.id_doc, "
                + "tinv.code, inv.num_ser, LPAD(inv.num, " + SDataConstantsSys.NUM_LEN_DPS + ", '0'), inv.id_year, inv.id_doc, "
                + "tcn.code, cn.num_ser, LPAD(cn.num, " + SDataConstantsSys.NUM_LEN_DPS + ", '0'), cn.id_year, cn.id_doc;";
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

            if (button == mjbSelectOptions) {
                actionSelectOptions(true);
            }
            else if (button == mjbViewSelectedOptions) {
                actionViewSelectedOptions();
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
            else if (button == mjbViewContractAnalysis) {
                actionViewContractAnalysis();
            }
            else if (button == mjbPrintContractMoves) {
                actionPrintContractMoves();
            }
        }
    }
}
