/*
 * Utilidades para la gestión de roles de usuario en el sistema.
 */
package erp.mod.cfg.swapms.utils;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.musr.data.SDataUser;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Clase de utilidades para obtener los roles asignados a un usuario
 * según su configuración y permisos en la empresa.
 * 
 * @author Edwin Carmona, Sergio Flores
 */
public class SUserRolesUtils {
    
    /**
     * Obtiene la lista de roles asignados a un usuario específico en una empresa.
     * 
     * @param statement Statement de SQL para realizar la consulta.
     * @param userId ID del usuario.
     * @param companyId ID de la empresa.
     * @return Lista de IDs de roles asignados al usuario.
     */
    public static ArrayList<Integer> getRolesOfUser(Statement statement, int userId, final int companyId) {
        SDataUser user = new SDataUser();
        user.read(new int[] { userId }, statement); // Carga los datos del usuario desde la base de datos
        
        ArrayList<Integer> lRoles = new ArrayList<>();
        
        // Si el usuario es universal, se le asignan todos los roles relevantes
        if (user.getIsUniversal()) {
            lRoles.add(SExportUtils.ROL_BUYER);
            lRoles.add(SExportUtils.ROL_ACCOUNTANT);
            lRoles.add(SExportUtils.ROL_PAYER);
            return lRoles;
        }
        
        // Si el usuario tiene acceso al módulo de compras, se le asigna el rol de comprador
        if (user.hasAccessToModule(SDataConstants.MOD_PUR, companyId)) {
            lRoles.add(SExportUtils.ROL_BUYER);
        }
        // Si el usuario tiene alguno de los roles financieros, se le asigna el rol de contador
        if (user.hasRole(SDataConstantsSys.ROL_FIN_ADMOR) ||
            user.hasRole(SDataConstantsSys.ROL_FIN_ACCT) || 
            user.hasRole(SDataConstantsSys.ROL_FIN_ACCT_AUX)) {
            lRoles.add(SExportUtils.ROL_ACCOUNTANT);
        }
        // Si el usuario tiene el rol de caja, se le asigna el rol de pagador
        if (user.hasRole(SDataConstantsSys.ROL_FIN_ACCT_CASH)) {
            lRoles.add(SExportUtils.ROL_PAYER);
        }
        
        return lRoles;
    }
}
