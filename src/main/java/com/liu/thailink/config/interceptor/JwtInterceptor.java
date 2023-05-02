package com.liu.thailink.config.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.liu.thailink.common.Constants;
import com.liu.thailink.entities.User;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.httpervletRequest;
import javax.servlet.http.httpervletResponse;


public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(httpervletRequest request, httpervletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
        int port = request.getRemotePort();

        System.out.println("received from" + ipAddress + ":" + port);

        // check token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            throw new ServiceException(Constants.CODE_401, "no token, please login again");
        }

        // get user ID from token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException(Constants.CODE_401, "401 token verification failed");
        }

        // query the database according to the token
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(Constants.CODE_401, "the user does not exist");
        }

        // use encoded password as token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // verify token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "401 token verification failed");
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
