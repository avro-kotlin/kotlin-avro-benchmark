# Kotlin Avro Benchmark

This project contains a benchmark that compares the serialization / deserialization performance of the following kotlin libraries:
- [Avro4k](https://github.com/avro-kotlin/avro4k/)
- [Jackson Avro](https://github.com/FasterXML/jackson-dataformats-binary/tree/master/avro)
- Coming soon: [Avro](https://avro.apache.org/)

## Results

The following table contains the benchmarks result:
```
Benchmark                       Mode  Cnt       Score       Error  Units
Avro4kBenchmark.clients        thrpt    3   29557,032 ±  3002,497  ops/s
Avro4kBenchmark.users          thrpt    3  290324,243 ± 23988,233  ops/s
Avro4kDirectBenchmark.clients  thrpt    3  583662,180 ± 68148,244  ops/s
Avro4kDirectBenchmark.users    thrpt    3  425400,322 ± 29352,531  ops/s
JacksonBenchmark.clients       thrpt    3  239120,408 ± 22238,596  ops/s
JacksonBenchmark.users         thrpt    3  304450,006 ± 63307,328  ops/s
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