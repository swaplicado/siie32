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

    /**
     * Gets all delivery acknowledgment registries of desired document.
     * @param client GUI client.
     * @param dpsKey Desired document key.
     * @return Array list of delivery acknowledgment registries of desired document.
     * @throws Exception 
     */
    public static ArrayList<SDataDpsDeliveryAck> getDpsDeliveryAcks(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        ArrayList<SDataDpsDeliveryAck> dpsDeliveryAcks = new ArrayList<>();
        String sql = "";
        Statement statement = client.getSession().getDatabase().getConnection().createStatement();
        Statement statementRead = client.getSession().getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        sql = "SELECT id_dps_ack "
                + "FROM trn_dps_ack "
                + "WHERE fid_year = " + dpsKey[0] + " AND "
                + "fid_doc = " + dpsKey[1] + " AND "
                + "b_del = 0 "
                + "ORDER BY id_dps_ack ";
        resultSet = statement.executeQuery(sql);
        
        while (resultSet.next()){
                SDataDpsDeliveryAck deliveryAck = new SDataDpsDeliveryAck();
                deliveryAck.read(new int[] { resultSet.getInt(1) }, statementRead);
                dpsDeliveryAcks.add(deliveryAck);
        } 
        
        return dpsDeliveryAcks;
    }
    
    /**
     * Gets all delivery acknowledgment files of desired document.
     * @param client GUI client.
     * @param dpsKey Desired document key.
     * @return Array list of delivery acknowledgment files array of two dimensions (index 0: user name; index 1: system name) of desired document.
     * @throws Exception 
     */
    public static ArrayList<File[]> getFiles(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        ArrayList<File[]> files = new ArrayList<>();
        String sql = "";
        Statement statement = client.getSession().getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;
        
        sql = "SELECT id_dps_ack, name_usr, name_sys "
                + "FROM trn_dps_ack "
                + "WHERE fid_year = " + dpsKey[0] + " AND "
                + "fid_doc = " + dpsKey[1] + " AND "
                + "b_del = 0 "
                + "ORDER BY name_sys, id_dps_ack ";
        resultSet = statement.executeQuery(sql);
        
        while(resultSet.next()) {
             files.add(new File[] { 
                 new File(resultSet.getString("name_usr")),
                 new File(SDataParamsCompany.FILES_DIR + "\\" + resultSet.getString("name_sys"))
             });
        }
        
        return files;
    }
    
    /**
     * Download all delivery acknowledgment files of desired document into user selected directory.
     * @param client GUI client.
     * @param dpsKey Desired document key.
     * @throws Exception 
     */
    public static void downloadFiles(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        //solicitar directorio de destino al usuario (≈ solicitar confirmación al usuario)
        client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) { //si el usuario sí proporcionó el directorio de destino
            String path = client.getFileChooser().getSelectedFile().getAbsolutePath();
            
            //obtener lista de registros de acuse de entrega del documento (ArrayList<SDataDpsDeliveryAck>)
            ArrayList<SDataDpsDeliveryAck> dpsDeliveryAcks = getDpsDeliveryAcks(client, dpsKey);
            
            if (dpsDeliveryAcks.isEmpty()) { //si no hay registros:
                //informar al usuario que no hay archivos
                client.showMsgBoxInformation("El documento no tiene archivos de acuse de entrega.");
            }
            else {
                //para cada registro en la lista de registros de acuse de entrega del documento:
                for (SDataDpsDeliveryAck deliveryAck : dpsDeliveryAcks) {
                    //leer archivo del registro de acuse de entrega
                    deliveryAck.setAuxFile(deliveryAck.createFileFromSystemPath());
                    
                    //guardar archivo en el directorio de destino
                    deliveryAck.saveFileCustomPath(path);
                }
                
                //informar al usuario el número de archivos descargados
                client.showMsgBoxInformation("Se descargaron " + dpsDeliveryAcks.size() + " archivo(s).");
            }
        }
    }

    /**
     * Delete all delivery acknowledgment files of desired document.
     * @param client GUI client.
     * @param dpsKey Desired document key.
     * @throws Exception 
     */
    public static void deleteFiles(erp.client.SClientInterface client, int[] dpsKey) throws Exception {
        //solicitar confirmación al usuario para eliminar los archivos de acuses de entrega
        if(client.showMsgBoxConfirm("Se eliminarán todos los acuses de entrega del documento."+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
            ArrayList<SDataDpsDeliveryAck> dpsDeliveryAcks = getDpsDeliveryAcks(client, dpsKey); //obtener lista de registros de acuse de entrega del documento
            if (dpsDeliveryAcks.isEmpty()) { //si no hay registros:
                client.showMsgBoxInformation("El documento no tiene archivos de acuse de entrega.");
            }
            else {
                for (SDataDpsDeliveryAck deliveryAck : dpsDeliveryAcks) {
                    File filePath = new File(SDataParamsCompany.FILES_DIR + "\\" + deliveryAck.getNameSystem());
                    
                    filePath.delete();   //eliminar archivo en el directorio de destino
                                        
                    String sql = "UPDATE trn_dps_ack SET "
                        + "b_del = 1, "
                        + "fid_usr_del = " + client.getSession().getUser().getPkUserId() + ", "
                        + "ts_del = NOW() "
                        + "WHERE fid_year = " + dpsKey[0] + " AND fid_doc = " + dpsKey[1] + " AND b_del = 0 ";  //modificar registro de acuse de entrega del documento
                    client.getSession().getStatement().execute(sql);
                }
                client.showMsgBoxInformation("Se eliminaron " + dpsDeliveryAcks.size() + " archivo(s).");
            }		
        }							
    }

    /**
     * Reasign all delivery acknowledgment files from source document to destiny document.
     * @param client
     * @param dpsSourceKey Source document key.
     * @param dpsDestinyKey Destiny document key.
     * @throws Exception 
     */
    public static void reasignFiles(erp.client.SClientInterface client, int[] dpsSourceKey, int[] dpsDestinyKey) throws Exception {
        if(client.showMsgBoxConfirm("Se reasignarán los acuses de entrega."+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION){
            ArrayList<SDataDpsDeliveryAck> dpsDeliveryAcks = getDpsDeliveryAcks(client, dpsSourceKey);  //obtener lista de registros de acuse de entrega del documento 
            if (dpsDeliveryAcks.isEmpty()) { //si no hay registros:
                client.showMsgBoxInformation("El documento no tiene archivos de acuse de entrega.");    //informar al usuario que no hay archivos
            }
            else {
                for (SDataDpsDeliveryAck deliveryAck : dpsDeliveryAcks) {                                        
                    String sql = "UPDATE trn_dps_ack SET "
                        + "fid_year = " + dpsDestinyKey[0] + ", "
                        + "fid_doc = " + dpsDestinyKey[1] + ", "
                        + "fid_usr_edit = " + client.getSession().getUser().getPkUserId() + ", "
                        + "ts_edit = NOW() "
                        + "WHERE fid_year = " + dpsSourceKey[0] + " AND fid_doc = " + dpsSourceKey[1] + " AND b_del = 0 ";  //reasignar el documento de destino en el registro de acuse de entrega
                    client.getSession().getStatement().execute(sql);    //guardar cambios al registro de acuse de entrega
                }
                client.showMsgBoxInformation("Se reasignaron " + dpsDeliveryAcks.size() + " archivo(s).");  //informar al usuario el número de archivos reasignados	
            }
        }
    }
}