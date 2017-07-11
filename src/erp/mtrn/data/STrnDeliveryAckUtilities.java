/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.mcfg.data.SDataParamsCompany;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel López
 */
public abstract class STrnDeliveryAckUtilities {

    private static ArrayList<SDataDpsDeliveryAck> getDpsDeliveryAcks(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        ArrayList<SDataDpsDeliveryAck> deliveryAcks = new ArrayList<>();
        String sql = "";
        Statement statement = client.getSession().getDatabase().getConnection().createStatement();
        Statement statementRead = client.getSession().getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        sql = "SELECT id_dps_ack "
                + "FROM trn_dps_ack "
                + "WHERE fid_year = " + dpsKey[0] + " AND "
                + "fid_doc = " + dpsKey[1] + " AND "
                + "b_del = 0 ";
        resultSet = statement.executeQuery(sql);
        
        while (resultSet.next()){
                SDataDpsDeliveryAck deliveryAck = new SDataDpsDeliveryAck();
                deliveryAck.read(new int[] { resultSet.getInt(1) }, statementRead);
                deliveryAcks.add(deliveryAck);
        } 
        
        return deliveryAcks;
    }
    
    public static void downloadFiles(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        String path = "";        
        ArrayList<SDataDpsDeliveryAck> deliveryAcks = null;
        
        //solicitar directorio de destino al usuario (≈ solicitar confirmación al usuario)
        client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) { //si el usuario sí proporcionó el directorio de destino
            path = client.getFileChooser().getSelectedFile().getAbsolutePath();
            
            //obtener lista de registros de acuse de entrega del documento (ArrayList<SDataDpsDeliveryAck>)
            deliveryAcks = getDpsDeliveryAcks(client, dpsKey);
            
            if (deliveryAcks.isEmpty()) { //si no hay registros:
                //informar al usuario que no hay archivos
                client.showMsgBoxInformation("El documento no tiene archivos de acuse de entrega.");
            }
            else {
                //para cada registro en la lista de registros de acuse de entrega del documento:
                for (SDataDpsDeliveryAck deliveryAck : deliveryAcks) {
                    //leer archivo del registro de acuse de entrega
                    deliveryAck.setAuxFile(deliveryAck.createFileFromSystemPath());
                    
                    //guardar archivo en el directorio de destino
                    deliveryAck.saveFileCustomPath(path);
                }
                
                //informar al usuario el número de archivos descargados
                client.showMsgBoxInformation("Se descargaron " + deliveryAcks.size() + " archivo(s).");
            }
        }
    }

    public static void deleteFiles(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        File filePath; 
        String sql = "";
        
        ArrayList<SDataDpsDeliveryAck> deliveryAcks = null;
        //solicitar confirmación al usuario para eliminar los archivos de acuses de entrega
        if(client.showMsgBoxConfirm("Se eliminarán todos los acuses de entrega del documento."+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
            deliveryAcks = getDpsDeliveryAcks(client, dpsKey); //obtener lista de registros de acuse de entrega del documento
            if (deliveryAcks.isEmpty()) { //si no hay registros:
                client.showMsgBoxInformation("El documento no tiene archivos de acuse de entrega.");
            }
            else {
                for (SDataDpsDeliveryAck deliveryAck : deliveryAcks) {
                    filePath = new File(SDataParamsCompany.FILES_DIR + "/" + deliveryAck.getNameSystem());
                    
                    filePath.delete();   //eliminar archivo en el directorio de destino
                                        
                    sql = "UPDATE trn_dps_ack SET "
                        + "b_del = 1, "
                        + "fid_usr_del = " + client.getSession().getUser().getPkUserId() + ", "
                        + "ts_del = NOW() "
                        + "WHERE fid_year = " + dpsKey[0] + " AND fid_doc = " + dpsKey[1] + " AND b_del = 0 ";  //modificar registro de acuse de entrega del documento
                    client.getSession().getStatement().execute(sql);
                }
                client.showMsgBoxInformation("Se eliminaron " + deliveryAcks.size() + " archivo(s).");
            }		
        }							
    }

    public static void reasignFiles(erp.client.SClientInterface client, int[] dpsSourceKey, int[] dpsDestinyKey) throws Exception {		
//	solicitar confirmación al usuario para reasignar los archivos de acuses de entrega		
//	SI el usuario sí desea reasignarlos:		
//		obtener lista de registros de acuse de entrega del documento (ArrayList<SDataDpsDeliveryAck>)	
//		SI no hay registros:	
//			informar al usuario que no hay archivos
//		SI NO:	
//			PARA cada registro en la lista de registros de acuse de entrega del documento:
//                          reasignar el documento de destino en el registro de acuse de entrega
//                          guardar cambios al registro de acuse de entrega			
//			FIN PARA
//			informar al usuario el número de archivos reasignados
//		FIN SI	
//	FIN SI			
    }
}