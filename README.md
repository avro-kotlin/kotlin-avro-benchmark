# Kotlin Avro Benchmark

This project contains a benchmark that compares the serialization / deserialization performance of the following kotlin libraries:
- [Avro4k](https://github.com/avro-kotlin/avro4k/)
- [Jackson Avro](https://github.com/FasterXML/jackson-dataformats-binary/tree/master/avro)
- Coming soon: [Avro](https://avro.apache.org/)

## Results

The following table contains the benchmarks result:
```
Benchmark                       Mode  Cnt       Score       Error  Units
Avro4kBenchmark.clients        thrpt   10   30098,735 ±   367,147  ops/s
Avro4kBenchmark.users          thrpt   10  365203,063 ±  4513,715  ops/s
Avro4kDirectBenchmark.clients  thrpt   10  580846,822 ±  6803,729  ops/s
Avro4kDirectBenchmark.users    thrpt   10  525127,635 ±  4431,561  ops/s
JacksonBenchmark.clients       thrpt   10  240452,160 ± 10799,963  ops/s
JacksonBenchmark.users         thrpt   10  341116,523 ±  5938,359  ops/s
```

## Run the benchmark locally

The benchmark currently runs against an unreleased version of avro4k. 
In order to run the benchmark locally, you'll need to build and deploy the [way-to-multiplatform branch](https://github.com/avro-kotlin/avro4k/tree/way-to-multiplatform) of avro4k:
```shell
git clone git@github.com:avro-kotlin/avro4k.git
cd avro4k
git checkout way-to-multiplatform
./gradlew publishToMavenLocal
```

Now you can execute the benchmark:
```shell
./gradlew benchmark
```