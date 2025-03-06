/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.account;

import java.text.DecimalFormat;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.gui.bean.SBeanFieldText;

/**
 *
 * @author Sergio Flores
 */
public abstract class SAccountUtils {

    public static final DecimalFormat LevelFormat = new DecimalFormat(SLibUtils.textRepeat("0", SAccountConsts.LENGTH_LEVEL));

    /*
     * Private methods
     */

    /**
     * Finds recursively an account on provided array list of account objects and all of its descendants (children accounts included), on current level, searching it by account code on standard-format.
     * @param accounts Array list of account objects.
     * @param codeStd Account code to find on standard-format.
     * @param level Current level.
     * @return Found account if any, otherwise <code>null</code>.
     */
    private static SAccount findAccountByCodeStd(final ArrayList<SAccount> accounts, final String codeStd, final int level) {
        SAccount found = null;
        String codeLevelStd = "";

        if (level <= SAccountConsts.LEVELS) {
            codeLevelStd = subtractCodeLevelStd(codeStd, level);
            if (SLibUtils.parseInt(codeLevelStd) > 0) {
                found = findAccountByCodeLevelStd(accounts, codeLevelStd);
                if (found != null) {
                    if (found.getCodeStd().compareTo(codeStd) != 0) {
                        found = findAccountByCodeStd(found.getChildren(), codeStd, level + 1);
                    }
                }
            }
        }

        return found;
    }

    /*
     * Public methods
     */

    /**
     * Gets the number of digits on desired level on provided account mask.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting account codes will have the following user-format:
     * <code>"0000-000-00-0"</code>
     * @param mask Account mask.
     * @param level Desired level.
     * @return Number of digits on desired level, otherwise <code>-1</code> when mask is not made up of 8 digits, or desired level is less than 1 or greater than 8.
     */
    public static int getDigits(final int mask, final int level) {
        return (mask < 10000000 || mask > 99999999 || level < 1 || level > SAccountConsts.LEVELS) ? -1 : ((int) (mask / Math.pow(10, SAccountConsts.LEVELS - level))) % 10;
    }

    /**
     * Gets the number of levels on provided account mask.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting account codes will have the following user-format:
     * <code>"0000-000-00-0"</code>
     * @param mask Account mask.
     * @return Number of levels on provided account mask.
     */
    public static int getLevels(final int mask) {
        int deep = 0;
        int levels = 0;

        for (int i = 1; i <= SAccountConsts.LEVELS; i++) {
            deep = getDigits(mask, i);
            if (deep == 0 || deep == -1) {
                break;
            }
            else {
                levels++;
            }
        }

        return levels;
    }

    /**
     * Composes a new account mask based on provided original account mask up to desired level limit.
     * @param mask Original account mask.
     * @param levelLimit Desired level limit of new account mask.
     * @return New account mask.
     */
    public static int composeMask(final int mask, final int levelLimit) {
        return SLibUtils.parseInt(SLibUtils.textLeft("" + mask, levelLimit) + SLibUtils.textRepeat("0", SAccountConsts.LEVELS - levelLimit));
    }

    /**
     * Finds an account only on top level provided array list of account objects (children accounts excluded), searching it by account code-level on standard-format.
     * <strong>Example:</strong>
     * <pre>
     * Let <i>lev</i> be an account code-level on user-format.
     * <code><i>acc</i> = "1110"</code>
     * Its corresponding account code-level on standard-format, is: <code>"001110"</code>.
     * </pre>
     * @param accounts Array list of account objects.
     * @param codeLevelStd Account code-level to find on standard-format.
     * @return Found account if any, otherwise <code>null</code>.
     */
    public static SAccount findAccountByCodeLevelStd(final ArrayList<SAccount> accounts, final String codeLevelStd) {
        SAccount found = null;

        for (SAccount account : accounts) {
            if (codeLevelStd.compareTo(account.getCodeLevelStd()) == 0) {
                found = account;
                break;
            }
        }

        return found;
    }

