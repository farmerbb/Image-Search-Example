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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import com.example.imagesearch.databinding.ImageViewBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint class FullImageFragment: Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private lateinit var binding: ImageViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // TODO fix and re-enable shared element transitions
        // sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.view_image)

        binding = ImageViewBinding.inflate(inflater, container, false).apply {
            image.transitionName = vm.selectedImage?.link
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        Picasso.get()
                .load(vm.selectedImage?.link)
                .into(image, object: Callback {
                    override fun onSuccess() {
                        progress.isVisible = false
                    }

                    override fun onError(e: Exception?) {
                        e?.printStackTrace()
                        progress.isVisible = false
                    }
                })
    }
}
