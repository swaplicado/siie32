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
    
    private final String msDocType;
    private final String msDocNumber;
    private final Date mdDocDate;
    private final String msBpName;
    private final String msBpFiscalId;
    private final String msUuid;
    private final SCfdUtilsHandler.CfdiAckQuery moCfdiAckQuery;
    
    /**
     * 
     * @param docType
     * @param docNumber
     * @param docDate
     * @param bpName
     * @param bpFiscalId
     * @param uuid
     * @param cfdiAckQuery
     */
    public SRowCfdSatStatus(String docType, String docNumber, Date docDate, String bpName, String bpFiscalId, String uuid, SCfdUtilsHandler.CfdiAckQuery cfdiAckQuery) {
        msDocType = docType;
        msDocNumber = docNumber;
        mdDocDate = docDate;
        msBpName = bpName;
        msBpFiscalId = bpFiscalId;
        msUuid = uuid;
        moCfdiAckQuery = cfdiAckQuery;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msDocType);
        mvValues.add(msDocNumber);
        mvValues.add(mdDocDate);
        mvValues.add(msBpName);
        mvValues.add(msBpFiscalId);
        mvValues.add(msUuid);
        mvValues.add(moCfdiAckQuery.CfdiStatus);
        mvValues.add(moCfdiAckQuery.RetrievalInfo);
        mvValues.add(moCfdiAckQuery.CancellableInfo);
        mvValues.add(moCfdiAckQuery.CancelStatus);
    }
}
