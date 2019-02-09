package com.quicktutorial.learnmicroservices.CouponMicroservice.services;

public interface CouponService {

    String getAvailableCoupon(String jwt);
}
