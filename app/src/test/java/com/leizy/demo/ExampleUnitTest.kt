package com.leizy.demo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import kotlin.concurrent.thread

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        test()
    }

    private fun test() {
/*        GlobalScope.launch {
            delay(1000)
            println("world.${System.currentTimeMillis()}")
        }
        println("Hello ${System.currentTimeMillis()}")
        runBlocking {
//            Thread.sleep(2000)
            println("${System.currentTimeMillis()}")
        }*/
        thread(
            true, false, null, "this", -1,
            block = {
//                Thread.sleep(1000)
                println("world ${System.currentTimeMillis()}")
            }
        )
        runBlocking {
            Thread.sleep(1000)
            println("${System.currentTimeMillis()}")
        }
        println("Hello ${System.currentTimeMillis()}")
    }
}
