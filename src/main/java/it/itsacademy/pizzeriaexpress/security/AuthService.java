package it.itsacademy.pizzeriaexpress.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication getAuthentication(HttpServletRequest request);
}
