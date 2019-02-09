package com.quicktutorial.learnmicroservices.CouponMicroservice.services;

import com.quicktutorial.learnmicroservices.CouponMicroservice.daos.CouponDao;
import com.quicktutorial.learnmicroservices.CouponMicroservice.entities.Coupon;
import com.quicktutorial.learnmicroservices.CouponMicroservice.entities.JsonResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponDao couponDao;

    @Override
    public String getAvailableCoupon(String jwt) {
        List<LinkedHashMap> accounts = getAccountsGivenJwt(jwt);
        if(accounts!=null && accounts.size()>0) {
            String availableCoupons = "";
            for(int i=0; i<accounts.size(); i++) {
                LinkedHashMap account = accounts.get(i);
                String idAccount = (String) account.get("id");
                Optional<Coupon> coupon =  couponDao.findByAccount(idAccount);

                if(coupon.isPresent()) {
                    availableCoupons = availableCoupons + "CouponCode: " + coupon.get().getCode() + " (for Account: " + idAccount + ") ";
                }
            }

            return "Available coupons: " + availableCoupons;
        }

        return "No coupon available for the user";
    }

    /**
     * Make the call to the AccountMicroservice
     *
     * @param jwt Json web token
     * @return List of account
     */
    private List<LinkedHashMap> getAccountsGivenJwt(String jwt) {
        //preparing the header for the request (adding jwt)
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("jwt", jwt);
        HttpEntity<?> request = new HttpEntity(String.class, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonResponseBody> responseEntity = restTemplate.exchange("http://localhost:8094/accounts/user", HttpMethod.POST, request, JsonResponseBody.class);

        List<LinkedHashMap> accounts = (List) responseEntity.getBody().getResponse();
        return accounts;
    }
}
