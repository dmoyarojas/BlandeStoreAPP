package com.example.demo.interceptor;

import com.example.demo.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null && "admin".equals(usuario.getRol())) {
                return true; // El usuario es admin, permitir el acceso
            }
        }
        
        // Si no es admin, redirigir al login
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return false;
    }
}
