package org.springframework.cloud.config.server.environment;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.concurrent.locks.Lock;

/**
 * @author Ryan Lynch
 */
public abstract class AbstractVersionedScmEnvironmentRepository extends AbstractScmEnvironmentRepository implements VersionedEnvironmentRepository, VersionedSearchPathLocation {
	public AbstractVersionedScmEnvironmentRepository(ConfigurableEnvironment environment, JGitEnvironmentProperties properties) {
		super(environment, properties);
	}

	/**
	 * Same as {@link EnvironmentRepository#findOne(String, String, String)} except
	 * it takes in an optional version.  Should the repo be current with that version
	 * then this method will return null for the environment.  This will remove a lot of
	 * additional overhead and can be used to support eTag based solutions
	 *
	 * @param application the application requesting the environment
	 * @param profile     the profile for the application
	 * @param label       the label of the applicaiton
	 * @param clientVersion     the version that the caller already has.  If null then acts the
	 *                    same as {@link EnvironmentRepository#findOne(String, String, String)}
	 * @return Returns the environment.  Will return null if the supplied version matches
	 * the one the EnvironmentRepository has as its current version.
	 */
	@Override
	public synchronized Environment findOne(String application, String profile, String label, String clientVersion) {
		NativeEnvironmentRepository delegate = new NativeEnvironmentRepository(getEnvironment(),
				new NativeEnvironmentProperties());
		Locations locations = getLocations(application, profile, label, clientVersion);
		if (locations == null) {
			return new Environment(application, StringUtils.commaDelimitedListToStringArray(profile), label, clientVersion, null);
		}
		delegate.setSearchLocations(locations.getLocations());
		Environment result = delegate.findOne(application, profile, "");
		result.setVersion(locations.getVersion());
		result.setLabel(label);
		return this.cleaner.clean(result, getWorkingDirectory().toURI().toString(),
				getUri());
	}
}
