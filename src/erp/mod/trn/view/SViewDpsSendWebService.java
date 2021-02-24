/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Isabel Servín
 */
public class SViewDpsSendWebService extends SGridPaneView implements ActionListener {

    private JButton mjbViewDps;
    private JButton mjbViewNotes;
    private JButton jbSend;
    private JButton jbBack;
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewDpsSendWebService(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_CFD, gridSubtype, title, params);
        initComponents();
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }
    
    private void initComponents() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento", this);
        mjbViewNotes = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NOTES), "Ver notas", this);
        jbSend = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar", this);
        jbBack = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Regresar a facturas x enviar x WS", this);
        
        if (mnGridSubtype != SModSysConsts.TRNS_ST_XML_DVY_PENDING) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewNotes);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSend);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbBack);
        
        jbSend.setEnabled(mnGridSubtype != SModSysConsts.TRNS_ST_XML_DVY_APPROVED);
        jbBack.setEnabled(mnGridSubtype != SModSysConsts.TRNS_ST_XML_DVY_PENDING);
    }

    private void actionViewDps() {
        int gui = SDataConstants.MOD_SAL;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                ((SClientInterface) miClient).getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_SAL_INV);
                ((SClientInterface) miClient).getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionViewNotes() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SModuleUtilities.showDocumentNotes((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionSend() {
        if (jbSend.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        STrnUtilities.sendDocumentSoriana(((SClientInterface) miClient), gridRow.getRowPrimaryKey());

                        miClient.getSession().notifySuscriptors(mnGridType);
                        miClient.getSession().notifySuscriptors(mnGridSubtype);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionBack() {
        if (jbBack.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        SDataDps dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        Date dateDelivery = dps.getDateDocDelivery_n();
                        String confirmMessage = "";
                        if (dateDelivery != null) {
                            confirmMessage = "La factura fue enviada el día " + SLibUtils.DbmsDateFormatDate.format(dateDelivery) + "\n";
                        }
                        confirmMessage += "¿Seguro desea regresarla a facturas x enviar x WS?";
                        if (miClient.showMsgBoxConfirm(confirmMessage) == JOptionPane.OK_OPTION){
                            SDataCfd cfd = dps.getDbmsDataCfd();
                            cfd.saveField(((SClientInterface) miClient).getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_ST, SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        miClient.getSession().notifySuscriptors(mnGridSubtype);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(2);

        if (mnGridSubtype != SModSysConsts.TRNS_ST_XML_DVY_PENDING) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "AND " : "") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        
        msSql = "SELECT "
                + "v.fid_dps_year_n AS " + SDbConsts.FIELD_ID + "1, "
                + "v.fid_dps_doc_n AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "d.dt, d.dt_doc_delivery_n, d.num_ser, d.num, d.num_ref, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, "
                + "d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, bb.id_bpb, bb.bpb, cb.code, ui.usr, "
                + "v.ack_dvy, "
                + "v.msg_dvy, "
                + "tp.tp_xml_dvy, "
                + "st.st_xml_dvy, "
                + "v.fid_usr_dvy AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.ts_dvy AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "v.fid_dps_year_n = d.id_year AND v.fid_dps_doc_n = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND ";
        
                switch (mnGridMode) {
                    case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        msSql += "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                        break;

                    case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                        msSql += "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2] + " ";
                        break;
                    default:
                }
                
        msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_XML_DVY) + " AS tp ON "
                + "v.fid_tp_xml_dvy = tp.id_tp_xml_dvy "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_XML_DVY) + " AS st ON "
                + "v.fid_st_xml_dvy = st.id_st_xml_dvy "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fid_usr_dvy = ui.id_usr "
                + "WHERE d.b_del = 0 AND v.fid_tp_xml_dvy = " + SModSysConsts.TRNS_TP_XML_DVY_WS_SOR + " AND v.fid_st_xml_dvy = " + mnGridSubtype + " " + sql + " "
                + "ORDER BY dt.code, f_num, num_ref, d.dt ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dt.code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "num_ref", "Referencia doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", "Sucursal empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada"));
        
        if (((SClientInterface) miClient).getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave cliente"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.bp", "Cliente"));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.bp", "Cliente"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave cliente"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.bpb", "Sucursal cliente"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "d.tot_cur_r", "Total mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda"));
        
        if (mnGridSubtype != SModSysConsts.TRNS_ST_XML_DVY_PENDING) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.msg_dvy", "Mensaje"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp.tp_xml_dvy", "Tipo entrega"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, "Usr envío"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, "Envío"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SDataConstants.TRN_DPS);
        moSuscriptionsSet.add(SDataConstants.TRNU_TP_DPS);
        moSuscriptionsSet.add(SDataConstants.CFGU_CUR);
        moSuscriptionsSet.add(SDataConstants.BPSU_BP);
        moSuscriptionsSet.add(SDataConstants.BPSU_BP_CT);
        moSuscriptionsSet.add(SDataConstants.BPSU_BPB);
        moSuscriptionsSet.add(SDataConstants.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == jbSend) {
                actionSend();
            }
            else if (button == jbBack) {
                actionBack();
            }
        }
    }
}
