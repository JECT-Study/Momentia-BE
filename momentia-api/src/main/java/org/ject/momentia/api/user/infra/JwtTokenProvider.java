package org.ject.momentia.api.user.infra;

import java.util.Base64;
import java.util.Date;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.user.model.AuthorizationToken;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
	private final UserRepository userRepository;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access-token-validity-in-seconds}")
	private long accessTokenValidityInSeconds;

	@Value("${jwt.refresh-token-validity-in-seconds}")
	private long refreshTokenValidityInSeconds;

	private JwtParser jwtParser;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		jwtParser = Jwts.parser()
			.verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build();
	}

	public AuthorizationToken createTokenInfo(User user) {
		return new AuthorizationToken(createAccessToken(user), createRefreshToken(user));
	}

	public AuthorizationToken createTokenInfo(String refreshToken) {
		var userId = jwtParser.parseSignedClaims(refreshToken)
			.getPayload()
			.get("id", Long.class);

		var user = userRepository.findById(userId).orElseThrow(ErrorCd.NOT_AUTHORIZED::serviceException);
		var result = createTokenInfo(user);

		RefreshTokenHolder.removeUserRefreshToken(refreshToken);
		RefreshTokenHolder.setRefreshToken(result.refreshToken(), user.getId());
		return result;
	}

	public String createAccessToken(User user) {
		var claims = Jwts.claims()
			.subject(user.getEmail())
			.add("id", user.getId())
			.build();

		return createToken(claims, accessTokenValidityInSeconds);
	}

	public String createRefreshToken(User user) {
		var claims = Jwts.claims()
			.add("id", user.getId())
			.build();

		return createToken(claims, refreshTokenValidityInSeconds);
	}

	private String createToken(Claims claims, long validityInSeconds) {
		var now = new Date();
		var validity = new Date(now.getTime() + validityInSeconds * 1000);
		var key = Keys.hmacShaKeyFor(secretKey.getBytes());

		return Jwts.builder()
			.claims(claims)
			.issuedAt(now)
			.expiration(validity)
			.signWith(key)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			jwtParser.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			if (e instanceof SecurityException || e instanceof MalformedJwtException) {
				log.debug("invalid jwt sign - token: {}, exception: {}", token, e);
			} else if (e instanceof ExpiredJwtException) {
				log.debug("expired jwt token - token: {}, exception: {}", token, e);
			} else if (e instanceof UnsupportedJwtException) {
				log.debug("unsupported jwt token - token: {}, exception: {}", token, e);
			} else if (e instanceof IncorrectClaimException) {
				log.debug("incorrect jwt token - token: {}, exception: {}", token, e);
			}
		}
		return false;
	}

	public Authentication getAuthentication(String token) {
		var claims = jwtParser.parseSignedClaims(token)
			.getPayload();
		var userId = claims.get("id", Long.class);

		var user = userRepository.findById(userId).orElseThrow(ErrorCd.NOT_AUTHORIZED::serviceException);
		var principal = CustomUserDetails.from(user);

		return new UsernamePasswordAuthenticationToken(principal,token,principal.getAuthorities());
		// 기존 코드 -> return new UsernamePasswordAuthenticationToken(principal, token);
	}

	public Long parseAccessToken(String token) {
		var claims = jwtParser.parseSignedClaims(token)
			.getPayload();

		return claims.get("id", Long.class);
	}
}