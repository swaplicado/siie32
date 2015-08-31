/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadRegistries;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.print.SPrintConstants;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataCheckPrintingFormatGraphic;
import erp.mfin.data.SDataCheckWallet;
import erp.print.SPrintCheck;
import erp.print.SPrintCheckBack;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.print.*;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Alfonso Flores
 */
public class SViewCheck extends erp.lib.table.STableTab implements java.awt.event.ActionListener, Printable {

    private javax.swing.JButton mjbPrintCheck;
    private javax.swing.JButton mjbPrintCheckRecord;
    private javax.swing.JButton mjbPrintPreviewCheck;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.data.SDataCheck moCheck;

    public SViewCheck(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FIN_CHECK);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        mjbPrintPreviewCheck = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        mjbPrintPreviewCheck.setPreferredSize(new Dimension(23, 23));
        mjbPrintPreviewCheck.addActionListener(this);
        mjbPrintPreviewCheck.setToolTipText("Imprimir vista previa cheque");

        mjbPrintCheck = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        mjbPrintCheck.setPreferredSize(new Dimension(23, 23));
        mjbPrintCheck.addActionListener(this);
        mjbPrintCheck.setToolTipText("Imprimir cheque");

        mjbPrintCheckRecord = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        mjbPrintCheckRecord.setPreferredSize(new Dimension(23, 23));
        mjbPrintCheckRecord.addActionListener(this);
        mjbPrintCheckRecord.setToolTipText("Imprimir póliza de cheque");

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbPrintPreviewCheck);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbPrintCheck);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbPrintCheckRecord);

        mjbPrintPreviewCheck.setEnabled(true);
        mjbPrintCheck.setEnabled(true);
        mjbPrintCheckRecord.setEnabled(true);

        jbNew.setToolTipText("Crear cheque anulado [Ctrl+N]");
        jbEdit.setToolTipText("Modificar cheque anulado [Ctrl+M]");
        jbDelete.setToolTipText("Anular cheque [Ctrl+D]");

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[15];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_check_wal");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_check");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Cuenta efectivo", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "c.num", "Folio", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.benef", "Beneficiario", 200);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "c.val", "Monto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "s.st_dps", "Estado cheque", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_benef_acc", "Abono cta. benef.", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_CHECK).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.FIN_REC);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "c.dt");
            }
        }

        msSql = "SELECT c.id_check_wal, c.id_check, c.dt, c.num, c.benef, c.val, c.b_benef_acc, c.b_del, s.st_dps, e.ent, " +
            "cur.cur_key, c.ts_new, c.ts_edit, c.ts_del, un.usr, ue.usr, ud.usr " +
            "FROM fin_check AS c " +
            "INNER JOIN erp.trns_st_dps AS s ON c.fid_st_check = s.id_st_dps " +
            "INNER JOIN fin_check_wal AS w ON c.id_check_wal = w.id_check_wal " +
            "INNER JOIN erp.cfgu_cob_ent AS e ON w.fid_cob = e.id_cob AND w.fid_acc_cash = e.id_ent " +
            "INNER JOIN fin_acc_cash AS acc ON w.fid_cob = acc.id_cob AND w.fid_acc_cash = acc.id_acc_cash " +
            "INNER JOIN erp.cfgu_cur AS cur ON acc.fid_cur = cur.id_cur " +
            "INNER JOIN erp.usru_usr AS un ON c.fid_usr_new = un.id_usr " +
            "INNER JOIN erp.usru_usr AS ue ON c.fid_usr_edit = ue.id_usr " +
            "INNER JOIN erp.usru_usr AS ud ON c.fid_usr_del = ud.id_usr " +
            "WHERE " + sqlWhere +
            "ORDER BY c.dt, e.ent, c.num, c.id_check ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_CHECK, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moCheck = new SDataCheck();

                moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (moCheck.getFkCheckStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED || moCheck.getBeneficiary().length() > 0) {
                    miClient.showMsgBoxWarning(STableConstants.MSG_WAR_REGISTRY_NO_EDITABLE);
                }
                else {
                    if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        Vector<Object> params = new Vector<Object>();

        if (jbDelete.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moCheck = new SDataCheck();

                moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (moCheck.getFkCheckStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    miClient.showMsgBoxWarning("El registro seleccionado ya esta anulado");
                }
                else {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                        params.clear();

                        params.add(moCheck.getPkCheckWalletId());
                        params.add(moCheck.getPkCheckId());
                        params.add(SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                        params.add(miClient.getSession().getUser().getPkUserId());
                        params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_CHECK_CANCEL, params, SLibConstants.EXEC_MODE_SILENT);

                        miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    public void actionPrint(boolean bPrintPreview) {
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SPrintCheck oPrint = null;
        SDataCheckWallet oWallet = null;
        SDataCheckPrintingFormatGraphic oFormat = null;
        SDataAccountCash oAccCash = null;
        SDataBizPartnerBranch  oBranch = null;
        SDataCurrency oCurrency = null;
        Vector<Object> params = new Vector<Object>();

        if (moTablePane.getSelectedTableRow() != null) {
            moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            if (moCheck.getFkCheckStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                oWallet = (SDataCheckWallet) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK_WAL, new int[] { moCheck.getPkCheckWalletId() }, SLibConstants.EXEC_MODE_SILENT);
                oAccCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, new int[] { oWallet.getFkCompanyBranchId(), oWallet.getFkAccountCashId() }, SLibConstants.EXEC_MODE_SILENT);

                if (oAccCash.getFkCheckFormatId_n() > 0) {
                    oPrint = new SPrintCheck(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    //oPrint.preparePrinting();
                    oPrint.setPrintPreview(bPrintPreview);
                    oPrint.printDocument();
                }
                else {
                    oFormat = (SDataCheckPrintingFormatGraphic) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_CHECK_FMT_GP, new int[] { oAccCash.getFkCheckFormatGpId_n() }, SLibConstants.EXEC_MODE_SILENT);
                    oCurrency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR,
                        new int[] { moCheck.getAuxCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    try {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));

                        map = miClient.createReportParams();
                        map.put("sDate", miClient.getSessionXXX().getFormatters().getDateTextFormat().format(moCheck.getDate()).toUpperCase());
                        map.put("sBeneficiary", moCheck.getBeneficiary());
                        map.put("dValue", moCheck.getValue());
                        map.put("sValueText", SLibUtilities.translateValueToText(moCheck.getValue(), 2,
                            oCurrency.getPkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH,
                            oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));

                        params.clear();
                        params.add(moCheck.getPkCheckWalletId());
                        params.add(moCheck.getPkCheckId());
                        params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_CHECK_NUM_REC, params, SLibConstants.EXEC_MODE_SILENT);

                        if (params.get(0) == null) {
                            miClient.showMsgBoxWarning("No se encontro la póliza contable.");
                        }
                        else {
                            map.put("sRecordNumber", (String) params.get(0));
                        }

                        map.put("sCheckNumber", "C" + moCheck.getNumber());
                        map.put("sIsForBeneficiaryText", SLibConstants.TXT_BENEF_ACC_DEP);

                        oBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oWallet.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);

                        map.put("sLocality", oBranch.getDbmsBizPartnerBranchAddressOfficial().getLocality() + ", " +
                            oBranch.getDbmsBizPartnerBranchAddressOfficial().getCounty());
                        map.put("bIsRecordNumberApplying", true);
                        map.put("bIsCheckNumberApplying", true);
                        map.put("bIsLocalityApplying", false);
                        map.put("bIsForBeneficiaryApplying", moCheck.getIsForBeneficiaryAccount());

                        jasperPrint = SDataUtilities.fillCheck(miClient, "reps/" + oFormat.getFormatName(), map);
                        jasperViewer = new JasperViewer(jasperPrint, false);
                        jasperViewer.setTitle("Impresión cheque");
                        jasperViewer.setVisible(true);
                    }
                    catch(Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                    finally {
                        setCursor(cursor);
                    }
                }
            }

            actionPrintBack(bPrintPreview);
        }
    }

    public void actionPrintBack(boolean bPrintPreview) {
        SPrintCheckBack oPrintBack = null;
        SDataBizPartnerBranchBankAccount oBankAccount = null;
        SDataCheckWallet oWallet = null;
        SDataAccountCash oAccCash = null;
        Vector<erp.lib.data.SDataRegistry> vAccount = new Vector<SDataRegistry>();

        if (moTablePane.getSelectedTableRow() != null) {
            moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            vAccount = SDataReadRegistries.readRegistries(miClient, SDataConstants.BPSX_BANK_ACC_CHECK, moCheck.getPrimaryKey());
            oWallet = (SDataCheckWallet) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK_WAL, new int[] { moCheck.getPkCheckWalletId() }, SLibConstants.EXEC_MODE_SILENT);
            oAccCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, new int[] { oWallet.getFkCompanyBranchId(), oWallet.getFkAccountCashId() }, SLibConstants.EXEC_MODE_SILENT);

            if (vAccount.size() > 0) {
                oBankAccount = (SDataBizPartnerBranchBankAccount) vAccount.get(0);

                if (oBankAccount != null) {
                    oPrintBack = new SPrintCheckBack(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), oAccCash.getFkCheckFormatId_n() != 0 ? 0 : SPrintConstants.TAB_QUARTER);
                    //oPrint.preparePrinting();
                    oPrintBack.setPrintPreview(bPrintPreview);
                    oPrintBack.printDocument();
                }
            }
        }
    }

    public void actionPrintCheckRecord() {
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SDataCurrency oCurrency = null;

        if (moTablePane.getSelectedTableRow() != null) {
            moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            if (moCheck.getFkCheckStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                oCurrency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR,
                        new int[] { moCheck.getAuxCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                try {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    map = miClient.createReportParams();
                    map.put("nIdCheckWallet", moCheck.getPkCheckWalletId());
                    map.put("nIdCheck", moCheck.getPkCheckId());
                    map.put("sValueText", SLibUtilities.translateValueToText(moCheck.getValue(), 2,
                        oCurrency.getPkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH,
                        oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                    map.put("sIsForBeneficiaryText", SLibConstants.TXT_BENEF_ACC_DEP);

                    jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_CHECK_REC, map);
                    jasperViewer = new JasperViewer(jasperPrint, false);
                    jasperViewer.setTitle("Impresión póliza de cheque");
                    jasperViewer.setVisible(true);
                }
                catch(Exception e) {
                    SLibUtilities.renderException(this, e);
                }
                finally {
                    setCursor(cursor);
                }
            }
        }
    }

    public void actionPrintTemporal() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
          try {
            job.print();
          } catch (PrinterException e) {
          }
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbPrintCheck) {
                actionPrint(false);
            }
            else if (button == mjbPrintPreviewCheck) {
                actionPrint(true);
            }
            else if (button == mjbPrintCheckRecord) {
                actionPrintCheckRecord();
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

        return PAGE_EXISTS;
    }
}
