package com.example.postgres.config.jwt;
import com.example.postgres.classes.Channel;
import com.example.postgres.classes.User;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import com.example.postgres.config.user.UserConfig;
import com.example.postgres.config.RSAKeyRecord;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private  final RSAKeyRecord rsaKeyRecord;
    private final UserRepository userInfoRepo;

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        //Username extracted from token
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        //Checking if the username in the token is same as the username in the database
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;

    }

    // Check if the token is expired
    //involves seeing the expiry time of the token and comparing it with the current time
    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public UserDetails userDetails(String emailId){
        //returning the user details from the database
        return userInfoRepo
                .findByEmail(emailId)
                .map(UserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+emailId+" does not exist"));
    }

    public UserDetailsDto getUserDetails(String header) {
        System.out.println("get Token process has started! ");
        final String token = header.substring(7);
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

        final Jwt jwtToken = jwtDecoder.decode(token);
        System.out.println(jwtToken.getSubject());

        User user = userInfoRepo.findByEmail(jwtToken.getSubject()).orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + jwtToken.getSubject() + " does not exist"));

//         Get all subscribed channels of the user but only the names by iterating and sttore in List<String>
        List<String> subscribedChannels = user.getSubscribedChannels().stream().map(Channel::getName).toList();


        return new UserDetailsDto(user.getName(),user.getUsername(),user.getEmail(),user.getId(), subscribedChannels);
    }

}
