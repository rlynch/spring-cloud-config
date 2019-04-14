package org.springframework.cloud.config.server.environment;

/**
 *  Interface of SearchPathLocator which will support etags via their
 *  controller.
 *
 * @author Ryan Lynch
 */
public interface VersionedSearchPathLocation extends SearchPathLocator {

	Locations getLocations(String application, String profile, String label, String clientVersion);
}
