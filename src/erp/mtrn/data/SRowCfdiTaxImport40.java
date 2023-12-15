/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.ver40.DCfdi40Catalogs;
import erp.mod.SModSysConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public final class SRowCfdiTaxImport40 extends erp.lib.table.STableRow {
    
    private final cfd.ver40.DElementConceptoImpuestos moImpuestos;
    private final int mnTaxType;
    private final int mnRow;
    
    /**
     * 
     * @param impuestos Impuestos del concepto del CFDI.
     * @param taxType Tipo de impuesto. Valores soportados: SModSysConsts.FINS_TP_TAX_CHARGED o SModSysConsts.FINS_TP_TAX_RETAINED.
     * @param row Número del renglón del grid principal.
     */
    public SRowCfdiTaxImport40(cfd.ver40.DElementConceptoImpuestos impuestos, int taxType, int row){
        moImpuestos = impuestos;
        mnTaxType = taxType;
        mnRow = row;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        switch (mnTaxType) {
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver40.DElementConceptoImpuestoTraslado oTraslado = moImpuestos.getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados().get(mnRow);
                mvValues.add(DCfdi40Catalogs.DescripciónImpuestoTrasladado);
                mvValues.add(DCfdi40Catalogs.Impuesto.get(oTraslado.getAttImpuesto().getString()));
                mvValues.add(SLibUtils.DecimalFormatPercentage2D.format(oTraslado.getAttTasaOCuota().getDouble()));
                break;
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver40.DElementConceptoImpuestoRetencion oRetenciones = moImpuestos.getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().get(mnRow);
                mvValues.add(DCfdi40Catalogs.DescripciónImpuestoRetenido);
                mvValues.add(DCfdi40Catalogs.Impuesto.get(oRetenciones.getAttImpuesto().getString()));
                mvValues.add(SLibUtils.DecimalFormatPercentage2D.format(oRetenciones.getAttTasaOCuota().getDouble()));
                break;
            default:
        }
    }
}
