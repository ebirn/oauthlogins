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

    Properties prop = new Properties();

    @PostConstruct
    public void init() throws IOException {


        prop.load(getClass().getResourceAsStream("oauth.properties"));



        service = buildService().build();
    }

    public abstract String getName();

    public abstract Class<? extends Api> getApiClass();

    public abstract String extractVerifier(HttpServletRequest request);

    protected  String extractVerifier(String verifierStr) {
        System.out.println("GOT verifier: " + verifierStr);

        this.verifier = new Verifier(verifierStr);

    return verifierStr;
}

    public abstract void updateLoginInfo(LoginInfo info);

    public String requestAuthUrl() {
        return requestAuthUrl(null);
    }

    protected String requestAuthUrl(Token requestToken) {

        String authUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("authUrl: " + authUrl);

        return authUrl;
    }

    public String requestAccessToken() {

        if(accessToken == null) {
            accessToken = service.getAccessToken(requestToken, verifier);
        }
        return accessToken.getToken();
    }

    public ServiceBuilder buildService() {
        ServiceBuilder builder;
        builder = new ServiceBuilder().provider(getApiClass());
        builder.apiKey(prop.getProperty(getName().toLowerCase()+".api.key"));
        builder.apiSecret(prop.getProperty(getName().toLowerCase()+".api.secret"));


        // send redirects to our own login servlet
        //String contextPath = facesContext.getExternalContext().getRequestContextPath();
        //String callbackUrl = contextPath + "/login";
        //builder.callback(callbackUrl);
        builder.callback(prop.getProperty("local.callback"));
        builder.debug();

        return builder;
    }

    public OAuthService getService() {
        return service;
    }

    @Override
    public String toString() {
        return "AuthApi: " + getName();
    }

    public Response getUserData() {

        OAuthRequest request = new OAuthRequest(Verb.GET, prop.getProperty(getName().toLowerCase()+".api.userinfo"));

        service.signRequest(getAccessToken(), request);
        Response response = request.send();

        return response;
    }

    protected Token getAccessToken() {
        if(accessToken == null) {
            requestAccessToken();
        }
        return accessToken;
    }

}
