package com.ryu.personal.android.displaydetectorutil

data class DisplayState(
    val displayPxSize: DisplaySize,
    val displayDpSize: DisplaySize,
    val deviceType: DeviceType,
    val multiWindowType: MultiWindowType,
    val deviceOrientation: Orientation,
    val foldableState: FoldableState,
) {
    enum class MultiWindowType { SINGLE, MULTI_HORIZONTAL, MULTI_VERTICAL, MULTI_BOTH }
    enum class DeviceType { PHONE, TABLET, FLIP, FOLD }
    enum class FoldableState { FLAT, HALF_OPENED, CLOSED }
    enum class Orientation { HORIZONTAL, VERTICAL }
}

data class DisplaySize(
    val width: Int = 0,
    val height: Int = 0,
)
