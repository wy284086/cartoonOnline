package com.cartoononline.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.plugin.common.utils.DataModelBase;
import com.plugin.common.utils.image.ImageUtils;
import com.plugin.database.dao.helper.DBTableAccessHelper;

public class SessionModel extends DataModelBase {
    
    private DBTableAccessHelper<SessionReadModel> mHelper;
    
    private boolean mDataChanged = true;
    
    @Override
    public void init(Context context) {
        super.init(context);
        mHelper = new DBTableAccessHelper<SessionReadModel>(context, SessionReadModel.class);
    }

    public boolean isDataChanged() {
        return mDataChanged;
    }
    
    public void insertOrRelace(SessionReadModel item) {
        mHelper.insertOrReplace(item);
        mDataChanged = true;
    }
    
    public void updateItem(SessionReadModel item) {
        mHelper.update(item);
        mDataChanged = true;
    }
    
    public void deleteItem(SessionReadModel item) {
        mHelper.delete(item);
        mDataChanged = true;
    }
    
    @Override
    public void asyncLoadDataServer(DataDownloadListener l) {

    }

    @Override
    public void asyncLoadDataLocal(final DataDownloadListener l) {
        this.asyncWork(new Runnable() {
           @Override
           public void run() {
               List<SessionReadModel> ret = new ArrayList<SessionReadModel>();
               List<SessionReadModel> lists = mHelper.queryItems();
                if (lists != null) {
                    for (SessionReadModel m : lists) {
                        File localFile = new File(m.localFullPath);
                        if (localFile.exists() && localFile.isDirectory()) {
                            m.coverBt = ImageUtils.loadBitmapWithSizeCheck(new File(m.coverPath));
                            ret.add(m);
                        }
                    }
                }
                
                if (l != null) {
                    l.onDataLoadSuccess(ret);
                }
                mDataChanged = false;
           }
        });
    }

}
