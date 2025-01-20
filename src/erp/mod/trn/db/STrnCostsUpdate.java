package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnUtilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Actualización de costos de ítems en el inventario de acuerdo a archivo fuente
 * CSV.
 *
 * @author Sergio Flores
 */
public class STrnCostsUpdate {

    public static final int MOMENT_START = 1;
    public static final int MOMENT_END = 2;
    public static final String SERIES = "COSTSUPD";

    protected SGuiSession moSession;
    protected int mnYear;
    protected int mnMonth;
    protected int mnMoment;
    protected String msFileCsvPath;
    protected int mnCsvLines;
    protected int mnCostsUpdates;

    /**
     * Instancia objeto para la actualización inmediata de costos de ítems en el
     * inventario de acuerdo a archivo fuente CSV.
     *
     * @param session Sesión de usuario.
     * @param year Año para generar movimientos de almacén que ajusten sólo los
     * costos deseados.
     * @param month Mes para generar movimientos de almacén que ajusten sólo los
     * costos deseados.
     * @param moment Momento del mes para generar movimientos de almacén que
     * ajusten sólo los costos deseados: al inicio de mes
     * (STrnCostsUpdate.MOMENT_START), al final de mes
     * (STrnCostsUpdate.MOMENT_END).
     * @param fileCsvPath Ruta del archivo fuente CSV, con los datos: Clave
     * ítem, Nombre ítem, Clave unidad de medida, Costo deseado.
     */
    public STrnCostsUpdate(SGuiSession session, int year, int month, int moment, String fileCsvPath) throws Exception {
        moSession = session;
        mnYear = year;
        mnMonth = month;
        mnMoment = moment;
        msFileCsvPath = fileCsvPath;
        mnCsvLines = 0;
        mnCostsUpdates = 0;
    }

    public SGuiSession getSession() {
        return moSession;
    }

    public int getYear() {
        return mnYear;
    }

    public int getMonth() {
        return mnMonth;
    }

    public int getMoment() {
        return mnMoment;
    }

    public String getFileCsvPath() {
        return msFileCsvPath;
    }

