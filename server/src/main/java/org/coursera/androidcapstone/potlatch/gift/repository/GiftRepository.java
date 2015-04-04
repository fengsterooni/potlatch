package org.coursera.androidcapstone.potlatch.gift.repository;

import java.util.Collection;
import java.util.List;

import org.coursera.androidcapstone.potlatch.gift.GiftAuthSvcController;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Video objects and allow them to
 * be searched by title.
 * 
 * @author jules
 *
 */
// This @RepositoryRestResource annotation tells Spring Data Rest to
// expose the GiftRepository through a controller and map it to the
// "/gift" path. This automatically enables you to do the following:
//
// 1. List all gifts by sending a GET request to /gift
// 2. Add a gift by sending a POST request to /gift with the JSON for a gift
//
@RepositoryRestResource(path = GiftAuthSvcController.GIFT_SVC_PATH)
public interface GiftRepository extends CrudRepository<Gift, Long> {

	/*
	 * See:
	 * http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html
	 * /jpa.repositories.html for more examples of writing query methods
	 */

	public Collection<Gift> findByTitle(
			@Param(GiftAuthSvcController.TITLE_PARAMETER) String title);

	public Collection<Gift> findByParentId(
			@Param(GiftAuthSvcController.PARENTID_PARAMETER) long parentId);

	public Collection<Gift> findByUser(
			@Param(GiftAuthSvcController.NAME_PARAMETER) String userName);

	@Query("select g.user, sum(g.touches) as popularity from Gift g group by g.user order by popularity desc")
	public List<Object[]> listTopGivers();

	public Collection<Gift> findByObscene(
			@Param(GiftAuthSvcController.OBSCENE_PARAMETER) boolean obscene);
}
