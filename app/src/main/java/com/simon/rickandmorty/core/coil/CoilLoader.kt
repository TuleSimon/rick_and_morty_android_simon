package com.simon.rickandmorty.core.coil


import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import okhttp3.OkHttpClient

class SimonCoilLoader {
    companion object{

        private var coilLoader:ImageLoader?=null

        fun getImageLoader():ImageLoader{
            return coilLoader!!
        }

        @Synchronized
        fun getOrSettImageLoader(context: Context,):ImageLoader {
            if (coilLoader == null) {
                coilLoader = ImageLoader(context).newBuilder()
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCache {
                        MemoryCache.Builder(context)
                            .maxSizePercent(0.7)
                            .strongReferencesEnabled(true)
                            .build()
                    }
                    .allowHardware(true)
                    .respectCacheHeaders(false)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCache {
                        DiskCache.Builder()
                            .maxSizePercent(0.8)
                            .directory(context.filesDir)
                            .build()
                    }.build()
            }
            return coilLoader!!
        }

        @Synchronized
        fun setImageLoader(context: Context,okHttpClient: OkHttpClient?=null) {
            if (coilLoader == null) {
                coilLoader = ImageLoader(context).newBuilder()
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCache {
                        MemoryCache.Builder(context)
                            .maxSizePercent(0.7)
                            .strongReferencesEnabled(true)
                            .build()
                    }
                    .apply {
                        if (okHttpClient != null)
                            okHttpClient {
                                okHttpClient
                            }
                    }
                    .allowHardware(true)
                    .respectCacheHeaders(false)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCache {
                        DiskCache.Builder()
                            .maxSizePercent(0.8)
                            .directory(context.filesDir)
                            .build()
                    }.build()
            }
        }
    }
}