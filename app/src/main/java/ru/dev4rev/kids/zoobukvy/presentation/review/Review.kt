package ru.dev4rev.kids.zoobukvy.presentation.review

import android.app.Activity

interface Review {
    /**
     * Запрашиваем где-то за 0-3 минуты до показа шторки,
     * чтобы запуск шторки оценки произошел мгновенно.
     */
    fun requestReviewFlow(activity: Activity)

    /**
     * Запуск шторки оценки
     */
    fun launchReviewFlow(activity: Activity, onCompleteListener: ()->Unit)
}