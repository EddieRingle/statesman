package io.ringle.statesman

import kotlin.platform.platformStatic

public object Statesman {

    platformStatic val sKeyPrefix = "__statesman_"

    platformStatic val sKeyKeyList = "${sKeyPrefix}keyList"

    platformStatic val sKeyNewState = "${sKeyPrefix}newState"

    platformStatic val sKeyState = { key: Int -> "${sKeyPrefix}state[${key}]" }
}
