/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
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
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterUsers;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Sergio Flores
 */
public class SViewDpsLink extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbClose;
    private javax.swing.JButton mjbOpen;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjbViewContractAnalysis;
    private javax.swing.JButton mjbPrintContractMoves;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;

    private boolean mbHasRightAuthor = false;

    /**
     * View for documents linked or pending to be linked to other documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param type Constants defined in SDataConstants (TRNX_DPS_LINK_PEND..., TRNX_DPS_LINKED...).
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsLink(erp.client.SClientInterface client, java.lang.String tabTitle, int type, int auxType01, int auxType02) {
        super(client, tabTitle, type, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        boolean hasRightToLink = false;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mbHasRightAuthor = false;

        if (isViewForCategoryPur()) {
            if (isViewForEstimate()) {
                hasRightToLink = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
            else if (isViewForOrder()) {
                hasRightToLink = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
            }
        }
        else {
            if (isViewForEstimate()) {
                hasRightToLink = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
            else if (isViewForOrder()) {
                hasRightToLink = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level == SUtilConsts.LEV_MANAGER;
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
            }
        }
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjbViewContractAnalysis = new JButton(miClient.getImageIcon(SLibConstants.ICON_CONTRACT_ANALYSIS));
        mjbPrintContractMoves = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_moves.gif")));
        
        mjbClose.setPreferredSize(new Dimension(23, 23));
        mjbOpen.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjbViewContractAnalysis.setPreferredSize(new Dimension(23, 23));
        mjbPrintContractMoves.setPreferredSize(new Dimension(23, 23));

        mjbClose.addActionListener(this);
        mjbOpen.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        mjbViewContractAnalysis.addActionListener(this);
        mjbPrintContractMoves.addActionListener(this);

        mjbClose.setToolTipText("Cerrar documento");
        mjbOpen.setToolTipText("Abrir documento");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        mjbViewContractAnalysis.setToolTipText("Ver detalles de vínculos");
        mjbPrintContractMoves.setToolTipText("Imprimir movimientos de contrato");

        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogContractAnalysis = new SDialogContractAnalysis(miClient);

        if (isViewForDocLinked()) {
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, isViewForEstimateCon() ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);
            moTabFilterDateCutOff = null;
        }
        else {
            moTabFilterDatePeriod = null;
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
        }

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(mjbClose);
        addTaskBarUpperComponent(mjbOpen);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(isViewForDocLinked() ? moTabFilterDatePeriod : moTabFilterDateCutOff);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperComponent(mjbViewContractAnalysis);
        addTaskBarUpperComponent(mjbPrintContractMoves);
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterDocumentNature);

        mjbClose.setEnabled(hasRightToLink && !isViewForDocLinked());
        mjbOpen.setEnabled(hasRightToLink && isViewForDocLinked());
        mjbViewNotes.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        mjbViewContractAnalysis.setEnabled(isViewForEstimateCon());
        mjbPrintContractMoves.setEnabled(isViewForEstimateCon());

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[!isViewForDocEntries() ? 17 : 26];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_doc_delivery_n", "Entrega inicial", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_doc_lapsing_n", "Entrega final", STableConstants.WIDTH_DATE);
        if (isViewForCategoryPur()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
            }
        }
        else {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
            }
        }

        if (isViewForDocEntries()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "igen", "Ítem genérico", 200);
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_link_orig_qty", "Cant. procesada", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());

        if (isViewForDocEntries()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_orig_unit", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Avance %", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_link_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Cant. pendiente", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_link_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));

        if (isViewForDocEntries()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_orig_unit", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "surplus_per", "Excedente %", STableConstants.WIDTH_PERCENTAGE);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_orig_price_u", "Precio u. moneda $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Monto pendiente $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
            aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
            aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_link_orig_qty", SLibRpnArgumentType.OPERAND));
            aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
            aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_orig_price_u", SLibRpnArgumentType.OPERAND));
            aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza doc.", STableConstants.WIDTH_CODE_DOC);

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_link", "Cerrado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr", "Usr. cierre", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_link", "Cierre", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINK_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINKED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private boolean isViewForCategoryPur() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }

    private boolean isViewForDocLinked() {
        return mnTabType == SDataConstants.TRNX_DPS_LINKED || mnTabType == SDataConstants.TRNX_DPS_LINKED_ETY;
    }

    private boolean isViewForDocEntries() {
        return mnTabType == SDataConstants.TRNX_DPS_LINK_PEND_ETY || mnTabType == SDataConstants.TRNX_DPS_LINKED_ETY;
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

    private void actionCloseDoc() {
        if (mjbClose.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_CLOSE) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<Object>();

                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(SDataConstantsSys.UPD_DPS_FL_LINK);
                    params.add(1);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.size() == 0) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                    }
                    else {
                        if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                        }
                        else {
                            miClient.getGuiModule(isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabTypeAux01);
                        }
                    }
                }
            }
        }
    }

    private void actionOpenDoc() {
        if (mjbOpen.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient,
                    SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (isViewForDocLinked() && !oDps.getIsLinked()) {
                    miClient.showMsgBoxInformation("No se puede abrir un documento vinculado al 100%.");
                }
                else {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_OPEN) == JOptionPane.YES_OPTION) {
                        Vector<Object> params = new Vector<Object>();

                        params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                        params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                        params.add(SDataConstantsSys.UPD_DPS_FL_LINK);
                        params.add(0);
                        params.add(miClient.getSession().getUser().getPkUserId());
                        params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                        if (params.size() == 0) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                        }
                        else {
                            if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                            }
                            else {
                                miClient.getGuiModule(isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabTypeAux01);
                            }
                        }
                    }
                }
            }
        }
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
        String sqlDpsType = "";
        String sqlDatePeriod = "";
        String sqlCompanyBranch = "";
        String sqlBizPartner = "";
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewForDocLinked()) {
                    sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
                }
                else {
                    sqlDatePeriod = setting.getSetting() == null ? "" : "AND d.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_bp_r = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
        }

        sqlDpsType = "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " ";

        if (isViewForDocEntries()) {
            msSql = "";
        }
        else {
            msSql = "SELECT id_year, id_doc, dt, dt_doc_delivery_n, dt_doc_lapsing_n, num_ref, b_link, ts_link, " +
                    "f_num, fid_cob, fid_bpb, fid_bp_r, fid_usr_link, " +
                    "f_dt_code, f_dn_code, f_cob_code, bpb, bp, bp_key, usr, " +
                    "SUM(f_qty) AS f_qty, SUM(f_orig_qty) AS f_orig_qty, " +
                    "SUM(f_link_qty) AS f_link_qty, SUM(f_link_orig_qty) AS f_link_orig_qty, " +
                    "COUNT(*) AS f_count, " +
                    "COUNT(f_link_orig_qty >= f_orig_qty) AS f_count_link " +
                    "FROM (";
        }

        msSql += "SELECT de.id_year, de.id_doc, d.dt, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, d.num_ref, d.b_link, d.ts_link, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, " +
                "dt.code AS f_dt_code, dn.code AS f_dn_code, cob.code AS f_cob_code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, de.surplus_per, " +
                "de.qty AS f_qty, de.orig_qty AS f_orig_qty, " +
                "CASE WHEN de.qty = 0 THEN 0 ELSE de.stot_cur_r / de.qty END AS f_price_u, " +
                "CASE WHEN de.orig_qty = 0 THEN 0 ELSE de.stot_cur_r / de.orig_qty END AS f_orig_price_u, " +
                "i.item_key, i.item, ig.igen, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "COALESCE((SELECT SUM(ds.qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd " +
                "WHERE ds.id_src_year = de.id_year AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety AND " +
                "ds.id_des_year = xde.id_year AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND " +
                "xde.id_year = xd.id_year AND xde.id_doc = xd.id_doc AND " +
                "xde.b_del = 0 AND xd.b_del = 0 AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0) AS f_link_qty, " +
                "COALESCE((SELECT SUM(ds.orig_qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd " +
                "WHERE ds.id_src_year = de.id_year AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety AND " +
                "ds.id_des_year = xde.id_year AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND " +
                "xde.id_year = xd.id_year AND xde.id_doc = xd.id_doc AND " +
                "xde.b_del = 0 AND xd.b_del = 0 AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0) AS f_link_orig_qty " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                sqlDpsType + sqlDatePeriod + sqlCompanyBranch + sqlBizPartner +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.trnu_dps_nat AS dn ON d.fid_dps_nat = dn.id_dps_nat " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " +
                (isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "GROUP BY de.id_year, de.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.b_link, d.ts_link, " +
                "d.num_ser, d.num, d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, " +
                "dt.code, cob.code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, de.surplus_per, " +
                "de.qty, de.orig_qty, de.stot_cur_r, " +
                "i.item_key, i.item, u.symbol, uo.symbol ";

        if (isViewForDocEntries()) {
            if (isViewForDocLinked()) {
                msSql += "HAVING f_link_orig_qty >= de.orig_qty OR d.b_link = 1 ";
            }
            else {
                msSql += "HAVING f_link_orig_qty < de.orig_qty AND d.b_link = 0 ";
            }

            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, de.id_year, de.id_doc, ";

            if (isViewForCategoryPur()) {
                msSql += miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bc.bp_key, b.bp, " : "b.bp, bc.bp_key, ";
            }
            else {
                msSql += miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bc.bp_key, b.bp, " : "b.bp, bc.bp_key, ";
            }

            msSql += "d.fid_bp_r, bb.bpb, d.fid_bpb, ";

            if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                msSql += "i.item_key, i.item, ";
            }
            else {
                msSql += "i.item, i.item_key, ";
            }

            msSql += "de.fid_item, uo.symbol, de.fid_orig_unit ";
        }
        else {
            msSql += ") AS T " +
                    "GROUP BY id_year, id_doc, dt, dt_doc_delivery_n, num_ref, b_link, ts_link, " +
                    "f_num, fid_cob, fid_bpb, fid_bp_r, fid_usr_link, " +
                    "f_dt_code, f_cob_code, bpb, bp, bp_key, usr ";

            if (isViewForDocLinked()) {
                msSql += "HAVING (f_link_orig_qty >= f_orig_qty OR b_link = 1) AND f_count = f_count_link ";
            }
            else {
                msSql += "HAVING (f_link_orig_qty < f_orig_qty AND b_link = 0) OR f_count <> f_count_link ";
            }

            msSql += "ORDER BY f_dt_code, f_num, dt, id_year, id_doc, ";

            if (isViewForCategoryPur()) {
                msSql += miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bp_key, bp, " : "bp, bp_key, ";
            }
            else {
                msSql += miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bp_key, bp, " : "bp, bp_key, ";
            }

            msSql += "fid_bp_r, bpb, fid_bpb ";
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

            if (button == mjbClose) {
                actionCloseDoc();
            }
            else if (button == mjbOpen) {
                actionOpenDoc();
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
