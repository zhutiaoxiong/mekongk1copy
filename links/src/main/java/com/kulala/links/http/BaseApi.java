package com.kulala.links.http;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApi {
    @FormUrlEncoded
    @POST("/mm/xxx")
    Call<ResponseBody> post(@FieldMap TreeMap<String,String> map);
}
