package com.aliter.http.service;


import com.aliter.entity.AuthCode;
import com.aliter.entity.AuthCodeBean;
import com.aliter.entity.CheckAuthCode;
import com.aliter.entity.CheckAuthCodeBean;
import com.aliter.http.BaseResponse;
import com.zxly.o2o.application.AppController;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;


public interface SmsVerificationService {

    @POST(AppController.check_auth_code)
    Observable <BaseResponse<CheckAuthCodeBean>> CheckAuthCode(@Body CheckAuthCode checkAuthCode);

    @POST(AppController.shop_get_security_code)
    Observable <BaseResponse<AuthCodeBean>> getAuthCode(@Body AuthCode authCode);

}