package io.gridgo.example.gridgoboot.gateways;

import static io.gridgo.example.gridgoboot.Constant.GATEWAY_KAFKA_CONSUMER;

import io.gridgo.boot.support.annotations.Connector;
import io.gridgo.boot.support.annotations.Gateway;
import io.gridgo.core.GridgoContext;
import io.gridgo.core.impl.AbstractProcessor;
import io.gridgo.core.support.RoutingContext;
import io.gridgo.framework.support.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by nauh94@gmail.com on 2019-04-12
 */
@Gateway(GATEWAY_KAFKA_CONSUMER)
@Connector("${kafka_consumer_endpoint}")
@Slf4j
public class KafkaConsumerGateway extends AbstractProcessor {

    @Override
    public void process(RoutingContext rc, GridgoContext gc) {
        var msg = rc.getMessage();
        var deferred = rc.getDeferred();
        // log the received message
        log.info("Hey! Restaurant has received an order: " + msg.getPayload().getBody().toJson());
        // make a kafka consumer's commit
        deferred.resolve(Message.ofAny("success"));
    }
}
