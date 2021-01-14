/* Copyright 2021 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.imagesearch

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.internal.platform.Platform

@InstallIn(SingletonComponent::class)
@Module class ImageSearchModule {

    @Provides
    fun imgurAPI(client: OkHttpClient): ImgurAPI = Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.imgur.com/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ImgurAPI::class.java)

    @Provides
    fun okHttpClient() = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor.Builder()
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("ImgurAPI")
                    .response("ImgurAPI")
                    .build())
            .addInterceptor {
                it.proceed(it.request()
                        .newBuilder()
                        .header("Authorization", "Client-ID $CLIENT_ID")
                        .build())
            }.build()

    companion object {
        const val CLIENT_ID = "insert Imgur Client ID here"
    }
}