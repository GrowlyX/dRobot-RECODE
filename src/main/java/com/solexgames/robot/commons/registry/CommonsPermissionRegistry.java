package com.solexgames.robot.commons.registry;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author GrowlyX
 * @since 8/2/2021
 */

public class CommonsPermissionRegistry {

    private final Map<Long, Set<String>> permissions = new HashMap<>();

    /**
     * Add a permission to a user
     *
     * @param userId     Users id
     * @param permission Permission to add
     */
    public void add(final @NonNull Long userId, final @NonNull String permission) {
        this.getPermissions(userId).add(permission.toLowerCase());
    }

    /**
     * Remove a permission from a user
     *
     * @param userId     Users id
     * @param permission Permission to remove
     */
    public void remove(final @NonNull Long userId, final @NonNull String permission) {
        this.getPermissions(userId).remove(permission.toLowerCase());
    }

    /**
     * Check if a user has a specific permission
     *
     * @param userId     Users id
     * @param permission Permission to check
     * @return True if the user has a permission
     */
    public boolean hasPermission(final @NonNull Long userId, final @Nullable String permission) {
        if (permission == null) {
            return true;
        }

        return this.getPermissions(userId).contains(permission.toLowerCase());
    }

    private Set<String> getPermissions(final @NonNull Long userId) {
        this.permissions.putIfAbsent(userId, new HashSet<>());
        return this.permissions.get(userId);
    }

}
