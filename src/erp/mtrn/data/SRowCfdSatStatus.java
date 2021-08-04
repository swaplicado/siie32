/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import java.util.Date;

/**
 * Renglón de la tabla donde se muestran los datos de estatus de documentos de la validación del SAT.
 * @author Isabel Servín
 */
public final class SRowCfdSatStatus extends erp.lib.table.STableRow {
    
    private final String msTipoDoc;
    private final String msFolio;
    private final Date mdFecha;
    private final String msAsocNegocio;
    private final String msRfcAsocNegocio;
    private final String msUuid;
    private final String[] moStatus; // Arreglo de cuatro pocisiones que contiene el estatus del SAT: Estatus, código de estatus, información de cancelación y estatus de cancelación
    
    /**
     * 
     * @param tipoDoc
     * @param folio
     * @param fecha
     * @param asocNegocio
     * @param rfcAsocNegocio
     * @param uuid
     * @param status Arreglo de cuatro pocisiones que contiene el estatus del SAT: Estatus, código de estatus, información de cancelación y estatus de cancelación
     */
    public SRowCfdSatStatus(String tipoDoc, String folio, Date fecha, String asocNegocio, String rfcAsocNegocio, String uuid, String[] status) {
        msTipoDoc = tipoDoc;
        msFolio = folio;
        msAsocNegocio = asocNegocio;
        msRfcAsocNegocio = rfcAsocNegocio;
        msUuid = uuid;
        mdFecha = fecha;
        moStatus = status;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msTipoDoc);
        mvValues.add(msFolio);
        mvValues.add(mdFecha);
        mvValues.add(msAsocNegocio);
        mvValues.add(msRfcAsocNegocio);
        mvValues.add(msUuid);
        mvValues.add(moStatus[0]);
        mvValues.add(moStatus[1]);
        mvValues.add(moStatus[2]);
        mvValues.add(moStatus[3]);
    }
}
