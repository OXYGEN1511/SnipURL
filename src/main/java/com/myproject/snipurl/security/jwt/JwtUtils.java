package com.myproject.snipurl.security.jwt;

import com.myproject.snipurl.services.UserDetailImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int  jwtExpiration;

    public String getJWTFromHeader(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token !=null && token.startsWith("Bearer ")){
            return token.substring(7);
        }

        return  null;

    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(UserDetailImpl userDetail){
        String username = userDetail.getUsername();
        String roles = userDetail.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.joining(","));
        return Jwts.builder().subject(username).claim("roles",roles).issuedAt(new Date()).expiration(new Date(new Date().getTime()+jwtExpiration)).signWith(key()).compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
            return true;

        } catch (JwtException | IllegalArgumentException ex){
            throw new RuntimeException(ex);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
