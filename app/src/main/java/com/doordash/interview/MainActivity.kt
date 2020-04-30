/*
 * Copyright (C) 2019 Yubico.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doordash.interview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main activity that operates with fragments
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set theme before calling setContentView
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_main)
        // set up ActionBar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.fragment_container)
        toolbar.setupWithNavController(navController)
    }
}
