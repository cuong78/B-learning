package com.example.demo.config;

import com.example.demo.entity.Account;
import com.example.demo.exception.exceptions.AuthorizeException;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;


@Component
//OcenPerRequestFilter đảm bảo fillter chỉ chạy mootj lần cho 1 request
public class Filter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    @Autowired
    TokenService tokenService;

    //List.of mình đang gán cứng dữ liệu ,  ko cho phép thêm xóa sửa khi gọi biến public_api
    // list.of ko được null
    List<String> PUBLIC_API = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/login",
            "/api/register",
            "/api/refresh-token",
            "/api/logout",
            "/api/reset-password",
            "/api/forgot-password"

    );

    //kiểm tra xem request có thuộc danh sách PUBLIC_API
    boolean isPermitted(HttpServletRequest request){
        AntPathMatcher patchMatch = new AntPathMatcher();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        // kiểm tra thử uri và method của request có bằng với dữ liệu mình cấu hin
        if(method.equals("GET") && patchMatch.match("/api/product/**", uri)){
            return true; // public api
        }
        // chuyển đổi list thành một stream
        return PUBLIC_API.stream()
                // trong stream có thư viện anyMatch
                // anyMatch duyệt qua từng phần tử
                // item có nghĩa đang đại diện cho list publis_api
                // so sánh uri  với item
                .anyMatch(item -> patchMatch.match(item, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if(isPermitted(request)){
            // public API
            filterChain.doFilter(request,response);
        }else{
            // không phải là public API => check role
            String token = getToken(request);

            if(token == null){
                // chưa đăng nhập => quăng lỗi
                resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is missing!"));
            }

            Account account = null;
            try{
                account = tokenService.getAccountByToken(token);
            }catch (MalformedJwtException malformedJwtException){
                resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is invalid!"));
            }catch (ExpiredJwtException expiredJwtException){
                resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is expired!"));
            }catch (Exception exception){
                resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is invalid!"));
            }

            // => token chuẩn
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account, token, account.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request,response);
        }
    }

    String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null) return null;
        return token.substring(7);
    }


}
