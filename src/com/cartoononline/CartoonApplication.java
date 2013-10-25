package com.cartoononline;

import java.io.File;

import android.app.Application;

import com.plugin.common.cache.CacheFactory;
import com.plugin.common.cache.image.BitmapCacheOption;
import com.plugin.common.utils.Environment;
import com.plugin.common.utils.UtilsConfig;
import com.umeng.analytics.MobclickAgent;

public class CartoonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Config.CURRENT_PACKAGE_NAME = Environment.getPackageName(this);
        for (int index = 0; index < Config.PACKAGE_NAME.length; ++index) {
            if (Config.PACKAGE_NAME[index].equals(Config.CURRENT_PACKAGE_NAME)) {
                Config.INDEX = index;
                break;
            }
        }
        
        //init special case
        if (Config.INDEX == 3 || Config.INDEX == 4) {
            Config.BOOK_REVIEW = true;
            Config.DOWNLOAD_NEED_POINT = 20;
        }
        
        UtilsConfig.init(getApplicationContext());
        SettingManager.getInstance().init(getApplicationContext());
        BitmapCacheOption opt = new BitmapCacheOption();
        opt.needThumbnail = false;
        opt.openThumbnailSizeCheck = false;
        opt.thumbnailSize = 200;
        CacheFactory.init(opt);

        initUMeng();
        
        File file = new File(Config.BOOK_DOWNLOAD_DIR);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        
        if (file.exists() && file.isDirectory()) {
            return;
        }
        
        file.mkdirs();
    }

//    private void initYoumi() {
//        AdManager.getInstance(this).init(Config.YOUMI_APP_ID[Config.INDEX], Config.YOUMI_APP_SECRET_KEY[Config.INDEX],
//                false);
//        OffersManager.getInstance(this.getApplicationContext()).onAppLaunch();
//        SingleInstanceManager.getInstance().init(getApplicationContext());
//    }

    private void initUMeng() {
        MobclickAgent.setSessionContinueMillis(60 * 1000);
        MobclickAgent.setDebugMode(false);
        com.umeng.common.Log.LOG = false;
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.onError(this);
    }
}
