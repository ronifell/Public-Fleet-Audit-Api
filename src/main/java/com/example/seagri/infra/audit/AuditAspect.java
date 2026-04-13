package com.example.seagri.infra.audit;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.seagri.infra.config.UserProfile;

/**
 * Aspecto de auditoria que intercepta automaticamente todas as operações
 * de escrita (save, update, delete) nos serviços da aplicação.
 *
 * Não é necessário modificar nenhum serviço — o log é gerado
 * de forma transparente via AOP.
 */
@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogService auditLogService;

    // ── Pointcuts ────────────────────────────────────────────────────────────

    @Pointcut("execution(* com.example.seagri.infra.service.*Service.save*(..)) || " +
              "execution(* com.example.seagri.infra.service.*Service.update*(..))")
    public void saveOrUpdateOps() {}

    @Pointcut("execution(* com.example.seagri.infra.service.*Service.delete*(..))")
    public void deleteOps() {}

    // ── Advices ──────────────────────────────────────────────────────────────

    @Around("saveOrUpdateOps()")
    public Object auditSaveOrUpdate(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object firstArg = args.length > 0 ? args[0] : null;

        // Executa o método original
        Object result = pjp.proceed();

        try {
            String action = resolveAction(firstArg);
            String entityType = resolveEntityType(firstArg != null ? firstArg : result);
            Long entityId = resolveId(result != null ? result : firstArg);
            String description = buildDescription(action, entityType, entityId, pjp.getSignature().getName());

            UserInfo ui = extractUser();
            auditLogService.record(entityType, entityId, action, ui.id(), ui.login(), description);
        } catch (Exception e) {
            // Nunca deixa uma falha de auditoria derrubar a operação principal
        }

        return result;
    }

    @Around("deleteOps()")
    public Object auditDelete(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Long entityId = (args.length > 0 && args[0] instanceof Long l) ? l : null;
        String entityType = resolveEntityTypeFromServiceName(pjp.getTarget().getClass().getSimpleName());

        Object result = pjp.proceed();

        try {
            String description = buildDescription("DELETE", entityType, entityId, pjp.getSignature().getName());
            UserInfo ui = extractUser();
            auditLogService.record(entityType, entityId, "DELETE", ui.id(), ui.login(), description);
        } catch (Exception e) {
            // Nunca deixa uma falha de auditoria derrubar a operação principal
        }

        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String resolveAction(Object entity) {
        if (entity == null) return "UPDATE";
        Long id = resolveId(entity);
        return (id == null) ? "CREATE" : "UPDATE";
    }

    private String resolveEntityType(Object obj) {
        if (obj == null) return "Unknown";
        Class<?> clazz = obj.getClass();
        // Para listas, pega o nome do elemento
        if (obj instanceof java.util.List<?> list && !list.isEmpty()) {
            return list.get(0).getClass().getSimpleName();
        }
        return clazz.getSimpleName();
    }

    private String resolveEntityTypeFromServiceName(String serviceName) {
        // Ex: "SupplyService" → "Supply"
        return serviceName.replace("Service", "").replace("$$SpringCGLIB$$0", "");
    }

    private Long resolveId(Object obj) {
        if (obj == null) return null;
        try {
            // Para listas, não há um único ID
            if (obj instanceof java.util.List) return null;
            Method getId = obj.getClass().getMethod("getId");
            Object idValue = getId.invoke(obj);
            return idValue instanceof Long l ? l : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String buildDescription(String action, String entityType, Long entityId, String method) {
        String idPart = entityId != null ? " #" + entityId : "";
        return action + " em " + entityType + idPart + " (método: " + method + ")";
    }

    private UserInfo extractUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserProfile up) {
            return new UserInfo(up.user.getId(), up.user.getUserName());
        }
        return new UserInfo(null, "system");
    }

    private record UserInfo(Long id, String login) {}
}
