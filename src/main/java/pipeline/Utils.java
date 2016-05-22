package pipeline;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.util.JournalLogger;

import frmk.FData;

public class Utils {

    // TODO ajouter le niveau de log
    public static void log(String message) {
        int niveau = JournalLogger.ERROR;
        
        JournalLogger.log(niveau, JournalLogger.FAC_FLOW_SVC,
                niveau,message, null);
    }

    public static void removeAll(FData pipeline) {
        pipeline.getCursor().hasMoreData();
        pipeline.next();
    }
}
