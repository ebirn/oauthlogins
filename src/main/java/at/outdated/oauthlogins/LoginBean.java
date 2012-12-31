package at.outdated.oauthlogins;

import at.outdated.oauthlogins.providers.AuthApiProvider;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
@RequestScoped
@ManagedBean
public class LoginBean {

    @Inject
    BeanManager manager;

    @Inject
    Instance<AuthApiProvider> apiProviders;

    public void forward() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExternalContext ectx = ctx.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login");

            dispatcher.forward(request, response);
            ctx.responseComplete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<AuthApiProvider> getApiProviders() {

        List<AuthApiProvider> providerList = new ArrayList<>();

        Iterator<AuthApiProvider> it = apiProviders.iterator();
        while(it.hasNext()) {
            providerList.add(it.next());
        }

        return providerList;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        final HttpServletRequest request = (HttpServletRequest)ec.getRequest();
        request.getSession( false ).invalidate();

        return "/index.xhtml";
    }
}