    /**
     * Finds an account on provided array list of account objects and all of its descendants (children accounts included), searching it by account code on standard-format.
     * <strong>Example:</strong>
     * <pre>
     * Let <i>acc</i> be an account account code on standard-format.
     * <code><i>acc</i> = "00110500000100000200000300000000000000000000000"</code>
     * </pre>
     * To find <i>acc</i> on array list of account objects, first the top level account must be found, and then each subsecuent levels, recursively by matching children accounts.
     * @param accounts Array list of account objects.
     * @param codeStd Account code to find on standard-format.
     * @return Found account if any, otherwise <code>null</code>.
     */
    public static SAccount findAccountByCodeStd(final ArrayList<SAccount> accounts, final String codeStd) {
        return findAccountByCodeStd(accounts, codeStd, 1);
    }

    /**
     * Composes an account code on standard-format.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting account codes will have the following user-format:
     * <code>"0000-000-00-0"</code>
     * <p>
     * Given the account code <code>"1105-001-02-3"</code>, its corresponding account code on standard-format is:
     * <code>"00110500000100000200000300000000000000000000000"</code> (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     * @param codeLevelsUsr Array of account code-levels on user-format.
     * @return Composed account code on standard-format according to provided account mask (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     */
    public static String composeCodeStd(final SBeanFieldText[] codeLevelsUsr) {
        String codeStd = "";

        for (int i = 0; i < SAccountConsts.LEVELS; i++) {
            codeStd += LevelFormat.format(SLibUtils.parseInt(codeLevelsUsr[i].getValue()));
        }

        return codeStd;
    }

    /**
     * Composes minimum posible account code on standard-format.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * Allways minimum posible account code will have the following standard-format:
     * <code>"000000000000000000000000000000000000000000000000"</code>
     * <p>
     * @return Composed minimum posible account code on standard-format according to provided account mask (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     */
    public static String composeCodeStdMin() {
        String codeStd = "";

        for (int i = 1; i <= SAccountConsts.LEVELS; i++) {
            codeStd += LevelFormat.format(0);
        }

        return codeStd;
    }

    /**
     * Composes maximum posible account code on standard-format.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting maximum posible account code will have the following standard-format:
     * <code>"009999000999000099000009000000000000000000000000"</code>
     * <p>
     * @param mask Account mask.
     * @return Composed maximum posible account code on standard-format according to provided account mask (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     */
    public static String composeCodeStdMax(final int mask) {
        int levels = getLevels(mask);
        String codeStd = "";

        for (int i = 1; i <= SAccountConsts.LEVELS; i++) {
            codeStd += LevelFormat.format(i > levels ? 0 : SLibUtils.parseInt(SLibUtils.textRepeat("9", getDigits(mask, i))));
        }

        return codeStd;
    }

    /**
     * Composes default zero-filled account code on user-format.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, the composed default zero-filled account code on user-format is:
     * <code>"0000-000-00-0"</code>
     * <p>
     * @param mask Account mask.
     * @return Composed default zero-filled account code on user-format (<code>String</code> of variable length).
     */
    public static String composeCodeUsrZeros(final int mask) {
        int levels = getLevels(mask);
        String codeUserZeros = "";

        for (int i = 1; i <= levels; i++) {
            codeUserZeros += (!codeUserZeros.isEmpty() ? "-" : "") + SLibUtils.textRepeat("0", getDigits(mask, i));
        }

        return codeUserZeros;
    }

    /**
     * Get length of account code on user-format up to given level.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, and the composed default zero-filled account code on user-format as:
     * <code>"0000-000-00-0"</code>
     * The length of account code on user-format up to 4 level would be 13.
     * <p>
     * @param mask Account mask.
     * @param level Required level.
     * @return Length of account code on user-format up to given level.
     */
    public static int getLengthCodeUsr(final int mask, final int level) {
        int levels = getLevels(mask);
        int levelRequired = level <= levels ? level : levels;
        String codeUserZeros = "";

        for (int i = 1; i <= levelRequired; i++) {
            codeUserZeros += (!codeUserZeros.isEmpty() ? "-" : "") + SLibUtils.textRepeat("0", getDigits(mask, i));
        }

        return codeUserZeros.length();
    }

