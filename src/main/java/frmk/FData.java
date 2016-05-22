package frmk;

import com.wm.data.DataException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataHashCursor;
import com.wm.data.IDataIndexCursor;
import com.wm.data.IDataSharedCursor;
import com.wm.data.IDataTreeCursor;
import com.wm.data.IDataUtil;

public class FData extends IDataUtil implements IData, IDataCursor {
    private IData idata;
    private IDataCursor cursor;

    public FData(IData iData) {
        this.idata = iData;
        this.cursor = idata.getCursor();
    }

    public FData() {
        this(IDataFactory.create());
    }

    /**
     * 
     */
    public IData getIData() {
        return idata;
    }

    /**
     * 
     * @param key
     * @return
     */
    public String[] getStringArray(String key) {
        return super.getStringArray(cursor, key);
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        super.put(cursor, key, value);
    }

    /**
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        return super.getString(cursor, key);
    }

    /**
     * 
     * @param key
     * @return
     */
    public IData getIData(String key) {
        return super.getIData(cursor, key);
    }

    public IData[] getIDataArray(String key) {
        return super.getIDataArray(cursor, key);
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        return super.get(cursor, key);
    }

    @Override
    public IDataCursor getCursor() {
        return this.cursor;
    }

    @Override
    public IDataSharedCursor getSharedCursor() {
        return idata.getSharedCursor();
    }

    @Override
    public IDataTreeCursor getTreeCursor() {
        return idata.getTreeCursor();
    }

    @Override
    public IDataHashCursor getHashCursor() {
        return idata.getHashCursor();
    }

    @Override
    public IDataIndexCursor getIndexCursor() {
        return idata.getIndexCursor();
    }

    // methode IDataCursor
    @Override
    public boolean delete() {
        return cursor.delete();
    }

    @Override
    public void destroy() {
        cursor.destroy();
    }

    @Override
    public boolean first() {
        return cursor.first();
    }

    @Override
    public boolean first(String arg0) {
        return cursor.first(arg0);
    }

    @Override
    public IDataCursor getCursorClone() {
        return cursor.getCursorClone();
    }

    @Override
    public String getKey() {
        return cursor.getKey();
    }

    @Override
    public DataException getLastError() {
        return cursor.getLastError();
    }

    @Override
    public Object getValue() {
        return cursor.getValue();
    }

    @Override
    public boolean hasMoreData() {
        return cursor.hasMoreData();
    }

    @Override
    public boolean hasMoreErrors() {
        return cursor.hasMoreErrors();
    }

    @Override
    public void home() {
        cursor.home();
    }

    @Override
    public void insertAfter(String arg0, Object arg1) {
        cursor.insertAfter(arg0, arg1);
    }

    @Override
    public void insertBefore(String arg0, Object arg1) {
        cursor.insertBefore(arg0, arg1);
    }

    @Override
    public IData insertDataAfter(String arg0) {
        return cursor.insertDataAfter(arg0);
    }

    @Override
    public IData insertDataBefore(String arg0) {
        return cursor.insertDataBefore(arg0);
    }

    @Override
    public boolean last() {
        return cursor.last();
    }

    @Override
    public boolean last(String arg0) {
        return cursor.last(arg0);
    }

    @Override
    public boolean next() {
        return cursor.next();
    }

    @Override
    public boolean next(String arg0) {
        return cursor.next(arg0);
    }

    @Override
    public boolean previous() {
        return cursor.previous();
    }

    @Override
    public boolean previous(String arg0) {
        return cursor.previous(arg0);
    }

    @Override
    public void setErrorMode(int arg0) {
        cursor.setErrorMode(arg0);
    }

    @Override
    public void setKey(String arg0) {
        cursor.setKey(arg0);
    }

    @Override
    public void setValue(Object arg0) {
        cursor.setValue(arg0);
    }
}
