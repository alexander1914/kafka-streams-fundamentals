# kafka-streams-fundamentals
Rigth now, I currently learning more about the strems apacha kafka concepts on udemy

   <P>
       <img src="https://www.vectorlogo.zone/logos/apache_kafka/apache_kafka-icon.svg" alt="kafka" width="40" height="40"/>
       <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/>    
   </P>   

## kafka commands on Windows

Start zookeeper

    .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

Start Kafka server

    .\bin\windows\kafka-server-start.bat .\config\server.properties

Create a topic on Kafka

    .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-plaintext-input
    
    .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-wordcount-output

Two partitions

    .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-input
    
    .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-outpu


List all topics

    .\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list

write on topic Kafka
    
    .\bin\windows\kafka-console-producer.bat --broker-list 127.0.0.1:9092 --topic strems-plaintext-input

Read the data of a topic kafka

    .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic streams-plaintext-input --from-beginning

List my class kafka

    .\bin\windows\kafka-run-class.bat org.apache.kafka.streams.examples.wordcount.WordCountDemo
