/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.form.SDialogMfgAssignDate;
import erp.mmfg.form.SDialogMfgAssignRework;
import erp.mmfg.form.SDialogMfgChangeState;
import erp.mmfg.form.SDialogMfgCreateLot;
import erp.mtrn.data.StockException;
import erp.mtrn.form.SDialogStockCardexProdOrder;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterProductionOrderType;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Néstor Ávalos, Edwin Carmona
 */

public class SViewProductionOrder extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    SClientInterface miClient;
    
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterProductionOrderType moTabFilterProdOrderType;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private javax.swing.JButton jbAssignLotFinishedGood;
    private javax.swing.JButton jbAssignDate;
    private javax.swing.JButton jbCardex;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintPerformance;
    private javax.swing.JButton jbProductionOrderPreviousStep;
    private javax.swing.JButton jbProductionOrderNextStep;
    private javax.swing.JButton jbProgramOrder;
    private javax.swing.JButton jbAssignQuantityRework;

    private erp.mmfg.data.SDataProductionOrder moProductionOrder;
    private erp.mmfg.form.SDialogMfgAssignDate moDialogMfgAssignDate;
    private erp.mmfg.form.SDialogMfgAssignRework moDialogMfgAssignRework;
    private erp.mmfg.form.SDialogMfgChangeState moDialogMfgChangeState;
    private erp.mmfg.form.SDialogMfgCreateLot moDialogMfgCreateLot;
    private erp.mtrn.form.SDialogStockCardexProdOrder moDialogStockCardexProdOrder;

    public SViewProductionOrder(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MFG_ORD, auxType01);
        miClient = client;
        initComponents();
    }

    public SViewProductionOrder(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int autType02) {
        super(client, tabTitle, SDataConstants.MFG_ORD, auxType01, autType02);
        miClient = client;
        initComponents();
    }

    private void initComponents() {
        int i;
        int nLevelOrderStatusNew = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_NEW).Level;
        int nLevelOrderStatusLot = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_LOT).Level;
        int nLevelOrderStatusLotAssing = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_LOT_ASG).Level;
        int nLevelOrderStatusLotProcess = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_PROC).Level;
        int nLevelOrderStatusLotEnd = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_END).Level;
        int nLevelOrderStatusLotClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_CLS).Level;
        int nLevelOrderStatusDelay = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.MFGS_ST_ORD_DLY).Level;
        boolean bPrivilegeOrderCreate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_NEW).HasRight;
        boolean bPrivilegeOrderStatusNew = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_NEW).HasRight;
        boolean bPrivilegeOrderStatusLot = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_LOT).HasRight;
        boolean bPrivilegeOrderStatusLotAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_LOT_ASG).HasRight;
        boolean bPrivilegeOrderStatusProcess = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_PROC).HasRight;
        boolean bPrivilegeOrderStatusEnd = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_END).HasRight;
        boolean bPrivilegeOrderStatusClose = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ORD_ST_CLS).HasRight;
        boolean bPrivilegeOrderStatusDelay = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.MFGS_ST_ORD_DLY).HasRight;
        boolean bPrivilegeOrderAssignRework = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ASG_REW).HasRight;
        boolean bPrivilegeOrderAssignDate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ASG_DATE).HasRight;
        
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, mnTabTypeAux01 != SDataConstantsSys.MFGS_ST_ORD_DLY ?
            SLibConstants.GUI_DATE_AS_YEAR_MONTH : SLibConstants.GUI_DATE_AS_DATE);
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterProdOrderType = new STabFilterProductionOrderType(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);

        jbProductionOrderPreviousStep = new JButton(miClient.getImageIcon(SLibConstants.ICON_ARROW_LEFT));
        jbProductionOrderPreviousStep.setPreferredSize(new Dimension(23, 23));
        jbProductionOrderPreviousStep.addActionListener(this);
        jbProductionOrderPreviousStep.setToolTipText("Regresar ord. prod. al estado anterior");
           /*+ ((mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW || mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT) ? "Nueva" :
                 mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG ? "En pesado" :
                 mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC ? "En piso" :
                 mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_END ? "En proceso" :
                 mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_CLS ? "Terminada" : "?") + "'"); */

        jbProductionOrderNextStep = new JButton(miClient.getImageIcon(SLibConstants.ICON_ARROW_RIGHT));
        jbProductionOrderNextStep.setPreferredSize(new Dimension(23, 23));
        jbProductionOrderNextStep.addActionListener(this);
        jbProductionOrderNextStep.setToolTipText("Pasar ord. prod. al siguiente estado");
                /*+ "/* '" + (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW ? "En pesado" :
                     mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT ? "En piso" :
                 mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG ? "En proceso" :
                 (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC || mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_DLY) ? "Terminada" :
                 (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_END || mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_CLS) ? "Cerrada" : "?") + "'"); */
        
        jbProgramOrder = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbProgramOrder.setPreferredSize(new Dimension(23, 23));
        jbProgramOrder.setToolTipText("Programar orden de producción");
        jbProgramOrder.addActionListener(this);
        
        jbAssignQuantityRework = new JButton(miClient.getImageIcon(SLibConstants.ICON_ACTION));
        jbAssignQuantityRework.setPreferredSize(new Dimension(23, 23));
        jbAssignQuantityRework.setToolTipText("Asignar reproceso");
        jbAssignQuantityRework.addActionListener(this);

        jbAssignLotFinishedGood = new JButton(miClient.getImageIcon(SLibConstants.ICON_INSERT));
        jbAssignLotFinishedGood.setPreferredSize(new Dimension(23, 23));
        jbAssignLotFinishedGood.setToolTipText("Asignar lote a PT");
        jbAssignLotFinishedGood.addActionListener(this);

        jbAssignDate = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_DELIVERY));
        jbAssignDate.setPreferredSize(new Dimension(23, 23));
        jbAssignDate.setToolTipText("Asignar fecha");
        jbAssignDate.addActionListener(this);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.setToolTipText("Imprimir orden de producción");
        jbPrint.addActionListener(this);

        jbPrintPerformance = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrintPerformance.setPreferredSize(new Dimension(23, 23));
        jbPrintPerformance.setToolTipText("Imprimir rendimiento");
        jbPrintPerformance.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbProductionOrderPreviousStep);
        addTaskBarUpperComponent(jbProductionOrderNextStep);
        addTaskBarUpperComponent(jbProgramOrder);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbAssignQuantityRework);
        addTaskBarUpperComponent(jbAssignLotFinishedGood);
        addTaskBarUpperComponent(jbAssignDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbPrint);
        addTaskBarUpperComponent(jbPrintPerformance);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCardex);
        addTaskBarUpperComponent(moTabFilterProdOrderType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);

        jbNew.setEnabled(mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW && bPrivilegeOrderCreate);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(false);
        jbProductionOrderPreviousStep.setEnabled(
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT && bPrivilegeOrderStatusLot && nLevelOrderStatusLot >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG && bPrivilegeOrderStatusLotAssign && nLevelOrderStatusLotAssing >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC && bPrivilegeOrderStatusProcess && nLevelOrderStatusLotProcess >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_END && bPrivilegeOrderStatusEnd && nLevelOrderStatusLotEnd >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_CLS && bPrivilegeOrderStatusClose && nLevelOrderStatusLotClose >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_DLY && bPrivilegeOrderStatusDelay && nLevelOrderStatusDelay >= SUtilConsts.LEV_CAPTURE));  //XXX VALIDATIONS FOR STATUS WHERE CAN NOT BE BACK      
        jbProductionOrderNextStep.setEnabled(
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW && bPrivilegeOrderStatusNew && nLevelOrderStatusNew >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT && bPrivilegeOrderStatusLot && nLevelOrderStatusLot >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG && bPrivilegeOrderStatusLotAssign && nLevelOrderStatusLotAssing >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC && bPrivilegeOrderStatusProcess && nLevelOrderStatusLotProcess >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_END && bPrivilegeOrderStatusEnd && nLevelOrderStatusLotEnd >= SUtilConsts.LEV_CAPTURE) ||
                (mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_DLY && bPrivilegeOrderStatusDelay && nLevelOrderStatusDelay >= SUtilConsts.LEV_CAPTURE));  //XXX VALIDATIONS FOR STATUS WHERE CAN NOT BE MOVED FORWARD
        
        jbProgramOrder.setEnabled((mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW));
        jbAssignLotFinishedGood.setEnabled(mnTabTypeAux02 == SDataConstants.MFGX_ORD_LOT_FG);
        jbPrintPerformance.setEnabled(mnTabTypeAux01 != SDataConstants.MFGX_ORD_FOR);
        jbAssignQuantityRework.setEnabled(bPrivilegeOrderAssignRework && (
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG ||
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC));
        jbAssignDate.setEnabled(bPrivilegeOrderAssignDate && mnTabTypeAux01 < SDataConstantsSys.MFGS_ST_ORD_CLS);

        moDialogMfgAssignDate = new SDialogMfgAssignDate(miClient);
        moDialogMfgAssignRework = new SDialogMfgAssignRework(miClient);
        moDialogMfgChangeState = new SDialogMfgChangeState(miClient);
        moDialogMfgCreateLot = new SDialogMfgCreateLot(miClient);
        moDialogStockCardexProdOrder = new SDialogStockCardexProdOrder(miClient);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_ORD_FOR:
                aoTableColumns = new STableColumn[36];
                break;
            case SDataConstantsSys.MFGS_ST_ORD_NEW:
                aoTableColumns = new STableColumn[37];
                break;
            case SDataConstantsSys.MFGS_ST_ORD_LOT:
                aoTableColumns = new STableColumn[36];
                break;
            default:
                aoTableColumns = new STableColumn[34];
                break;
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Planta", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt", "F. doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp", "Tipo", 60);

        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_ORD_FOR:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "o.b_for", "Pronóstico", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_exp", "Explosión", STableConstants.WIDTH_BOOLEAN);
                break;

            case SDataConstantsSys.MFGS_ST_ORD_NEW:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_exp", "Explosión", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_prog", "Programada", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_perc", "%Segregado", STableConstants.WIDTH_PERCENTAGE);
                break;
            case SDataConstantsSys.MFGS_ST_ORD_LOT:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_exp", "Explosión", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_perc", "%Segregado", STableConstants.WIDTH_PERCENTAGE);
                break;
        }

        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "st.st", "Estado", 60);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_fid_st_ord", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", 65);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Producto", 150);

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty_ori", "Cantidad original", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty_rew", "Cantidad reproceso", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cantidad total", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.chgs", "Cargas prod.", 40);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererSimpleInteger());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.lot", "Lote", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.dt_exp_n", "F. caducidad", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Ord. prod. padre", 80);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bom", "Fórmula", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_exp_ref", "Explosión mats.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "o.num_ref_n", "Pedido cliente", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpo.bp", "Operador", 100);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pty.pty", "Prioridad", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_dly", "F. entrega prog.", STableConstants.WIDTH_DATE);
        // aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "td.turn", "T. entrega prog.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_lot_n", "F. en pesado", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_lot_asg_n", "F. en piso", STableConstants.WIDTH_DATE);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tl.turn", "T. lotes asig.", 100);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_lot_asg_n", "M. T. lotes asig.", STableConstants.WIDTH_DATE_TIME);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ul.usr", "Usr. lotes asig.", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_start_n", "F. inicio", STableConstants.WIDTH_DATE);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ts.turn", "T. inicio", 100);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_start_n", "M. T. inicio", STableConstants.WIDTH_DATE_TIME);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.usr", "Usr. inicio", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_end_n", "F. termino", STableConstants.WIDTH_DATE);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "te.turn", "T. termino", 100);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_end_n", "M. T. termino", STableConstants.WIDTH_DATE_TIME);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uen.usr", "Usr. termino", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_close_n", "F. cierre", STableConstants.WIDTH_DATE);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tc.turn", "T. cierre", 100);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_close_n", "M. T. cierre", STableConstants.WIDTH_DATE_TIME);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.usr", "Usr. cierre", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "o.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "o.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.BPSU_BP); // ***
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_NEW);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_LOT);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_PROC);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_END);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_CLS);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.MFG_BOM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    public void actionCardex() {
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moDialogStockCardexProdOrder.formReset();
                moDialogStockCardexProdOrder.setFormParams((int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogStockCardexProdOrder.setVisible(true);
            }
        }
    }

    public void actionPrint() {
        SDataProductionOrder oProductionOrder = null;
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (moTablePane.getSelectedTableRow() != null) {
            oProductionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("sReportTitle", "Reporte de orden de producción");
                map.put("nIdYear", oProductionOrder.getPkYearId());
                map.put("nIdOrd", oProductionOrder.getPkOrdId());
                map.put("sCompanyBranch", oProductionOrder.getDbmsCompanyBranch());
                map.put("sCompanyBranchCode", oProductionOrder.getDbmsCompanyBranchCode());
                map.put("sPlant", oProductionOrder.getDbmsEntity());
                map.put("sPlantCode", oProductionOrder.getDbmsEntityCode());
                map.put("sType", oProductionOrder.getDbmsProductionOrderType());
                map.put("sFinishedGood", oProductionOrder.getDbmsFinishedGood());
                map.put("sFormula", oProductionOrder.getDbmsBom());
                map.put("sReference", oProductionOrder.getReference());
                map.put("sNumberFather", oProductionOrder.getDbmsNumberFather());
                map.put("sNumber", oProductionOrder.getDbmsNumber());
                map.put("nQuantity", oProductionOrder.getQuantity());
                map.put("sUnit", oProductionOrder.getDbmsBomUnitSymbol());
                map.put("tDate", oProductionOrder.getDate());
                map.put("tDateAssignLots", oProductionOrder.getDateLot_n());
                map.put("nSqlItemId", oProductionOrder.getFkItemId_r());
                map.put("nSqlQty", oProductionOrder.getQuantity());
                map.put("nSqlSortItemKey", miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                map.put("nSqlPkCategoryBizPartnerId", SDataConstantsSys.BPSS_CT_BP_CUS);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_MFG_ORD, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Impresión orden de producción");
                jasperViewer.setVisible(true);
            }
            catch (Exception e) {
                System.out.println("Mensaje de error " + e.getMessage());
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    public void actionPrintPerformance() {
        SDataProductionOrder oProductionOrder = null;
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (moTablePane.getSelectedTableRow() != null) {
            oProductionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("bIsView", true);
                map.put("nIdYear", oProductionOrder.getPkYearId());
                map.put("nIdOrd", oProductionOrder.getPkOrdId());
                map.put("sCurCode", SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] {miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
                map.put("Ct_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0]);
                map.put("Cl_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1]);
                map.put("Tp_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2]);
                map.put("Ct_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0]);
                map.put("Cl_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1]);
                map.put("Tp_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2]);
                map.put("Ct_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0]);
                map.put("Cl_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1]);
                map.put("Tp_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2]);
                map.put("Ct_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0]);
                map.put("Cl_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1]);
                map.put("Tp_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2]);
                map.put("Ct_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0]);
                map.put("Cl_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1]);
                map.put("Tp_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2]);
                map.put("Ct_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0]);
                map.put("Cl_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1]);
                map.put("Tp_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2]);
                map.put("Ct_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0]);
                map.put("Cl_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1]);
                map.put("Tp_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2]);
                map.put("Ct_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0]);
                map.put("Cl_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1]);
                map.put("Tp_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2]);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_MFG_ORD_PERFORMANCE, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Rendimiento de orden de producción");
                jasperViewer.setVisible(true);
            }
            catch (Exception e) {
                System.out.println("Mensaje de error " + e.getMessage());
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

	}
    }

    public void actionChangeState(boolean bNextState) {
        Vector<SDataProductionOrder> vProductionOrders = new Vector<SDataProductionOrder>();

        if (moTablePane.getSelectedTableRow() != null) {

            moDialogMfgChangeState.formReset();
            moDialogMfgChangeState.formRefreshCatalogues();
            moDialogMfgChangeState.setValue(1, moTablePane.getSelectedTableRow().getPrimaryKey());
            moDialogMfgChangeState.setValue(2, mnTabTypeAux01);
            moDialogMfgChangeState.setValue(3, bNextState); // True -> Next state
            moDialogMfgChangeState.setTitle(bNextState ? "Pasar ord. prod. al siguiente estado" : "Regresar ord. prod. al estado anterior");
            moDialogMfgChangeState.setVisible(true);

            if (moDialogMfgChangeState.getFormResult() == SLibConstants.FORM_RESULT_OK) {

                    vProductionOrders = moDialogMfgChangeState.getProductionOrders();
                    /*for (int i=0; i<vProductionOrders.size(); i++) {
                        moProductionOrder = vProductionOrders.get(i);
                        if (moProductionOrder != null) {
                            try {
                                miClient.getGuiModule(SDataConstants.MOD_MFG).processRegistry(moProductionOrder);
                                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                            }
                            catch (Exception e) {
                                System.out.println("Mensaje de error " + e.getMessage());
                            }
                        }
                    }*/

                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                }
        }
    }
    
    private void actionProgramOrder() {
        if (moTablePane.getSelectedTableRow() != null) {
            if (jbProgramOrder.isEnabled()) {
                SDataProductionOrder productionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                try {
                    productionOrder.programProductionOrder(miClient, false, true);
                }
                catch (StockException ex) {
                    SLibUtils.showException(this, ex);
                }
                actionReload();
            }
        }
    }

    public void actionAssignLotFinishedGood() {
        if (jbAssignLotFinishedGood.isEnabled() && moTablePane.getSelectedTableRow() != null) {

            moDialogMfgCreateLot.formReset();
            moDialogMfgCreateLot.formRefreshCatalogues();
            moDialogMfgCreateLot.setValue(1, moTablePane.getSelectedTableRow().getPrimaryKey());
            moDialogMfgCreateLot.setVisible(true);
            if (moDialogMfgCreateLot.getFormResult() == SLibConstants.FORM_RESULT_OK) {

                moProductionOrder = (SDataProductionOrder) moDialogMfgCreateLot.getRegistry();
                if (moProductionOrder != null) {
                    try {
                        miClient.getGuiModule(SDataConstants.MOD_MFG).processRegistry(moProductionOrder);
                        miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                    }
                    catch (Exception e) {
                        System.out.println("Mensaje de error " + e.getMessage());
                    }
                }
                else {
                    miClient.showMsgBoxWarning("No se pudo realizar el cambio de estado.");
                }
            }
        }
    }

    public void actionAssignQuantityRework() {
        if (jbAssignQuantityRework.isEnabled() && moTablePane.getSelectedTableRow() != null) {

            moDialogMfgAssignRework.formReset();
            moDialogMfgAssignRework.formRefreshCatalogues();
            moDialogMfgAssignRework.setValue(1, moTablePane.getSelectedTableRow().getPrimaryKey());
            moDialogMfgAssignRework.setVisible(true);
            if (moDialogMfgAssignRework.getFormResult() == SLibConstants.FORM_RESULT_OK) {

                moProductionOrder = (SDataProductionOrder) moDialogMfgAssignRework.getRegistry();
                if (moProductionOrder != null) {
                    try {
                        miClient.getGuiModule(SDataConstants.MOD_MFG).processRegistry(moProductionOrder);
                        miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                    }
                    catch (Exception e) {
                        System.out.println("Mensaje de error " + e.getMessage());
                    }
                }
                else {
                    miClient.showMsgBoxWarning("No se pudo asignar la cantidad de reproceso.");
                }
            }
        }
    }

    public void actionAssignDate() {
        if (jbAssignDate.isEnabled() && moTablePane.getSelectedTableRow() != null) {

            moDialogMfgAssignDate.formReset();
            moDialogMfgAssignDate.formRefreshCatalogues();
            moDialogMfgAssignDate.setValue(1, moTablePane.getSelectedTableRow().getPrimaryKey());
            moDialogMfgAssignDate.setVisible(true);
            if (moDialogMfgAssignDate.getFormResult() == SLibConstants.FORM_RESULT_OK) {

                moProductionOrder = (SDataProductionOrder) moDialogMfgAssignDate.getRegistry();
                if (moProductionOrder != null) {
                    try {
                        miClient.getGuiModule(SDataConstants.MOD_MFG).processRegistry(moProductionOrder);
                        miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                    }
                    catch (Exception e) {
                        System.out.println("Mensaje de error " + e.getMessage());
                    }
                }
                else {
                    miClient.showMsgBoxWarning("No se pudo realizar el cambio de estado.");
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        int[] period = null;
        Date tSqlDateCut = miClient.getSessionXXX().getWorkingDate();
        String sqlWhere = (mnTabTypeAux01 == SDataConstants.MFGX_ORD_ALL ? "o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_NEW ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_NEW + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_LOT + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_PROC ? "(st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_PROC +
                    (mnTabTypeAux02 != SDataConstants.MFGX_ORD_LOT_FG ? "" : " OR st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG +
                    " OR st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_END) + ") AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_END ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_END + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_CLS ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_CLS + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstantsSys.MFGS_ST_ORD_DLY ? "st.id_st = " + SDataConstantsSys.MFGS_ST_ORD_PROC + " AND o.b_for = false " :
                mnTabTypeAux01 == SDataConstants.MFGX_ORD_FOR ? "o.b_for = true " : "");

        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "o.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                period = (int[]) setting.getSetting();
                switch (period.length) {
                    case 1:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " ";
                        break;
                    case 2:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " AND MONTH(o.dt) = " + period[1] + " ";
                        break;
                    case 3:
                        if (mnTabTypeAux01 != SDataConstantsSys.MFGS_ST_ORD_DLY) {
                            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " AND MONTH(o.dt) = " + period[1] + " AND DAY(o.dt) = " + period[2] + " ";
                        }
                        else {
                            tSqlDateCut = SLibTimeUtilities.createDate(period[0], period[1], period[2]);
                            sqlWhere += "AND o.dt_dly <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "' ";
                        }
                        break;
                    default:
                        break;
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_MFG_ORD_TP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlWhere += "AND o.fid_tp_ord = " + setting.getSetting() + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlWhere += "AND o.fid_bp_n = " + (Integer) setting.getSetting() + " ";
                }
            }
        }

        msSql = "SELECT o.*, CONCAT(po.id_year, '-',po.num, ' ', po.ref) AS f_ref, CONCAT(o.id_year, '-',o.num) AS f_num, " +
                "bpb.bpb, bpb.code, ent.ent, ent.code, tp.tp, b.bom, i.item_key, i.item, u.symbol, st.st, l.lot, l.dt_exp_n, un.usr, ue.usr, ud.usr, " +
                STableConstants.ICON_MFG_ST + " + o.fid_st_ord AS f_fid_st_ord, IF(COALESCE(MAX(exp.id_exp), 0) > 0, 1, 0) AS f_exp, MAX(exp.ref) AS f_exp_ref, " +
                "COALESCE((SELECT COALESCE(SUM(qty_inc) * 100.0, 0) " +
                "FROM trn_stk_seg AS seg " +
                "INNER JOIN trn_stk_seg_whs_ety AS sety ON seg.id_stk_seg = sety.id_stk_seg " +
                "WHERE seg.fid_ref_1 = o.id_year AND seg.fid_ref_2 = o.id_ord AND NOT seg.b_del) / " +
                "(SELECT COALESCE(SUM(eti.gross_req), 0) " +
                "FROM  mfg_exp_ord AS meo " +
                "INNER JOIN mfg_exp AS exp ON meo.id_exp = exp.id_exp AND meo.id_exp_year = exp.id_year " +
                "INNER JOIN mfg_exp_ety AS exy ON exp.id_exp = exy.id_exp AND exp.id_year = exy.id_year " +
                "INNER JOIN mfg_exp_ety_item AS eti ON exp.id_exp = eti.id_exp AND exp.id_year = eti.id_year AND exy.id_item = eti.id_item AND exy.id_unit = eti.id_unit " +
                "WHERE meo.id_ord = o.id_ord AND meo.id_ord_year = o.id_year AND NOT exp.b_del), 0) AS f_perc, " +
                "bp.bp, bpo.bp " +
                //", td.turn, pty.pty, tl.turn, ul.usr, ts.turn, ul.usr, te.turn, uen.usr, tc.turn, uc.usr " +
                "FROM mfg_ord AS o " +
                "INNER JOIN erp.mfgu_tp_ord AS tp ON o.fid_tp_ord = tp.id_tp " +
                "INNER JOIN mfg_bom AS b ON o.fid_bom = b.id_bom " +
                /*"INNER JOIN erp.mfgu_turn AS td ON o.fid_turn_dly = td.id_turn " +
                "INNER JOIN erp.MFGU_TURN AS tl ON o.fid_turn_lot_asg = tl.id_turn " +
                "INNER JOIN erp.USRU_USR AS ul ON o.fid_turn_lot_asg = ul.id_usr " +
                "INNER JOIN erp.MFGU_TURN AS ts ON o.fid_turn_start = ts.id_turn " +
                "INNER JOIN erp.USRU_USR AS us ON o.fid_turn_start = us.id_usr " +
                "INNER JOIN erp.MFGU_TURN AS te ON o.fid_turn_end = te.id_turn " +
                "INNER JOIN erp.USRU_USR AS uen ON o.fid_turn_end = uen.id_usr " +
                "INNER JOIN erp.MFGU_TURN AS tc ON o.fid_turn_close = tc.id_turn " +
                "INNER JOIN erp.USRU_USR AS uc ON o.fid_turn_close = uc.id_usr " + */ // Query very slow with this lines
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                (mnTabTypeAux02 != SDataConstants.MFGX_ORD_LOT_FG ? "" : "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen AND ig.b_lot = 1 " ) +
                "INNER JOIN erp.bpsu_bpb AS bpb ON o.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
                //"INNER JOIN erp.mfgs_pty_ord AS pty ON o.fid_pty_ord = pty.id_pty " +
                "INNER JOIN erp.mfgs_st_ord AS st ON o.fid_st_ord = st.id_st " +
                "INNER JOIN erp.usru_usr AS un ON o.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON o.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON o.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_lot AS l ON o.fid_lot_item_nr = l.id_item AND o.fid_lot_unit_nr = l.id_unit AND o.fid_lot_n = l.id_lot " +
                "LEFT OUTER JOIN mfg_ord AS po ON o.fid_ord_year_n = po.id_year AND o.fid_ord_n = po.id_ord " +
                "LEFT OUTER JOIN mfg_exp_ord AS ex ON o.id_year = ex.id_ord_year AND o.id_ord = ex.id_ord " +
                "LEFT OUTER JOIN mfg_exp AS exp ON ex.id_exp_year = exp.id_year AND ex.id_exp = exp.id_exp AND exp.b_del = 0 " +
                "LEFT OUTER JOIN erp.bpsu_bp AS bp ON o.fid_bp_n = bp.id_bp " +
                "LEFT OUTER JOIN erp.bpsu_bp AS bpo ON o.fid_bp_ope_n = bpo.id_bp " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "GROUP BY o.id_year, o.id_ord " +
                "ORDER BY o.id_year, o.id_ord, o.dt, bpb.bpb, ent.ent, tp.tp, o.ref ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbProductionOrderNextStep) {
                if (jbProductionOrderNextStep.isEnabled()) actionChangeState(true);
            }
            else if (button == jbProgramOrder) {
                actionProgramOrder();
            }
            else if (button == jbProductionOrderPreviousStep) {
                if (jbProductionOrderPreviousStep.isEnabled()) actionChangeState(false);
            }
            else if (button == jbAssignLotFinishedGood) {
                actionAssignLotFinishedGood();
            }
            else if (button == jbAssignQuantityRework) {
                actionAssignQuantityRework();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbPrintPerformance) {
                actionPrintPerformance();
            }
            else if (button == jbCardex) {
                actionCardex();
            }
            else if (button == jbAssignDate) {
                actionAssignDate();
            }
        }
    }
}
