package com.solexgames.kiwi.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

public class RoleUtil {

    public static final List<String> MANAGEMENT_ROLES = Arrays.asList(
            "Owner",
            "Developer",
            "Platform Admin",
            "Head Admin",
            "Senior Admin"
    );

    public static final List<String> MODERATION_ROLES = Arrays.asList(
            "Admin",
            "Senior Mod",
            "Mod+",
            "Mod",
            "Trainee",
            "Chat Mod"
    );

    public static final List<String> DEVELOPER_ROLES = Arrays.asList(
            "Developer",
            "Owner"
    );

    public static boolean isDeveloper(Member member) {
        final List<Role> roles = member.getRoles();

        final List<Role> developerRoles = RoleUtil.DEVELOPER_ROLES.stream()
                .map(name -> member.getGuild().getRolesByName(name, true).get(0))
                .filter(Objects::nonNull).collect(Collectors.toList());

        for (Role role : developerRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isManager(Member member) {
        final List<Role> roles = member.getRoles();

        final List<Role> managementRoles = RoleUtil.MANAGEMENT_ROLES.stream()
                .map(name -> member.getGuild().getRolesByName(name, true).get(0))
                .filter(Objects::nonNull).collect(Collectors.toList());

        for (Role role : managementRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isModerator(Member member) {
        final List<Role> roles = member.getRoles();

        final List<Role> managementRoles = RoleUtil.MANAGEMENT_ROLES.stream()
                .map(name -> member.getGuild().getRolesByName(name, true).get(0))
                .filter(Objects::nonNull).collect(Collectors.toList());
        final List<Role> moderationRoles = RoleUtil.MODERATION_ROLES.stream()
                .map(name -> member.getGuild().getRolesByName(name, true).get(0))
                .filter(Objects::nonNull).collect(Collectors.toList());

        for (Role role : managementRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }

        for (Role role : moderationRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }

        return false;
    }
}
