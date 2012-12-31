package at.outdated.oauthlogins;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {


    @EJB
    OAuthLogin login;

    @Inject
    LoginInfo info;

    @Override
    public void init() throws ServletException {
        System.out.println("init LoginServlet");
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        System.out.println("LoginServlet: doPOST");

        if(info.getAccessToken() == null) {
            String authUrl = login.requestAuthUrl();
            httpServletResponse.sendRedirect(authUrl);
        }
        else {
            login.updateUserInfo();
            httpServletResponse.sendRedirect(getServletContext().getContextPath() + "/hello.xhtml");
        }
        //super.doPost(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {


        try {

            login.processVerifier(httpServletRequest);

            boolean loginOk = login.verifyLogin();

            if(loginOk) {
                //httpServletRequest.login("myUserName", "myPassword");
                login.updateUserInfo();
                // all is well
                httpServletResponse.sendRedirect(getServletContext().getContextPath() + "/hello.xhtml");


            }
            else {
                httpServletRequest.logout();
                httpServletResponse.sendError(403, "login failed");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        System.out.println("LoginServlet: service");
        System.out.println("UserInfo: " + myUserInfo);
        System.out.println("OAuthLogin: " + login);
        dummy.huh();




        super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }
    */
}


