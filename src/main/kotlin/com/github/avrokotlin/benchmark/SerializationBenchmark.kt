package com.github.avrokotlin.benchmark

import com.github.avrokotlin.benchmark.data.gen.ClientsGenerator
import com.github.avrokotlin.benchmark.data.gen.UsersGenerator
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Level
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit


@State(Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
abstract class SerializationBenchmark {
    lateinit var clients: Clients
    lateinit var users: Users
    lateinit var out: OutputStream
       
    @Setup
    fun initTestData(){
        clients = Clients()
        ClientsGenerator.populate(clients, 1000)
        users = Users()
        UsersGenerator.populate(users, 1000)
    }
    @Setup(Level.Invocation)
    fun setupOutput() {
        out = ByteArrayOutputStream()
    }
    
    abstract fun clients()    
    
    abstract fun users()
}