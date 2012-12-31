package at.outdated.oauthlogins.providers;

import at.outdated.oauthlogins.LoginInfo;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.resource.ResourceException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
@SessionScoped
public abstract class AuthApiProvider implements Serializable {

    protected OAuthService service;

    protected Verifier verifier;
    protected Token accessToken;
    protected Token requestToken;

    protected Properties prop = new Properties();

    protected Logger log;

    @PostConstruct
    public void init() throws IOException {
        log = Logger.getLogger(getName());
        log.setLevel(Level.INFO);

        prop.load(getClass().getResourceAsStream("oauth.properties"));
        service = buildService().build();
    }

    public abstract String getName();

    public abstract Class<? extends Api> getApiClass();

    public abstract String extractVerifier(HttpServletRequest request);

    protected String extractVerifier(String verifierStr) {
        log.log(Level.FINE, "received verifier: {0}", verifierStr);
        this.verifier = new Verifier(verifierStr);
        return verifierStr;
    }

    public abstract void updateLoginInfo(LoginInfo info);

    public String requestAuthUrl() {
        return requestAuthUrl(null);
    }

    protected String requestAuthUrl(Token requestToken) {
        String authUrl = service.getAuthorizationUrl(requestToken);
        log.log(Level.FINE, "authUrl: {0}", authUrl);
        return authUrl;
    }

    public String requestAccessToken() {

        if (accessToken == null) {
            accessToken = service.getAccessToken(requestToken, verifier);
            log.log(Level.FINE, "new accessToken: {0}", accessToken);
        }
        return accessToken.getToken();
    }

    public ServiceBuilder buildService() {
        ServiceBuilder builder;
        builder = new ServiceBuilder().provider(getApiClass());

        // retrieve api secret/keys from config file
        builder.apiKey(prop.getProperty(getName().toLowerCase() + ".api.key"));
        builder.apiSecret(prop.getProperty(getName().toLowerCase() + ".api.secret"));


        // set callback from textfile: this url should be generated automatically
        builder.callback(prop.getProperty("local.callback"));

        String scope = prop.getProperty(getName().toLowerCase() + ".api.scope", null);

        if(scope != null) {
            builder.scope(scope);
        }

        if(log.getLevel().intValue() < Level.INFO.intValue()) {
            builder.debug();
        }

        return builder;
    }

    @Override
    public String toString() {
        return "AuthApi: " + getName();
    }

    public Response getUserData() {

        OAuthRequest request = new OAuthRequest(Verb.GET, prop.getProperty(getName().toLowerCase() + ".api.userinfo"));

        service.signRequest(getAccessToken(), request);
        Response response = request.send();

        return response;
    }

    protected Token getAccessToken() {
        if (accessToken == null) {
            requestAccessToken();
        }
        return accessToken;
    }

}
