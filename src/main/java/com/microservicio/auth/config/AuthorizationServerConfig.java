package com.microservicio.auth.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.microservicio.auth.services.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  public static final String SCOPE_READ = "read";
  public static final String SCOPE_WRITE = "write";
  public static final String AUTHORIZED_GRANT_TYPE_PASSWORD = "password";
  public static final String AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String AUTHORIZED_GRANT_CLIENT_CREDENTIALS = "client_credentials";

  @Qualifier("authenticationManagerBean")
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Value("${security.oauth2.access-token-validity-seconds}")
  private Integer accessTokenValiditySeconds;

  @Value("${security.oauth2.refresh-token-validity-seconds}")
  private Integer refreshTokenValiditySeconds;

  @Value("${security.oauth2.client-id}")
  private String clientId;

  @Value("${security.oauth2.client-secret}")
  private String clientSecret;

  @Value("${security.oauth2.public-key}")
  private String publicKey;

  @Value("${security.oauth2.private-key}")
  private String privateKey;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {

    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {

      @Override
      public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
      }

      @Override
      public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication auth = super.extractAuthentication(map);
        auth.setDetails(map);
        return auth;
      }
    };

    converter.setSigningKey(privateKey);
    converter.setVerifierKey(publicKey);

    return converter;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
	//Aqui podria tener un check por ROLES security.checkTokenAccess("hasAuthority('ROLE_C')"); y seria mas seguro 
	oauthServer.checkTokenAccess("permitAll()");
    oauthServer.allowFormAuthenticationForClients();
  }

  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
	  
    endpoints
      .authenticationManager(authenticationManager)
      .userDetailsService(userService)
      .accessTokenConverter(accessTokenConverter())
      .tokenStore(tokenStore())
      .tokenEnhancer(accessTokenConverter());
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
      .inMemory()
      .withClient(clientId)
      .secret(passwordEncoder.encode(clientSecret))
      .scopes(SCOPE_READ, SCOPE_WRITE)
      .authorizedGrantTypes(AUTHORIZED_GRANT_TYPE_PASSWORD, AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN, AUTHORIZED_GRANT_CLIENT_CREDENTIALS)
      .accessTokenValiditySeconds(accessTokenValiditySeconds)
      .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
      .redirectUris("");
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {

    TokenEnhancerChain chain = new TokenEnhancerChain();
    chain.setTokenEnhancers(Collections.singletonList(accessTokenConverter()));

    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(Boolean.TRUE);
    defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
    defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
    defaultTokenServices.setAuthenticationManager(authenticationManager);
    defaultTokenServices.setTokenEnhancer(chain);

    return defaultTokenServices;
  }

}
