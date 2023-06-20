package com.interviewtest.task2;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BusinessServiceTest {

    private static final BusinessService SERVICE = new BusinessService();

    @Test
    public void test() {
        Assert.assertEquals(SERVICE.calculate("directory_path"), 11);
    }

}
