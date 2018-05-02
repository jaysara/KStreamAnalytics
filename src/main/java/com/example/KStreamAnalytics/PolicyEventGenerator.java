package com.example.KStreamAnalytics;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Component
@EnableBinding(ChannelBindings.class)
public class PolicyEventGenerator implements ApplicationRunner{

    private final MessageChannel policyPaid;

    private final Log log = LogFactory.getLog(getClass());

    public PolicyEventGenerator(ChannelBindings channelBindings) {
        this.policyPaid = channelBindings.policyPaid();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runnable runnable =  () -> {
            String[] insurableStates = {"IL","WI","NY","MI","CA","TX"};
            String[] policyTypes = {"AUTO","HOME","LIFE"};

            String randomState = insurableStates[new Random().nextInt(insurableStates.length)];
            String randomPolicyType = policyTypes[new Random().nextInt(policyTypes.length)];
            Long randomLong= new Random().nextLong();
            PolicyPaidEvent policyPaidEvent = PolicyPaidEvent.builder().customerId(randomLong.toString()).policyType(randomPolicyType).state(randomState)
                    .amount(new Random().nextInt(1000)).build();

            Message<PolicyPaidEvent> message = MessageBuilder.withPayload(policyPaidEvent)
                    .setHeader(KafkaHeaders.MESSAGE_KEY,policyPaidEvent.getCustomerId().getBytes()).build();
            try {
                this.policyPaid.send(message);
                log.info(String.format("Message sent %s", message.toString()));
            }catch (Exception e){
                    System.out.println(" Exception while sending the message ! "+e);
                    log.error("Exception while sending the message ",e);
            }

        };
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable,1,10, TimeUnit.SECONDS);
    }
}
