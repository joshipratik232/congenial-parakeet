/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.*
import android.text.format.DateUtils
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.MyTheme
lateinit var viewmodel:CountdownViewmodel
var _count = MutableLiveData<String>()
var count : LiveData<String> = _count

private var _second = MutableLiveData("")
val second: LiveData<String> = _second

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this).get(CountdownViewmodel::class.java)
        viewmodel.second.observe(this,{
            _count.value = it
        })

        setContent {
            MyTheme {
                Timer()
                MyApp()
            }
        }
    }
}

@Composable
fun Timer(){
    val timer: CountDownTimer
    timer = object : CountDownTimer(
        CountdownViewmodel.COUNTDOWN_TIME,
        CountdownViewmodel.ONE_SECOND
    ) {

        override fun onTick(millisUntilFinished: Long) {
            _second.value = DateUtils.formatElapsedTime(millisUntilFinished / CountdownViewmodel.ONE_SECOND)
        }

        override fun onFinish() {
            _second.value = "0"
            //_eventBuzz.value = BuzzType.GAME_OVER
        }
    }
    timer.start()
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

private fun buzz(pattern: LongArray) {
    val buzzer = MainActivity().getSystemService<Vibrator>()
    buzzer?.let {
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            //deprecated in API 26
            buzzer.vibrate(pattern, -1)
        }
    }
}

