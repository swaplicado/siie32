/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableTabComponent;
import erp.lib.table.STableTabInterface;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogAdjustmentType;
import erp.mtrn.data.SDataStockConfig;
import erp.mtrn.data.SDataStockConfigDns;
import erp.mtrn.data.SDataStockConfigItem;
import erp.mtrn.data.SDataStockLot;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.form.SDialogDiogSaved;
import erp.mtrn.form.SDialogRepStock;
import erp.mtrn.form.SDialogRepStockMoves;
import erp.mtrn.form.SDialogRepStockTrackingLot;
import erp.mtrn.form.SDialogUtilStockClosing;
import erp.mtrn.form.SFormDiog;
import erp.mtrn.form.SFormDiogAdjustmentType;
import erp.mtrn.form.SFormStockConfig;
import erp.mtrn.form.SFormStockConfigDns;
import erp.mtrn.form.SFormStockConfigItem;
import erp.mtrn.form.SFormStockLot;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleTrnInv extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmMenuCat;
    private javax.swing.JMenuItem jmiCatStockLot;
    private javax.swing.JMenuItem jmiCatStockConfig;
    private javax.swing.JMenuItem jmiCatStockAdjustmentType;
    private javax.swing.JMenuItem jmiCatStockConfigItem;
    private javax.swing.JMenuItem jmiCatStockConfigDns;

    private javax.swing.JMenu jmMenuDpsPurSup;
    private javax.swing.JMenuItem jmiDpsPurSupplyPend;
    private javax.swing.JMenuItem jmiDpsPurSupplyPendEty;
    private javax.swing.JMenuItem jmiDpsPurSupplied;
    private javax.swing.JMenuItem jmiDpsPurSuppliedEty;
    private javax.swing.JMenuItem jmiDpsPurOrderSupplies;

    private javax.swing.JMenu jmMenuDpsPurRet;
    private javax.swing.JMenuItem jmiDpsPurReturnPend;
    private javax.swing.JMenuItem jmiDpsPurReturnPendEty;
    private javax.swing.JMenuItem jmiDpsPurReturned;
    private javax.swing.JMenuItem jmiDpsPurReturnedEty;

    private javax.swing.JMenu jmMenuDpsSalSup;
    private javax.swing.JMenuItem jmiDpsSalSupplyPend;
    private javax.swing.JMenuItem jmiDpsSalSupplyPendEntry;
    private javax.swing.JMenuItem jmiDpsSalSupplied;
    private javax.swing.JMenuItem jmiDpsSalSuppliedEntry;
    private javax.swing.JMenuItem jmiDpsSalOrderSupplies;

    private javax.swing.JMenu jmMenuDpsSalRet;
    private javax.swing.JMenuItem jmiDpsSalReturnPend;
    private javax.swing.JMenuItem jmiDpsSalReturnPendEntry;
    private javax.swing.JMenuItem jmiDpsSalReturned;
    private javax.swing.JMenuItem jmiDpsSalReturnedEntry;

    private javax.swing.JMenu jmMenuMfg;
    private javax.swing.JMenuItem jmiMfgPanelProdOrder;
    private javax.swing.JMenuItem jmiMfgAssignPend;
    private javax.swing.JMenuItem jmiMfgAssignPendEntry;
    private javax.swing.JMenuItem jmiMfgAssigned;
    private javax.swing.JMenuItem jmiMfgAssignedEntry;
    private javax.swing.JMenuItem jmiMfgFinishPend;
    private javax.swing.JMenuItem jmiMfgFinishPendEntry;
    private javax.swing.JMenuItem jmiMfgFinished;
    private javax.swing.JMenuItem jmiMfgFinishedEntry;
    private javax.swing.JMenuItem jmiMfgConsumePend;
    private javax.swing.JMenuItem jmiMfgConsumePendEntry;
    private javax.swing.JMenuItem jmiMfgConsumed;
    private javax.swing.JMenuItem jmiMfgConsumedEntry;

    private javax.swing.JMenu jmMenuIog;
    private javax.swing.JMenuItem jmiIogStock;
    private javax.swing.JMenuItem jmiIogMfgRmAssign;
    private javax.swing.JMenuItem jmiIogMfgRmReturn;
    private javax.swing.JMenuItem jmiIogMfgWpAssign;
    private javax.swing.JMenuItem jmiIogMfgWpReturn;
    private javax.swing.JMenuItem jmiIogMfgFgAssign;
    private javax.swing.JMenuItem jmiIogMfgFgReturn;
    private javax.swing.JMenuItem jmiIogMfgConsumeIn;
    private javax.swing.JMenuItem jmiIogMfgConsumeOut;
    private javax.swing.JMenuItem jmiIogAuditPending;
    private javax.swing.JMenuItem jmiIogAudited;
    private javax.swing.JMenuItem jmiIogInventoryValuation;
    private javax.swing.JMenuItem jmiIogInventoryMfgCost;
    private javax.swing.JMenuItem jmiIogStockValueByWarehouse;
    private javax.swing.JMenuItem jmiIogStockValueByItem;
    private javax.swing.JMenuItem jmiIogStockValueByDiogType;
    private javax.swing.JMenuItem jmiIogStockClosing;

    private javax.swing.JMenu jmMenuStk;
    private javax.swing.JMenuItem jmiStkStock;
    private javax.swing.JMenuItem jmiStkStockLot;
    private javax.swing.JMenuItem jmiStkStockWarehouse;
    private javax.swing.JMenuItem jmiStkStockWarehouseLot;
    private javax.swing.JMenuItem jmiStkStockMovements;
    private javax.swing.JMenuItem jmiStkStockMovementsEntry;
    private javax.swing.JMenuItem jmiStkStockRotation;
    private javax.swing.JMenuItem jmiStkStockRotationLot;

    private javax.swing.JMenu jmMenuRep;
    private javax.swing.JMenuItem jmiReportStock;
    private javax.swing.JMenuItem jmiReportStockMoves;
    private javax.swing.JMenuItem jmiReportStockTrackingLot;
    private javax.swing.JMenu jmMenuRepStats;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumePendMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumePendEntryMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumedMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumedEntryMass;

    private erp.mtrn.form.SDialogDiogSaved moDialogDiogSaved;
    private erp.mtrn.form.SDialogRepStock moDialogRepStock;
    private erp.mtrn.form.SDialogRepStockMoves moDialogRepStockMoves;
    private erp.mtrn.form.SDialogRepStockTrackingLot moDialogRepStockTrackingLot;
    private erp.mtrn.form.SFormDiog moFormDiog;
    private erp.mtrn.form.SFormStockLot moFormStockLot;
    private erp.mtrn.form.SFormStockConfig moFormStockConfig;
    private erp.mtrn.form.SFormDiogAdjustmentType moFormDiogAdjustmentType;
    private erp.mtrn.form.SFormStockConfigItem moFormStockConfigItem;
    private erp.mtrn.form.SFormStockConfigDns moFormStockConfigDns;
    private erp.mtrn.form.SDialogUtilStockClosing moDialogUtilStockClosing;

    public SGuiModuleTrnInv(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_INV);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightInPur = false;
        boolean hasRightInSal = false;
        boolean hasRightInAdj = false;
        boolean hasRightInMfg = false;
        boolean hasRightInOtherInt = false;
        boolean hasRightInOtherExt = false;
        boolean hasRightOutPur = false;
        boolean hasRightOutSal = false;
        boolean hasRightOutAdj = false;
        boolean hasRightOutMfg = false;
        boolean hasRightOutOtherInt = false;
        boolean hasRightOutOtherExt = false;
        boolean hasRightStock = false;
        boolean hasRightAudit = false;
        boolean hasRightReports = false;
        boolean hasRightMfgRmAsg = false;
        boolean hasRightMfgRmDev = false;
        boolean hasRightMfgWpAsg = false;
        boolean hasRightMfgWpDev = false;
        boolean hasRightMfgFgAsg = false;
        boolean hasRightMfgFgDev = false;
        boolean hasRightMfgCon = false;

        jmMenuCat = new JMenu("Catálogos");
        jmiCatStockLot = new JMenuItem("Lotes");
        jmiCatStockConfig = new JMenuItem("Máximos y mínimos");
        jmiCatStockAdjustmentType = new JMenuItem("Tipos de ajuste de inventario");
        jmiCatStockConfigItem = new JMenuItem("Configuración de ítems por almacén");
        jmiCatStockConfigDns = new JMenuItem("Configuración de series docs. de ventas por almacén");
        jmMenuCat.add(jmiCatStockLot);
        jmMenuCat.add(jmiCatStockConfig);
        jmMenuCat.addSeparator();
        jmMenuCat.add(jmiCatStockAdjustmentType);
        jmMenuCat.add(jmiCatStockConfigItem);
        jmMenuCat.add(jmiCatStockConfigDns);
        jmiCatStockLot.addActionListener(this);
        jmiCatStockConfig.addActionListener(this);
        jmiCatStockAdjustmentType.addActionListener(this);
        jmiCatStockConfigItem.addActionListener(this);
        jmiCatStockConfigDns.addActionListener(this);

        jmMenuDpsPurSup = new JMenu("Surtidos compras");
        jmiDpsPurSupplyPend = new JMenuItem("Compras por surtir");
        jmiDpsPurSupplyPendEty = new JMenuItem("Compras por surtir a detalle");
        jmiDpsPurSupplied = new JMenuItem("Compras surtidas");
        jmiDpsPurSuppliedEty = new JMenuItem("Compras surtidas a detalle");
        //jmiDpsPurOrderSupplies = new JMenuItem("Pedidos de compras con surtidos");
        jmMenuDpsPurSup.add(jmiDpsPurSupplyPend);
        jmMenuDpsPurSup.add(jmiDpsPurSupplyPendEty);
        jmMenuDpsPurSup.addSeparator();
        jmMenuDpsPurSup.add(jmiDpsPurSupplied);
        jmMenuDpsPurSup.add(jmiDpsPurSuppliedEty);
        jmMenuDpsPurSup.addSeparator();
        //jmMenuDpsPurSup.add(jmiDpsPurOrderSupplies);
        jmiDpsPurSupplyPend.addActionListener(this);
        jmiDpsPurSupplyPendEty.addActionListener(this);
        jmiDpsPurSupplied.addActionListener(this);
        jmiDpsPurSuppliedEty.addActionListener(this);
        //jmiDpsPurOrderSupplies.addActionListener(this);

        jmMenuDpsPurRet = new JMenu("Devoluciones compras");
        jmiDpsPurReturnPend = new JMenuItem("Compras por devolver");
        jmiDpsPurReturnPendEty = new JMenuItem("Compras por devolver a detalle");
        jmiDpsPurReturned = new JMenuItem("Compras devueltas");
        jmiDpsPurReturnedEty = new JMenuItem("Compras devueltas a detalle");
        jmMenuDpsPurRet.add(jmiDpsPurReturnPend);
        jmMenuDpsPurRet.add(jmiDpsPurReturnPendEty);
        jmMenuDpsPurRet.addSeparator();
        jmMenuDpsPurRet.add(jmiDpsPurReturned);
        jmMenuDpsPurRet.add(jmiDpsPurReturnedEty);
        jmiDpsPurReturnPend.addActionListener(this);
        jmiDpsPurReturnPendEty.addActionListener(this);
        jmiDpsPurReturned.addActionListener(this);
        jmiDpsPurReturnedEty.addActionListener(this);

        jmMenuDpsSalSup = new JMenu("Surtidos ventas");
        jmiDpsSalSupplyPend = new JMenuItem("Ventas por surtir");
        jmiDpsSalSupplyPendEntry = new JMenuItem("Ventas por surtir a detalle");
        jmiDpsSalSupplied = new JMenuItem("Ventas surtidas");
        jmiDpsSalSuppliedEntry = new JMenuItem("Ventas surtidas a detalle");
        //jmiDpsSalOrderSupplies = new JMenuItem("Pedidos de ventas con surtidos");
        jmMenuDpsSalSup.add(jmiDpsSalSupplyPend);
        jmMenuDpsSalSup.add(jmiDpsSalSupplyPendEntry);
        jmMenuDpsSalSup.addSeparator();
        jmMenuDpsSalSup.add(jmiDpsSalSupplied);
        jmMenuDpsSalSup.add(jmiDpsSalSuppliedEntry);
        jmMenuDpsSalSup.addSeparator();
        //jmMenuDpsSalSup.add(jmiDpsSalOrderSupplies);
        jmiDpsSalSupplyPend.addActionListener(this);
        jmiDpsSalSupplyPendEntry.addActionListener(this);
        jmiDpsSalSupplied.addActionListener(this);
        jmiDpsSalSuppliedEntry.addActionListener(this);
        //jmiDpsSalOrderSupplies.addActionListener(this);

        jmMenuDpsSalRet = new JMenu("Devoluciones ventas");
        jmiDpsSalReturnPend = new JMenuItem("Ventas por devolver");
        jmiDpsSalReturnPendEntry = new JMenuItem("Ventas por devolver a detalle");
        jmiDpsSalReturned = new JMenuItem("Ventas devueltas");
        jmiDpsSalReturnedEntry = new JMenuItem("Ventas devueltas a detalle");
        jmMenuDpsSalRet.add(jmiDpsSalReturnPend);
        jmMenuDpsSalRet.add(jmiDpsSalReturnPendEntry);
        jmMenuDpsSalRet.addSeparator();
        jmMenuDpsSalRet.add(jmiDpsSalReturned);
        jmMenuDpsSalRet.add(jmiDpsSalReturnedEntry);
        jmiDpsSalReturnPend.addActionListener(this);
        jmiDpsSalReturnPendEntry.addActionListener(this);
        jmiDpsSalReturned.addActionListener(this);
        jmiDpsSalReturnedEntry.addActionListener(this);

        jmMenuMfg = new JMenu("Movs. producción");
        jmiMfgPanelProdOrder = new JMenuItem("Panel movs. producción");
        jmiMfgAssignPend = new JMenuItem("Órdenes prod. por asignar");
        jmiMfgAssignPendEntry = new JMenuItem("Órdenes prod. por asignar a detalle");
        jmiMfgAssigned = new JMenuItem("Órdenes prod. asignadas");
        jmiMfgAssignedEntry = new JMenuItem("Órdenes prod. asignadas a detalle");
        jmiMfgFinishPend = new JMenuItem("Órdenes prod. por terminar");
        jmiMfgFinishPendEntry = new JMenuItem("Órdenes prod. por terminar a detalle");
        jmiMfgFinished = new JMenuItem("Órdenes prod. terminadas");
        jmiMfgFinishedEntry = new JMenuItem("Órdenes prod. terminadas a detalle");
        jmiMfgConsumePend = new JMenuItem("Insumos y productos por consumir");
        jmiMfgConsumePendEntry = new JMenuItem("Insumos y productos por consumir a detalle");
        jmiMfgConsumed = new JMenuItem("Insumos y productos consumidos");
        jmiMfgConsumedEntry = new JMenuItem("Insumos y productos consumidos a detalle");
        jmMenuMfg.add(jmiMfgPanelProdOrder);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgAssignPend);
        jmMenuMfg.add(jmiMfgAssignPendEntry);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgAssigned);
        jmMenuMfg.add(jmiMfgAssignedEntry);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgFinishPend);
        jmMenuMfg.add(jmiMfgFinishPendEntry);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgFinished);
        jmMenuMfg.add(jmiMfgFinishedEntry);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgConsumePend);
        jmMenuMfg.add(jmiMfgConsumePendEntry);
        jmMenuMfg.addSeparator();
        jmMenuMfg.add(jmiMfgConsumed);
        jmMenuMfg.add(jmiMfgConsumedEntry);
        jmiMfgPanelProdOrder.addActionListener(this);
        jmiMfgAssignPend.addActionListener(this);
        jmiMfgAssignPendEntry.addActionListener(this);
        jmiMfgAssigned.addActionListener(this);
        jmiMfgAssignedEntry.addActionListener(this);
        jmiMfgFinishPend.addActionListener(this);
        jmiMfgFinishPendEntry.addActionListener(this);
        jmiMfgFinished.addActionListener(this);
        jmiMfgFinishedEntry.addActionListener(this);
        jmiMfgConsumePend.addActionListener(this);
        jmiMfgConsumePendEntry.addActionListener(this);
        jmiMfgConsumed.addActionListener(this);
        jmiMfgConsumedEntry.addActionListener(this);

        jmMenuIog = new JMenu("Docs. inventarios");
        jmiIogStock = new JMenuItem("Docs. inventarios");
        jmiIogMfgRmAssign = new JMenuItem("Docs. entrega de materia prima (MP)");
        jmiIogMfgRmReturn = new JMenuItem("Docs. devolución de materia prima (MP)");
        jmiIogMfgWpAssign = new JMenuItem("Docs. entrega de producto en proceso (PP)");
        jmiIogMfgWpReturn = new JMenuItem("Docs. devolución de producto en proceso (PP)");
        jmiIogMfgFgAssign = new JMenuItem("Docs. entrega de producto terminado (PT)");
        jmiIogMfgFgReturn = new JMenuItem("Docs. devolución de producto terminado (PT)");
        jmiIogMfgConsumeIn = new JMenuItem("Docs. entrada por consumo de insumos y productos");
        jmiIogMfgConsumeOut = new JMenuItem("Docs. salida por consumo de insumos y productos");
        jmiIogAuditPending = new JMenuItem("Docs. inventarios por auditar");
        jmiIogAudited = new JMenuItem("Docs. inventarios auditados");
        jmiIogInventoryValuation = new JMenuItem("Valuación de inventarios");
        jmiIogInventoryMfgCost = new JMenuItem("Costos de producción por producto");
        jmiIogStockValueByWarehouse = new JMenuItem("Valor de inventarios por almacén");
        jmiIogStockValueByItem = new JMenuItem("Valor de inventarios por ítem");
        jmiIogStockValueByDiogType = new JMenuItem("Valor de inventarios por tipo movimiento");
        jmiIogStockClosing = new JMenuItem("Generación de inventarios iniciales...");
        jmMenuIog.add(jmiIogStock);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogMfgRmAssign);
        jmMenuIog.add(jmiIogMfgRmReturn);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogMfgWpAssign);
        jmMenuIog.add(jmiIogMfgWpReturn);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogMfgFgAssign);
        jmMenuIog.add(jmiIogMfgFgReturn);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogMfgConsumeIn);
        jmMenuIog.add(jmiIogMfgConsumeOut);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogAuditPending);
        jmMenuIog.add(jmiIogAudited);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogInventoryValuation);
        jmMenuIog.add(jmiIogInventoryMfgCost);
        jmMenuIog.add(jmiIogStockValueByWarehouse);
        jmMenuIog.add(jmiIogStockValueByItem);
        //jmMenuIog.add(jmiIogStockValueByDiogType); sflores, 2016-03-09, evaluating to remove it
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogStockClosing);
        
        jmiIogStock.addActionListener(this);
        jmiIogMfgRmAssign.addActionListener(this);
        jmiIogMfgRmReturn.addActionListener(this);
        jmiIogMfgWpAssign.addActionListener(this);
        jmiIogMfgWpReturn.addActionListener(this);
        jmiIogMfgFgAssign.addActionListener(this);
        jmiIogMfgFgReturn.addActionListener(this);
        jmiIogMfgConsumeIn.addActionListener(this);
        jmiIogMfgConsumeOut.addActionListener(this);
        jmiIogAuditPending.addActionListener(this);
        jmiIogAudited.addActionListener(this);
        jmiIogInventoryValuation.addActionListener(this);
        jmiIogInventoryMfgCost.addActionListener(this);
        jmiIogStockValueByWarehouse.addActionListener(this);
        jmiIogStockValueByItem.addActionListener(this);
        jmiIogStockValueByDiogType.addActionListener(this);
        jmiIogStockClosing.addActionListener(this);

        jmMenuStk = new JMenu("Inventarios");
        jmiStkStock = new JMenuItem("Existencias");
        jmiStkStockLot = new JMenuItem("Existencias por lote");
        jmiStkStockWarehouse = new JMenuItem("Existencias por almacén");
        jmiStkStockWarehouseLot = new JMenuItem("Existencias por almacén por lote");
        jmiStkStockMovements = new JMenuItem("Movimientos de inventarios");
        jmiStkStockMovementsEntry = new JMenuItem("Movimientos de inventarios a detalle");
        jmiStkStockRotation = new JMenuItem("Rotación");
        jmiStkStockRotationLot = new JMenuItem("Rotación por lote");
        jmMenuStk.add(jmiStkStock);
        jmMenuStk.add(jmiStkStockLot);
        jmMenuStk.addSeparator();
        jmMenuStk.add(jmiStkStockWarehouse);
        jmMenuStk.add(jmiStkStockWarehouseLot);
        jmMenuStk.addSeparator();
        jmMenuStk.add(jmiStkStockMovements);
        jmMenuStk.add(jmiStkStockMovementsEntry);
        jmMenuStk.addSeparator();
        jmMenuStk.add(jmiStkStockRotation);
        jmMenuStk.add(jmiStkStockRotationLot);
        jmiStkStock.addActionListener(this);
        jmiStkStockLot.addActionListener(this);
        jmiStkStockWarehouse.addActionListener(this);
        jmiStkStockWarehouseLot.addActionListener(this);
        jmiStkStockMovements.addActionListener(this);
        jmiStkStockMovementsEntry.addActionListener(this);
        jmiStkStockRotation.addActionListener(this);
        jmiStkStockRotationLot.addActionListener(this);

        jmMenuRep = new JMenu("Reportes");
        jmiReportStock = new JMenuItem("Reporte de existencias...");
        jmiReportStockMoves = new JMenuItem("Reporte de movimientos de inventarios...");
        jmiReportStockTrackingLot = new JMenuItem("Reporte de rastreo de lotes...");
        jmMenuRep.add(jmiReportStock);
        jmMenuRep.add(jmiReportStockMoves);
        jmMenuRep.add(jmiReportStockTrackingLot);
        jmiReportStock.addActionListener(this);
        jmiReportStockMoves.addActionListener(this);
        jmiReportStockTrackingLot.addActionListener(this);

        jmMenuRepStats = new JMenu("Estadísticas de producción");
        jmiRepStatsMfgConsumePendMass = new JMenuItem("Masa de insumos y productos por consumir");
        jmiRepStatsMfgConsumePendEntryMass = new JMenuItem("Masa de insumos y productos por consumir a detalle");
        jmiRepStatsMfgConsumedMass = new JMenuItem("Masa de insumos y productos consumidos");
        jmiRepStatsMfgConsumedEntryMass = new JMenuItem("Masa de insumos y productos consumidos a detalle");
        jmMenuRepStats.add(jmiRepStatsMfgConsumePendMass);
        jmMenuRepStats.add(jmiRepStatsMfgConsumePendEntryMass);
        jmMenuRepStats.addSeparator();
        jmMenuRepStats.add(jmiRepStatsMfgConsumedMass);
        jmMenuRepStats.add(jmiRepStatsMfgConsumedEntryMass);
        jmiRepStatsMfgConsumePendMass.addActionListener(this);
        jmiRepStatsMfgConsumePendEntryMass.addActionListener(this);
        jmiRepStatsMfgConsumedMass.addActionListener(this);
        jmiRepStatsMfgConsumedEntryMass.addActionListener(this);

        jmMenuRep.addSeparator();
        jmMenuRep.add(jmMenuRepStats);

        hasRightInPur = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_PUR).HasRight;
        hasRightInSal = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_SAL).HasRight;
        hasRightInAdj = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_ADJ).HasRight;
        hasRightInMfg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_MFG).HasRight;
        hasRightInOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_INT).HasRight;
        hasRightInOtherExt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_EXT).HasRight;
        hasRightOutPur = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_PUR).HasRight;
        hasRightOutSal = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_SAL).HasRight;
        hasRightOutAdj = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_ADJ).HasRight;
        hasRightOutMfg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_MFG).HasRight;
        hasRightOutOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_INT).HasRight;
        hasRightOutOtherExt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_EXT).HasRight;
        hasRightStock = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_STOCK).HasRight;
        hasRightAudit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_AUDIT).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_REP).HasRight;
        hasRightMfgRmAsg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_ASG).HasRight;
        hasRightMfgRmDev = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_DEV).HasRight;
        hasRightMfgWpAsg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_ASG).HasRight;
        hasRightMfgWpDev = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_DEV).HasRight;
        hasRightMfgFgAsg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_ASG).HasRight;
        hasRightMfgFgDev = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_DEV).HasRight;
        hasRightMfgCon = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_CON).HasRight;

        jmMenuCat.setEnabled(hasRightInAdj || hasRightOutAdj || hasRightOutOtherInt);
        jmMenuDpsPurSup.setEnabled(hasRightInPur);
        jmMenuDpsPurRet.setEnabled(hasRightOutPur);
        jmMenuDpsSalSup.setEnabled(hasRightOutSal);
        jmMenuDpsSalRet.setEnabled(hasRightInSal);
        jmMenuIog.setEnabled(hasRightInAdj || hasRightOutAdj || hasRightOutOtherInt || hasRightMfgRmAsg || hasRightMfgRmDev || hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiIogMfgRmAssign.setEnabled(hasRightMfgRmAsg);
        jmiIogMfgRmReturn.setEnabled(hasRightMfgRmDev);
        jmiIogMfgWpAssign.setEnabled(hasRightMfgWpAsg);
        jmiIogMfgWpReturn.setEnabled(hasRightMfgWpDev);
        jmiIogMfgFgAssign.setEnabled(hasRightMfgFgAsg);
        jmiIogMfgFgReturn.setEnabled(hasRightMfgFgDev);
        jmiIogMfgConsumeIn.setEnabled(hasRightMfgCon);
        jmiIogMfgConsumeOut.setEnabled(hasRightMfgCon);
        jmiIogAuditPending.setEnabled(hasRightAudit);
        jmiIogAudited.setEnabled(hasRightAudit);
        jmiIogInventoryValuation.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogInventoryMfgCost.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockValueByWarehouse.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockValueByItem.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockValueByDiogType.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockClosing.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmMenuMfg.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev || hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiMfgPanelProdOrder.setEnabled(true);
        jmiMfgAssignPend.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgAssignPendEntry.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgAssigned.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgAssignedEntry.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgFinishPend.setEnabled(hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiMfgFinishPendEntry.setEnabled(hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiMfgFinishPendEntry.setEnabled(false);    // not implemented yet
        jmiMfgFinished.setEnabled(hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiMfgFinishedEntry.setEnabled(hasRightMfgWpAsg || hasRightMfgWpDev || hasRightMfgFgAsg || hasRightMfgFgDev);
        jmiMfgFinishedEntry.setEnabled(false);      // not implemented yet
        jmiMfgConsumePend.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgConsumePendEntry.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgConsumed.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmiMfgConsumedEntry.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);
        jmMenuStk.setEnabled(hasRightStock);
        jmMenuRep.setEnabled(hasRightReports);
        jmMenuRepStats.setEnabled(hasRightMfgRmAsg || hasRightMfgRmDev);

        moDialogDiogSaved = new SDialogDiogSaved(miClient);
        moDialogUtilStockClosing = new SDialogUtilStockClosing(miClient);
    }

    private void menuRepStock() {
        if (moDialogRepStock == null) {
            moDialogRepStock = new SDialogRepStock(miClient);
        }

        moDialogRepStock.formRefreshCatalogues();
        moDialogRepStock.formReset();
        moDialogRepStock.setFormVisible(true);
    }

    private void menuRepStockMoves() {
        if (moDialogRepStockMoves == null) {
            moDialogRepStockMoves = new SDialogRepStockMoves(miClient);
        }

        moDialogRepStockMoves.formRefreshCatalogues();
        moDialogRepStockMoves.formReset();
        moDialogRepStockMoves.setFormVisible(true);
    }

    private void menuRepStockTrackingLot() {
        if (moDialogRepStockTrackingLot == null) {
            moDialogRepStockTrackingLot = new SDialogRepStockTrackingLot(miClient);
        }

        moDialogRepStockTrackingLot.formRefreshCatalogues();
        moDialogRepStockTrackingLot.formReset();
        moDialogRepStockTrackingLot.setFormVisible(true);
    }

    private void showPanelProdOrder(int panelType) {
        int index = 0;
        int count = 0;
        boolean exists = false;
        String title = "";
        STableTabInterface tableTab = null;

        count = miClient.getTabbedPane().getTabCount();
        for (index = 0; index < count; index++) {
            if (miClient.getTabbedPane().getComponentAt(index) instanceof STableTabInterface) {
                tableTab = (STableTabInterface) miClient.getTabbedPane().getComponentAt(index);
                if (tableTab.getTabType() == panelType && tableTab.getTabTypeAux01() == SLibConstants.UNDEFINED && tableTab.getTabTypeAux02() == SLibConstants.UNDEFINED) {
                    exists = true;
                    break;
                }
            }
        }

        if (exists) {
            miClient.getTabbedPane().setSelectedIndex(index);
        }
        else {
            switch (panelType) {
                case SDataConstants.TRNX_MFG_ORD:
                    title = "Panel - Movs. prod.";
                    tableTab = new SPanelInvProdOrder(miClient);
                    break;
                default:
                    tableTab = null;
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            if (tableTab != null) {
                miClient.getTabbedPane().addTab(title, (JComponent) tableTab);
                miClient.getTabbedPane().setTabComponentAt(count, new STableTabComponent(miClient.getTabbedPane(), miClient.getImageIcon(mnModuleType)));
                miClient.getTabbedPane().setSelectedIndex(count);
            }
        }
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;
        SDataDiog iog = null;
        STrnDiogComplement iogComplement = null;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.TRN_DIOG:
                    if (moFormDiog == null) {
                        moFormDiog = new SFormDiog(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDiog();
                    }
                    if (moFormComplement != null) {
                        iogComplement = (STrnDiogComplement) moFormComplement;
                        moFormDiog.formReset();
                        moFormDiog.setParamIogTypeKey(iogComplement.getDiogTypeKey());
                        moFormDiog.setParamDpsSource(iogComplement.getDpsSource());
                        moFormDiog.setParamProdOrderSource(iogComplement.getProdOrderKey());
                    }
                    miForm = moFormDiog;
                    break;
                case SDataConstants.TRN_LOT:
                    if (moFormStockLot == null) {
                        moFormStockLot = new SFormStockLot(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataStockLot();
                    }
                    miForm = moFormStockLot;
                    break;
                case SDataConstants.TRN_STK_CFG:
                    if (moFormStockConfig == null) {
                        moFormStockConfig = new SFormStockConfig(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataStockConfig();
                    }
                    miForm = moFormStockConfig;
                    break;
                case SDataConstants.TRNU_TP_IOG_ADJ:
                    if (moFormDiogAdjustmentType == null) {
                        moFormDiogAdjustmentType = new SFormDiogAdjustmentType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDiogAdjustmentType();
                    }
                    miForm = moFormDiogAdjustmentType;
                    break;
                case SDataConstants.TRN_STK_CFG_ITEM:
                    if (moFormStockConfigItem == null) {
                        moFormStockConfigItem = new SFormStockConfigItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataStockConfigItem();
                    }
                    miForm = moFormStockConfigItem;
                    break;
                case SDataConstants.TRN_STK_CFG_DNS:
                    if (moFormStockConfigDns == null) {
                        moFormStockConfigDns = new SFormStockConfigDns(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataStockConfigDns();
                    }
                    miForm = moFormStockConfigDns;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            result = processForm(pk, isCopy);

            if (result == SLibConstants.DB_ACTION_SAVE_OK) {
                switch (formType) {
                    case SDataConstants.TRN_DIOG:
                        iog = (SDataDiog) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DIOG, getLastSavedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (iog.getUserNewTs().getTime() == iog.getUserEditTs().getTime()) {
                            moDialogDiogSaved.formReset();
                            moDialogDiogSaved.setFormParams(iog);
                            moDialogDiogSaved.setVisible(true);
                        }
                        break;
                    default:
                }
            }

            clearFormComplement();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        String sViewTitle = "";
        Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.TRN_LOT:
                    oViewClass = erp.mtrn.view.SViewStockLot.class;
                    sViewTitle = "Lotes";
                    break;
                case SDataConstants.TRN_STK_CFG:
                    oViewClass = erp.mtrn.view.SViewStockConfig.class;
                    sViewTitle = "Máximos y mínimos";
                    break;
                case SDataConstants.TRNU_TP_IOG_ADJ:
                    oViewClass = erp.mtrn.view.SViewDiogAdjustmentType.class;
                    sViewTitle = "Tipos ajuste inventario";
                    break;
                case SDataConstants.TRN_STK_CFG_ITEM:
                    oViewClass = erp.mtrn.view.SViewStockConfigItem.class;
                    sViewTitle = "Config. ítems x almacén ";
                    break;
                case SDataConstants.TRN_STK_CFG_DNS:
                    oViewClass = erp.mtrn.view.SViewStockConfigDns.class;
                    sViewTitle = "Config. series docs. ventas x almacén ";
                    break;
                case SDataConstants.TRN_STK:
                    oViewClass = erp.mtrn.view.SViewStock.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_STK_STK:
                            sViewTitle = "Existencias";
                            break;
                        case SDataConstants.TRNX_STK_STK_WH:
                            sViewTitle = "Existencias x almacén";
                            break;
                        case SDataConstants.TRNX_STK_LOT:
                            sViewTitle = "Existencias x lote";
                            break;
                        case SDataConstants.TRNX_STK_LOT_WH:
                            sViewTitle = "Existencias x almacén x lote";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_STK_MOVES:
                    oViewClass = erp.mtrn.view.SViewStockMoves.class;
                    sViewTitle = "Movimientos inventarios";
                    break;
                case SDataConstants.TRNX_STK_MOVES_ETY:
                    oViewClass = erp.mtrn.view.SViewStockMovesEntry.class;
                    sViewTitle = "Movimientos inventarios (detalle)";
                    break;
                case SDataConstants.TRNX_STK_ROTATION:
                    oViewClass = erp.mtrn.view.SViewStockRotation.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_STK_STK:
                            sViewTitle = "Rotación";
                            break;
                        case SDataConstants.TRNX_STK_LOT:
                            sViewTitle = "Rotación x lote";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_STK_COMSUME:
                    oViewClass = erp.mtrn.view.SViewDpsStockConsume.class;
                    switch (auxType01) {
                        case SDataConstantsSys.TRNS_CT_DPS_PUR:
                            sViewTitle = "Consumo compras";
                            break;
                        case SDataConstantsSys.TRNS_CT_DPS_SAL:
                            sViewTitle = "Consumo ventas";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_DPS_SUPPLY_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras x surtir" : "Ventas x surtir";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras x surtir (detalle)" : "Ventas x surtir (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED:
                    oViewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras surtidas" : "Ventas surtidas";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras surtidas (detalle)" : "Ventas surtidas (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_RETURN_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras x devolver" : "Ventas x devolver";
                    break;
                case SDataConstants.TRNX_DPS_RETURN_PEND_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras x devolver (detalle)" : "Ventas x devolver (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_RETURNED:
                    oViewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras devueltas" : "Ventas devueltas";
                    break;
                case SDataConstants.TRNX_DPS_RETURNED_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    sViewTitle = auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "Compras devueltas (detalle)" : "Ventas devueltas (detalle)";
                    break;
                case SDataConstants.TRN_DIOG:
                    oViewClass = erp.mtrn.view.SViewDiog.class;
                    sViewTitle = "Docs. inventarios";
                    if (auxType01 != SLibConstants.UNDEFINED && auxType02 != SLibConstants.UNDEFINED) {
                        sViewTitle += " (" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_IOG, new int[] { auxType01, auxType02, 1 }, SLibConstants.DESCRIPTION_CODE) + ")";
                    }
                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_AUDIT_PEND:
                            oViewClass = erp.mtrn.view.SViewDiogAudit.class;
                            sViewTitle = "Docs. inventarios x auditar";
                            break;
                        case SDataConstants.TRNX_DIOG_AUDITED:
                            oViewClass = erp.mtrn.view.SViewDiogAudit.class;
                            sViewTitle = "Docs. inventarios auditad@s";
                            break;
                    }
                    break;
                case SDataConstants.TRNX_DIOG_MFG:
                    oViewClass = erp.mtrn.view.SViewDiogProdOrder.class;

                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_MFG_RM:
                        case SDataConstants.TRNX_DIOG_MFG_WP:
                        case SDataConstants.TRNX_DIOG_MFG_FG:
                            sViewTitle = "Docs. " + (auxType02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG ? "entrega" : "devolución");
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_CON:
                            sViewTitle = "Docs. " + (auxType02 == SDataConstants.TRNX_DIOG_MFG_MOVE_IN ? "entrada" : "salida");
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }

                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_MFG_RM:
                            sViewTitle += " MP";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_WP:
                            sViewTitle += " PP";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_FG:
                            sViewTitle += " PT";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_CON:
                            sViewTitle += " x consumo";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }

                    break;
                case SDataConstants.TRNX_MFG_ORD:
                    sViewTitle = "Movs. prod. - ";
                    switch (auxType01) {
                        case SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            sViewTitle += "OP x asignar";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            sViewTitle += "OP x asignar (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGNED:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            sViewTitle += "OP asignadas";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGNED_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            sViewTitle += "OP asignadas (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISH_PEND:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            sViewTitle += "OP x terminar";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISH_PEND_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            sViewTitle += "OP x terminar (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISHED:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            sViewTitle += "OP terminadas";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISHED_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            sViewTitle += "OP terminadas (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle += "MP & P x consumir";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle += "MP & P x consumir (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle += "MP & P consumidos";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle += "MP & P consumidos (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_MASS:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle = "Masa MP & P x consumir";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle = "Masa MP & P x consumir (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_MASS:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle = "Masa MP & P consumidos";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY_MASS:
                            oViewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            sViewTitle = "Masa MP & P consumidos (detalle)";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        int result = SLibConstants.UNDEFINED;
        boolean isDocBeingDeleted = false;
        String message = "";

        try {
            switch (registryType) {
                case SDataConstants.TRN_DIOG:
                    moRegistry = (SDataDiog) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DIOG, pk, SLibConstants.EXEC_MODE_VERBOSE);
                    if (moRegistry.canDelete(null) != SLibConstants.DB_CAN_DELETE_YES) {
                        throw new Exception(moRegistry.getDbmsError());
                    }
                    else {
                        isDocBeingDeleted = !moRegistry.getIsDeleted();

                        message = ((SDataDiog) moRegistry).validateStockLots(miClient, isDocBeingDeleted);
                        if (message.length() > 0) {
                            throw new Exception(message);
                        }

                        message = ((SDataDiog) moRegistry).validateStockMoves(miClient, isDocBeingDeleted);
                        if (message.length() > 0) {
                            throw new Exception(message);
                        }
                    }
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            result = processActionDelete(pk, isDocBeingDeleted);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return result;
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        return new JMenu[] { jmMenuCat, jmMenuDpsPurSup, jmMenuDpsPurRet, jmMenuDpsSalSup, jmMenuDpsSalRet, jmMenuMfg, jmMenuIog, jmMenuStk, jmMenuRep };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCatStockLot) {
                showView(SDataConstants.TRN_LOT);
            }
            else if (item == jmiCatStockConfig) {
                showView(SDataConstants.TRN_STK_CFG);
            }
            else if (item == jmiCatStockAdjustmentType) {
                showView(SDataConstants.TRNU_TP_IOG_ADJ);
            }
            else if (item == jmiCatStockConfigItem) {
                showView(SDataConstants.TRN_STK_CFG_ITEM);
            }
            else if (item == jmiCatStockConfigDns) {
                showView(SDataConstants.TRN_STK_CFG_DNS);
            }
            else if (item == jmiDpsPurSupplyPend) {
                showView(SDataConstants.TRNX_DPS_SUPPLY_PEND, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiDpsPurSupplyPendEty) {
                showView(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiDpsPurSupplied) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiDpsPurSuppliedEty) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiDpsPurReturnPend) {
                showView(SDataConstants.TRNX_DPS_RETURN_PEND, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiDpsPurReturnPendEty) {
                showView(SDataConstants.TRNX_DPS_RETURN_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiDpsPurOrderSupplies) {
                miClient.getSession().showView(SModConsts.TRNX_DPS_ORDER_SUP, SDataConstantsSys.TRNS_CT_DPS_PUR, new SGuiParams(SDataConstantsSys.UNDEFINED));
            }
            else if (item == jmiDpsPurReturned) {
                showView(SDataConstants.TRNX_DPS_RETURNED, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiDpsPurReturnedEty) {
                showView(SDataConstants.TRNX_DPS_RETURNED_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiDpsSalSupplyPend) {
                showView(SDataConstants.TRNX_DPS_SUPPLY_PEND, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiDpsSalSupplyPendEntry) {
                showView(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiDpsSalSupplied) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiDpsSalSuppliedEntry) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiDpsSalOrderSupplies) {
                miClient.getSession().showView(SModConsts.TRNX_DPS_ORDER_SUP, SDataConstantsSys.TRNS_CT_DPS_SAL, new SGuiParams(SDataConstantsSys.UNDEFINED));
            }
            else if (item == jmiDpsSalReturnPend) {
                showView(SDataConstants.TRNX_DPS_RETURN_PEND, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiDpsSalReturnPendEntry) {
                showView(SDataConstants.TRNX_DPS_RETURN_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiDpsSalReturned) {
                showView(SDataConstants.TRNX_DPS_RETURNED, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiDpsSalReturnedEntry) {
                showView(SDataConstants.TRNX_DPS_RETURNED_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiMfgPanelProdOrder) {
                showPanelProdOrder(SDataConstants.TRNX_MFG_ORD);
            }
            else if (item == jmiMfgAssignPend) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND);
            }
            else if (item == jmiMfgAssignPendEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND_ETY);
            }
            else if (item == jmiMfgAssigned) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_ASSIGNED);
            }
            else if (item == jmiMfgAssignedEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_ASSIGNED_ETY);
            }
            else if (item == jmiMfgFinishPend) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_FINISH_PEND);
            }
            else if (item == jmiMfgFinishPendEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_FINISH_PEND_ETY);
            }
            else if (item == jmiMfgFinished) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_FINISHED);
            }
            else if (item == jmiMfgFinishedEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_FINISHED_ETY);
            }
            else if (item == jmiMfgConsumePend) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUME_PEND, SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY);
            }
            else if (item == jmiMfgConsumePendEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY, SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY);
            }
            else if (item == jmiMfgConsumed) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUMED, SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY);
            }
            else if (item == jmiMfgConsumedEntry) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY, SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY);
            }
            else if (item == jmiRepStatsMfgConsumePendMass) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_MASS, SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS);
            }
            else if (item == jmiRepStatsMfgConsumePendEntryMass) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS, SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS);
            }
            else if (item == jmiRepStatsMfgConsumedMass) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUMED_MASS, SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS);
            }
            else if (item == jmiRepStatsMfgConsumedEntryMass) {
                showView(SDataConstants.TRNX_MFG_ORD, SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY_MASS, SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS);
            }
            else if (item == jmiIogStock) {
                showView(SDataConstants.TRN_DIOG);
            }
            else if (item == jmiIogMfgRmAssign) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_RM, SDataConstants.TRNX_DIOG_MFG_MOVE_ASG);
            }
            else if (item == jmiIogMfgRmReturn) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_RM, SDataConstants.TRNX_DIOG_MFG_MOVE_RET);
            }
            else if (item == jmiIogMfgWpAssign) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_WP, SDataConstants.TRNX_DIOG_MFG_MOVE_ASG);
            }
            else if (item == jmiIogMfgWpReturn) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_WP, SDataConstants.TRNX_DIOG_MFG_MOVE_RET);
            }
            else if (item == jmiIogMfgFgAssign) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_FG, SDataConstants.TRNX_DIOG_MFG_MOVE_ASG);
            }
            else if (item == jmiIogMfgFgReturn) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_FG, SDataConstants.TRNX_DIOG_MFG_MOVE_RET);
            }
            else if (item == jmiIogMfgConsumeIn) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_CON, SDataConstants.TRNX_DIOG_MFG_MOVE_IN);
            }
            else if (item == jmiIogMfgConsumeOut) {
                showView(SDataConstants.TRNX_DIOG_MFG, SDataConstants.TRNX_DIOG_MFG_CON, SDataConstants.TRNX_DIOG_MFG_MOVE_OUT);
            }
            else if (item == jmiIogAuditPending) {
                showView(SDataConstants.TRN_DIOG, SDataConstants.TRNX_DIOG_AUDIT_PEND);
            }
            else if (item == jmiIogAudited) {
                showView(SDataConstants.TRN_DIOG, SDataConstants.TRNX_DIOG_AUDITED);
            }
            else if (item == jmiIogInventoryValuation) {
                miClient.getSession().showView(SModConsts.TRN_INV_VAL, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogInventoryMfgCost) {
                miClient.getSession().showView(SModConsts.TRN_INV_MFG_CST, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogStockValueByWarehouse) {
                miClient.getSession().showView(SModConsts.TRNX_STK_COST, SModConsts.CFGU_COB_ENT, null);
            }
            else if (item == jmiIogStockValueByItem) {
                miClient.getSession().showView(SModConsts.TRNX_STK_COST, SModConsts.ITMU_ITEM, null);
            }
            else if (item == jmiIogStockValueByDiogType) {
                miClient.getSession().showView(SModConsts.TRNX_STK_DIOG_TP, SModConsts.TRNX_STK_WAH, null);
            }
            else if (item == jmiIogStockClosing) {
                moDialogUtilStockClosing.resetForm();
                moDialogUtilStockClosing.setVisible(true);
            }
            else if (item == jmiStkStock) {
                showView(SDataConstants.TRN_STK, SDataConstants.TRNX_STK_STK);
            }
            else if (item == jmiStkStockLot) {
                showView(SDataConstants.TRN_STK, SDataConstants.TRNX_STK_LOT);
            }
            else if (item == jmiStkStockWarehouse) {
                showView(SDataConstants.TRN_STK, SDataConstants.TRNX_STK_STK_WH);
            }
            else if (item == jmiStkStockWarehouseLot) {
                showView(SDataConstants.TRN_STK, SDataConstants.TRNX_STK_LOT_WH);
            }
            else if (item == jmiStkStockMovements) {
                showView(SDataConstants.TRNX_STK_MOVES);
            }
            else if (item == jmiStkStockMovementsEntry) {
                showView(SDataConstants.TRNX_STK_MOVES_ETY);
            }
            else if (item == jmiStkStockRotation) {
                showView(SDataConstants.TRNX_STK_ROTATION, SDataConstants.TRNX_STK_STK);
            }
            else if (item == jmiStkStockRotationLot) {
                showView(SDataConstants.TRNX_STK_ROTATION, SDataConstants.TRNX_STK_LOT);
            }
            else if (item == jmiReportStock) {
                menuRepStock();
            }
            else if (item == jmiReportStockMoves) {
                menuRepStockMoves();
            }
            else if (item == jmiReportStockTrackingLot) {
                menuRepStockTrackingLot();
            }
        }
    }
}
