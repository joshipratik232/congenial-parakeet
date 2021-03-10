package com.example.androiddevchallenge

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class CountdownViewmodel : ViewModel() {
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private var timer: CountDownTimer

    private var _second = MutableLiveData("")
    val second: LiveData<String> = _second

    private val _finish = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean>
        get() = _finish

    init {
        _finish.value = false

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _second.value = DateUtils.formatElapsedTime(millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _second.value = "0"
                _finish.value = true
                //_eventBuzz.value = BuzzType.GAME_OVER
            }
        }
        timer.start()
    }

    fun onGameComplete() {
        _finish.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
    override fun onCleared() {
        super.onCleared()
    }
}
@Composable
fun MyApp(viewmodel: CountdownViewmodel = viewModel()) {
    Surface(color = MaterialTheme.colors.background) {
        val count: String by viewmodel.second.observeAsState("")
        Text(count)
    }
}


