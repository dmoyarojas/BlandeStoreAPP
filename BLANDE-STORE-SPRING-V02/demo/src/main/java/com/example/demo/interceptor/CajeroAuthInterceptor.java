package com.example.demo.interceptor;

import com.example.demo.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CajeroAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            // Permitir acceso si el rol es 'cajero' o 'admin'
            if (usuario != null && ("cajero".equals(usuario.getRol()) || "admin".equals(usuario.getRol()))) {
                return true; 
            }
        }
        
        // Si no tiene el rol adecuado, redirigir al login principal
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
