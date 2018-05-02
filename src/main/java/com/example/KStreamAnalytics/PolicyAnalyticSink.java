package com.example.KStreamAnalytics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class PolicyAnalyticSink {

    Log log = LogFactory.getLog(getClass());

    @StreamListener
    public void process(@Input(ChannelBindings.AMOUNT_BY_STATE_IN)KTable<String, Long> amount)
    {
        System.out.println(" Incoming messae on analytic....");
        amount.toStream().foreach(((key, value) -> log.info("Message By State " + key +" =  "+ value)));
       // amount.foreach(((key, value) -> log.info("Message By State " + key +" =  "+ value)));
    }

}
