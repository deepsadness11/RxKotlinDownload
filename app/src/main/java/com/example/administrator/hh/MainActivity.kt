package com.example.administrator.hh

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.hh.ex.select
import com.example.administrator.hh.ex.showToast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var onNextInt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView = findViewById(R.id.btn)
        var btn2 = findViewById(R.id.btn2)

        RxView.clicks(textView)
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onNext ->
                            println("Thread.currentThread().name!=${Thread.currentThread().name}")
                            println("BindView Click!")
//                            showToast("result=${1 orElse { 2 }}")
                            showToast(select(1 == 1, "=1", "!=1"))
                        }
                )

        val obs = object : Observer<Int> {
            override fun onComplete() {
                println("Thread.currentThread().name!=${Thread.currentThread().name}")
                println("onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                println("Thread.currentThread().name!=${Thread.currentThread().name}")
                println("onSubscribe=${d?.isDisposed}")
            }

            override fun onError(e: Throwable?) {
                println("Thread.currentThread().name!=${Thread.currentThread().name}")
                println("onError=${e?.message}")
            }

            override fun onNext(t: Int?) {
                println("Thread.currentThread().name!=${Thread.currentThread().name}")
                println("onNext=${t}")
            }

        }

        btn2.clicks().throttleFirst(1000L, TimeUnit.MILLISECONDS).subscribe({
            onNext ->
            onNextInt += 10
            doOnNextOnOutSide(obs)
        })

        println()

    }

    fun doOnNextOnOutSide(observer: Observer<Int>): Unit {
        observer.onNext(onNextInt)
    }


}
