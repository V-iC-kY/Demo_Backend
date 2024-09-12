package com.demo.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.demo.Model.TokenModel;
import com.demo.Model.UserModel;
import com.demo.Repository.TokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtToken {

	@Autowired
	private TokenRepo tokenRepoapi;

	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	private static final Key SECRET_KEY = Keys.secretKeyFor(SIGNATURE_ALGORITHM);
	private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

	public String generateToken(UserModel user) {

		Map<String, Object> claims = new HashMap<>();

		Date now = new Date();

		Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

		String token = Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setSubject(user.getEmail())
				.setIssuedAt(now).setExpiration(expiryDate).signWith(SECRET_KEY, SIGNATURE_ALGORITHM).compact();

		TokenModel tokenEntity = new TokenModel();

		tokenEntity.setToken(token);
		tokenEntity.setUserId(user.getId());
		tokenEntity.setExpiryDate(expiryDate);

		tokenRepoapi.save(tokenEntity);
		return token;
	}

//	========================================================================================================================

	public Claims verifyToken(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();

	}

}
