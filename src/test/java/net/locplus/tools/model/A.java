package net.locplus.tools.model;

import net.locplus.tools.ToString;
import net.locplus.tools.ToStringType;
import net.locplus.tools.ToStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dean on 14/9/27.
 */
public class A {
    private final static Logger logger = LoggerFactory.getLogger(A.class);

    @ToString
    private int int0 = 0;

    @ToString(ToStringType.LIST)
    private int int1 = 1;

    @ToString
    private String str0 = "str0";

    @ToString(ToStringType.LIST)
    private String str1 = "str1";

    @ToString
    private boolean isBoolean1 = true;

    @ToString(ToStringType.SHORT)
    private boolean IsBoolean2 = false;

    @ToString
    private boolean boolean3 = true;

    @ToString(ToStringType.LIST)
    private Boolean isBoolean4 = false;

    @ToString
    private Boolean boolean5 = true;

    @ToString
    private B b = new B();

    @ToString
    private List<B> bs = Arrays.asList(new B(), new B());

    @ToString
    public boolean isdelete;

    @ToString
    public boolean isD;

    @ToString
    private boolean isd;

    @ToString
    private C c;

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public boolean isIsd() {
        return isd;
    }

    public void setIsd(boolean isd) {
        this.isd = isd;
    }

    public boolean isD() {
        return isD;
    }

    public void setD(boolean isD) {
        this.isD = isD;
    }

    public boolean isIsdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public List<B> getBs() {
        return bs;
    }

    public void setBs(List<B> bs) {
        this.bs = bs;
    }

    public int getInt0() {
        return int0;
    }

    public int getInt1() {
        return int1;
    }

    public String getStr0() {
        return str0;
    }

    public String getStr1() {
        return str1;
    }

    public boolean isBoolean1() {
        return isBoolean1;
    }

    public boolean isBoolean2() {
        return IsBoolean2;
    }

    public boolean isBoolean3() {
        return boolean3;
    }

    public Boolean getIsBoolean4() {
        return isBoolean4;
    }

    public Boolean getBoolean5() {
        return boolean5;
    }

    public void setInt0(int int0) {
        this.int0 = int0;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public void setStr0(String str0) {
        this.str0 = str0;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public void setBoolean1(boolean isBoolean1) {
        this.isBoolean1 = isBoolean1;
    }

    public void setBoolean2(boolean isBoolean2) {
        IsBoolean2 = isBoolean2;
    }

    public void setBoolean3(boolean boolean3) {
        this.boolean3 = boolean3;
    }

    public void setIsBoolean4(Boolean isBoolean4) {
        this.isBoolean4 = isBoolean4;
    }

    public void setBoolean5(Boolean boolean5) {
        this.boolean5 = boolean5;
    }

    @Override
    public String toString() {
        return ToStringUtil.toString(this);
    }
}
