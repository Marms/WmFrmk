package pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Before;
import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;
import com.wm.util.coder.InvalidDatatypeException;

import elasticsearch.ElasticsSearchHelper;

public class testPIpelineCaptureSendESProcessor {

    IData pipeline;
    
    @Before
    public void init() {
        pipeline = IDataFactory.create();
        IDataCursor curs = pipeline.getCursor();
        IDataUtil.put(curs, "test", 1);
        IDataUtil.put(curs, "test2", 2);
        String[] array = {"array1", "array2", "array3"};
        IDataUtil.put(curs, "array", array);
        
        curs.destroy();
    }
    @Test
    public void testXmlToJson() throws Exception {
        IDataXMLCoder coder = new IDataXMLCoder();
        OutputStream os = new ByteArrayOutputStream();
        coder.encode(os, pipeline);
        // convert pipeline to json 
        String soapmessageString = os.toString();
        JSONObject soapDatainJsonObject = XML.toJSONObject(soapmessageString);
        ElasticsSearchHelper.index("input", "testCapture", "1_1"+new Date(),  soapDatainJsonObject);
        System.out.println(soapDatainJsonObject);

        
    }
}
