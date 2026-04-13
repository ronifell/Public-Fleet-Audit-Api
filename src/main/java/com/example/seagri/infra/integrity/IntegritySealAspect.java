package com.example.seagri.infra.integrity;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Item 18 — AOP aspect that auto-seals any entity returned by a method
 * annotated with {@link IntegritySealed}.
 *
 * Works with any entity that:
 *   1. Implements {@link Hashable} (provides toHashString())
 *   2. Has a setIntegrityHash(String) method
 *
 * If the entity already has a hash set (e.g. by manual logic), the aspect
 * will NOT overwrite it, preserving explicit hash-chaining workflows.
 *
 * Architecture benefit: a new module (e.g. Medications) can get full
 * AP 04 integrity support without modifying any infrastructure code.
 */
@Aspect
@Component
public class IntegritySealAspect {

    @Autowired
    private HashService hashService;

    @Around("@annotation(com.example.seagri.infra.integrity.IntegritySealed)")
    public Object sealIntegrity(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Hashable h) {
                sealIfNeeded(h);
            }
        }

        Object result = pjp.proceed(args);

        if (result instanceof Hashable h) {
            sealIfNeeded(h);
        }

        return result;
    }

    private void sealIfNeeded(Hashable entity) {
        try {
            Method getHash = entity.getClass().getMethod("getIntegrityHash");
            Object currentHash = getHash.invoke(entity);
            if (currentHash != null && !currentHash.toString().isBlank()) {
                return;
            }

            String hash = hashService.generate(entity);
            Method setHash = entity.getClass().getMethod("setIntegrityHash", String.class);
            setHash.invoke(entity, hash);
        } catch (NoSuchMethodException e) {
            // Entity doesn't expose setIntegrityHash — skip silently
        } catch (Exception e) {
            // Never let sealing break the operation
        }
    }
}
