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
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearch.databinding.ImageSearchBinding
import com.example.imagesearch.databinding.ViewholderBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint class SearchFragment: Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter().apply {
            addLoadStateListener {
                binding.progress.isVisible = it.source.refresh is LoadState.Loading
            }
        }
    }

    private lateinit var binding: ImageSearchBinding
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ImageSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = imagesAdapter.withLoadStateFooter(FooterAdapter())
            setHasFixedSize(true)
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                search(query.orEmpty())
                return true
            }

            override fun onQueryTextSubmit(newText: String?) = true
        })
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            vm.searchImages(query).collectLatest {
                imagesAdapter.submitData(it)
            }
        }
    }

    inner class ImagesViewHolder(private val binding: ViewholderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindImage(image: Image) = with(binding) {
            title.text = image.title
            description.text = image.description
            thumbnail.transitionName = image.link

            root.setOnClickListener {
                vm.selectedImage = image

                findNavController().navigate(
                        R.id.image_view,
                        bundleOf("IMAGE_URL" to image.link),
                        null,
                        FragmentNavigatorExtras(thumbnail to image.link)
                )
            }

            Picasso.get()
                    .load(image.link)
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerInside()
                    .into(thumbnail)
        }
    }

    inner class ImagesAdapter: PagingDataAdapter<Image, ImagesViewHolder>(
            object: DiffUtil.ItemCallback<Image>() {
                override fun areItemsTheSame(oldItem: Image, newItem: Image) = oldItem.id == newItem.id
                override fun areContentsTheSame(oldItem: Image, newItem: Image) = oldItem == newItem
            }
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
            val binding = ViewholderBinding.inflate(layoutInflater, parent, false)
            return ImagesViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
            getItem(position)?.let { holder.bindImage(it) }
        }
    }

    inner class FooterAdapter: LoadStateAdapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RecyclerView.ViewHolder {
            val view = layoutInflater.inflate(R.layout.footer, parent, false)
            return object: RecyclerView.ViewHolder(view) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
            // nothing to bind
        }
    }
}
