package com.mani.car.mekongk1.common;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxCoundDown {
    private static RxCoundDown _instance;

    private RxCoundDown() {
    }

    public static RxCoundDown getInstance() {
        if (_instance == null)
            _instance = new RxCoundDown();
        return _instance;
    }
    private Disposable mDisposable;

    /**
     * 启动定时器
     */
    public void startTime(final int count_time) {
//      final  int count_time = 10; //总时间
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count_time+1)//设置总共发送的次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        //aLong从0开始
                        return count_time-aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        if(listner!=null){
                            listner.onStart();
                        }
                    }

                    @Override
                    public void onNext(Long value) {
                        //Log.d("Timer",""+value);
                        if(listner!=null){
                            listner.OnCount(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // TODO:2017/12/1
                        closeTimer();
                        if(listner!=null){
                            listner.Complete();
                        }
                    }
                });
    }

    /**
     * 关闭定时器
     */
    public void closeTimer(){
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
    public interface OnTimeCompleteListner{
        void onStart();
        void Complete();
        void OnCount(Long value);
    }
    public void setOnTimeCompleteListner(OnTimeCompleteListner  listner){
        this.listner=listner;
    }
   private OnTimeCompleteListner  listner;
}
