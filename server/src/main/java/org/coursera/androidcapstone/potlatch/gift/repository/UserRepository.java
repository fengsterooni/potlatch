package org.coursera.androidcapstone.potlatch.gift.repository;

import java.util.Collection;

import org.coursera.androidcapstone.potlatch.gift.GiftAuthSvcController;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = GiftAuthSvcController.USER_SVC_PATH)
public interface UserRepository extends CrudRepository<User, String> {

	/*
	 * See:
	 * http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html
	 * /jpa.repositories.html for more examples of writing query methods
	 */

	// Find all user with a matching name (e.g., user.name)
	public Collection<User> findByName(
			@Param(GiftAuthSvcController.NAME_PARAMETER) String name);

	public Collection<User> findAll(Sort sort);
}
