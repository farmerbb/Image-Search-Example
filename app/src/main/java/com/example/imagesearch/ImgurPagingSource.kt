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

import androidx.paging.PagingSource

class ImgurPagingSource(
        private val api: ImgurAPI,
        private val query: String
): PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val pageNumber = params.key ?: 0
        val response = search(pageNumber, query)

        val prevKey = if(pageNumber > 0) pageNumber - 1 else null
        val nextKey = if(response.isNotEmpty()) pageNumber + 1 else null

        LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
        )
    } catch (e: Exception) {
        e.printStackTrace()
        LoadResult.Error(e)
    }

    private suspend fun search(page: Int, query: String) =
            api.search(page, query).data.filter { !it.nsfw }.flatMap { data ->
                data.images?.filter {
                    it.type.startsWith("image")
                }?.map {
                    it.copy(title = data.title)
                }.orEmpty()
            }
}