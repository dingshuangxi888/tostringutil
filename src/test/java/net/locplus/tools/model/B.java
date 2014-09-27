package net.locplus.tools.model;

import net.locplus.tools.ToString;
import net.locplus.tools.ToStringType;
import net.locplus.tools.ToStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dean on 14/9/27.
 */
public class B {
    private final static Logger logger = LoggerFactory.getLogger(B.class);

    @ToString
    private int bInt0 = 0;

    @ToString(ToStringType.LIST)
    private int bInt1 = 1;

    @ToString
    private String bStr0 = "bStr0";

    @ToString(ToStringType.LIST)
    private String bStr1 = "bStr1";

    @ToString
    private boolean isBBoolean0 = true;

    @ToString(ToStringType.LIST)
    private boolean IsBBoolean1 = false;

    public int getbInt0() {
        return bInt0;
    }

    public void setbInt0(int bInt0) {
        this.bInt0 = bInt0;
    }

    public int getbInt1() {
        return bInt1;
    }

    public void setbInt1(int bInt1) {
        this.bInt1 = bInt1;
    }

    public String getbStr0() {
        return bStr0;
    }

    public void setbStr0(String bStr0) {
        this.bStr0 = bStr0;
    }

    public String getbStr1() {
        return bStr1;
    }

    public void setbStr1(String bStr1) {
        this.bStr1 = bStr1;
    }

    public boolean isBBoolean0() {
        return isBBoolean0;
    }

    public void setBBoolean0(boolean isBBoolean0) {
        this.isBBoolean0 = isBBoolean0;
    }

    public boolean isBBoolean1() {
        return IsBBoolean1;
    }

    public void setBBoolean1(boolean isBBoolean1) {
        IsBBoolean1 = isBBoolean1;
    }

    @Override
    public String toString() {
        return ToStringUtil.toString(this);
    }
}
