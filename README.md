# Sample Programs for Kafka 0.9 API

This project provides a simple but realistic example of a Kafka
producer and consumer.

## Pre-requisites
To start, you need to get Kafka up and running and create some topics.

### Step 1: Download Kafka
Download the 0.9.0.0 release and un-tar it.
```
$ tar -xzf kafka_2.11-0.9.0.0.tgz
$ cd kafka_2.11-0.9.0.0
```
### Step 2: Start the server
Start a ZooKeeper server. Kafka has a single node Zookeeper configuration built-in.
```
$ .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
Note that this will start Zookeeper in the background. To stop
Zookeeper, you will need to bring it back to the foreground and use
control-C or you will need to find the process and kill it.

Now start Kafka itself:
```
$ .\bin\windows\kafka-server-start.bat .\config\server.properties
```
As with Zookeeper, this runs the Kafka broker in the background. To
stop Kafka, you will need to bring it back to the foreground or find
the process and kill it explicitly using `kill`.

### Step 3: Create the topics for the example programs
We need one topics for the example program
```
$ .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic vacations
```
These can be listed
```
$ .\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181
vacations
```
Note that you will see log messages from the Kafka process when you
run Kafka commands. You can switch to a different Mac if these are
distracting.

The broker can be configured to auto-create new topics as they are mentioned, but that is often considered a bit 
dangerous because mis-spelling a topic name doesn't cause a failure.

## Now for the real fun
At this point, you should have a working Kafka broker running on your
machine. The next steps are to compile the example programs and play
around with the way that they work.

### Step 4: Compile and run the example programs

### Step 5: Run the example producer

The producer will send a large number of messages to `vacations`. Since there isn't
any consumer running yet, nobody will receive the messages. 

```
Sending message :0
Sending message :1
messages are send successfully!
...
Sending message :99999
Sending message :100000
messages are send successfully!
```
### Step 6: Start the example consumer
Running the consumer will not actually cause any messages to be
processed. The reason is that the first time that the consumer is run,
this will be the first time that the Kafka broker has ever seen the
consumer group that the consumer is using. That means that the
consumer group will be created and the default behavior is to position
newly created consumer groups at the end of all existing data.
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
After running the consumer once, however, if we run the producer again
and then run the consumer *again*, we will see the consumer pick up
and start processing messages shortly after it starts.


```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
mobile no:0 message:"Wishing you all a happy new year 2018!"
mobile no:1 message:"Wishing you all a happy new year 2018!"
...
mobile no:99999 message:"Wishing you all a happy new year 2018!"
mobile no:100000 message:"Wishing you all a happy new year 2018!"
```