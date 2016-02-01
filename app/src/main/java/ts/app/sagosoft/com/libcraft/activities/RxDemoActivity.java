package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.model.HttpProperty;
import ts.app.sagosoft.com.libcraft.util.FdHttp;

/**
 * Created by FRED on 2016/1/21.
 */
public class RxDemoActivity extends BaseActivity {

    @InjectView(R.id.tv_rxdemo_result)
    TextView tvRxdemoResult;
    private Subscriber<HttpProperty> subscriber;
    private Observable<HttpProperty> observable;

    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, RxDemoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo);
        ButterKnife.inject(this);
        setTitle("RxDemo");

        //
        observable = Observable.create(new Observable.OnSubscribe<HttpProperty>() {
            @Override
            public void call(Subscriber<? super HttpProperty> subscriber2) {
                Log.i("RxDemoActivity call",Thread.currentThread().getName());
                HttpProperty property = FdHttp.httpRequest("GET", "https://www.baidu.com/", "", null, null);
                subscriber2.onNext(property);
                subscriber2.onCompleted();

            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()); // 指定 Subscriber 的回调发生在主线程;

    }


    @OnClick(R.id.bt_rxdemo_subscribe)
    public void subscribe() {
        //也可使用基本的Observer
        subscriber = new Subscriber<HttpProperty>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.fillInStackTrace();
            }

            @Override
            public void onNext(HttpProperty httpProperty) {
                //mainUI
                Log.i("RxDemoActivity onNext",Thread.currentThread().getName());
                showCustomToast("提示", "trunk => " + String.valueOf(httpProperty.getResponseCode() + " " + httpProperty.getResponseMsg()));
            }
        };
        //subscriber 需要更新
        observable.subscribe(subscriber);
    }



    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        if (subscriber!=null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
        super.onDestroy();
    }


}
