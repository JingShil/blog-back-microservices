package com.ccsu.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1) //过滤链执行优先级
@Component
public class AuthorizeFilter implements GlobalFilter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private static final String[] ALLOWED_PATHS = {
            "/user/login",
            "/article/list",
            "/article/tag/list",
            "/user/admin/info",
            "/img/download",
            "/article/get-one",
            "/user/article/info",
    }; // 添加需要直接通过的路径

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        if (isAllowedPath(path)) {
            return chain.filter(exchange);
        }
        //1.得到session对象
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Token");
//        String token = request.getHeader("Token");
        String userId = headers.getFirst("UserId");
        String key = "blog:token:" + userId;
        Long ttl = redisTemplate.getExpire(key);

        if (ttl != null && ttl == -1) {
            // Key exists and is expired
            redisTemplate.delete(key);

        } else if (ttl != null && ttl == -2) {

        } else {
            String tokenRedis = redisTemplate.opsForValue().get(key);
            if(tokenRedis!=null){
                if(tokenRedis.equals(token)){
                    return chain.filter(exchange);
                }
            }
        }

//        TokenUtil tokenUtil = new TokenUtil();
//        if (token != null && tokenUtil.validateToken(token)) {
//            //说明已经登陆，可以放行
//            return true;
//        }

//        response.sendError(2000);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    // 判断路径是否是需要直接通过的路径
    private boolean isAllowedPath(String path) {
        for (String allowedPath : ALLOWED_PATHS) {
            if (allowedPath.equals(path)) {
                return true;
            }
        }
        return false;
    }
}
