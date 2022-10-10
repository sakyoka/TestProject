package com.csy.test.webui.jarmanage.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.csy.test.commons.utils.Properties;
import com.csy.test.commons.utils.UUID;

/**
 * 
 * 描述：jwt token生成、校验
 * @author csy
 * @date 2022年8月31日 下午5:20:35
 */
public class JwtTokenUtils {
	
	private JwtTokenUtils(){}
	
	/**有效时长：默认半个小时*/
	public static Long EXPIRATION_TIME = 
			Long.valueOf(Properties.get("login.jwt.expiration_date", Long.valueOf(30 * 60 * 1000).toString()));
	
	/**token秘钥*/
	private static String TOKEN_SECRET = Properties.get("login.jwt.token_secret", "sakyoka");
	
	/**签发主体*/
	private static String TOKEN_USER = Properties.get("login.jwt.token_user", "sakyoka");
	
	/**
	 * 
	 * 描述：生成token
	 * @author csy
	 * @date 2022年9月6日 上午9:38:33
	 * @param userName
	 * @param expriationTime
	 * @return token
	 */
	public static String generateToken(String userName, long expriationTime){
		String token = JWT.create()
				.withJWTId(UUID.getString())
				.withIssuer(TOKEN_USER)
				.withClaim("username", userName)
				.withExpiresAt(new Date(System.currentTimeMillis() + expriationTime))
				.sign(Algorithm.HMAC256(TOKEN_SECRET));
		return token;
	}
	
	/**
	 * 
	 * 描述：生成access_token
	 * @author csy
	 * @date 2022年9月6日 上午9:38:01
	 * @param userName
	 * @param expriationTime
	 * @return access_token
	 */
	public static String generateAccessToken(String userName){
		//5分钟过期
		return generateToken(userName, 5 * 60 * 1000);
		//return generateToken(userName, EXPIRATION_TIME);
	}
	
	/**
	 * 
	 * 描述：生成refresh_token
	 * @author csy
	 * @date 2022年9月6日 上午9:36:49
	 * @param userName
	 * @param expriationTime
	 * @return refresh_token
	 */
	public static String generateRefreshToken(String userName){
		return generateToken(userName, EXPIRATION_TIME);
	}
	
	/**
	 * 
	 * 描述：token校验
	 * @author csy
	 * @date 2022年8月31日 下午5:21:41
	 * @param token
	 * @return true有效，false无效
	 */
	public static boolean tokenValid(String token){

		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
					.withIssuer(TOKEN_USER).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			Claim userClaim = decodedJWT.getClaim("username");
			if (userClaim.isNull()){
				System.out.println("token get username is null");
				return false;
			}
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 描述：获取DecodedJWT
	 * @author csy
	 * @date 2022年9月1日 上午10:01:33
	 * @param token
	 * @return DecodedJWT
	 */
	public static DecodedJWT getJwt(String token){
		return JWT.decode(token);
	}
}
