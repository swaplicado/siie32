/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SMatRequestAuthorizationUtils {
    
    public static int[] stringToIntArray(String input) {
        // Eliminar los corchetes al principio y al final del string
        input = input.substring(1, input.length() - 1);
        
        // Dividir el string en elementos separados por comas
        String[] elements = input.split(",");
        
        // Crear un arreglo de enteros y convertir los elementos de string a int
        int[] intArray = new int[elements.length];
        for (int i = 0; i < elements.length; i++) {
            intArray[i] = Integer.parseInt(elements[i]);
        }
        
        return intArray;
    }
    
    /**
     * Obtiene las entidades de consumo relacionadas con la requisición de materiales.
     * Contempla las entidades tanto a nivel encabezado como de las partidas
     * 
     * @param oMatReq
     * 
     * @return Lista de ids de las entidades de consumo
     */
    public static ArrayList<Integer> getConsumeEntitys(SDbMaterialRequest oMatReq) {
        ArrayList<Integer> consumeEntitys = new ArrayList<>();
        consumeEntitys.add(oMatReq.getFkEntMatConsumptionEntityId());
        for (SDbMaterialRequestEntry oEty : oMatReq.getChildEntries()) {
            consumeEntitys.add(oEty.getFkEntMatConsumptionEntityId_n());
        }
        
        return consumeEntitys;
    }
    
    /**
     * Determina si la configuración recibida aplica para la requisición de materiales actual.
     * 
     * @param session
     * @param idMatReq
     * @param oCfg 
     * @return 
     */
    public static String applyCfg(SGuiSession session, final int idMatReq, SDbAuthorizationPath oCfg) {
        ArrayList<SCondition> lConditions = oCfg.getAuthorizationConfigObject().getConditions();
        if (lConditions.isEmpty()) {
            return "";
        }
        
        SDbMaterialRequest oMatReq = new SDbMaterialRequest();
        try {
            oMatReq.read(session, new int[] { idMatReq });
        }
        catch (Exception ex) {
            Logger.getLogger(SMatRequestAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "Ocurrión un error al leer la requisición" + ex.getMessage();
        }
        
        if (oMatReq.getQueryResultId() != SDbConsts.READ_OK) {
            return "Ocurrión un error al leer la requisición";
        }
          
        for (SCondition oCondition : lConditions) {
            switch (oCondition.getKeyName()) {
                // Entidad de consumo igual a, o diferente a
                case "ConsumeEntity":
                    ArrayList<Integer> lEntities = SMatRequestAuthorizationUtils.getConsumeEntitys(oMatReq);
                    int consumeEntity = Integer.parseInt(oCondition.getStrValue());
                    boolean apply = false;
                    switch (oCondition.getOperator()) {
                        case "=":
                            for (Integer idEntity : lEntities) {
                                if (idEntity == consumeEntity) {
                                    apply = true;
                                    break;
                                }
                            }
                            break;
                        case "!=":
                            for (Integer iEntity : lEntities) {
                                if (iEntity != consumeEntity) {
                                    apply = true;
                                    break;
                                }
                            }
                            break;
                    }
                    
                    if (! apply) {
                        return "-1";
                    }
                    
                    break;
                    
                // Usuario de la requisición igual a, o diferente a
                case "MatReqUser":
                    int userReqMat = Integer.parseInt(oCondition.getStrValue());
                    boolean applyUsr = false;
                    switch (oCondition.getOperator()) {
                        case "=":
                            if (oMatReq.getFkUserInsertId() == userReqMat) {
                                applyUsr = true;
                            }
                            break;
                        case "!=":
                            if (oMatReq.getFkUserInsertId() != userReqMat) {
                                applyUsr = true;
                            }
                            break;
                    }
                    
                    if (! applyUsr) {
                        return "-1";
                    }
                    
                    break;
                    
                default:
                    return "-1";
            }
        }
        
        return "";
    }
}
