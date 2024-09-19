/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Cesar Orozco
 */
public class SBreachInfo {
    private int num;
    private String sec;
    private String sub;
    private String prec;
    private String breachTs ;
    private String breachAbstract;
    private String breachDescrip;
    private int fk_emp_offender;
    private int fk_emp_boss;
    private int fk_emp_author;

    public String getBreachTs() {
        return breachTs;
    }

    public String getBreachAbstract() {
        return breachAbstract;
    }

    public String getBreachDescrip() {
        return breachDescrip;
    }

    public int getFk_emp_offender() {
        return fk_emp_offender;
    }

    public int getFk_emp_boss() {
        return fk_emp_boss;
    }

    public int getFk_emp_author() {
        return fk_emp_author;
    }

    public int getNum() {
        return num;
    }
    
    public String getSec() {
        return sec;
    }
    
    public String getSub()  {
        return sub;
    }
    
    public String getPrec() {
        return prec;
    }
    
    public void setBreachTs(String breachTs) {
        this.breachTs = breachTs;
    }

    public void setBreachAbstract(String breachAbstract) {
        this.breachAbstract = breachAbstract;
    }

    public void setBreachDescrip(String breachDescrip) {
        this.breachDescrip = breachDescrip;
    }

    public void setFk_emp_offender(int fk_emp_offender) {
        this.fk_emp_offender = fk_emp_offender;
    }

    public void setFk_emp_boss(int fk_emp_boss) {
        this.fk_emp_boss = fk_emp_boss;
    }

    public void setFk_emp_author(int fk_emp_author) {
        this.fk_emp_author = fk_emp_author;
    }

    public void setNum(int num) {
        this.num = num;
    }
    
    public void setSec(String sec) {
        this.sec = sec;
    }
    
    public void setSub(String sub) {
        this.sub = sub;
    }
    
    public void setPrec(String prec) {
        this.prec = prec;
    }
    
    
}
