package pl.lodz.p.it.ssbd2022.ssbd01.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.annotation.security.PermitAll;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Set;

public class JWTGeneratorVerifier {

    private static final String SECRET = "CxJg-jun3gY4CuLcNBtM1Fl9BqbsyeWglTymfgIq-5A";
    private static final long ACCESS_JWT_TIMEOUT_MS = 30 * 60 * 1000;
    private static final long REFRESH_JWT_TIMEOUT_MS = 24 * 60 * 60 * 1000;

    @PermitAll
    public static String generateAccessJWTString(String username, Set<String> callerGroups) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("auth", String.join(",", callerGroups))
                .setIssuer("SSBD01 Chads")
                .setIssuedAt(now)
                .setExpiration(new Date(new Date().getTime() + ACCESS_JWT_TIMEOUT_MS))
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWT");

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    @PermitAll
    public static String generateRefreshJWTString(String username) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuer("SSBD01 Chads")
                .setExpiration(new Date(new Date().getTime() + REFRESH_JWT_TIMEOUT_MS))
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWT");

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    @PermitAll
    public static boolean validateJWTSignature(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @PermitAll
    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }
}
