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
public class SAdmRecInfo {
    private int num;
  
    private String sec;

    private String sub;

    private String prec;

    private String recDtSta;

    private String recDtEnd;

    private String breachAbstract;

    private String breachDescrip;

    private int fk_emp_offender;

    private int fk_emp_boss;

    public String getRecDtSta() {
      return this.recDtSta;
    }

    public String getRecDtEnd() {
      return this.recDtEnd;
    }

    public String getBreachAbstract() {
      return this.breachAbstract;
    }

    public String getBreachDescrip() {
      return this.breachDescrip;
    }

    public int getFk_emp_offender() {
      return this.fk_emp_offender;
    }

    public int getFk_emp_boss() {
      return this.fk_emp_boss;
    }

    public int getNum() {
      return this.num;
    }

    public String getSec() {
      return this.sec;
    }

    public String getSub() {
      return this.sub;
    }

    public String getPrec() {
      return this.prec;
    }

    public void setRecDtSta(String recDtSta) {
      this.recDtSta = recDtSta;
    }

    public void setRecDtEnd(String recDtEnd) {
      this.recDtEnd = recDtEnd;
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
