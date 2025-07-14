package com.example.card_cost_api.auth;

import com.example.card_cost_api.domain.User;
import com.example.card_cost_api.persistence.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PrivateKey privateKey;

    public AuthService(UserRepository userRepository,
                       @Value("classpath:keys/privateKey.pem") org.springframework.core.io.Resource keyResource) throws Exception {
        this.userRepository = userRepository;
        this.privateKey = loadPrivateKey(keyResource.getInputStream());
    }

    public Optional<String> authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            String token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim("groups", user.getRole()) // ή "roles"
                    .setIssuer("helpdesk-system")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1h
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

            return Optional.of(token);
        }

        return Optional.empty();
    }

    private PrivateKey loadPrivateKey(InputStream keyInputStream) throws Exception {
        String privateKeyPEM = new String(keyInputStream.readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
