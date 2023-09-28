# Kotlin Avro Benchmark

This project contains a benchmark that compares the serialization / deserialization performance of the following kotlin libraries:
- [Avro4k](https://github.com/avro-kotlin/avro4k/)
- [Jackson Avro](https://github.com/FasterXML/jackson-dataformats-binary/tree/master/avro)
- Coming soon: [Avro](https://avro.apache.org/)

## Results

The following table contains the benchmarks result:
```
Benchmark                             Mode  Cnt       Score       Error  Units
DeserializationAvro4k.clients        thrpt    5   69112,726 ±  1142,820  ops/s
DeserializationAvro4k.users          thrpt    5  198955,534 ±  3460,397  ops/s
DeserializationAvro4kDirect.clients  thrpt    5  467419,904 ±  5076,498  ops/s
DeserializationAvro4kDirect.users    thrpt    5  362091,092 ±  4585,828  ops/s
DeserializationJackson.clients       thrpt    5  187465,951 ±  2305,600  ops/s
DeserializationJackson.users         thrpt    5  127586,528 ±  1245,258  ops/s
SerializationAvro4k.clients          thrpt    5   28469,775 ±   411,850  ops/s
SerializationAvro4k.users            thrpt    5  390932,344 ± 41827,719  ops/s
SerializationAvro4kDirect.clients    thrpt    5  557651,668 ±  4317,495  ops/s
SerializationAvro4kDirect.users      thrpt    5  445721,886 ± 11015,122  ops/s
SerializationJackson.clients         thrpt    5  257334,355 ±  3181,248  ops/s
SerializationJackson.users           thrpt    5  314527,930 ±  7471,340  ops/s

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