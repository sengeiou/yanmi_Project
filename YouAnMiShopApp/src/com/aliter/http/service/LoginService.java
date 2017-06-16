package com.aliter.http.service;


import com.aliter.entity.AuthCode;
import com.aliter.entity.Login;
import com.aliter.http.BaseResponse;
import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.application.AppController;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by quantan.liu on 2017/3/30.
 */

public interface LoginService {

    @POST(AppController.auth_shop_login2)   // 登录接口
    Observable <BaseResponse<IMUserInfoVO>> AuthShopLogin2(@Body Login login);


    @POST(AppController.shop_get_security_code)    //  获取验证码接口
    Observable <BaseResponse > ShopGetSecurityCode(@Body AuthCode authCode);

}
