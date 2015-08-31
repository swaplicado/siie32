/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataSqlUtilities {

    public static java.lang.String composeFilter(java.lang.String field, java.util.Vector<Integer> keys) {
        int i = 0;
        String sql = "";

        for (i = 0; i < keys.size(); i++) {
            sql += (sql.length() == 0 ? "" : ", ") + keys.get(i);
        }

        sql = field + " IN (" + sql + ") ";

        return sql;
    }

    public static java.lang.String composeFilter(java.lang.String[] fields, java.util.Vector<int[]> keys) {
        int i = 0;
        int j = 0;
        String sql = "";
        String sqlAux = "";

        for (i = 0; i < keys.size(); i++) {
            sqlAux = "";
            for (j = 0; j < fields.length; j++) {
                sqlAux += (sqlAux.length() == 0 ? "" : " AND ") + fields[j] + " = " + keys.get(i)[j];
            }

            sql += (sql.length() == 0 ? "" : " OR ") + "(" + sqlAux + ")";
        }

        sql = "(" + sql + ") ";

        return sql;
    }

    public static java.lang.String composePeriodFilter(final int[] period, final java.lang.String field) {
        String filter = "";

        switch (period.length) {
            case 1:
                filter = "YEAR(" + field + ") = " + period[0] + " ";
                break;
            case 2:
                filter = "YEAR(" + field + ") = " + period[0] + " AND MONTH(" + field + ") = " + period[1] + " ";
                break;
            case 3:
                filter = "YEAR(" + field + ") = " + period[0] + " AND MONTH(" + field + ") = " + period[1] + " AND DAY(" + field + ") = " + period[2] + " ";
                break;
            default:
        }

        return filter;
    }
}
