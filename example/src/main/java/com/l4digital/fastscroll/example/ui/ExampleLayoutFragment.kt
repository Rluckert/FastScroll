/*
 * Copyright 2022 Randy Webster. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.l4digital.fastscroll.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.l4digital.fastscroll.FastScrollView
import com.l4digital.fastscroll.example.R
import com.l4digital.fastscroll.example.ui.adapter.ItemExampleAdapter

@Suppress("MagicNumber")
class ExampleLayoutFragment(@LayoutRes private val layoutId: Int) : Fragment() {

    private val itemList = mutableListOf<String>().apply {
        repeat(26) { i ->
            // add several items for each letter in the alphabet
            repeat(5) { add("${(65 + i).toChar()} example item") }
        }
    }

    private val itemListAdapter = ItemExampleAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId, container, false).apply {
            findViewById<RecyclerView>(R.id.recycler_view)?.adapter = itemListAdapter
            findViewById<FastScrollView>(R.id.fastscroll_view)?.adapter = itemListAdapter
        }.rootView
}
