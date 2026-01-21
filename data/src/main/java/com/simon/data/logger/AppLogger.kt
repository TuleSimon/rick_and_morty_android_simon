package com.simon.data.logger

import timber.log.Timber

object AppLogger {

    val GLOBAL_TAG = "Abaaly_Tag"

    fun log(message:String){
        Timber.tag(GLOBAL_TAG).v(message)
    }


    fun e(message:String){
        Timber.tag(GLOBAL_TAG).e(message)
    }

    fun i(message:String){
        Timber.tag(GLOBAL_TAG).i(message)
    }

    fun w(message:String){
        Timber.tag(GLOBAL_TAG).w(message)
    }

    fun v(message:String){
        Timber.tag(GLOBAL_TAG).v(message)
    }

    fun d(message:String){
        Timber.tag(GLOBAL_TAG).d(message)
    }
    fun e(callBack:()->String){
        Timber.tag(GLOBAL_TAG).e(callBack())
    }
    fun d(callBack:()->String){
        Timber.tag(GLOBAL_TAG).d(callBack())
    }
    fun w(callBack:()->String){
        Timber.tag(GLOBAL_TAG).w(callBack())
    }
    fun v(callBack:()->String){
        Timber.tag(GLOBAL_TAG).v(callBack())
    }

    fun i(callBack:()->String){
        Timber.tag(GLOBAL_TAG).i(callBack())
    }

    fun e(tag:String,message:String){
        Timber.tag(tag).e(message)
    }

    fun v(tag:String,message:String){
        Timber.tag(tag).v(message)
    }

    fun d(tag:String,message:String){
        Timber.tag(tag).v(message)
    }

    fun w(tag:String,message:String){
        Timber.tag(tag).w(message)
    }



}