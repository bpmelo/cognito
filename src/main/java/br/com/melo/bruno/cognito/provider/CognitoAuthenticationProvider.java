package br.com.melo.bruno.cognito.provider;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class CognitoAuthenticationProvider implements AuthenticationProvider {

    @Value("${authentication.provider.cognito.region}")
    private String region;

    @Value("${authentication.provider.cognito.poolId}")
    private String poolId;

    @Value("${authentication.provider.cognito.clientId}")
    private String clientId;

    @Value("${authentication.provider.cognito.clientSecret}")
    private String clientSecret;

    private DefaultAWSCredentialsProviderChain awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getName();
        String password = authentication.getCredentials().toString();

        Map<String, String> params = new HashMap<>();
        params.put("USERNAME", userId);
        params.put("SECRET_HASH", calculateSecretHash(userId));
        params.put("PASSWORD", password);

        AdminInitiateAuthRequest request = new AdminInitiateAuthRequest()
                .withUserPoolId(poolId)
                .withClientId(clientId)
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withAuthParameters(params);

        AWSCognitoIdentityProvider identityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(awsCredentialsProvider).withRegion(region).build();

        AdminInitiateAuthResult result = identityProvider.adminInitiateAuth(request);

        if (ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equals(result.getChallengeName())) {
            // Handle new password requirement
            //throw new RuntimeException("NEW PASSWORD REQUIRED: " + result.getChallengeName());
        }

        return new UsernamePasswordAuthenticationToken(
                userId, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private String calculateSecretHash(String userName) {

        SecretKeySpec signingKey = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8),
                HmacAlgorithms.HMAC_SHA_256.toString());
        try {
            Mac mac = Mac.getInstance(HmacAlgorithms.HMAC_SHA_256.toString());
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception ex) {
            throw new RuntimeException("Error calculating secret hash", ex);
        }
    }

}