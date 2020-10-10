package com.mani.car.mekongk1.sample;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyService {
    @GET("user/login" )
    Observable<User> login(
            @Query("username") String username,
            @Query("password") String password
    );
    @GET("/user")
    public Observable<User> getUser(
            @Query("token") String token);
}
