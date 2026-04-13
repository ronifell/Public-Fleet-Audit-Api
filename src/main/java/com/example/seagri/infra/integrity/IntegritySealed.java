package com.example.seagri.infra.integrity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Item 18 — Marks a service method so that the returned entity (which must
 * implement {@link Hashable}) is automatically sealed with a SHA-256 hash
 * before being persisted.
 *
 * Usage: annotate any save* method in a @Service class.
 * The aspect {@link IntegritySealAspect} will intercept the call,
 * generate the integrity hash from {@code Hashable.toHashString()},
 * and set it on the entity via {@code setIntegrityHash(String)}.
 *
 * This means a new module (e.g. Medications) only needs to:
 *   1. Implement {@link Hashable} on the entity
 *   2. Add a {@code setIntegrityHash(String)} method
 *   3. Annotate its service save method with @IntegritySealed
 *
 * No changes to the hash engine or audit infrastructure are required.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegritySealed {
}
