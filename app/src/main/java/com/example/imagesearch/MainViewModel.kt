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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class MainViewModel @ViewModelInject constructor(
        private val repository: ImageRepository
): ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Image>>? = null

    var selectedImage: Image? = null

    fun searchImages(queryString: String): Flow<PagingData<Image>> {
        val lastResult = currentSearchResult
        if(queryString == currentQueryValue && lastResult != null)
            return lastResult

        currentQueryValue = queryString

        val newResult = repository.getPager(queryString).flow.cachedIn(viewModelScope)
        currentSearchResult = newResult

        return newResult
    }
}