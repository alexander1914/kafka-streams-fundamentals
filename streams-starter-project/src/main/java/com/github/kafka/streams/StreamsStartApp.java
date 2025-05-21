package com.github.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class StreamsStartApp {
    public static void main(String[] args) {
        System.out.println("Hi, Streams kafka APIs");

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        System.out.println("My properties: " + properties);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, String> wordCountInput = builder.stream("word-count-input");

        //TODO: Preparing a data(KTable) to send the kafka
        KTable<String, Long> wordCounts = wordCountInput
                .mapValues(textLine -> textLine.toLowerCase())
                .flatMapValues(lowercaseTextLine -> Arrays.asList(lowercaseTextLine.split(" ")))
                .selectKey((ignoreKey, word) -> word)
                .groupByKey()
                .count("Counts");

        //TODO: Sending this topic to kafka
        wordCounts.to(Serdes.String(), Serdes.Long(), "word-count-output");

        //TODO: Creating new KafkaStreams to receive new message topic
        KafkaStreams streams = new KafkaStreams(builder, properties);

        streams.start();

        System.out.println(streams.toString());

        //TODO: Shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}
