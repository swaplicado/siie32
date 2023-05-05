/*
 * (c) Software Aplicado SA de CV.
 */
package erp.mod.hrs.utils;

import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;
import sa.lib.gui.SGuiUser;

/**
 * Utilería para reacomodar cronológicamente los aniversarios de la toma de vacaciones gozadas y ajustadas de empleados activos.
 * ADVERTENCIA:
 * Tener sumo cuidado con los argumentos de línea de comandos de la utilería porque son determinantes para la afectación a la información de la base de datos.
 * @author Sergio Flores
 */
public class SUtilFixVacationTaking {
    
    public static final int YEARS = 50;

    /**
     * Ejecuta la utilería para reacomodar cronológicamente los aniversarios de la toma de vacaciones gozadas y ajustadas de empleados activos.
     * @param args Argumnetos de línea de comandos:
     * 1) DBMS host;
     * 2) DBMS port;
     * 3) base de datos de la empresa;
     * 4) número de ejecución de la utilería en el mismo día (para diferenciar los cambios a la base de datos que se hagan en cada reacomodo.)
     */
    public static void main(String[] args) {
        System.out.println("*** Utilería para reacomodar por aniversario la toma de vacaciones ***");
        
        /*
        Algoritmo:

        1. Recuperar el conjunto de resultados de días de vacaciones otorgadas por la empresa por aniversario (tope de 50 aniversarios).
        2. Recuperar y recorrer el conjunto de resultados del resumen de días de vacaciones tomadas por empleado.
            2.1. Iniciar una transacción del DBMS para el empleado actual.
            2.2. Recuperar y recorrer conjunto de resultados del detalle de movimientos de recibos de nómina de vacaciones tomadas del empleado actual.
                2.2.1. Reacomodar las vacaciones tomadas del empleado actual.
            2.3. Completar la transacción del DBMS del empleado actual.
        */
        
        SDbDatabase database = null;
        Statement statement = null;
        boolean trxActive = false;
        
        try {
            // Argumentos para la ejecución de la utilería:
            
//            String argDbmsHost = "";
//            String argDbmsPort = "";
//            String argDbName = "";
            
//            String argDbmsHost = "192.168.1.60";
//            String argDbmsPort = "3307";
//            String argDbName = "erp_otsa";
            
            String argDbmsHost = "192.168.1.233";
            String argDbmsPort = "3306";
//            String argDbName = "erp_sasa";
//            String argDbName = "erp_otsa";
//            String argDbName = "erp_amesa";
            String argDbName = "erp_aeth";
            
            int argExecNumber = 0;
            
            if (args.length > 0) {
                argDbmsHost = args[0];
            }
            
            if (args.length > 1) {
                argDbmsPort = args[1];
            }
            
            if (args.length > 2) {
                argDbName = args[2];
            }
            
            if (args.length > 3) {
                argExecNumber = SLibUtils.parseInt(args[3]);
            }
            
            LocalDateTime fixDateTime = new LocalDateTime(); // hoy 00:00:00 + número de execution como segundos
            fixDateTime = fixDateTime.withMillisOfDay(argExecNumber);
            
            System.out.println("CLI arguments: <DBMS_host> <DBMS_port> <DB_name> <execution_number>");
            System.out.println("DBMS host & port: " + argDbmsHost + ":" + argDbmsPort);
            System.out.println("DB name: " + argDbName);
            System.out.println("Execution number: " + argExecNumber);
            System.out.println("Fix datetime: " + fixDateTime.toString());
            System.out.print("Continue? (Y/N) ");
            
            byte[] input = new byte[256];
            System.in.read(input);
            String option = new String(input);
            if (option.isEmpty() || option.toUpperCase().charAt(0) != 'Y') {
                System.exit(0);
            }
            
            // Crear conexión a la base de datos de la empresa y sesión dummy de usuario:
            
            database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            database.connect(argDbmsHost, argDbmsPort, argDbName, "root", "msroot");
            statement = database.getConnection().createStatement();
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);
            session.setUser(new SGuiUser() {

                @Override
                public int getPkUserId() {
                    return SDataConstantsSys.USRX_USER_SUPER;
                }

                @Override
                public String getName() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public int getFkUserTypeId() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean isAdministrator() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean isSupervisor() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean hasModuleAccess(int module) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean hasPrivilege(int privilege) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean hasPrivilege(int[] privileges) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public int getPrivilegeLevel(int privilege) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public HashMap<Integer, Integer> getPrivilegesMap() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public HashSet<Integer> getModulesSet() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void computeAccess(SGuiSession session) throws SQLException, Exception {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public SGuiSessionCustom createSessionCustom(SGuiClient client) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public SGuiSessionCustom createSessionCustom(SGuiClient client, int terminal) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean showUserSessionConfigOnLogin() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            // 1. Recuperar el conjunto de resultados de días de vacaciones otorgadas por la empresa por aniversario (tope de 50 aniversarios).
            
            System.out.println("Recuperando días de vacaciones otorgadas por la empresa por aniversario...");
            
            String sql;
            
            Statement stVac = database.getConnection().createStatement();
            ResultSet rsVac;
            
            int annum = 0;
            int[] comVacDaysArray = new int[YEARS];
            
            sql = "SELECT * "
                    + "FROM hrs_ben_row_aux "
                    + "WHERE id_ben = 1 "
                    + "ORDER BY ann;";
            rsVac = stVac.executeQuery(sql);
            while (rsVac.next()) {
                comVacDaysArray[annum++] = rsVac.getInt("ben_day");
                if (annum == YEARS) {
                    break;
                }
            }
            
            if (annum != YEARS) {
                throw new Exception("El número de aniversarios recuperados de días de vacaciones otorgadas por la empresa " + annum + " no es correcto, deben ser " + YEARS + ".");
            }
            
            // 2. Recuperar y recorrer el conjunto de resultados del resumen de días de vacaciones tomadas por empleado.
            
            System.out.println("Recuperando resumen de días de vacaciones tomadas por empleado...");
            
            int gblMovsCreated = 0;
            int empCount = 0;
            
            sql = "SELECT e.dt_ben AS _emp_dt_ben, b.bp AS _emp, e.id_emp AS _id_emp, SUM(pre.unt_all) AS _vac_taken, SUM(pre.amt_r) AS _vac_payed, COUNT(*) AS _movs "
                    + ""
                    + "FROM hrs_pay AS p "
                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                    + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                    + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = pr.id_emp "
                    + "INNER JOIN erp.hrsu_emp AS e ON e.id_emp = pr.id_emp "
                    + "INNER JOIN hrs_emp_member AS em ON em.id_emp = e.id_emp " // sólo empleados de la empresa
                    + ""
                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del " // sólo movimientos activos de recibos de nómina activos de nóminas activas
                    + "AND pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " " // tipo de prestaciones vacaciones
                    + "AND pre.ben_year >= YEAR(e.dt_ben) " // año de prestaciones en recibos de nóminas acordes al año de prestaciones del empleado
                    + "AND (p.dt_end >= e.dt_ben || p.id_pay = 0) " // fecha final de nómina acorde al año de prestaciones del empleado, incluyendo nómina de ajustes
                    + "AND e.b_act " // sólo empleados activos
                    ///// XXX TESTING XXX
                    //+ "AND e.dt_ben >= '2013-01-01' " // para pruebas
                    //+ "AND e.id_emp = 3274 " // para pruebas
                    ///// XXX TESTING XXX
                    + ""
                    + "GROUP BY e.dt_ben, b.bp, e.id_emp "
                    + "ORDER BY e.dt_ben, b.bp, e.id_emp;";
            rsVac = stVac.executeQuery(sql);
            while (rsVac.next()) {
                empCount++;
                
                /*
                Inicializar 'año ben emp' = dato del registro actual del conjunto de resultados
                Inicializar 'días vacs emp reacomodadas' = 0
                Inicializar 'días vacs emp tomadas' = dato del registro actual del conjunto de resultados
                Inicializar 'días vacs emp tomadas x aniv' = nuevo arreglo
                Inicializar 'pago vacs emp reacomodadas' = 0
                Inicializar 'pago vacs emp tomadas' = dato del registro actual del conjunto de resultados
                Inicializar 'aniv actual' = 0 (como índice)
                */
                Date empBensDate = rsVac.getDate("_emp_dt_ben");
                int empBensYear = new LocalDate(empBensDate).getYear();
                double empVacDaysFixed = 0; // para validar que la totalidad de días tomados del empleado actual sean reacomodados
                double empVacDaysTaken = rsVac.getDouble("_vac_taken");
                double[] empVacDaysTakenArray = new double[comVacDaysArray.length];
                double empVacPaymtFixed = 0; // para validar que la totalidad del pago del empleado actual sea reacomodado
                double empVacPaymtTaken = rsVac.getDouble("_vac_payed");
                int empMovsOriginal = rsVac.getInt("_movs");
                int empMovsCount = 0;
                int empMovsCreated = 0;
                annum = 0;
                        
                // Mostrar inicio del procesamiento del empleado actual:
                
                String emp = rsVac.getString("_emp");
                int empId = rsVac.getInt("_id_emp");
                
                System.out.println("\n" + empCount + ". "
                        + "Inicia empleado: " + emp + "; "
                        + "ID: " + empId + "; "
                        + "fecha prestaciones: " + SLibUtils.DateFormatDate.format(empBensDate) + "; "
                        + "año prestaciones: " + empBensYear + "; "
                        + "días vacaciones tomadas: " + empVacDaysTaken + "; "
                        + "pago vacaciones tomadas: $" + SLibUtils.getDecimalFormatAmount().format(empVacPaymtTaken) + "; "
                        + "movimientos originales: " + empMovsOriginal + "...");
                
                // 2.1. Iniciar una transacción del DBMS para el empleado actual.

                System.out.println(" - Inicia transacción DBMS del empleado actual.");

                statement.execute("START TRANSACTION;");
                trxActive = true;
                
                // 2.2. Recuperar y recorrer conjunto de resultados del detalle de movimientos de recibos de nómina de vacaciones tomadas del empleado actual.
                // (Las vacaciones ajustadas vienen primero, las gozadas después.)
                
                Statement stMoves = database.getConnection().createStatement();
                ResultSet rsMoves;
                
                int movCount = 0;
                
                sql = "SELECT b.bp AS _emp, e.id_emp AS _id_emp, "
                        + "YEAR(e.dt_ben) AS _emp_year, e.dt_ben AS _emp_dt_ben, e.dt_hire AS _emp_dt_hire, e.dt_dis_n AS _emp_dt_dis, "
                        + "YEAR(pr.dt_ben) AS _rcp_year, pr.dt_ben AS _rcp_dt_ben, pr.dt_hire AS _rcp_dt_hire, pr.dt_dis_n AS _rcp_dt_dis, "
                        + "p.id_pay <> 0 AS _pay_real, p.dt_sta AS _pay_sta, p.dt_end AS _pay_end, "
                        + "pre.id_pay, pre.id_emp, pre.id_mov "
                        + ""
                        + "FROM hrs_pay AS p "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                        + "INNER JOIN erp.hrsu_emp AS e ON e.id_emp = pr.id_emp "
                        + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = pr.id_emp "
                        + "INNER JOIN hrs_emp_member AS em ON em.id_emp = e.id_emp " // sólo empleados de la empresa
                        + ""
                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del " // sólo movimientos activos de recibos de nómina activos de nóminas activas
                        + "AND pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " " // tipo de prestaciones vacaciones
                        + "AND pre.ben_year >= YEAR(e.dt_ben) " // año de prestaciones en recibos de nóminas acordes al año de prestaciones del empleado
                        + "AND (p.dt_end >= e.dt_ben || p.id_pay = 0) " // fecha final de nómina acorde al año de prestaciones del empleado, incluyendo nómina de ajustes
                        + "AND e.id_emp = " + empId + " " // sólo el empleado actual
                        + ""
                        + "ORDER BY e.dt_ben, b.bp, e.id_emp, p.id_pay <> 0, pr.dt_ben, pre.id_pay, pre.ben_year, pre.ben_ann, pre.id_mov;";
                rsMoves = stMoves.executeQuery(sql);
                while (rsMoves.next()) {
                    movCount++;
                    
                    /*
                    Sea:
                        'mov': registro de base de datos de un movimiento de recibo de nómina.
                        'mov original': el 'mov' con los datos originales de la base de datos.
                        'mov actual': el 'mov original' o una copia del mismo con el remanente de vacaciones tomadas por reacomodar.
                    
                    Lógica general:
                        Si en el 'mov original' alguno de los siguientes datos no son adecuados: 'días vacs tomadas' (unt_all), año y aniversario prestaciones (ben_year y ben_ann), entonces:
                            Hacer una copia del 'mov original', y asignarla al 'mov actual'.
                            Eliminar el 'mov original' con el fin de preservar sus datos sin alterarlos, marcándolo como MODIFICADO (update) en fecha 1 de enero del año de procesamiento, a las 0 hr, por el superusuario.
                            Guardar subsecuentes copias del 'movimiento orignal', las necesariAs, marcándolas como CREADAS (insertion) en fecha 1 de enero del año de procesamiento, a las 0 hr, por el superusuario.
                    */
                    
                    // 2.2.1. Reacomodar las vacaciones tomadas del empleado actual.
                    
                    /*
                    Inicializar 'mov actual' = null
                    Inicializar 'mov original' = registro actual del conjunto de resultados
                    Inicializar 'días vacs mov actual reacomodadas' = 0
                    Inicializar 'días vacs mov actual tomadas' = 'mov original'.'días vacs tomadas'
                    Inicializar 'pago vacs mov actual reacomodadas' = 0
                    Inicializar 'pago vacs mov actual tomadas' = 'mov original'.'pago vacs tomadas'
                    */
                    SDbPayrollReceiptEarning movActual = null;
                    SDbPayrollReceiptEarning movOriginal = new SDbPayrollReceiptEarning();
                    movOriginal.read(session, new int[] { rsMoves.getInt("pre.id_pay"), rsMoves.getInt("pre.id_emp"), rsMoves.getInt("pre.id_mov") });
                    double movVacDaysFixed = 0; // para validar que la totalidad de días tomados del movimiento actual sean reacomodados
                    double movVacDaysTaken = movOriginal.getUnitsAlleged();
                    double movVacPaymtFixed = 0; // para validar que la totalidad del pago del movimiento actual sea reacomodado
                    double movVacPaymtTaken = movOriginal.getAmount_r();
                    
                    boolean doUpdateMovOriginal = false;
                    boolean isMovOriginalUpdated = false;
                    double movVacPaymtUnit = movOriginal.getAmountUnitary();
                    double ratVacDays = movVacDaysTaken / movOriginal.getUnits();
                    int movMovsCreated = 0;
                    
                    // Mostrar inicio del procesamiento del movimiento actual:
                    System.out.println(" " + empCount + "." + movCount + ". "
                            + "Inicia movimiento ID: " + SLibUtils.textKey(movOriginal.getPrimaryKey()) + "; "
                            + "días por reacomodar: " + movVacDaysTaken + "; "
                            + "aniversario actual: " + (annum + 1) + "; "
                            + "días acumulados aniversario: " + empVacDaysTakenArray[annum] + " de " + comVacDaysArray[annum] + "...");
                    
                    // Mientras 'días vacs mov actual reacomodadas' < 'días vacs mov actual tomadas':
                    while ((movVacDaysTaken >= 0 && movVacDaysFixed < movVacDaysTaken) || (movVacDaysFixed > movVacDaysTaken)) {
                        /*
                        Inicializar 'días vacs mov actual x reacomodar' = 'días vacs mov actual tomadas' - 'días vacs mov actual reacomodadas'
                        Inicializar 'días vacs aniv actual x gozar' = 'días vacs empresa x aniv'['aniv actual'] - 'días vacs emp tomadas x aniv'['aniv actual']
                        */
                        double movVacDaysToFix = movVacDaysTaken - movVacDaysFixed;
                        double curVacDaysToEnjoy = comVacDaysArray[annum] - empVacDaysTakenArray[annum];
                        
                        int curBensYear = empBensYear + annum; // 'annum' inicia en cero
                        int curBensAnniv = annum + 1;
                        boolean doFixVacDays = false;
                        double auxVacDaysToFix = 0;
                        
                        // Si 'días vacs mov actual x reacomodar' <= 'días vacs aniv actual x gozar', entonces:
                        if (movVacDaysToFix <= curVacDaysToEnjoy) {
                            // no es necesario reacomodar los días de vacaciones tomados del movimiento actual
                            doFixVacDays = false;
                            
                            // Si 'mov original' no se ha modificado:
                            if (!isMovOriginalUpdated) {
                                // Si datos del 'mov original' son correctos:
                                if (movOriginal.getBenefitsYear() != curBensYear || movOriginal.getBenefitsAnniversary() != curBensAnniv) {
                                    // es necesario modificar el 'mov original' (año y aniversario prestaciones no son correctos)
                                    doUpdateMovOriginal = true;
                                }
                                else {
                                    // no es necesario modificar el 'mov original' (año y aniversario prestaciones son correctos)
                                    doUpdateMovOriginal = false;
                                    empMovsCount++;
                                }
                            }
                            
                            // determinar 'días vacs x reacomodar':
                            auxVacDaysToFix = movVacDaysToFix;
                        }
                        else {
                            // es necesario reacomodar los días de vacaciones tomados del movimiento actual
                            doFixVacDays = true;
                            
                            // Si movimiento original no se ha modificado:
                            if (!isMovOriginalUpdated) {
                                // es necesario modificar el 'mov original' (días y monto pagado de vacaciones tomadas no son correctos)
                                doUpdateMovOriginal = true;
                            }
                            
                            // determinar 'días vacs x reacomodar':
                            auxVacDaysToFix = curVacDaysToEnjoy;
                        }
                        
                        if (doUpdateMovOriginal) {
                            /*
                            Asignar 'mov actual' = nueva copia de 'mov original'
                            Adecuar 'mov actual': 'bandera es registro nuevo' = true; actualizar insert user ID & TS; limpiar update user ID & TS
                            */
                            movActual = movOriginal.clone();
                            movActual.setRegistryNew(true);
                            movActual.setCustomTsUserInsert(fixDateTime.toDate());
                            movActual.setCustomTsUserUpdate(fixDateTime.toDate());

                            /*
                            Adecuar 'mov original': 'bandera borrado' = true; actualizar update user ID & TS
                            Guardar 'mov original' (como registro eliminado)
                            */
                            movOriginal.setDeleted(true);
                            movOriginal.setCustomTsUserUpdate(fixDateTime.toDate());
                            movOriginal.save(session);
                            
                            isMovOriginalUpdated = true;
                            doUpdateMovOriginal = false;
                        }
                        
                        double units;
                        double amount;
                        
                        if (doFixVacDays) {
                            units = auxVacDaysToFix * ratVacDays;
                            amount = SLibUtils.roundAmount(units * movVacPaymtUnit);
                        }
                        else {
                            units = 0;
                            amount = movActual != null ? movActual.getAmount_r() : movOriginal.getAmount_r();
                        }
                        
                        // Si 'mov actual' <> null, entonces:
                        if (movActual != null) {
                            /*
                            Adecuar 'mov actual': año y aniversario prestaciones; días y monto pagado de vacaciones tomadas, si es necesario
                            Guardar 'mov actual' (como registro nuevo)
                            */
                            
                            movActual.setBenefitsYear(curBensYear);
                            movActual.setBenefitsAnniversary(curBensAnniv);
                            
                            if (doFixVacDays) {
                                movActual.setUnitsAlleged(auxVacDaysToFix);
                                movActual.setUnits(units);
                                
                                movActual.setAmountSystem_r(amount);
                                movActual.setAmount_r(amount);
                                movActual.setAmountTaxable(amount);
                            }
                            
                            movActual.save(session);
                            
                            empMovsCount++;
                            empMovsCreated++;
                            movMovsCreated++;
                        }

                        /*
                        Asignar 'días vacs mov actual reacomodadas' += 'días vacs x reacomodar'
                        Asignar 'días vacs emp reacomodadas' += 'días vacs x reacomodar'
                        Asignar 'días vacs emp tomadas x aniv'['aniv actual'] += 'días vacs x reacomodar'
                        Asignar 'pago vacs mov actual reacomodadas' += 'pago vacs x reacomodar'
                        Asignar 'pago vacs emp reacomodadas' += 'pago vacs x reacomodar'
                        */
                        movVacDaysFixed += auxVacDaysToFix; // así se finalizará este 'while'
                        empVacDaysFixed += auxVacDaysToFix; // 
                        empVacDaysTakenArray[annum] += auxVacDaysToFix;
                        movVacPaymtFixed = SLibUtils.roundAmount(movVacPaymtFixed + amount);
                        empVacPaymtFixed = SLibUtils.roundAmount(empVacPaymtFixed + amount);
                        
                        // Si faltan días de vacacioes tomadas por reacomodar en el aniversario actual, entonces:
                        if (movVacDaysFixed < movVacDaysTaken) {
                            /*
                            Asignar 'mov actual' = nueva copia de 'mov actual'
                            Adecuar 'mov actual': 'bandera es registro nuevo' = true; días y monto pagado de vacaciones tomadas
                            */
                            movActual = movActual.clone();
                            movActual.setRegistryNew(true);
                            
                            auxVacDaysToFix = movVacDaysTaken - movVacDaysFixed;
                            units = auxVacDaysToFix * ratVacDays;
                            amount = SLibUtils.roundAmount(movVacPaymtTaken - movVacPaymtFixed);
                            
                            movActual.setUnitsAlleged(auxVacDaysToFix);
                            movActual.setUnits(units);

                            movActual.setAmountSystem_r(amount);
                            movActual.setAmount_r(amount);
                            movActual.setAmountTaxable(amount);
                        }
                        // Si no, si se reacomodaron días de más:
                        else if (movVacDaysFixed > movVacDaysTaken) {
                            throw new Exception("Se excedió el número de días a reacomodar del movimiento ID " + SLibUtils.textKey(movOriginal.getPrimaryKey()) + " de " + emp + ": " + movVacDaysFixed + " en vez de " + movVacDaysTaken + ".");
                        }
                        else if (!SLibUtils.compareAmount(movVacPaymtFixed, movVacPaymtTaken)) {
                            throw new Exception("Se desvirtuó el monto de pago a reacomodar del movimiento ID " + SLibUtils.textKey(movOriginal.getPrimaryKey()) + " de " + emp + ": $" + SLibUtils.getDecimalFormatAmount().format(movVacPaymtFixed) + " en vez de $" + SLibUtils.getDecimalFormatAmount().format(movVacPaymtTaken) + ".");
                        }
                        // Si no, si ya se reacomodaron todos los días:
                        else {
                            // Mostrar fin del procesamiento del movimiento actual:
                            System.out.println(" - "
                                    + "Termina movimiento ID: " + SLibUtils.textKey(movOriginal.getPrimaryKey()) + "; "
                                    + "días reacomodados: " + movVacDaysFixed + "; "
                                    + "aniversario actual: " + (annum + 1) + "; "
                                    + "días acumulados aniversario: " + empVacDaysTakenArray[annum] + " de " + comVacDaysArray[annum] + "; "
                                    + "estatus movimiento: " + (movMovsCreated == 0 ? "¡sin cambios!" : "reemplazado por " + movMovsCreated + " " + (movMovsCreated == 1 ? "movimiento" : "movimientos") + "."));
                        }
                        
                        // Si 'días vacs emp tomadas x aniv'['aniv actual'] == 'días vacs empresa x aniv'['aniv actual'], entonces:
                        if (empVacDaysTakenArray[annum] == comVacDaysArray[annum]) {
                            /*
                            Asignar 'aniv actual' += 1 // como índice
                            */
                            annum++;
                            
                            // Mostrar aniversario actual:
                            System.out.println(" - Aniversario actual: " + (annum + 1) + "...");
                            
                            // Si 'aniv actual' == 50 aniversarios, entonces:
                            if (annum == YEARS) {
                                throw new Exception("Se sobrepasó el número máximo de aniversarios a procesar de " + emp + ", el tope es " + YEARS + ".");
                            }
                        }
                        // Si no, si 'días vacs emp tomadas x aniv'['aniv actual'] > 'días vacs empresa x aniv'['aniv actual'], entonces:
                        else if (empVacDaysTakenArray[annum] > comVacDaysArray[annum]) {
                            throw new Exception("Se excedió el número de días a reacomodar del aniversario " + (annum + 1) + " de " + emp + ": " + empVacDaysTakenArray[annum] + " en vez de " + comVacDaysArray[annum] + ".");
                        }
                    }
                }
                
                // Validar las vacaciones tomadas reacomodades del empleado actual:
                if (empVacDaysFixed != empVacDaysTaken) {
                    throw new Exception("Los días reacomodados de " + emp + " fueron " + empVacDaysFixed + ", pero los originales son " + empVacDaysTaken + ".");
                }
                else if (!SLibUtils.compareAmount(empVacPaymtFixed, empVacPaymtTaken)) {
                    throw new Exception("El pago reacomodado de " + emp + " fue $" + SLibUtils.getDecimalFormatAmount().format(empVacPaymtFixed) + ", pero el original es $" + SLibUtils.getDecimalFormatAmount().format(empVacPaymtTaken) + ".");
                }
                
                // 2.3. Completar la transacción del DBMS del empleado actual.

                System.out.println(" - Compleción transacción DBMS del empleado actual.");
                statement.execute("COMMIT;");
                trxActive = false;
                
                // Mostrar fin del procesamiento del empleado actual:
                System.out.println("- "
                        + "Termina empleado: " + emp + "; "
                        + "ID: " + empId + "; "
                        + "días vacaciones reacomodadas: " + empVacDaysFixed + "; "
                        + "pago vacaciones reacomodado: $" + SLibUtils.getDecimalFormatAmount().format(empVacPaymtFixed) + "; "
                        + "movimientos nuevos: " + (empMovsCreated == 0 ? "¡ninguno!" : empMovsCreated) + "; "
                        + "movimientos actuales: " + empMovsCount + ".");
                
                gblMovsCreated += empMovsCreated;
            }
            
            System.out.println("\nTotal movimientos nuevos: " + (gblMovsCreated == 0 ? "¡ninguno!" : gblMovsCreated) + ".");
        }
        catch (Exception e) {
            SLibUtils.printException(SUtilFixVacationTaking.class.getName(), e);
            
            if (trxActive && database != null && database.isConnected()) {
                try {
                    System.out.println("Revirtiendo última transacción DBMS...");
                    database.getConnection().createStatement().execute("ROLLBACK;");
                }
                catch (Exception e1) {
                    SLibUtils.printException(SUtilFixVacationTaking.class.getName(), e1);
                }
            }
        }
    }
}
