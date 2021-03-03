package com.solexgames.robot.util;

import com.solexgames.core.server.NetworkServer;

public final class ServerUtil {

    public static String getServerBuilder(NetworkServer naProxy, NetworkServer asProxy, NetworkServer euProxy) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("You can connect to our network using one\n");
        stringBuilder.append("of the following proxies below.\n");
        stringBuilder.append("\n");
        stringBuilder.append(":flag_us: **__North America__**:\n");
        if (naProxy != null) {
            stringBuilder.append("`na.potclub.vip` • **").append(naProxy.getOnlinePlayers()).append(" Online").append("**\n");
        } else {
            stringBuilder.append("`na.potclub.vip` • **").append(" Offline").append("**\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append(":flag_eu: **__Europe__**:\n");
        if (euProxy != null) {
            stringBuilder.append("`eu.potclub.vip` • **").append(euProxy.getOnlinePlayers()).append(" Online").append("**\n");
        } else {
            stringBuilder.append("`eu.potclub.vip` • **").append(" Offline").append("**\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append(":flag_cn: **__Asia__**:\n");
        if (asProxy != null) {
            stringBuilder.append("`as.potclub.vip` • **").append(asProxy.getOnlinePlayers()).append(" Online").append("**\n");
        } else {
            stringBuilder.append("`as.potclub.vip` • **").append(" Offline").append("**\n");
        }

        return stringBuilder.toString();
    }
}
