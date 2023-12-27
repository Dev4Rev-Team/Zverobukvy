package ru.dev4rev.kids.zoobukvy.data.stopwatch

import javax.inject.Inject

class GameStopwatchImpl @Inject constructor(
    private val timeStampProvider: TimeStampProvider,
    private val timeFormatter: TimeFormatter,
) : GameStopwatch {

    private var currentState: StopwatchState = StopwatchState.Pause(0L)

    override fun start() {
        currentState = calculateRunningState(currentState)
    }

    override fun stop() {
        currentState = calculatePauseState(currentState)
    }

    override fun getGameRunningTime(): String {
        val gameRunningTime = calculatePauseState(currentState).elapsedTime
        return timeFormatter.format(gameRunningTime)
    }

    private fun calculateRunningState(currentState: StopwatchState): StopwatchState.Running {
        return when (currentState) {
            is StopwatchState.Pause -> {
                val elapsedTime = currentState.elapsedTime
                val startTime = timeStampProvider.getCurrentTime()

                StopwatchState.Running(
                    startTime,
                    elapsedTime
                )
            }

            is StopwatchState.Running -> {
                return currentState
            }
        }
    }

    private fun calculatePauseState(currentState: StopwatchState): StopwatchState.Pause {
        return when (currentState) {
            is StopwatchState.Running -> {
                val startTime = currentState.startTime
                val pauseTime = timeStampProvider.getCurrentTime()

                val elapsedTime = currentState.elapsedTime + pauseTime - startTime

                StopwatchState.Pause(elapsedTime)
            }

            is StopwatchState.Pause -> {
                currentState
            }
        }
    }


    sealed interface StopwatchState {
        data class Running(
            val startTime: Long,
            val elapsedTime: Long,
        ) : StopwatchState

        data class Pause(
            val elapsedTime: Long,
        ) : StopwatchState
    }
}