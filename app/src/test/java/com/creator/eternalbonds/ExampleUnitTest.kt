package com.creator.eternalbonds

import com.creator.common.utils.LogUtil
import org.junit.Assert.*
import org.junit.Test
import java.net.InetAddress


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
// 获取 InetAddress 对象
        // 获取 InetAddress 对象
        val inetAddress = InetAddress.getByName("240e:379:20a4:3c10:a054:b2d0:6d3e:bf9a")
        val reachable = inetAddress.isReachable(2000)
        LogUtil.d("ExampleUnitTest",reachable.toString())

    }
}