package com.atguigu.gmall.hive.udtf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ExplodeJsonArray extends GenericUDTF {

    @Override
    public void close() throws HiveException {
    }

    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        List<? extends StructField> allStructFieldRefs = argOIs.getAllStructFieldRefs();
        if (allStructFieldRefs.size()!=1) {
            throw new UDFArgumentException("Explode_Json_Array only accepts one argument");
        }
        String typeName = allStructFieldRefs.get(0).getFieldObjectInspector().getTypeName();
        if (!typeName.equals("string")) {
            throw new UDFArgumentException("Only string type argument is accepted!");
        }


        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("item");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,
                fieldOIs);
    }

    @Override
    public void process(Object[] args) throws HiveException {
        String s = args[0].toString();
        JSONArray jsonArray = new JSONArray(s);
        int length= jsonArray.length();
        for (int i=0;i<length;i++) {
            String string = jsonArray.getString(i);
            String[] result=new String[1];
            result[0]=string;
            forward(result);
        }
    }
}
