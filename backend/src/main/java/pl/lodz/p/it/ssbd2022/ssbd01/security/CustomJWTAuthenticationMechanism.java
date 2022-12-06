package pl.lodz.p.it.ssbd2022.ssbd01.security;

import io.jsonwebtoken.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomJWTAuthenticationMechanism implements HttpAuthenticationMechanism {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String BEARER = "Bearer ";

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) {

        if (httpServletRequest.getRequestURL().toString().endsWith("api/auth")
                || httpServletRequest.getRequestURL().toString().contains("api/auth/refresh")
                || httpServletRequest.getRequestURL().toString().contains("api/mok/create/register")
                || httpServletRequest.getRequestURL().toString().contains("api/mok/create/confirmation")
                || httpServletRequest.getRequestURL().toString().contains("api/mok/read/serviceProvider")
                || httpServletRequest.getRequestURL().toString().contains("api/mok/update/password-reset")
                || httpServletRequest.getRequestURL().toString().contains("api/mo/view/service_providers")
                || httpServletRequest.getRequestURL().toString().endsWith("api/mo/view/allActive")
                || httpServletRequest.getRequestURL().toString().contains("api/mo/view/byId")
                || httpServletRequest.getRequestURL().toString().endsWith("api/mo/view")
                || httpServletRequest.getRequestURL().toString().endsWith("api/mo/view/offerDates")
                || httpServletRequest.getRequestURL().toString().contains("api/mo/view/byPrice")
                || (httpServletRequest.getPathInfo() == null)) {
            return httpMessageContext.doNothing();
        }

        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            return httpMessageContext.responseUnauthorized();
        }

        String jwtSerializedToken = authorizationHeader.substring(BEARER.length()).trim();

        if (JWTGeneratorVerifier.validateJWTSignature(jwtSerializedToken)) {
            Claims claims = JWTGeneratorVerifier.decodeJWT(jwtSerializedToken);
            String login = claims.getSubject();
            String groups = (String) claims.get("auth");

            Set<String> stringSet = Arrays.stream(groups.split(","))
                    .map(x -> mapRoles.get(x))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return httpMessageContext.notifyContainerAboutLogin(login, stringSet);
        } else {
            return httpMessageContext.responseUnauthorized();
        }
    }

    private static Map<String, Set<String>> mapRoles = Map.of(
            "Admin", Stream.of("Admin", "ALL", "AdminService", "AdminRenter").collect(Collectors.toSet()),
            "Renter", Stream.of("Renter","ALL", "AdminRenter" ,"RenterService").collect(Collectors.toSet()),
            "ServiceProvider", Stream.of("ServiceProvider", "ALL", "AdminService", "RenterService").collect(Collectors.toSet())
    );
}
