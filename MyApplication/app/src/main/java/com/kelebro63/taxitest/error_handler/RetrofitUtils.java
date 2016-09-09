package com.kelebro63.taxitest.error_handler;

import com.kelebro63.taxitest.models.ErrorModel;

import java.io.IOException;


/**
 * Created by Bistrov Alexey on 09.09.2016.
 */
public class RetrofitUtils {
    public static int getErrorCode(Throwable throwable) {
        if (throwable instanceof RetrofitException) {
            RetrofitException error = (RetrofitException) throwable;
            if (error.getResponse() == null) {
                return -1;
            } else {
                return (error.getResponse().code());
            }
        } else {
            return -1;
        }
    }

    public static ErrorModel getError(Throwable throwable) throws IOException {
        RetrofitException error = (RetrofitException) throwable;
        if (error.getResponse() == null)
            return null;
        return ((ErrorModel) error.getErrorBodyAs(ErrorModel.class));
    }
}