    /**
     * Actualización de costos de ítems en el inventario de acuerdo a archivo
     * fuente CSV.
     *
     * @return <code>SDataBookkeepingNumber</code> con el que se registraron
     * todos los ajustes a los costos, tanto en documentos de almacén como en
     * los inventarios. Si no hay costos a actualizar devuelve
     * <code>null</code>.
     * @throws Exception
     */
    public SDataBookkeepingNumber updateCosts() throws Exception {
        HashMap<String, SDataDiog> diogs = new HashMap<>();
        int unitDecs = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();

        // leer archivo CSV, con los datos: Clave ítem, Nombre ítem, Clave unidad de medida, Costo deseado:
        FileReader fr = new FileReader(msFileCsvPath);
        ArrayList<CsvLine> csvLines;

        try (BufferedReader br = new BufferedReader(fr)) {
            csvLines = new ArrayList<>();
            String csvLine = br.readLine();
            while (csvLine != null) {
                csvLines.add(new CsvLine(csvLine));
                csvLine = br.readLine();
            }
        }

        // definir fecha de corte para generar movimientos de almacén que ajusten sólo los costos deseados:
        Date cutOff = SLibTimeUtils.createDate(mnYear, mnMonth);

        switch (mnMoment) {
            case MOMENT_START:
                cutOff = SLibTimeUtils.getBeginOfMonth(cutOff);
                break;
            case MOMENT_END:
                cutOff = SLibTimeUtils.getEndOfMonth(cutOff);
                break;
            default:
        }

        // procesar todos los costos de los ítems del archivo CSV:
        Statement statementCsv = moSession.getStatement().getConnection().createStatement();
        Statement statementEntity = moSession.getStatement().getConnection().createStatement();
        Statement statementEntityDef = moSession.getStatement().getConnection().createStatement();
        Statement statementCost = moSession.getStatement().getConnection().createStatement();

        SDataBookkeepingNumber bookkeepingNumber = new SDataBookkeepingNumber();
        bookkeepingNumber.setPkYearId(mnYear);
        //bookkeepingNumber.setPkNumberId(...);
        //bookkeepingNumber.setIsDeleted(...);
        bookkeepingNumber.setFkUserNewId(moSession.getUser().getPkUserId());
        //bookkeepingNumber.setFkUserEditId(...);
        //bookkeepingNumber.setFkUserDeleteId(...);
        //bookkeepingNumber.setUserNewTs(...);
        //bookkeepingNumber.setUserEditTs(...);
        //bookkeepingNumber.setUserDeleteTs(...);
        bookkeepingNumber.save(moSession.getStatement().getConnection());

        mnCsvLines = 0;
        mnCostsUpdates = 0;
        boolean withCost = false;
        for (CsvLine csvLine : csvLines) {
            System.out.println(++mnCsvLines + ". Procesando ítem '" + csvLine.ItemName + " - " + csvLine.ItemCode + "', unidad '" + csvLine.UnitCode + "'...");

            int ocurrences = 0;
            boolean found = false;

            String sqlCsv = "SELECT DISTINCT g.fid_ct_item, g.fid_cl_item, g.fid_tp_item, s.id_item, s.id_unit, u.symbol "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = s.id_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS g ON g.id_igen = i.fid_igen "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON u.id_unit = s.id_unit "
                    + "WHERE NOT s.b_del "
                    + "AND s.id_year = " + mnYear + " "
                    + "AND s.dt " + ((mnMonth == 1 || mnMoment == MOMENT_END) ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(cutOff) + "' "
                    + "AND i.item_key = '" + csvLine.ItemCode + "' AND u.symbol = '" + csvLine.UnitCode + "' "
                    + "ORDER BY g.fid_ct_item, g.fid_cl_item, g.fid_tp_item, s.id_item, s.id_unit, u.symbol;";
            ResultSet resultSetCsv = statementCsv.executeQuery(sqlCsv);
            while (resultSetCsv.next()) {
                if (!csvLine.UnitCode.equals(resultSetCsv.getString("u.symbol"))) { // prevent from mistaking 'kg' with 'KG'
                    System.out.println(" - descartando ocurrencia de existencias para la unidad '" + resultSetCsv.getString("u.symbol") + "'!");
                    continue;
                }

                found = true;

                if (++ocurrences > 1) {
                    // sólo debería haber una única coincidencia en inventarios para la dupla conformada por las claves de ítem y unidad:
                    throw new Exception("El ítem '" + csvLine.ItemName + " - " + csvLine.ItemCode + ")', unidad '" + csvLine.UnitCode + "', existe más de una vez en inventarios en " + mnYear + " al " + SLibUtils.DateFormatDate.format(cutOff) + "!");
                }

                csvLine.ItemId = resultSetCsv.getInt("s.id_item");
                csvLine.UnitId = resultSetCsv.getInt("s.id_unit");
                System.out.println(" - ID ítem: " + csvLine.ItemId + "; ID unidad: " + csvLine.UnitId + ".");

                // buscar el almacén apropiado para realizar los ajustes de costo según el tipo de ítem del ítem actual:
                int[] itemTypeKey = new int[] { resultSetCsv.getInt("g.fid_ct_item"), resultSetCsv.getInt("g.fid_cl_item"), resultSetCsv.getInt("g.fid_tp_item") };
                int[] entityKey = null;
                int[][] entityTypeKeys = null;

                if (SLibUtils.belongsTo(itemTypeKey, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD, SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP, SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMI })) {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM };
                }
                else if (SLibUtils.belongsTo(itemTypeKey, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP })) {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
                }
                else if (SLibUtils.belongsTo(itemTypeKey, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG, SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_SP })) {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
                }
                else if (SLibUtils.belongsTo(itemTypeKey, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WA })) {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_MS };
                }
                else if (SLibUtils.belongsTo(itemTypeKey, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_P })) {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
                }
                else {
                    entityTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
                }

                for (int[] entityTypeKey : entityTypeKeys) {
                    String sqlEntity = "SELECT DISTINCT s.id_cob, s.id_wh "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS e ON e.id_cob = s.id_cob AND e.id_ent = s.id_wh "
                            + "WHERE NOT s.b_del "
                            + "AND s.id_year = " + mnYear + " "
                            + "AND s.dt " + ((mnMonth == 1 || mnMoment == MOMENT_END) ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(cutOff) + "' "
                            + "AND s.id_item = " + csvLine.ItemId + " AND s.id_unit = " + csvLine.UnitId + " "
                            + "AND e.fid_ct_ent = " + entityTypeKey[0] + " AND e.fid_tp_ent = " + entityTypeKey[1] + " "
                            + "ORDER BY s.id_cob, s.id_wh;";
                    ResultSet resultSetEntity = statementEntity.executeQuery(sqlEntity);
                    if (resultSetEntity.next()) {
                        entityKey = new int[] { resultSetEntity.getInt("s.id_cob"), resultSetEntity.getInt("s.id_wh") };
                        break;
                    }
                }

                // no se encontró el almacén apropiado, buscar entonces el almacén predeterminado o, de plano, cualquier otro con movimientos del ítem actual:
                if (entityKey == null) {
                    String sqlEntityDef = "SELECT DISTINCT s.id_cob, s.id_wh, e.b_def "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS e ON e.id_cob = s.id_cob AND e.id_ent = s.id_wh "
                            + "WHERE NOT s.b_del "
                            + "AND s.id_year = " + mnYear + " "
                            + "AND s.dt " + ((mnMonth == 1 || mnMoment == MOMENT_END) ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(cutOff) + "' "
                            + "AND s.id_item = " + csvLine.ItemId + " AND s.id_unit = " + csvLine.UnitId + " "
                            + "ORDER BY e.b_def DESC, s.id_cob, s.id_wh;";
                    ResultSet resultSetEntityDef = statementEntityDef.executeQuery(sqlEntityDef);
                    if (resultSetEntityDef.next()) {
                        entityKey = new int[] { resultSetEntityDef.getInt("s.id_cob"), resultSetEntityDef.getInt("s.id_wh") };
                    }

                    if (entityKey == null) {
                        // no se encontró ningún almacén para actualizar el costo del ítem actual:
                        throw new Exception("No se encontró ningún almacén para actualizar el costo del ítem '" + csvLine.ItemName + " - " + csvLine.ItemCode + ")', unidad '" + csvLine.UnitCode + "', en " + mnYear + " al " + SLibUtils.DateFormatDate.format(cutOff) + "!");
                    }
                }

                csvLine.WarehouseCompanyBranchId = entityKey[0];
                csvLine.WarehouseEntityId = entityKey[1];
            }

            if (!found) {
                System.out.println(" - el ítem no tiene movimientos de almacén en " + mnYear + " al " + SLibUtils.DateFormatDate.format(cutOff) + "!");
            }
            else {
                // consultar existencias y saldo en inventarios del ítem actual:
                double stock = 0;
                double balance = 0;

                String sqlCost = "SELECT SUM(s.mov_in - s.mov_out) AS _stock, SUM(s.debit - s.credit) AS _balance "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                        + "WHERE NOT s.b_del "
                        + "AND s.id_year = " + mnYear + " "
                        + "AND s.dt " + ((mnMonth == 1 || mnMoment == MOMENT_END) ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(cutOff) + "' "
                        + "AND s.id_item = " + csvLine.ItemId + " AND s.id_unit = " + csvLine.UnitId + ";";
                ResultSet resultSetCost = statementCost.executeQuery(sqlCost);
                if (resultSetCost.next()) {
                    stock = resultSetCost.getDouble("_stock");
                    balance = resultSetCost.getDouble("_balance");
                }

                double currentUnitCost = SLibUtils.round(stock == 0 ? 0 : balance / stock, unitDecs);

                System.out.println(" - existencia actual: " + SLibUtils.getDecimalFormatAmountUnitary().format(stock) + " " + csvLine.UnitCode + "; "
                        + "saldo actual: $" + SLibUtils.getDecimalFormatAmount().format(balance) + "; "
                        + "costo unitario actual $" + SLibUtils.getDecimalFormatAmountUnitary().format(currentUnitCost) + "; "
                        + "costo unitario requerido $" + SLibUtils.getDecimalFormatAmountUnitary().format(csvLine.UnitCost) + ".");

                if (stock <= 0) {
                    System.out.println(" - el ítem no tiene existencias en " + mnYear + " al " + SLibUtils.DateFormatDate.format(cutOff) + "!");
                }
                else if (SLibUtils.compareAmount(currentUnitCost, csvLine.UnitCost)) {
                    System.out.println(" - el costo unitario actual del ítem en " + mnYear + " al " + SLibUtils.DateFormatDate.format(cutOff) + " es igual al costo unitario requerido!");
                }
                else {
                    // actualizar costo unitario requerido:

                    int[] diogTypeKey = null;
                    boolean isIncrement = currentUnitCost < csvLine.UnitCost; // convenience variable
                    double unitGap = csvLine.UnitCost - currentUnitCost;
                    double adjustment = SLibUtils.roundAmount(SLibUtils.roundAmount(stock * csvLine.UnitCost) - balance);

                    if (isIncrement) {
                        diogTypeKey = SDataConstantsSys.TRNS_TP_IOG_IN_EXP_PUR; // to allow a value (the adjustment) without ongoing units
                    }
                    else {
                        diogTypeKey = SDataConstantsSys.TRNS_TP_IOG_OUT_EXP_PUR; // to allow a value (the adjustment) without ongoing units
                        unitGap = -unitGap;
                        adjustment = SLibUtils.roundAmount(-adjustment);
                    }

                    if (SLibUtils.roundAmount(adjustment) < 0.01) {
                        System.out.println(" - el ajuste al costo de $" + SLibUtils.getDecimalFormatAmount().format(adjustment) + ", es menor que $0.01, no tiene sentido hacerlo!");
                    }
                    else {
                        System.out.println(" - diferencia entre costos unitarios: $" + SLibUtils.getDecimalFormatAmountUnitary().format(unitGap) + ", "
                                + "monto del ajuste $" + SLibUtils.getDecimalFormatAmount().format(adjustment) + " a manera de " + (isIncrement ? "INCREMENTO" : "DECREMENTO") + ".");

                        SDataDiogEntry diogEntry = new SDataDiogEntry();

                        diogEntry.setPkYearId(mnYear);
                        //diogEntry.setPkDocId(...);
                        //diogEntry.setPkEntryId(...);
                        diogEntry.setQuantity(0);

                        diogEntry.setValueUnitary(adjustment);
                        //diogEntry.setValue(...);
                        diogEntry.setOriginalQuantity(0);
                        diogEntry.setOriginalValueUnitary(0);
                        //diogEntry.setSortingPosition(...);
                        diogEntry.setIsInventoriable(true);
                        diogEntry.setIsDeleted(false);
                        diogEntry.setFkItemId(csvLine.ItemId);
                        diogEntry.setFkUnitId(csvLine.UnitId);
                        diogEntry.setFkOriginalUnitId(csvLine.UnitId);
                        diogEntry.setFkDpsYearId_n(0);
                        diogEntry.setFkDpsDocId_n(0);
                        diogEntry.setFkDpsEntryId_n(0);
                        diogEntry.setFkDpsAdjustmentYearId_n(0);
                        diogEntry.setFkDpsAdjustmentDocId_n(0);
                        diogEntry.setFkDpsAdjustmentEntryId_n(0);
                        diogEntry.setFkMfgYearId_n(0);
                        diogEntry.setFkMfgOrderId_n(0);
                        diogEntry.setFkMfgChargeId_n(0);
                        diogEntry.setFkMaintAreaId(SModSysConsts.TRN_MAINT_AREA_NA);
                        diogEntry.setFkCostCenterId(SModSysConsts.FIN_CC_NA);
                        diogEntry.setFkUserNewId(moSession.getUser().getPkUserId());
                        diogEntry.setFkUserEditId(SUtilConsts.USR_NA_ID);
                        diogEntry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                        //diogEntry.setUserNewTs(...);
                        //diogEntry.setUserEditTs(...);
                        //diogEntry.setUserDeleteTs(...);

                        STrnStockMove stockMove = new STrnStockMove();

                        stockMove.setPkYearId(mnYear);
                        stockMove.setPkItemId(csvLine.ItemId);
                        stockMove.setPkUnitId(csvLine.UnitId);
                        stockMove.setPkLotId(SUtilConsts.LOT_ID);
                        stockMove.setPkCompanyBranchId(csvLine.WarehouseCompanyBranchId);
                        stockMove.setPkWarehouseId(csvLine.WarehouseEntityId);
                        stockMove.setQuantity(0);
                        stockMove.setValue(adjustment);
                        stockMove.setSerialNumber("");
                        stockMove.setFkMaintUserId_n(0);
                        stockMove.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);

                        diogEntry.getAuxStockMoves().add(stockMove);

                        String mapKey = csvLine.getWarehouseKey() + "-" + diogTypeKey[0]; // organize documents by warehouse PK + movement category
                        SDataDiog diog = diogs.get(mapKey);

                        if (diog == null) {
                            diog = STrnUtilities.createDataDiogSystem((SClientInterface) moSession.getClient(), mnYear, cutOff, csvLine.WarehouseCompanyBranchId, csvLine.WarehouseEntityId, diogTypeKey, SERIES, new Vector<>(), withCost);
                            diog.setExternalBookkeepingNumber(bookkeepingNumber.getPkYearId(), bookkeepingNumber.getPkNumberId());
                            diogs.put(mapKey, diog);
                        }

                        diog.getDbmsEntries().add(diogEntry);
                        mnCostsUpdates++;
                    }
                }
            }
        }

        statementCsv.close();
        statementEntity.close();
        statementEntityDef.close();
        statementCost.close();

        if (!diogs.isEmpty()) {
            // guardar documentos de almacén con los ajustes a los costos unitarios:

            Statement statementSave = moSession.getStatement().getConnection().createStatement();

            try {
                statementSave.execute("START TRANSACTION;");
                for (SDataDiog diog : diogs.values()) {
                    diog.save(moSession.getStatement().getConnection());
                }
                statementSave.execute("COMMIT;");
            }
            catch (Exception e) {
                statementSave.execute("ROLLBACK;");
                throw e;
            }
            statementSave.close();
        }

        return !diogs.isEmpty() ? bookkeepingNumber : null;
    }
    
    public String getUpdateReport() {
        return "Resumen:"
                + "\nLíneas CSV procesadas: " + SLibUtils.DecimalFormatInteger.format(mnCsvLines)
                + "\nCostos actualizados: " + SLibUtils.DecimalFormatInteger.format(mnCostsUpdates);
    }

    protected class CsvLine {

        public String ItemCode;
        public String ItemName;
        public String UnitCode;
        public double UnitCost;

        public int ItemId;
        public int UnitId;
        public int WarehouseCompanyBranchId;
        public int WarehouseEntityId;

        public CsvLine(String fileCsvRow) {
            cropData(fileCsvRow);

            resetExtraData();
        }

        public CsvLine(String itemCode, String itemName, String unitCode, double unitCost) {
            ItemCode = itemCode;
            ItemName = itemName;
            UnitCode = unitCode;
            UnitCost = unitCost;

            resetExtraData();
        }

        private void resetExtraData() {
            ItemId = 0;
            UnitId = 0;
            WarehouseCompanyBranchId = 0;
            WarehouseEntityId = 0;
        }

        /**
         * Recuperar datos desde cadena con valores CSV: Clave ítem, Nombre
         * ítem, Clave unidad de medida, Costo deseado
         *
         * @param fileCsvRow
         */
        public final void cropData(String fileCsvRow) {
            ItemCode = "";
            ItemName = "";
            UnitCode = "";
            UnitCost = 0;

            String values[] = fileCsvRow.split(",");

            if (values != null) {
                if (values.length >= 1) {
                    ItemCode = values[0].replaceAll("\"", "");
                }
                if (values.length >= 2) {
                    ItemName = values[1].replaceAll("\"", "");
                }
                if (values.length >= 3) {
                    UnitCode = values[2].replaceAll("\"", "");
                }
                if (values.length >= 4) {
                    UnitCost = SLibUtils.parseDouble(values[3]);
                }
            }
        }

        public String getWarehouseKey() {
            return WarehouseCompanyBranchId + "-" + WarehouseEntityId;
        }
    }
}
