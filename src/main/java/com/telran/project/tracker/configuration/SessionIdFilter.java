package com.telran.project.tracker.configuration;

import com.telran.project.tracker.model.entity.ProjectUserSession;
import com.telran.project.tracker.repository.ProjectUserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class SessionIdFilter extends OncePerRequestFilter {

    @Autowired
    private ProjectUserSessionRepository projectUserSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null) {
            ProjectUserSession projectUserSession = projectUserSessionRepository.findBySessionIdAndIsValidTrue(header);
            if (projectUserSession != null) {

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        projectUserSession.getProjectUser(),
                        null,
                        Collections.emptyList());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


}
