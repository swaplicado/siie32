/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db.ssc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Estructura de datos para actualización masiva de SBC.
 * @author Claudio Peña
 */
public class SSalarySscBase {
    
    public int EmployeeId;
    public double SalarySscBase;
    public Date DateSalarySscBase;
    public int UserId;
    
    public SSalarySscBase(final SSalarySscBase salarySscBase) {
        this(salarySscBase.EmployeeId, salarySscBase.SalarySscBase, salarySscBase.DateSalarySscBase, salarySscBase.UserId);
    }
    
    public SSalarySscBase(final int employeeId, final double salarySscBase, final Date dateSalarySscBase, final int userId) {
        BigDecimal bd = new BigDecimal(Double.toString(salarySscBase));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double salarySscBaseDecimal = bd.doubleValue();
        EmployeeId = employeeId;
        SalarySscBase = salarySscBaseDecimal;
        DateSalarySscBase = dateSalarySscBase;
        UserId = userId;
    }
}
