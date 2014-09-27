package net.locplus.tools.test;

import net.locplus.tools.model.A;
import net.locplus.tools.model.C;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dean on 14/9/27.
 */
public class TestToString {
    private final static Logger logger = LoggerFactory.getLogger(TestToString.class);

    @Test
    public void testToString() {

        C c = new C();

        System.out.println(c.toString());

        A a = new A();
        System.out.println(a.toString());
    }
}
