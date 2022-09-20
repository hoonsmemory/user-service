package me.hoon.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoon.userservice.domain.dto.UserLoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * UsernamePasswordAuthenticationFilter
 * 로그인을 하게 되면 인증 처리를 하게 된다.
 * 그 인증 처리에 관련된 요청을 처리하는 필터다.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    ObjectMapper objectMapper;

    /*
        요청 정보를 보냈을 때 처리해줄 수 있는 메서드
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            UserLoginRequestDto dto
                    = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);

            //인증 정보 만들기, 인증처리를 하기 전에 스프링 시큐리티가 이해할 수 있는 토큰으로 만들어줘야 한다.
            return new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //로그인 성공 시 후 처리
    //가령 토큰을 만든다면 만료 시간이 언제인지, 사용자에게 반환 값으로 어떤 것을 줄건지 결정한다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
