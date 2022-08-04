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
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
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
import erp.mtrn.form.SDialogRepStockMovesSumSum;
import erp.mtrn.form.SDialogRepStockPeriod;
import erp.mtrn.form.SDialogRepStockTrackingLot;
import erp.mtrn.form.SDialogUtilStockClosing;
import erp.mtrn.form.SFormDiog;
import erp.mtrn.form.SFormDiogAdjustmentType;
import erp.mtrn.form.SFormMaintDiog;
import erp.mtrn.form.SFormStockConfig;
import erp.mtrn.form.SFormStockConfigDns;
import erp.mtrn.form.SFormStockConfigItem;
import erp.mtrn.form.SFormStockLot;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Cesar Orozco, Gil De Jesús, Sergio Flores, Claudio Peña
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
    private javax.swing.JMenuItem jmiDpsPurOrderSuppliesInvoice;

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
    private javax.swing.JMenuItem jmiDpsSalOrderSupply;

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
    private javax.swing.JMenuItem jmiIogInventoryValuationPrcCalc;
    private javax.swing.JMenuItem jmiIogInventoryValuationUpdCost;
    private javax.swing.JMenuItem jmiIogInventoryMfgCost;
    private javax.swing.JMenuItem jmiIogIdentifiedCosts;
    private javax.swing.JMenuItem jmiIogStockValueByWarehouse;
    private javax.swing.JMenuItem jmiIogStockValueByItem;
    private javax.swing.JMenuItem jmiIogStockTheoricalCost;
    
    private javax.swing.JMenu jmMenuMaint;
    private javax.swing.JMenuItem jmiMaintStockPart;
    private javax.swing.JMenuItem jmiMaintStockToolAvl;
    private javax.swing.JMenuItem jmiMaintStockToolLent;
    private javax.swing.JMenuItem jmiMaintStockToolMaint;
    private javax.swing.JMenuItem jmiMaintStockToolLost;
    private javax.swing.JMenuItem jmiMaintStockTool;
    private javax.swing.JMenuItem jmiMaintMovementPart;
    private javax.swing.JMenuItem jmiMaintMovementPartDetail;
    private javax.swing.JMenuItem jmiMaintMovementTool;
    private javax.swing.JMenuItem jmiMaintMovementToolDetail;
    private javax.swing.JMenuItem jmiMaintMovementToolLent;
    private javax.swing.JMenuItem jmiMaintMovementToolMaint;
    private javax.swing.JMenuItem jmiMaintMovementToolLost;
    private javax.swing.JMenuItem jmiMaintUserEmployee;
    private javax.swing.JMenuItem jmiMaintUserContractor;
    private javax.swing.JMenuItem jmiMaintUserContractorSupv;
    private javax.swing.JMenuItem jmiMaintUserToolMaintProv;
    private javax.swing.JMenuItem jmiMaintArea;
    
    private javax.swing.JMenu jmMenuStk;
    private javax.swing.JMenuItem jmiStkStock;
    private javax.swing.JMenuItem jmiStkStockValueCost;
    private javax.swing.JMenuItem jmiStkStockLot;
    private javax.swing.JMenuItem jmiStkStockWarehouse;
    private javax.swing.JMenuItem jmiStkStockWarehouseLot;
    private javax.swing.JMenuItem jmiStkStockMovements;
    private javax.swing.JMenuItem jmiStkStockMovementsEntry;
    private javax.swing.JMenuItem jmiStkStockRotation;
    private javax.swing.JMenuItem jmiStkStockRotationLot;
    private javax.swing.JMenuItem jmiStkStockClosing;
    private javax.swing.JMenuItem jmiItemHistoric;
    
    private javax.swing.JMenu jmMenuRep;
    private javax.swing.JMenuItem jmiReportStock;
    private javax.swing.JMenuItem jmiReportStockPeriod;
    private javax.swing.JMenuItem jmiReportStockMoves;
    private javax.swing.JMenuItem jmiReportStockMovesSumSum;
    private javax.swing.JMenuItem jmiReportStockTrackingLot;
    private javax.swing.JMenu jmMenuRepStats;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumePendMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumePendEntryMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumedMass;
    private javax.swing.JMenuItem jmiRepStatsMfgConsumedEntryMass;

    private erp.mtrn.form.SDialogDiogSaved moDialogDiogSaved;
    private erp.mtrn.form.SDialogRepStock moDialogRepStock;
    private erp.mtrn.form.SDialogRepStockPeriod moDialogRepStockPeriod;
    private erp.mtrn.form.SDialogRepStockMoves moDialogRepStockMoves;
    private erp.mtrn.form.SDialogRepStockMovesSumSum moDialogRepStockMovesSumSum;
    private erp.mtrn.form.SDialogRepStockTrackingLot moDialogRepStockTrackingLot;
    private erp.mtrn.form.SFormDiog moFormDiog;
    private erp.mtrn.form.SFormMaintDiog moFormMaintDiog;
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
        boolean hasRightMaint = false;
        int levelRightMaint = 0;

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
        jmiDpsPurOrderSupplies = new JMenuItem("Pedidos de compras con surtidos");
        jmiDpsPurOrderSuppliesInvoice = new JMenuItem("Pedidos de compras a surtir con factura");
        jmMenuDpsPurSup.add(jmiDpsPurSupplyPend);
        jmMenuDpsPurSup.add(jmiDpsPurSupplyPendEty);
        jmMenuDpsPurSup.addSeparator();
        jmMenuDpsPurSup.add(jmiDpsPurSupplied);
        jmMenuDpsPurSup.add(jmiDpsPurSuppliedEty);
        jmMenuDpsPurSup.addSeparator();
        jmMenuDpsPurSup.add(jmiDpsPurOrderSupplies);
        jmMenuDpsPurSup.add(jmiDpsPurOrderSuppliesInvoice);
        jmiDpsPurSupplyPend.addActionListener(this);
        jmiDpsPurSupplyPendEty.addActionListener(this);
        jmiDpsPurSupplied.addActionListener(this);
        jmiDpsPurSuppliedEty.addActionListener(this);
        jmiDpsPurOrderSupplies.addActionListener(this);
        jmiDpsPurOrderSuppliesInvoice.addActionListener(this);

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
        jmiDpsSalOrderSupply = new JMenuItem("Pedidos de ventas con surtidos");
        jmMenuDpsSalSup.add(jmiDpsSalSupplyPend);
        jmMenuDpsSalSup.add(jmiDpsSalSupplyPendEntry);
        jmMenuDpsSalSup.addSeparator();
        jmMenuDpsSalSup.add(jmiDpsSalSupplied);
        jmMenuDpsSalSup.add(jmiDpsSalSuppliedEntry);
        jmMenuDpsSalSup.addSeparator();
        jmMenuDpsSalSup.add(jmiDpsSalOrderSupply);
        jmiDpsSalSupplyPend.addActionListener(this);
        jmiDpsSalSupplyPendEntry.addActionListener(this);
        jmiDpsSalSupplied.addActionListener(this);
        jmiDpsSalSuppliedEntry.addActionListener(this);
        jmiDpsSalOrderSupply.addActionListener(this);

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
        jmiIogInventoryValuationPrcCalc = new JMenuItem("Valuación de inventarios");
        jmiIogInventoryValuationUpdCost = new JMenuItem("Actualización de costos de inventarios");
        jmiIogInventoryMfgCost = new JMenuItem("Costos de producción por producto");
        jmiIogIdentifiedCosts = new JMenuItem("Costos identificados de ventas");
        jmiIogStockValueByWarehouse = new JMenuItem("Valor de inventarios por almacén");
        jmiIogStockValueByItem = new JMenuItem("Valor de inventarios por ítem");
        jmiIogStockTheoricalCost = new JMenuItem("Valor valuación vs. valor teórico");
        
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
        jmMenuIog.add(jmiIogInventoryValuationPrcCalc);
        jmMenuIog.add(jmiIogInventoryValuationUpdCost);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogInventoryMfgCost);
        jmMenuIog.add(jmiIogIdentifiedCosts);
        jmMenuIog.addSeparator();
        jmMenuIog.add(jmiIogStockValueByWarehouse);
        jmMenuIog.add(jmiIogStockValueByItem);
        //jmMenuIog.add(jmiIogStockValueByDiogType); sflores, 2016-03-09, evaluating to remove it
        jmMenuIog.add(jmiIogStockTheoricalCost);
        
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
        jmiIogInventoryValuationPrcCalc.addActionListener(this);
        jmiIogInventoryValuationUpdCost.addActionListener(this);
        jmiIogInventoryMfgCost.addActionListener(this);
        jmiIogIdentifiedCosts.addActionListener(this);
        jmiIogStockValueByWarehouse.addActionListener(this);
        jmiIogStockValueByItem.addActionListener(this);
        jmiIogStockTheoricalCost.addActionListener(this);
        
        jmMenuMaint = new JMenu("Mantto.");
        jmiMaintStockPart = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase());
        jmiMaintStockToolAvl = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_TOOL_AVL.toLowerCase());
        jmiMaintStockToolLent = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_TOOL_LENT.toLowerCase());
        jmiMaintStockToolMaint = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_TOOL_MAINT.toLowerCase());
        jmiMaintStockToolLost = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_TOOL_LOST.toLowerCase());
        jmiMaintStockTool = new JMenuItem("Stock " + SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase() + " (todas)");
        jmiMaintMovementPart = new JMenuItem("Docs. consumos de " + SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase());
        jmiMaintMovementPartDetail = new JMenuItem("Docs. consumos de " + SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase() + " a detalle");      
        jmiMaintMovementTool = new JMenuItem("Docs. consumos de " + SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase());
        jmiMaintMovementToolDetail = new JMenuItem("Docs. consumos de " + SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase() + " a detalle");
        jmiMaintMovementToolLent = new JMenuItem("Docs. " + SModSysConsts.TXT_TRNX_MAINT_TOOL_LENT.toLowerCase());
        jmiMaintMovementToolMaint = new JMenuItem("Docs. " + SModSysConsts.TXT_TRNX_MAINT_TOOL_MAINT.toLowerCase());
        jmiMaintMovementToolLost = new JMenuItem("Docs. " + SModSysConsts.TXT_TRNX_MAINT_TOOL_LOST.toLowerCase());
        jmiMaintUserEmployee = new JMenuItem("Empleados");
        jmiMaintUserContractor = new JMenuItem("Contratistas");
        jmiMaintUserContractorSupv = new JMenuItem("Residentes de contratistas");
        jmiMaintUserToolMaintProv = new JMenuItem("Proveedores de mantto. de herramientas");
        jmiMaintArea = new JMenuItem("Áreas de mantenimiento");
        
        jmMenuMaint.add(jmiMaintStockPart);
        jmMenuMaint.addSeparator();
        jmMenuMaint.add(jmiMaintStockToolAvl);
        jmMenuMaint.add(jmiMaintStockToolLent);
        jmMenuMaint.add(jmiMaintStockToolMaint);
        jmMenuMaint.add(jmiMaintStockToolLost);
        jmMenuMaint.add(jmiMaintStockTool);
        jmMenuMaint.addSeparator();
        jmMenuMaint.add(jmiMaintMovementPart);
        jmMenuMaint.add(jmiMaintMovementPartDetail);
        jmMenuMaint.add(jmiMaintMovementTool);
        jmMenuMaint.add(jmiMaintMovementToolDetail);
        jmMenuMaint.addSeparator();
        jmMenuMaint.add(jmiMaintMovementToolLent);
        jmMenuMaint.add(jmiMaintMovementToolMaint);
        jmMenuMaint.add(jmiMaintMovementToolLost);
        jmMenuMaint.addSeparator();
        jmMenuMaint.add(jmiMaintUserEmployee);
        jmMenuMaint.add(jmiMaintUserContractor);
        jmMenuMaint.add(jmiMaintUserContractorSupv);
        jmMenuMaint.add(jmiMaintUserToolMaintProv);
        jmMenuMaint.addSeparator();
        jmMenuMaint.add(jmiMaintArea);
        
        jmiMaintStockPart.addActionListener(this);
        jmiMaintStockToolAvl.addActionListener(this);
        jmiMaintStockToolLent.addActionListener(this);
        jmiMaintStockToolMaint.addActionListener(this);
        jmiMaintStockToolLost.addActionListener(this);
        jmiMaintStockTool.addActionListener(this);
        jmiMaintMovementPart.addActionListener(this);
        jmiMaintMovementPartDetail.addActionListener(this);
        jmiMaintMovementTool.addActionListener(this);
        jmiMaintMovementToolDetail.addActionListener(this);
        jmiMaintMovementToolLent.addActionListener(this);
        jmiMaintMovementToolMaint.addActionListener(this);
        jmiMaintMovementToolLost.addActionListener(this);
        jmiMaintUserEmployee.addActionListener(this);
        jmiMaintUserContractor.addActionListener(this);
        jmiMaintUserContractorSupv.addActionListener(this);
        jmiMaintUserToolMaintProv.addActionListener(this);
        jmiMaintArea.addActionListener(this);
        
        jmMenuStk = new JMenu("Inventarios");
        jmiStkStock = new JMenuItem("Existencias");
        jmiStkStockValueCost = new JMenuItem("Existencias valor ítem");
        jmiStkStockLot = new JMenuItem("Existencias por lote");
        jmiStkStockWarehouse = new JMenuItem("Existencias por almacén");
        jmiStkStockWarehouseLot = new JMenuItem("Existencias por almacén por lote");
        jmiStkStockMovements = new JMenuItem("Movimientos de inventarios");
        jmiStkStockMovementsEntry = new JMenuItem("Movimientos de inventarios a detalle");
        jmiStkStockRotation = new JMenuItem("Rotación");
        jmiStkStockRotationLot = new JMenuItem("Rotación por lote");
        jmiStkStockClosing = new JMenuItem("Generación de inventarios iniciales...");
        jmiItemHistoric = new JMenuItem("Historial ítems");
        
        jmMenuStk.add(jmiStkStock);
        jmMenuStk.add(jmiStkStockValueCost);
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
        jmMenuStk.addSeparator();
        jmMenuStk.add(jmiStkStockClosing);
        jmMenuStk.addSeparator();
        jmMenuStk.add(jmiItemHistoric);
        
        jmiStkStock.addActionListener(this);
        jmiStkStockValueCost.addActionListener(this);
        jmiStkStockLot.addActionListener(this);
        jmiStkStockWarehouse.addActionListener(this);
        jmiStkStockWarehouseLot.addActionListener(this);
        jmiStkStockMovements.addActionListener(this);
        jmiStkStockMovementsEntry.addActionListener(this);
        jmiStkStockRotation.addActionListener(this);
        jmiStkStockRotationLot.addActionListener(this);
        jmiStkStockClosing.addActionListener(this);
        jmiItemHistoric.addActionListener(this);

        jmMenuRep = new JMenu("Reportes");
        jmiReportStock = new JMenuItem("Reporte de existencias...");
        jmiReportStockPeriod = new JMenuItem("Reporte de existencias por periodo...");
        jmiReportStockMoves = new JMenuItem("Reporte de movimientos de inventarios...");
        jmiReportStockMovesSumSum = new JMenuItem("Resumen de movimientos de inventarios...");
        jmiReportStockTrackingLot = new JMenuItem("Reporte de rastreo de lotes...");
        jmMenuRep.add(jmiReportStock);
        jmMenuRep.add(jmiReportStockPeriod);
        jmMenuRep.add(jmiReportStockMoves);
        jmMenuRep.add(jmiReportStockMovesSumSum);
        jmMenuRep.add(jmiReportStockTrackingLot);
        jmiReportStock.addActionListener(this);
        jmiReportStockPeriod.addActionListener(this);
        jmiReportStockMoves.addActionListener(this);
        jmiReportStockMovesSumSum.addActionListener(this);
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
        hasRightMaint = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MAINT).HasRight;
        levelRightMaint = miClient.getSessionXXX().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_INV_MAINT);

        jmMenuCat.setEnabled(hasRightInAdj || hasRightOutAdj || hasRightOutOtherInt);
        jmMenuDpsPurSup.setEnabled(hasRightInPur);
        jmMenuDpsPurRet.setEnabled(hasRightOutPur);
        jmMenuDpsSalSup.setEnabled(hasRightOutSal);
        jmMenuDpsSalRet.setEnabled(hasRightInSal);
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
        jmiIogInventoryValuationPrcCalc.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogInventoryValuationUpdCost.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogInventoryMfgCost.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogIdentifiedCosts.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockValueByWarehouse.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockValueByItem.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiIogStockTheoricalCost.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmMenuMaint.setEnabled(hasRightMaint);
        jmiMaintStockPart.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintStockToolAvl.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintStockToolLent.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintStockToolMaint.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintStockToolLost.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintStockTool.setEnabled(levelRightMaint >= SUtilConsts.LEV_READ);
        jmiMaintMovementPart.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementPartDetail.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementTool.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementToolDetail.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementToolLent.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementToolMaint.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintMovementToolLost.setEnabled(levelRightMaint >= SUtilConsts.LEV_CAPTURE);
        jmiMaintUserEmployee.setEnabled(levelRightMaint >= SUtilConsts.LEV_MANAGER);
        jmiMaintUserContractor.setEnabled(levelRightMaint >= SUtilConsts.LEV_MANAGER);
        jmiMaintUserContractorSupv.setEnabled(levelRightMaint >= SUtilConsts.LEV_MANAGER);
        jmiMaintUserToolMaintProv.setEnabled(levelRightMaint >= SUtilConsts.LEV_MANAGER);
        jmiMaintArea.setEnabled(levelRightMaint >= SUtilConsts.LEV_MANAGER);
        jmMenuStk.setEnabled(hasRightStock);
        jmiStkStockClosing.setEnabled(hasRightInAdj || hasRightOutAdj);
        jmiItemHistoric.setEnabled(hasRightInAdj || hasRightOutAdj);
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
    
    private void menuRepStockPeriod() {
        if (moDialogRepStockPeriod == null) {
            moDialogRepStockPeriod = new SDialogRepStockPeriod(miClient);
        }

        moDialogRepStockPeriod.formRefreshCatalogues();
        moDialogRepStockPeriod.formReset();
        moDialogRepStockPeriod.setFormVisible(true);
    }

    private void menuRepStockMoves() {
        if (moDialogRepStockMoves == null) {
            moDialogRepStockMoves = new SDialogRepStockMoves(miClient);
        }

        moDialogRepStockMoves.formRefreshCatalogues();
        moDialogRepStockMoves.formReset();
        moDialogRepStockMoves.setFormVisible(true);
    }
    
    private void menuRepStockMovesSumSum() {
        if (moDialogRepStockMovesSumSum == null) {
            moDialogRepStockMovesSumSum= new SDialogRepStockMovesSumSum(miClient, SDataConstants.ITMU_ITEM, SDataConstants.BPSU_BP );
        }

        moDialogRepStockMovesSumSum.formRefreshCatalogues();
        moDialogRepStockMovesSumSum.formReset();
        moDialogRepStockMovesSumSum.setFormVisible(true);
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
                        STrnDiogComplement complement = (STrnDiogComplement) moFormComplement;
                        moFormDiog.formReset();
                        moFormDiog.setParamIogTypeKey(complement.getDiogTypeKey());
                        moFormDiog.setParamDpsSource(complement.getDpsSource());
                        moFormDiog.setParamProdOrderSource(complement.getProdOrderKey());
                    }
                    miForm = moFormDiog;
                    break;
                case SDataConstants.TRNX_MAINT_DIOG:
                    if (moFormMaintDiog == null) {
                        moFormMaintDiog = new SFormMaintDiog(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDiog();
                    }
                    if (moFormComplement != null) {
                        STrnDiogComplement complement = (STrnDiogComplement) moFormComplement;
                        moFormMaintDiog.setParamMaintMovementSettings(complement.getMaintMovementType(), complement.getMaintUserType());
                        moFormMaintDiog.formReset();
                    }
                    miForm = moFormMaintDiog;
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
        String title = "";
        String dpsCategory = STrnUtils.getDpsCategoryName(auxType01, SUtilConsts.NUM_PLR);
        Class viewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {              
                case SDataConstants.TRN_LOT:
                    viewClass = erp.mtrn.view.SViewStockLot.class;
                    title = "Lotes";
                    break;
                case SDataConstants.TRN_STK_CFG:
                    viewClass = erp.mtrn.view.SViewStockConfig.class;
                    title = "Máximos y mínimos";
                    break;
                case SDataConstants.TRNU_TP_IOG_ADJ:
                    viewClass = erp.mtrn.view.SViewDiogAdjustmentType.class;
                    title = "Tipos ajuste inventario";
                    break;
                case SDataConstants.TRN_STK_CFG_ITEM:
                    viewClass = erp.mtrn.view.SViewStockConfigItem.class;
                    title = "Config. ítems x almacén ";
                    break;
                case SDataConstants.TRN_STK_CFG_DNS:
                    viewClass = erp.mtrn.view.SViewStockConfigDns.class;
                    title = "Config. series docs. ventas x almacén ";
                    break;
                case SDataConstants.TRN_STK:
                    viewClass = erp.mtrn.view.SViewStock.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_STK_STK:
                            title = "Existencias";
                            break;
                        case SDataConstants.TRNX_STK_STK_WH:
                            title = "Existencias x almacén";
                            break;
                        case SDataConstants.TRNX_STK_LOT:
                            title = "Existencias x lote";
                            break;
                        case SDataConstants.TRNX_STK_LOT_WH:
                            title = "Existencias x almacén x lote";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_STK_ITEM:
                    viewClass = erp.mtrn.view.SViewStockCostUnit.class;
                    title = "Existencias valor ítem";
                    break;
                case SDataConstants.TRNX_STK_MOVES:
                    viewClass = erp.mtrn.view.SViewStockMoves.class;
                    title = "Movimientos inventarios";
                    break;
                case SDataConstants.TRNX_STK_MOVES_ETY:
                    viewClass = erp.mtrn.view.SViewStockMovesEntry.class;
                    title = "Movimientos inventarios (detalle)";
                    break;
                case SDataConstants.TRNX_STK_ROTATION:
                    viewClass = erp.mtrn.view.SViewStockRotation.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_STK_STK:
                            title = "Rotación";
                            break;
                        case SDataConstants.TRNX_STK_LOT:
                            title = "Rotación x lote";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_STK_ITEM_HIS:
                    viewClass = erp.mitm.view.SViewItemHistoric.class;
                    title = "Ítems históricos";
                    break;
                case SDataConstants.TRNX_STK_COMSUME:
                    viewClass = erp.mtrn.view.SViewDpsStockConsume.class;
                    title = "Consumo " + dpsCategory.toLowerCase();
                    break;
                case SDataConstants.TRNX_DPS_SUPPLY_PEND:
                    viewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    title = dpsCategory + " x surtir";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY:
                    viewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    title = dpsCategory + " x surtir (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED:
                    viewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    title = dpsCategory + " surtidas";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED_ETY:
                    viewClass = erp.mtrn.view.SViewDpsStockSupply.class;
                    title = dpsCategory + " surtidas (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_RETURN_PEND:
                    viewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    title = dpsCategory + " x devolver";
                    break;
                case SDataConstants.TRNX_DPS_RETURN_PEND_ETY:
                    viewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    title = dpsCategory + " x devolver (detalle)";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED_ORDER:
                    viewClass = erp.mtrn.view.SViewOrderSuppliedWithMovs.class;                    
                    title = "Pedidos " + dpsCategory.toLowerCase() + " c/surtidos";
                    break;
                case SDataConstants.TRNX_DPS_SUPPLIED_ORDER_INVOICE:
                    viewClass = erp.mtrn.view.SViewOrderSuppliedWithMovsInvoice.class;                    
                    title = "Pedidos " + dpsCategory.toLowerCase() + " surtidos facturas";
                    break;
                case SDataConstants.TRNX_DPS_RETURNED:
                    viewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    title = dpsCategory + " devueltas";
                    break;
                case SDataConstants.TRNX_DPS_RETURNED_ETY:
                    viewClass = erp.mtrn.view.SViewDpsStockReturn.class;
                    title = dpsCategory + " devueltas (detalle)";
                    break;
                case SDataConstants.TRN_DIOG:
                    viewClass = erp.mtrn.view.SViewDiog.class;
                    title = "Docs. inventarios";
                    if (auxType01 != SLibConstants.UNDEFINED && auxType02 != SLibConstants.UNDEFINED) {
                        title += " (" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_IOG, new int[] { auxType01, auxType02, 1 }, SLibConstants.DESCRIPTION_CODE) + ")";
                    }
                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_AUDIT_PEND:
                            viewClass = erp.mtrn.view.SViewDiogAudit.class;
                            title = "Docs. inventarios x auditar";
                            break;
                        case SDataConstants.TRNX_DIOG_AUDITED:
                            viewClass = erp.mtrn.view.SViewDiogAudit.class;
                            title = "Docs. inventarios auditad@s";
                            break;
                    }
                    break;
                case SDataConstants.TRNX_MAINT_DIOG:
                    viewClass = erp.mtrn.view.SViewMaintDiog.class;
                    title = "Docs. ";
                    switch (auxType01){
                        case SModSysConsts.TRNX_MAINT_PART:
                            switch (auxType02) {
                                case SUtilConsts.PER_ITM:
                                    title += "consumos " + SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase() + " a detalle";
                                    break;
                                case SUtilConsts.PER_DOC:                           
                                    title += "consumos " + SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase();
                                    break;
                                default:
                            }
                            break;                        
                        case SModSysConsts.TRNX_MAINT_TOOL:
                            switch (auxType02) {
                                case SUtilConsts.PER_ITM:
                                    title += "consumos " + SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase()  + " a detalle";
                                    break;
                                case SUtilConsts.PER_DOC:                           
                                    title += "consumos " + SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase();
                                    break;
                                default:
                            }
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_LENT.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_MAINT.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_LOST.toLowerCase();
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_MAINT_STK:
                    viewClass = erp.mtrn.view.SViewMaintStock.class;
                    title = "Stock ";
                    switch (auxType01){
                        case SModSysConsts.TRNX_MAINT_PART:
                            title += SModSysConsts.TXT_TRNX_MAINT_PART.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_AVL:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_AVL.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_LENT.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_MAINT.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL_LOST.toLowerCase();
                            break;
                        case SModSysConsts.TRNX_MAINT_TOOL:
                            title += SModSysConsts.TXT_TRNX_MAINT_TOOL.toLowerCase() + " (todas)";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_DIOG_MFG:
                    viewClass = erp.mtrn.view.SViewDiogProdOrder.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_MFG_RM:
                        case SDataConstants.TRNX_DIOG_MFG_WP:
                        case SDataConstants.TRNX_DIOG_MFG_FG:
                            title = "Docs. " + (auxType02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG ? "entrega" : "devolución");
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_CON:
                            title = "Docs. " + (auxType02 == SDataConstants.TRNX_DIOG_MFG_MOVE_IN ? "entrada" : "salida");
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    switch (auxType01) {
                        case SDataConstants.TRNX_DIOG_MFG_RM:
                            title += " MP";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_WP:
                            title += " PP";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_FG:
                            title += " PT";
                            break;
                        case SDataConstants.TRNX_DIOG_MFG_CON:
                            title += " x consumo";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRNX_MFG_ORD:
                    title = "Movs. prod. - ";
                    switch (auxType01) {
                        case SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND:
                            viewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            title += "OP x asignar";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGN_PEND_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            title += "OP x asignar (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGNED:
                            viewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            title += "OP asignadas";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_ASSIGNED_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockAssign.class;
                            title += "OP asignadas (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISH_PEND:
                            viewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            title += "OP x terminar";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISH_PEND_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            title += "OP x terminar (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISHED:
                            viewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            title += "OP terminadas";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_FINISHED_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockFinish.class;
                            title += "OP terminadas (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title += "MP & P x consumir";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title += "MP & P x consumir (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title += "MP & P consumidos";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title += "MP & P consumidos (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_MASS:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title = "Masa MP & P x consumir";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title = "Masa MP & P x consumir (detalle)";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_MASS:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title = "Masa MP & P consumidos";
                            break;
                        case SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY_MASS:
                            viewClass = erp.mtrn.view.SViewProdOrderStockConsume.class;
                            title = "Masa MP & P consumidos (detalle)";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(viewClass, title, viewType, auxType01, auxType02);
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
        return new JMenu[] { jmMenuCat, jmMenuDpsPurSup, jmMenuDpsPurRet, jmMenuDpsSalSup, jmMenuDpsSalRet, jmMenuMfg, jmMenuIog, jmMenuMaint, jmMenuStk, jmMenuRep };
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
                showView(SDataConstants.TRNX_DPS_SUPPLIED_ORDER, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiDpsPurOrderSuppliesInvoice) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED_ORDER_INVOICE, SDataConstantsSys.TRNS_CT_DPS_PUR);
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
            else if (item == jmiDpsSalOrderSupply) {
                showView(SDataConstants.TRNX_DPS_SUPPLIED_ORDER, SDataConstantsSys.TRNS_CT_DPS_SAL);
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
            else if (item == jmiIogInventoryValuationPrcCalc) {
                miClient.getSession().showView(SModConsts.TRN_INV_VAL, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogInventoryValuationUpdCost) {
                miClient.getSession().showForm(SModConsts.TRNX_INV_VAL_COST_UPD, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogInventoryMfgCost) {
                miClient.getSession().showView(SModConsts.TRN_INV_MFG_CST, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogIdentifiedCosts) {
                miClient.getSession().showView(SModConsts.TRN_COST_IDENT_CALC, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiIogStockValueByWarehouse) {
                miClient.getSession().showView(SModConsts.TRNX_STK_COST, SModConsts.CFGU_COB_ENT, null);
            }
            else if (item == jmiIogStockValueByItem) {
                miClient.getSession().showView(SModConsts.TRNX_STK_COST, SModConsts.ITMU_ITEM, null);
            }
            else if (item == jmiMaintStockPart) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_PART);
            }
            else if (item == jmiMaintStockToolAvl) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_TOOL_AVL);
            }
            else if (item == jmiMaintStockToolLent) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_TOOL_LENT);
            }
            else if (item == jmiMaintStockToolMaint) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_TOOL_MAINT);
            }
            else if (item == jmiMaintStockToolLost) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_TOOL_LOST);
            }
            else if (item == jmiMaintStockTool) {
                showView(SDataConstants.TRNX_MAINT_STK, SModSysConsts.TRNX_MAINT_TOOL);
            }
            else if (item == jmiMaintMovementPart) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_PART, SUtilConsts.PER_DOC);
            }
            else if (item == jmiMaintMovementPartDetail) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_PART, SUtilConsts.PER_ITM);
            }
            else if (item == jmiMaintMovementTool) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_TOOL, SUtilConsts.PER_DOC);
            }
            else if (item == jmiMaintMovementToolDetail) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_TOOL, SUtilConsts.PER_ITM);
            }
            else if (item == jmiMaintMovementToolLent) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_TOOL_LENT);
            }
            else if (item == jmiMaintMovementToolMaint) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_TOOL_MAINT);
            }
            else if (item == jmiMaintMovementToolLost) {
                showView(SDataConstants.TRNX_MAINT_DIOG, SModSysConsts.TRNX_MAINT_TOOL_LOST);
            }
            else if (item == jmiMaintUserEmployee) {
                miClient.getSession().showView(SModConsts.TRN_MAINT_USER, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE, null);
            }
            else if (item == jmiMaintUserContractor) {
                miClient.getSession().showView(SModConsts.TRN_MAINT_USER, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR, null);
            }
            else if (item == jmiMaintUserContractorSupv) {
                miClient.getSession().showView(SModConsts.TRN_MAINT_USER_SUPV, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiMaintUserToolMaintProv) {
                miClient.getSession().showView(SModConsts.TRN_MAINT_USER, SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV, null);
            }
            else if (item == jmiMaintArea) {
                miClient.getSession().showView(SModConsts.TRN_MAINT_AREA, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiStkStock) {
                showView(SDataConstants.TRN_STK, SDataConstants.TRNX_STK_STK);
            }
            else if (item == jmiStkStockValueCost) {
                showView(SDataConstants.TRNX_STK_ITEM, SDataConstants.TRNX_STK_ITEM);
            }
            else if (item == jmiReportStockPeriod) {
                menuRepStockPeriod();
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
            else if (item == jmiStkStockClosing) {
                moDialogUtilStockClosing.resetForm();
                moDialogUtilStockClosing.setVisible(true);
            }
            else if (item == jmiItemHistoric) {
                showView(SDataConstants.TRNX_STK_ITEM_HIS, SDataConstants.TRNX_STK_STK); // poner las de la nueva clse
            }
            else if (item == jmiReportStock) {
                menuRepStock();
            }
            else if (item == jmiReportStockMoves) {
                menuRepStockMoves();
            }
            else if (item == jmiReportStockMovesSumSum) {
                menuRepStockMovesSumSum();
            }
            else if (item == jmiReportStockTrackingLot) {
                menuRepStockTrackingLot();
            }
            else if (item == jmiIogStockTheoricalCost) {
                miClient.getSession().showView(SModConsts.TRNX_INV_VAL_COST_QRY, SLibConstants.UNDEFINED, null);
            }
        }
    }
}
