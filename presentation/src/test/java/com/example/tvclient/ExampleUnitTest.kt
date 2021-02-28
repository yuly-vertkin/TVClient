package com.example.tvclient

import android.content.Context
import com.example.tvclient.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


//import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Mock
    private lateinit var mockContext: Context

    @Test
    fun addition_isCorrect() {
        val b = true
        assertThat(b).isTrue()
//        assertEquals(4, 2 + 2)

        `when`(mockContext.getString(R.string.app_name)).thenReturn("TVClient")
        assertThat(mockContext.getString(R.string.app_name)).isEqualTo("TVClient")

        val mockedList = mock(LinkedList::class.java)
        //stubbing
        `when`(mockedList.get(anyInt())).thenReturn("first")
        assertThat(mockedList.get(0)).isEqualTo("first")
    }
}