    /**
     * Composes an account code on user-format.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting account codes will have the following user-format:
     * <code>"0000-000-00-0"</code>
     * @param codeLevelsUsr Array of account code-levels on user-format.
     * @param codeLevelFormatsUsr Array of account code-level user-formats.
     * @return Composed account code on user-format according to provided account code-level user-formats.
     */
    public static String composeCodeUsr(final SBeanFieldText[] codeLevelsUsr, final DecimalFormat[] codeLevelFormatsUsr) {
        String codeUsr = "";

        for (int i = 0; i < SAccountConsts.LEVELS; i++) {
            codeUsr += (codeUsr.isEmpty() ? "" : "-") + codeLevelFormatsUsr[i].format(SLibUtils.parseInt(codeLevelsUsr[i].getValue()));
        }

        return codeUsr;
    }

    /**
     * Converts an account code on standard-format into user-format.
     * <p>
     * <i>Account mask</i> defines user-format guidelines for accounts. SIIE<sup>®</sup> accounts are divided into levels, maximum 8 levels, the latters defined by 6 digits each at the most.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>m</i> be an account mask.
     * <code><i>m</i> = 43210000</code>
     * Defined total number of levels: 4
     * Digits on level 1: 4
     * Digits on level 2: 3
     * Digits on level 3: 2
     * Digits on level 4: 1
     * </pre>
     * Given <i>m</i>, resulting account codes will have the following user-format:
     * <code>"0000-000-00-0"</code>
     * @param mask Account mask.
     * @param codeStd Account code on standard-format to be converted into user-format.
     * @return Converted account code on user-format according to provided account mask.
     */
    public static String convertCodeUsr(final int mask, final String codeStd) {
        int digits = 0;
        int levels = getLevels(mask);
        String level = "";
        String codeUsr = "";

        if (levels >= 1 && levels <= SAccountConsts.LEVELS) {
            for (int i = 1; i <= levels; i++) {
                codeUsr += (codeUsr.isEmpty() ? "" : "-");

                digits = getDigits(mask, i);
                if (digits > SAccountConsts.LENGTH_LEVEL) {
                    codeUsr += SLibUtils.textRepeat("#", SAccountConsts.LENGTH_LEVEL);
                }
                else {
                    level = "" + SLibUtils.parseInt(codeStd.substring((i - 1) * SAccountConsts.LENGTH_LEVEL, i * SAccountConsts.LENGTH_LEVEL));
                    if (level.length() > digits) {
                        codeUsr += SLibUtils.textRepeat("#", digits);
                    }
                    else {
                        codeUsr += SLibUtils.textRepeat("0", digits - level.length()) + level;
                    }
                }
            }
        }

        return codeUsr;
    }

    /**
     * Converts an account code on user-format into standard-format.
     * @param codeUsr Account code on user-format to be converted into standard-format.
     * @return Converted account code on standard-format.
     */
    public static String convertCodeStd(final String codeUsr) {
        int index = 0;
        String level = "";
        String codeStd = "";
        String codeUsrAux = codeUsr;

        for (int i = 1; i <= SAccountConsts.LEVELS; i++) {
            if (codeUsrAux.isEmpty()) {
                level = "";
            }
            else {
                index = codeUsrAux.indexOf('-');

                if (index == -1) {
                    level = codeUsrAux;
                    codeUsrAux = "";
                }
                else {
                    level = codeUsrAux.substring(0, index);
                    codeUsrAux = index + 1 < codeUsrAux.length() ? codeUsrAux.substring(index + 1) : "";
                }
            }

            if (level.length() > SAccountConsts.LENGTH_LEVEL) {
                codeStd += SLibUtils.textRepeat("#", SAccountConsts.LENGTH_LEVEL);
            }
            else {
                codeStd += LevelFormat.format(SLibUtils.parseInt(level));
            }
        }

        return codeStd;
    }

    /**
     * Transforms an account code on standard-format into some of its ancestor accounts on standard-format aswell.
     * @param codeStd Account code on standard-format to be converted into some of its ancestor accounts.
     * @param levelAncestor Ancestor level to transform provided account code.
     * @return Transformed ancestor account code on standard-format according to provided ancestor level.
     */
    public static String transformAncestorCodeStd(final String codeStd, final int levelAncestor) {
        return levelAncestor < 1 || levelAncestor > SAccountConsts.LEVELS || codeStd.length() > SAccountConsts.LENGTH_ACCOUNT ? "" : codeStd.substring(0, SAccountConsts.LENGTH_LEVEL * levelAncestor) + SLibUtils.textRepeat("0", (SAccountConsts.LEVELS - levelAncestor) * SAccountConsts.LENGTH_LEVEL);
    }

