package com.example.KStreamAnalytics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class PolicyEventProcessor {

    private  final Log log = LogFactory.getLog(getClass());

    final static String MATERIALIZE_BY_STATE = "mbstates";

    @StreamListener
    @SendTo(ChannelBindings.AMOUNT_BY_STATE)
    public KStream<String, Long> process(@Input (ChannelBindings.POLICY_PAID_IN)KStream<String,PolicyPaidEvent> events){
       // KTable<String, Long> kTable =
        System.out.println(" Processing Message .... ");
        log.info(" Processing Message .....");
       return  events.map((key,value) ->
                 new KeyValue<>(value.getState(),value.getAmount()))
               .groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
               .reduce(
                       ( value, aggvlue) -> {
                           long tmp = aggvlue + value;
                           System.out.println(" tmp is "+ tmp );
                           return new Long(tmp);
                           },
                       //Materialized.as(MATERIALIZE_BY_STATE))
                      Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(MATERIALIZE_BY_STATE).withKeySerde(Serdes.String()).
                       withValueSerde(Serdes.Long()))

               .toStream();
//               .aggregate(
//                       ()->0l,
//                       (key, value, aggvlue) -> {
//                           long tmp = aggvlue + value;
//                           System.out.println(" tmp is "+ tmp +" for key "+key);
//                           return new Long(tmp);
//                           },
//                       Materialized.as(MATERIALIZE_BY_STATE)
//                               .with(Serdes.String(), Serdes.Long())).toStream();
                //.groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
               //.aggregate(()-> 0L,
                 //      (aggKey, newValue, aggValue) -> aggValue + newValue),Serdes.Long(),"agg-sgore");

              // .count(Materialized.with(Serdes.String(), Serdes.Long()).as(MATERIALIZE_BY_STATE)).toStream();

    }

}
