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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class ImageRepository @Inject constructor(
        private val api: ImgurAPI
) {
    fun getPager(query: String) = Pager(
                config = PagingConfig(
                        pageSize = 50,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { ImgurPagingSource(api, query) }
        )
}