package com.mani.car.mekongk1.sample;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class demoRetrofitrxjava {
    private String BASE_URL;
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置
            .baseUrl(BASE_URL)
            .build();
    MyService service = retrofit.create(MyService.class);
/**
 * service.login("11111", "22222")
 .flatMap(new Func1<String, Observable<User>>() {  //得到token后获取用户信息
 @Override
 public Observable<User> onNext(String token) {
 return service.getUser(token);
 })
 .subscribeOn(Schedulers.newThread())//请求在新的线程中执行请求
 .observeOn(Schedulers.io())         //请求完成后在io线程中执行
 .doOnNext(new Action1<User>() {      //保存用户信息到本地
 @Override
 public void call(User userInfo) {
 saveUserInfo(userInfo);
 }
 })
 .observeOn(AndroidSchedulers.mainThread())//在主线程中执行
 .subscribe(new Observer<User>() {
 @Override
 public void onNext(User user) {
 //完成一次完整的登录请求
 userView.setUser(user);
 }

 @Override
 public void onCompleted() {

 }
 @Override
 public void onError(Throwable error) {
 //请求失败
 }
 });
 */



}
