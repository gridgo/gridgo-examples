package io.gridgo.example.gridgoboot.gateways;

import static io.gridgo.example.gridgoboot.Constant.GATEWAY_KAFKA_PRODUCER;

import io.gridgo.boot.support.annotations.Connector;
import io.gridgo.boot.support.annotations.Gateway;

/**
 * Created by nauh94@gmail.com on 2019-04-12
 */
@Gateway(GATEWAY_KAFKA_PRODUCER)
@Connector("${kafka_producer_endpoint}")
public class KafkaProducerGateway {

}
