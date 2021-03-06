package com.quicktutorial.learnmicroservices.CouponMicroservice.controllers;

import com.quicktutorial.learnmicroservices.CouponMicroservice.entities.JsonResponseBody;
import com.quicktutorial.learnmicroservices.CouponMicroservice.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CouponService couponService;

    @CrossOrigin
    @RequestMapping("/findCoupon")
    public ResponseEntity<JsonResponseBody> findCoupons(@RequestParam(value="jwt") String jwt) {
        String coupons = couponService.getAvailableCoupon(jwt);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), coupons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Error: "+ e.toString()));
        }
    }

    @RequestMapping("/test")
    public String test() {
        return "CouponService works correctly";
    }
}
