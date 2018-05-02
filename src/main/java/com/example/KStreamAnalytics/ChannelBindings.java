package com.example.KStreamAnalytics;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface ChannelBindings {

    String POLICY_PAID = "policyPaid";

    String POLICY_PAID_IN = "policyPaidIn";

    String AMOUNT_BY_STATE = "policyPaidAnalytic";

    String AMOUNT_BY_STATE_IN = "policyPaidAnalyticIn";

    @Input(POLICY_PAID_IN)
    KStream<String,PolicyPaidEvent> policyPaidIn();

    @Output(POLICY_PAID)
    MessageChannel policyPaid();

    @Output(AMOUNT_BY_STATE)
    KStream<String,Long> amountByState();


    @Input(AMOUNT_BY_STATE_IN)
    KTable<String,Long> amountByStateIn();

}