    /**
     * Converts an account code-level in on user-format into standard-format.
     * <p>
     * <strong>Example:</strong>
     * <pre>
     * Let <i>lev</i> be an account code-level on user-format.
     * <code><i>acc</i> = "1110"</code>
     * Its corresponding account codel-level, on standard-format, is: <code>"001110"</code>.
     * </pre>
     * @param codeLevelUsr Account code-level on user-format.
     * @return Converted account code-level on standard-format.
     */
    public static String convertCodeLevelStd(final String codeLevelUsr) {
        return codeLevelUsr.length() > SAccountConsts.LENGTH_LEVEL ? "" : SLibUtils.textRepeat("0", SAccountConsts.LENGTH_LEVEL - codeLevelUsr.length()) + codeLevelUsr;
    }

    /**
     * Subtracts an account code on standard-format up to desired level limit.
     * <strong>Example:</strong>
     * <pre>
     * Let <i>acc</i> be an account code on standard-format.
     * <code><i>acc</i> = "00110500000100000200000300000000000000000000000"</code>
     * When a value 2 is provided for <i>level limit</i>, resulting subtracted account code is: <code>"001105000001"</code>.
     * </pre>
     * @param codeStd Account code on standard-format.
     * @param levelLimit Desired level limit.
     * @return Subtracted account code on standard-format.
     */
    public static String subtractCodeStd(final String codeStd, final int levelLimit) {
        return levelLimit < 1 || levelLimit > SAccountConsts.LEVELS || codeStd.length() != SAccountConsts.LENGTH_ACCOUNT ? "" : codeStd.substring(0, levelLimit * SAccountConsts.LENGTH_LEVEL);
    }

    /**
     * Subtracts an account code-level on standard-format from desired level.
     * <strong>Example:</strong>
     * <pre>
     * Let <i>acc</i> be an account code on standard-format.
     * <code><i>acc</i> = "00110500000100000200000300000000000000000000000"</code>
     * When a value 2 is provided for <i>level</i>, resulting subtracted account code-level is: <code>"000001"</code>.
     * </pre>
     * @param codeStd Account code on standard-format.
     * @param level Desired level.
     * @return Subtracted account code-level on standard-format.
     */
    public static String subtractCodeLevelStd(final String codeStd, final int level) {
        return level < 1 || level > SAccountConsts.LEVELS || codeStd.length() != SAccountConsts.LENGTH_ACCOUNT ? "" : codeStd.substring((level - 1) * SAccountConsts.LENGTH_LEVEL, level * SAccountConsts.LENGTH_LEVEL);
    }

    /**
     * Subtracts an account code on user-format up to desired level limit.
     * <strong>Example:</strong>
     * <pre>
     * Let <i>acc</i> be an account code on user-format.
     * <code><i>acc</i> = "1105-001-02-3"</code>
     * When a value 2 is provided for <i>level limit</i>, resulting subtracted account code is: <code>"1105-001"</code>.
     * </pre>
     * @param codeUsr Account code on user-format.
     * @param levelLimit Desired level limit.
     * @return Subtracted account code on user-format.
     */
    public static String subtractCodeUsr(final String codeUsr, final int levelLimit) {
        int index = 0;
        int lastIndex = 0;
        String codeUsrSubtracted = "";

        if (!codeUsr.isEmpty() && levelLimit >= 1 && levelLimit <= SAccountConsts.LEVELS) {
            for (int i = 0; i < levelLimit; i++) {
                index = codeUsr.substring(lastIndex).indexOf('-');

                if (index == -1) {
                    codeUsrSubtracted += (codeUsrSubtracted.isEmpty() ? "" : "-") + codeUsr.substring(lastIndex);
                    break;
                }
                else {
                    codeUsrSubtracted += (codeUsrSubtracted.isEmpty() ? "" : "-") + codeUsr.substring(lastIndex, lastIndex + index);

                    lastIndex = lastIndex + index + 1;
                    if (lastIndex == codeUsr.length()) {
                        break;
                    }
                }
            }
        }

        return codeUsrSubtracted;
    }
}
