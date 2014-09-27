package net.locplus.tools.model;

import net.locplus.tools.ToStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dean on 14/9/27.
 */
public class C extends B {
    private final static Logger logger = LoggerFactory.getLogger(C.class);

    private String c;

    @Override
    public String toString() {
        return ToStringUtil.toString(this);
    }
}
