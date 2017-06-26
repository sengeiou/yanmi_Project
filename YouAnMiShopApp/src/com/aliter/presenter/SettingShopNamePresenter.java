package com.aliter.presenter;


import com.aliter.base.BaseView;
import com.aliter.entity.ShopUpdate;

import okhttp3.MultipartBody;

/**
 * Created by quantan.liu on 2017/3/28.
 */

public interface SettingShopNamePresenter {
    interface View extends BaseView {
         void  onCommonImageUploadSuccessView();
         void  onShopUpdateSuccessView();
         void  onFailView(String errorMsg);//获取数据失败调用该方法。



    }

    interface Presenter {
        void CommonImageUpload(MultipartBody.Part file);
        void ShopUpdate(ShopUpdate shopUpdate);
    }
}