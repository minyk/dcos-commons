package com.mesosphere.sdk.redis.api;


/**
 * An enum for redis sentinel info section.
 */
public enum RedisSentinelInfoSection {
    SERVER("Server"),
    CLIENTS("Clients"),
    CPU("CPU"),
    STATS("Stats"),
    SENTINEL("Sentinel");

    private final String text;

    /**
     * @param text
     */
    private RedisSentinelInfoSection(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
