package com.example.postgres.repository;

import com.example.postgres.classes.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    //Although not being used we can revoke all the tokens of a user using this
    @Query(value = "SELECT rt.* FROM REFRESH_TOKENS rt " +
            "INNER JOIN USER_DETAILS ud ON rt.user_id = ud.id " +
            "WHERE ud.EMAIL = :userEmail and rt.revoked = false ", nativeQuery = true)
    List<RefreshToken> findAllRefreshTokenByUserEmailId(String userEmail);
}
