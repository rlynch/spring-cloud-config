package org.springframework.cloud.config.server.environment;

import org.springframework.cloud.config.environment.Environment;

/**
 * Interface of EnvironmentRepostitories which will support etags via their
 * controller.
 *
 * @author Ryan Lynch
 */
public interface VersionedEnvironmentRepository extends EnvironmentRepository {

	/**
	 * Same as {@link EnvironmentRepository#findOne(String, String, String)} except
	 * it takes in an optional clientVersion.  Should the repo be current with that clientVersion
	 * then this method will return null for the environment.  This will remove a lot of
	 * additional overhead and can be used to support etag based solutions
	 * @param application the application requesting the environment
	 * @param profile the profile for the application
	 * @param label the label of the applicaiton
	 * @param clientVersion the clientVersion that the caller already has.  If null then acts the
	 *                same as {@link EnvironmentRepository#findOne(String, String, String)}
	 * @return Returns the environment.  If the returned environment matches the clientVersion passed in
	 * then the {@link Environment#getPropertySources()} will be empty.
	 */
	Environment findOne(String application, String profile, String label, String clientVersion);
}
