package com.doordash.interview.list

import com.doordash.interview.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import java.lang.reflect.Type

class NetworkItemTest {
    lateinit var fileStream: InputStream

    @Before
    fun setUp() {
        fileStream = this.javaClass.classLoader!!.getResourceAsStream("data.json");
    }

    @After
    fun cleanUp() {
        fileStream.close();
    }

    @Test
    fun parseJsonData() {
        val fileStream = this.javaClass.classLoader!!.getResourceAsStream("data.json");
        val listType: Type = object : TypeToken<List<ListItem>>(){}.getType();

        val list = Gson().fromJson(Utils.readTextStream(fileStream), listType) as List<ListItem>
        Assert.assertEquals(1, list.size)
        Assert.assertEquals("Carl's Jr. (1101040)", list[0].name)
    }
}