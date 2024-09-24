package com.ryu.personal.android.displaydetectorutil

import android.app.Activity
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import com.ryu.personal.android.displaydetectorutil.DisplayState.DeviceType
import com.ryu.personal.android.displaydetectorutil.DisplayState.FoldableState
import com.ryu.personal.android.displaydetectorutil.DisplayState.MultiWindowType
import com.ryu.personal.android.displaydetectorutil.DisplayState.Orientation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs
import kotlin.math.min

object DisplayDetector {

    fun Activity.observeDisplayState(minTableDp: Int = 600): Flow<DisplayState> = callbackFlow {
        val windowInfoTracker = WindowInfoTracker.getOrCreate(this@observeDisplayState)
        windowInfoTracker.windowLayoutInfo(this@observeDisplayState).collectLatest { windowLayoutInfo ->
            delay(200)
            val displayPxSize: DisplaySize = getDisplayPx(this@observeDisplayState)
            val displayDpSize: DisplaySize = getDisplayDp(this@observeDisplayState)
            val deviceOrientation: Orientation = getOrientation(this@observeDisplayState)
            val multiWindowType = getMultiWindowType(this@observeDisplayState)
            var foldableState = FoldableState.CLOSED
            windowLayoutInfo.displayFeatures
                .filterIsInstance<FoldingFeature>()
                .forEach {
                    foldableState = getFoldableState(it.state)
                }
            val deviceType = getDeviceType(displayDpSize, minTableDp, foldableState != FoldableState.CLOSED)

            trySend(DisplayState(
                displayPxSize = displayPxSize,
                displayDpSize = displayDpSize,
                deviceType = deviceType,
                multiWindowType = multiWindowType,
                deviceOrientation = deviceOrientation,
                foldableState = foldableState
            ))
        }
    }

    private fun getDisplayPx(activity: Activity): DisplaySize {
        val width = activity.resources.displayMetrics.run { widthPixels }
        val height = activity.resources.displayMetrics.run { heightPixels }
        return DisplaySize(width = width, height = height)
    }

    private fun getDisplayDp(activity: Activity): DisplaySize {
        val width = activity.resources.displayMetrics.run { widthPixels / density }
        val height = activity.resources.displayMetrics.run { heightPixels / density }
        return DisplaySize(width = width.toInt(), height = height.toInt())
    }

    private fun getOrientation(activity: Activity): Orientation {
        return if (activity.resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            Orientation.HORIZONTAL
        } else {
            Orientation.VERTICAL
        }
    }

    private fun getDeviceType(displayDpSize: DisplaySize, minTableDp: Int, isFoldable: Boolean): DeviceType {
        val targetDp = min(displayDpSize.width, displayDpSize.height)
        return if (isFoldable) {
            if (targetDp >= minTableDp) DeviceType.FOLD else DeviceType.FLIP
        } else {
            if (targetDp >= minTableDp) DeviceType.TABLET else DeviceType.PHONE
        }
    }

    private fun getMultiWindowType(activity: Activity): MultiWindowType {
        val wmc = WindowMetricsCalculator.getOrCreate()
        val currentWM = wmc.computeCurrentWindowMetrics(activity).bounds.flattenToString()
        val maximumWM = wmc.computeMaximumWindowMetrics(activity).bounds.flattenToString()

        val (currentLeft, currentTop, currentRight, currentBottom) = currentWM.split(" ").map { it.toInt() }
        val (maximumLeft, maximumTop, maximumRight, maximumBottom) = maximumWM.split(" ").map { it.toInt() }
        val currentWidth = abs(currentRight) - abs(currentLeft)
        val currentHeight = abs(currentBottom) - abs(currentTop)
        val maximumWidth = abs(maximumRight) - abs(maximumLeft)
        val maximumHeight = abs(maximumBottom) - abs(maximumTop)

        return if (currentWidth == maximumWidth && currentHeight == maximumHeight) {
            MultiWindowType.SINGLE
        } else if (currentWidth != maximumWidth && currentHeight != maximumHeight) {
            MultiWindowType.MULTI_BOTH
        } else if (currentWidth == maximumWidth) {
            MultiWindowType.MULTI_VERTICAL
        } else {
            MultiWindowType.MULTI_HORIZONTAL
        }
    }

    private fun getFoldableState(state: FoldingFeature.State?): FoldableState {
        return when (state) {
            FoldingFeature.State.FLAT -> FoldableState.FLAT
            FoldingFeature.State.HALF_OPENED -> FoldableState.HALF_OPENED
            else -> FoldableState.CLOSED
        }
    }
}