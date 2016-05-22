package pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.wm.data.IData;
import com.wm.data.IDataUtil;

import bo.Mock;
import frmk.FData;
import permafrost.tundra.data.IDataHelper;

/**
 * TODO creer description utiliser le pattern strategie ?
 * 
 * @author florent
 *
 */
public class MockProcessor extends AbstractProcessor {

    private static final String ROOT_THREAD_NAME = "WmFrmk/MockProcessor#";

    /**
     * contient les Mocks
     */
    private Map<String, Mock> mocksService;

    
    /**
     * 
     * @param servicePattern
     *            The regular expression that if matching an invoked service
     *            will save that service's pipeline.
     * @param directory
     *            The directory to which to save the pipelines.
     */
    public MockProcessor(Pattern servicePattern, File directory) {
        setServicePattern(servicePattern);
        setDirectory(directory);
        mocksService = new HashMap<>();
    }
    /**
     * @return the mocksService
     */
    public Map<String, Mock> getMocksService() {
        return mocksService;
    }

    /**
     * @param mocksService
     *            the mocksService to set
     */
    public void setMocksService(Map<String, Mock> mocksService) {
        this.mocksService = mocksService;
    }

    /**
     * Ajoute un mock a la liste de mock
     * 
     * @param name
     * @param mock
     */
    public void addMock(String name, Mock mock) {
        this.mocksService.put(name, mock);
    }

    /**
     * @param name
     * @return
     */
    public Mock removeMock(String name) {
        return this.mocksService.remove(name);
    }
    
    @Override
    public String getNamedThread() {
        // TODO Auto-generated method stub
        return ROOT_THREAD_NAME;
    }

    @Override
    public Runnable getRunnable(IData pipeline, File file) {
        PipelineInfo info = new PipelineInfo(file);
        // recuperation du mock correspondant au service:

        if (MockProcessor.PipelineInfo.PipelineType.PIPELINE_OUTPUT
                .equals(info.type)) {
            // si null renvoyer un runnable ne faisant rien
            Mock mock = mocksService.get(info.serviceName);
            if (null == mock) {
                Utils.log("no mock found " + info.serviceName);
                return new VoidService(pipeline, file);
            }
            return new MockRunnable(pipeline, info, mock);
        }

        Utils.log("type pipelineType Input " + info.type);
        // TODO interet de mocker l input ?
        return new VoidService(pipeline, file);
    }

    /**
     * A runnable wich do nothing
     * 
     * @author florent
     *
     */
    private static class VoidService implements Runnable {

        /**
         * Constructor ne fait rien
         * 
         * @param pipeline
         * @param file
         */
        public VoidService(IData pipeline, File file) {
        }

        @Override
        public void run() {
        }

    }

    /**
     * A runnable which mock service
     */
    private static class MockRunnable implements Runnable {
        /**
         * The IData pipeline to be saved.
         */
        protected IData pipeline;
        /**
         * The file to save the pipeline to.
         */
        PipelineInfo info;
        protected Mock mock;

        /**
         * Creates a new runnable that saves the given pipeline to the given
         * file.
         * 
         * @param pipeline
         *            The pipeline to be saved.
         * @param info
         *            The file to save the pipeline to.
         * @param mock
         */
        public MockRunnable(IData pipeline, PipelineInfo info, Mock mock) {
            if (pipeline == null)
                throw new NullPointerException("pipeline must not be null");
            if (info == null)
                throw new NullPointerException("target must not be null");
            this.mock = mock;
            this.pipeline = pipeline;
            this.info = info;

        }

        /**
         * Change the IData pipeline by corresponding mock.
         */
        public void run() {
            // pour savoir si input ou output check filename
            try {
                Utils.log("RUN replace pipeline " + info.serviceName
                                + " type pipline" + info.type);

                // TODO si le pipeline reponds à la condition remplacer le
                // pipeline actuel par le mock.
                // si cela ne marche pas c'est parce que nous sommes dans un
                // thread

                IDataHelper.clear(this.pipeline);
                IDataUtil.merge(mock.getPipelienOut().getIData(), this.pipeline);
                

                Utils.log("RUN replace pipeline " + this.pipeline);

            } catch (Exception ex) {
                Utils.log("RUN " + ex);

                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Object pipeline
     * 
     * @author florent
     */
    private static class PipelineInfo {

        String serviceName;
        PipelineType type;

        /**
         * Enumeration permettant de determiner le type du pipeline
         * 
         * @author florent
         *
         */
        public enum PipelineType {
            PIPELINE_OUTPUT("output.xml"), PIPELINE_INPUT("input.xml");
            private final String text;

            private PipelineType(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }

            public static PipelineType getPipelineType(String val) {
                for (PipelineType p : values()) {
                    if (p.text.equals(val)) {
                        return p;
                    }
                }
                throw new IllegalArgumentException(
                        "No enum constante PipelineType for value: '" + val
                                + "'");
            }

        }

        /**
         * Create new instance
         * 
         * @param file
         */
        public PipelineInfo(File file) {
            //
            Utils.log("PipelineInfo target " + file.getAbsolutePath());

            String[] info = file.getName().split("_");
            // le serviceName
            this.serviceName = info[4];

            Utils.log( "PipelineInfo serviceName :" + serviceName + " - type:" + info[5]);
            type = PipelineType.getPipelineType(info[5]);

            // id generer
            String id = info[1].concat(info[2]).concat(info[3]);
        }
    }

    @Override
    protected boolean isExecuteNewThread() {
        return false;
    }

}
