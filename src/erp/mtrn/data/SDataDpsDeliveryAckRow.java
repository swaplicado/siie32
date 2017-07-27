/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

/**
 *
 * @author Daniel López
 */
public class SDataDpsDeliveryAckRow extends erp.lib.table.STableRow {
    
    public SDataDpsDeliveryAckRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsDeliveryAck deliveryAck = (SDataDpsDeliveryAck) moData;
        
        mvValues.clear();
        mvValues.add(deliveryAck.getNameUser());
        setRowPrimaryKey( (int[]) deliveryAck.getPrimaryKey());
    }
}
