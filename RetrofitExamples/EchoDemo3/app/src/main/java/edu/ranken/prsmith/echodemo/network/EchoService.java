package edu.ranken.prsmith.echodemo.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EchoService {

    @GET("api/echo")
    Call<EchoResponse> getEcho(@Query("method") String method, @Query("message") String message);

    @FormUrlEncoded
    @POST("api/echo")
    Call<EchoResponse> postEcho(@Field("method") String method, @Field("message") String message);
}
