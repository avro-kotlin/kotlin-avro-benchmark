package com.github.avrokotlin.benchmark.data.gen

import com.github.avrokotlin.benchmark.data.gen.RandomUtils.nextDouble
import com.github.avrokotlin.benchmark.data.gen.RandomUtils.nextInt
import com.github.avrokotlin.benchmark.data.gen.RandomUtils.randomAlphabetic
import com.github.avrokotlin.benchmark.data.gen.RandomUtils.randomAlphanumeric
import com.github.avrokotlin.benchmark.data.gen.RandomUtils.randomNumeric
import com.github.avrokotlin.benchmark.data.model.Friend
import com.github.avrokotlin.benchmark.data.model.User
import com.github.avrokotlin.benchmark.data.model.Users

object UsersGenerator : DataGenerator<Users> {
    override fun populate(obj: Users, size: Int): Int {
        var approxSize = 12 // {'users':[]}
        obj.users = mutableListOf()
        while (approxSize < size) {
            approxSize += appendUser(obj, size - approxSize)
            approxSize += 1 // ,
        }
        return approxSize
    }


    private fun appendUser(uc: Users, sizeAvailable: Int): Int {
        var expectedSize = 2 // {}
        val u = User()
        u.id = randomNumeric(20)
        expectedSize += 9 + u.id!!.length // ,'id':''
        u.index = nextInt(0, Int.MAX_VALUE)
        expectedSize += 11 + u.index.toString().length // ,'index':''
        u.guid = randomAlphanumeric(20)
        expectedSize += 10 + u.guid!!.length // ,'guid':''
        u.isActive
        expectedSize += 17 + if (u.isActive) 4 else 5 // ,'isActive':''
        u.balance = randomAlphanumeric(20)
        expectedSize += 16 + u.balance!!.length // ,'balance':''
        u.picture = randomAlphanumeric(100)
        expectedSize += 16 + u.picture!!.length // ,'picture':''
        u.age = nextInt(0, 100)
        expectedSize += 9 + u.age.toString().length // ,'age':''
        u.eyeColor = randomAlphanumeric(20)
        expectedSize += 17 + u.eyeColor!!.length // ,'eyeColor':''
        u.name = randomAlphanumeric(20)
        expectedSize += 10 + u.name!!.length // ,'name':''
        u.gender = randomAlphanumeric(20)
        expectedSize += 12 + u.gender!!.length // ,'gender':''
        u.company = randomAlphanumeric(20)
        expectedSize += 13 + u.company!!.length // ,'company':''
        u.email = randomAlphanumeric(20)
        expectedSize += 11 + u.email!!.length // ,'email':''
        u.phone = randomAlphanumeric(20)
        expectedSize += 11 + u.phone!!.length // ,'phone':''
        u.address = randomAlphanumeric(20)
        expectedSize += 13 + u.address!!.length // ,'address':''
        u.about = randomAlphanumeric(20)
        expectedSize += 11 + u.about!!.length // ,'about':''
        u.registered = randomAlphanumeric(20)
        expectedSize += 16 + u.registered!!.length // ,'registered':''
        u.latitude = nextDouble(0.0, 90.0)
        expectedSize += 14 + u.latitude.toString().length // ,'latitude':''
        u.longitude = nextDouble(0.0, 180.0)
        expectedSize += 15 + u.longitude.toString().length // ,'longitude':''
        expectedSize += 10 // ,'tags':[]
        val nTags = nextInt(0, 50)
        val tags = mutableListOf<String>()
        for (i in 0..<nTags) {
            if (expectedSize > sizeAvailable) {
                break
            }
            val t = randomAlphanumeric(10)
            tags.add(t)
            expectedSize += t.length // '',
        }
        u.tags = tags
        val nFriends = nextInt(0, 50)
        u.friends = ArrayList()
        val friends = mutableListOf<Friend>()
        expectedSize += 13 // ,'friends':[]
        for (i in 0..<nFriends) {
            if (expectedSize > sizeAvailable) {
                break
            }
            val id = nextInt(0, 10000)
            val idStr = id.toString()
            val name = randomAlphabetic(30)
            friends.add(Friend(idStr, name))
            expectedSize += idStr.length + name.length + 20 // {'id':'','name':''},
        }
        u.friends = friends
        u.greeting = randomAlphanumeric(20)
        expectedSize += 14 + u.greeting!!.length // ,'greeting':''
        u.favoriteFruit = randomAlphanumeric(20)
        expectedSize += 19 + u.favoriteFruit!!.length // ,'favoriteFruit':''
        uc.users.add(u)
        return expectedSize
    }
    
}
