package hsge.hsgeback.util;

import hsge.hsgeback.exception.AccessTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JWTUtil {

    @Value("${secret.jwt.expire-days}")
    private int expireDays;

    @Value("${secret.jwt.refresh-days}")
    private int refreshDays;

    @Value("${secret.jwt.hash-key}")
    private String key;

    public String generateAccessToken(Map<String, Object> valueMap) {
        return generateToken(valueMap, expireDays);
    }

    public String generateRefreshToken(Map<String, Object> valueMap) {
        return generateToken(valueMap, refreshDays);
    }

    public String generateToken(Map<String, Object> valueMap, int days) {

        HashMap<String, Object> headers = new HashMap<>();

        //header
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload
        HashMap<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        int time = (60 * 24) * days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String, Object> validateToken(String token) throws JwtException {

        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }

    public String getEmail(HttpServletRequest request) throws JwtException {
        String headerStr = request.getHeader("Authorization");

        if (headerStr == null || headerStr.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if (!tokenType.equalsIgnoreCase("Bearer")) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        Map<String, Object> claim = validateToken(tokenStr);
        return (String) claim.get("email");
    }
}
