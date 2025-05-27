package com.github.kafka.streams.favoritecolour;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class FavoriteColourApp {
    public static void main(String[] args) {
        System.out.println("Hi, Streams kafka APIs");

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "favourite-colour-java");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        System.out.println("My properties: " + properties);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, String> textLines = builder.stream("favourite-colour-input");

        KStream<String, String> userAndColours = textLines
                .filter((key, value) -> value.contains(","))
                        .selectKey((key, value) -> value.split(",")[0].toLowerCase())
                                .mapValues(value -> value.split(",")[1].toLowerCase())
                                        .filter((user, colour) ->
                                                Arrays.asList("green", "white", "red").contains(colour));

        userAndColours.to("user-keys-and-colours");

        KTable<String, String> usersAndColoursTable = builder.table("user-key-and-colours");

        KTable<String, Long> favouriteColours = usersAndColoursTable
                .groupBy((user, colour) -> new KeyValue<>(colour, colour))
                        .count("CountsByColours");

        favouriteColours.to(Serdes.String(), Serdes.Long(), "favourite-colour-output");

        KafkaStreams streams = new KafkaStreams(builder, properties);
        streams.cleanUp();
        streams.start();

        //TODO: Shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
