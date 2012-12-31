package at.outdated.oauthlogins;

import at.outdated.oauthlogins.providers.AuthApiProvider;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */

@Named
@SessionScoped
public class LoginInfo implements Serializable {


    private String username;

    private String remoteId;

    private String nickname;

    private String password;

    private String email;

    private String accessToken;

    private String remoteApi;

    @Inject
    Instance<AuthApiProvider> apiProviders;

    public AuthApiProvider getApiProvider() {

        Iterator<AuthApiProvider> it = apiProviders.iterator();
        while(it.hasNext()) {
            AuthApiProvider p = it.next();
            if(p.getName().equalsIgnoreCase(remoteApi)) return p;
        }

        return null;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemoteApi() {
        return remoteApi;
    }

    public void setRemoteApi(String remoteApi) {
        this.remoteApi = remoteApi;
    }
}
