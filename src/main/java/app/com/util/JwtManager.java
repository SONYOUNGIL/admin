package app.com.util;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; 

public class JwtManager { 
    
    private final String securityKey = "TYS Application"; 

    private final Long expiredTime = 1000 * 60L * 60L * 3L; // 유효시간 3시간 

    /** 
     * User 정보를 담은 JWT 토큰을 생성한다. 
     * @param member User 정보를 담은 객체 * 
     * @return String JWT 토큰 
     */ 
    public String generateJwtToken(User user) { 
        Date now = new Date(); 
        return Jwts.builder().setSubject(user.getUserId()) // 보통 userId 
        .setHeader(createHeader()) .setClaims(createClaims(user)) // 클레임, 토큰에 포함될 정보 
        .setExpiration(new Date(now.getTime() + expiredTime)) // 만료일 
        .signWith(SignatureAlgorithm.HS256, securityKey) 
        .compact(); 
    } 
    
    private Map<String, Object> createHeader() { 
        Map<String, Object> header = new HashMap<>(); 
        header.put("typ", "JWT"); 
        header.put("alg", "HS256"); // 해시 256 사용하여 암호화 
        header.put("regDate", System.currentTimeMillis()); 
        return header; 
    } 
    
    /** 
     * 클레임(Claim)을 생성한다. 
     * @param member 토큰을 생성하기 위한 계정 정보를 담은 객체 
     * @return Map<String, Object> 클레임(Claim) 
     */ 
    private Map<String, Object> createClaims(User user) { 
        Map<String, Object> claims = new HashMap<>(); 
        claims.put("userId", user.getUserId()); // userId 
        return claims; 
    }

    /** 
     * Token 에서 Claim 을 가져온다.
     * @param token JWT 토큰 
     * @return Claims 클레임 
     */ 
    private Claims getClaims(String token) { 
        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody(); 
    }

    /** 
     * 토큰으로 부터 userId 을 가져온다. 
     * @param token JWT 토큰 
     * @return String User 의 userId 
     */
    public String getUsernameFromToken(String token) { 
        return (String) getClaims(token).get("userId"); 
    }
}
