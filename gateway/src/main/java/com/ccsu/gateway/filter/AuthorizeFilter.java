package com.ccsu.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(0) //过滤链执行优先级
@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
//        ServerHttpRequest request = exchange.getRequest();
//        HttpHeaders headers = request.getHeaders();
//        String token = headers.getFirst("Token");
//        if(token!=null) {
//            if (token.equals("1111")) {
//                //放行
//                return chain.filter(exchange);
//            }
//        }
//        //拦截
////        return exchange.getResponse().setComplete();
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        return exchange.getResponse().setComplete();
    }
}
