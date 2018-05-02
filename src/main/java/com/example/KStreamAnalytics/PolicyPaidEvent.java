package com.example.KStreamAnalytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyPaidEvent {

    private String customerId;
    private String policyType;
    private long amount;
    private String state;
}
