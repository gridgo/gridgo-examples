package io.gridgo.example.kafka_consumer;

import io.gridgo.bean.BObject;
import io.gridgo.core.GridgoContext;
import io.gridgo.core.impl.DefaultGridgoContextBuilder;
import io.gridgo.core.support.RoutingContext;
import io.gridgo.framework.support.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by nauh94@gmail.com on 2019-04-11
 */
@Slf4j
public class Main {

    private static final String KAFKA_BROKER = "your_kafka_broker_host:9092"; // replace this config with your

    private static final String KAFKA_CONNECTION_URI = "kafka:gridgo_example?brokers=" + KAFKA_BROKER + "&format=json";

    private static final String KAFKA_CONSUMER = KAFKA_CONNECTION_URI + "&groupId=gridgo_examples&mode=consumer" + //
            "&consumersCount=1&autoCommitEnable=true&autoOffsetReset=latest";

    private static final String KAFKA_PRODUCER = KAFKA_CONNECTION_URI + "&mode=producer";

    private static final String APPLICATION_NAME = "kafka.consumer.example";

    private static final String PRODUCER_GATEWAY_NAME = "kafka.producer.gateway";

    private static final String CONSUMER_GATEWAY_NAME = "kafka.consumer.gateway";

    public static void main(String[] args) {
        var context = new DefaultGridgoContextBuilder().setName(APPLICATION_NAME).build();
        context.openGateway(CONSUMER_GATEWAY_NAME) // open a gateway
                .attachConnector(KAFKA_CONSUMER) // attach a consumer
                .subscribe(Main::handleMessages); // subscribe for incoming messages

        context.openGateway(PRODUCER_GATEWAY_NAME) // open another gateway
                .attachConnector(KAFKA_PRODUCER); // attach a producer

        context.start(); // start this context
        // then sending a greeting message
        context.findGatewayMandatory(PRODUCER_GATEWAY_NAME)
                .sendWithAck(Message.ofAny(BObject.of("greeting", "hello everyone!")))
                .done(message -> log.info("Producer send message success!"))
                .fail(Exception::printStackTrace);

        Runtime.getRuntime().addShutdownHook(new Thread(context::stop));
    }

    private static void handleMessages(RoutingContext rc, GridgoContext gx) {
        var msg = rc.getMessage();
        var deferred = rc.getDeferred();
        // just log the message
        log.info("Received a message: " + msg.getPayload().getBody().toJson());
        // must do this statement to make a kafka consumer's commit
        deferred.resolve(Message.ofAny("success"));
    }
}
