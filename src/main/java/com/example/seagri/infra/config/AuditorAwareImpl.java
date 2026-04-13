package com.example.seagri.infra.config;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<Long> {

    @SuppressWarnings("null")
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of(0L); // ação de sistema sem usuário autenticado
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserProfile) {
            return Optional.of(((UserProfile) principal).user.getId());
        }
        

        return Optional.of(0L);

    }
}