package com.investorriskreturnprofiling.aml.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonalDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PersonalDetails getPersonalDetailsSample1() {
        return new PersonalDetails().id(1L).fullName("fullName1");
    }

    public static PersonalDetails getPersonalDetailsSample2() {
        return new PersonalDetails().id(2L).fullName("fullName2");
    }

    public static PersonalDetails getPersonalDetailsRandomSampleGenerator() {
        return new PersonalDetails().id(longCount.incrementAndGet()).fullName(UUID.randomUUID().toString());
    }
}
