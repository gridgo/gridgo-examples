package io.gridgo.example.gridgoboot.gateways;

import static io.gridgo.example.gridgoboot.Constant.GATEWAY_HTTP_SERVER;
import static io.gridgo.example.gridgoboot.Constant.GATEWAY_KAFKA_PRODUCER;

import lombok.Setter;
import io.gridgo.bean.BObject;
import io.gridgo.boot.support.annotations.Connector;
import io.gridgo.boot.support.annotations.Gateway;
import io.gridgo.boot.support.annotations.GatewayInject;
import io.gridgo.connector.support.MessageProducer;
import io.gridgo.core.GridgoContext;
import io.gridgo.core.impl.AbstractProcessor;
import io.gridgo.core.support.RoutingContext;
import io.gridgo.framework.support.Message;
import io.gridgo.framework.support.Payload;

/**
 * Created by nauh94@gmail.com on 2019-04-12
 */
@Setter
@Gateway(GATEWAY_HTTP_SERVER)
@Connector("${http_server_endpoint}")
public class HttpServerGateway extends AbstractProcessor {

    @GatewayInject(GATEWAY_KAFKA_PRODUCER)
    private MessageProducer producer;

    @Override
    public void process(RoutingContext rc, GridgoContext gc) {
        var msg = rc.getMessage();
        var deferred = rc.getDeferred();
        // push a received message to kafka
        producer.sendWithAck(Message.of(Payload.of(msg.getPayload().getBody())))
                .done(message -> deferred.resolve(
                        Message.ofAny(BObject.of("message", "we has received your order!"))))
                .fail(deferred::reject);
    }
}
