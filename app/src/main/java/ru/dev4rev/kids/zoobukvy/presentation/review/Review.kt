package ru.dev4rev.kids.zoobukvy.presentation.review

import android.app.Activity
import android.content.Context

interface Review {
    fun request(activity: Activity, onCompleteListener: ()->Unit)
}