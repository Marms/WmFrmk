package pipeline;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;

import elasticsearch.ElasticsSearchHelper;

/**
 * A service invocation processor that saves input and output pipelines to disk.
 */
public class PipelineCaptureSendESProcessor extends AbstractProcessor {
    private static final String ROOT_THREAD_NAME = "WmFrmk/PipelineCaptureSendESProcessor#";
    private static Logger logger = LoggerFactory
            .getLogger(PipelineCaptureSendESProcessor.class.getName());

    
    /**
     * Creates a new pipeline capture processor using default settings.
     */
    public PipelineCaptureSendESProcessor() {
        this(DEFAULT_SERVICE_PATTERN, DEFAULT_DIRECTORY);
    }

    /**
     * Creates a new pipeline capture processor.
     * 
     * @param servicePattern
     *            The regular expression that if matching an invoked service
     *            will save that service's pipeline.
     * @param directory
     *            The directory to which to save the pipelines.
     */
    public PipelineCaptureSendESProcessor(Pattern servicePattern,
            File directory) {
        setServicePattern(servicePattern);
        setDirectory(directory);
    }

    /**
     * A runnable which saves a pipeline to a Elastic search.
     */
    private static class SendPipelineToElasticSearch implements Runnable {
        /**
         * The IData pipeline to be saved.
         */
        protected IData pipeline;
        /**
         * The file to save the pipeline to.
         */
        protected File target;

        /**
         * Creates a new runnable that saves the given pipeline to the given
         * file.
         * 
         * @param pipeline
         *            The pipeline to be saved.
         * @param target
         *            The file to save the pipeline to.
         */
        public SendPipelineToElasticSearch(IData pipeline, File target) {
            if (pipeline == null)
                throw new NullPointerException("pipeline must not be null");
            if (target == null)
                throw new NullPointerException("target must not be null");

            try {
                this.pipeline = IDataUtil.deepClone(pipeline);
                this.target = target;
            } catch (IOException ex) {
                Utils.log( "new Send" + ex);
                throw new RuntimeException(ex);
            }
        }

        /**
         * Saves the IData pipeline to the target elastic search node.
         */
        public void run() {
            try {
                Utils.log( "RUN");
                Utils.log( "RUN target " + target.getName());

                IDataXMLCoder coder = new IDataXMLCoder();
                OutputStream os = new ByteArrayOutputStream();
                coder.encode(os, pipeline);
                // convert pipeline to json
                String soapmessageString = os.toString();
                Utils.log( "RUN message" + soapmessageString);
                JSONObject soapDatainJsonObject = null;
                try {
                    soapDatainJsonObject = XML.toJSONObject(soapmessageString);
                    Utils.log( "RUN json" + soapDatainJsonObject.toString());

                } catch (JSONException e) {
                    Utils.log("RUN error json" + e);
                }

                // create unique index
                String[] info = target.getName().split("_");
                // Input || Output
                String type = info[4];
                // le serviceName
                String index = info[5];
                // id generer
                String id = info[1].concat(info[2]).concat(info[3]);

                Utils.log( "RUN test" + index.concat(type).concat(id));

                // call ES index
                ElasticsSearchHelper.index(index, type, id,
                        soapDatainJsonObject);
                // coder.writeToFile(target, pipeline);
            } catch (Exception ex) {
                Utils.log("RUN " + ex);

                throw new RuntimeException(ex);
            }

        }

    }

    @Override
    public String getNamedThread() {
        // TODO Auto-generated method stub
        return ROOT_THREAD_NAME;
    }

    @Override
    public Runnable getRunnable(IData pipeline, File file) {
        return new SendPipelineToElasticSearch(pipeline, file);
    }

    @Override
    protected boolean isExecuteNewThread() {
        return true;
    }
}
