package org.mixit.cesar.site.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter which detects that the request is coming from a social network or web crawler user agent (facebook, twitter,
 * Google, etc.) and, if it's the case, for certain URLs, forwards to an "old-school" Spring controller which generates
 * a page at server-side specific for the bots
 *
 * @author JB Nizet
 */
@Component
public class BotFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest){
            HttpServletRequest req = (HttpServletRequest) request;
            if (mustFowardToBotUrl(req)) {
                request.getRequestDispatcher("/bot" + req.getRequestURI()).forward(request, response);
            }
            else{
                filterChain.doFilter(request, response);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean mustFowardToBotUrl(HttpServletRequest request) {
        return request.getMethod().equals("GET")
                && hasBotUserAgent(request)
                && hasShareableUri(request);
    }

    private boolean hasShareableUri(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/home") || request.getRequestURI().equals("/") || request.getRequestURI().startsWith("/index.html");
    }

    private boolean hasBotUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return false;
        }
        return userAgent.toLowerCase().startsWith("facebookexternalhit")
                || userAgent.toLowerCase().contains("http://www.google.com/webmasters/tools/richsnippets")
                || userAgent.toLowerCase().contains("+https://developers.google.com/+/web/snippet/")
                || userAgent.toLowerCase().startsWith("twitterbot")
                || userAgent.toLowerCase().contains("googlebot")
                || userAgent.toLowerCase().contains("bingbot")
                || userAgent.toLowerCase().contains("yahoo! slurp")
                || userAgent.toLowerCase().contains("duckduckbot");
    }


    @Override
    public void destroy() {

    }
}
