package org.coursera.androidcapstone.potlatch.gift;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.ElementCollection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coursera.androidcapstone.potlatch.gift.client.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.gift.repository.Gift;
import org.coursera.androidcapstone.potlatch.gift.repository.GiftRepository;
import org.coursera.androidcapstone.potlatch.gift.repository.GiftStatus;
import org.coursera.androidcapstone.potlatch.gift.repository.GiftStatus.GiftState;
import org.coursera.androidcapstone.potlatch.gift.repository.User;
import org.coursera.androidcapstone.potlatch.gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

@Controller
public class GiftAuthSvcController {

	@Autowired
	private GiftRepository gifts;

	@Autowired
	private UserRepository users;

	public static final String TOKEN_PATH = "/oauth/token";

	// The path where we expect the GiftSvc to live
	public static final String GIFT_SVC_PATH = "/gift";

	public static final String TITLE_PARAMETER = "title";

	public static final String USER_SVC_PATH = "/user";

	public static final String NAME_PARAMETER = "name";

	public static final String DATA_PARAMETER = "data";

	public static final String PARENTID_PARAMETER = "parentId";

	public static final String ID_PARAMETER = "id";

	public static final String OBSCENE_PARAMETER = "obscene";

	public static final String GIFT_DATA_PATH = GIFT_SVC_PATH + "/{id}/data";

	public static final String GIFT_OBSCENE_PATH = GIFT_SVC_PATH
			+ "/{id}/obscene";

	public static final String GIFT_UPDATE_PATH = GIFT_SVC_PATH + "/update";

	private static final AtomicLong currentId = new AtomicLong(0L);

	public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH
			+ "/search/findByTitle";

	public static final String GIFT_PARENTID_SEARCH_PATH = GIFT_SVC_PATH
			+ "/search/findByParentId";

	// Stores the gifts that meets certain criteria
	@ElementCollection
	private List<Gift> giftlist = new ArrayList<Gift>();

	// Stores <key> - gift ID, <value> - people name. Used to store people that
	// touched by a certain gift
	@ElementCollection
	private Map<Long, List<String>> giftmap = new HashMap<Long, List<String>>();

	// Stores people name
	private List<String> people = new ArrayList<String>();

	@RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Gift getGiftById(@PathVariable("id") long id,
			HttpServletResponse resp) throws ServletException, IOException {
		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		}
		return gifts.findOne(id);
	}

	@RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Gift> getGiftList() {
		return Lists.newArrayList(gifts.findAll());
	}

	@RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Gift addGift(@RequestBody Gift g, Principal p) {
		User user = users.findOne(p.getName());
		// if (!users.exists(user.getName()))
		// addUser(user);

		if (users.findByName(user.getName()) == null) {
			throw new RuntimeException("Unknown user:" + g.getUser());
		}

		user.numGiftsInc();
		users.save(user);

		Gift gift = gifts.save(g);
		Long id = gift.getId();
		String url = getGiftDataUrl(id);
		gift.setDataUrl(url);
		return gifts.save(gift);
	}

	@RequestMapping(value = GiftSvcApi.USER_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody boolean addUser(@RequestBody User user) {
		if (!users.exists(user.getName()))
			users.save(user);
		return true;
	}

	@RequestMapping(value = GIFT_UPDATE_PATH, method = RequestMethod.POST)
	public @ResponseBody Long updateGift(@RequestBody Gift g) {
		Gift gift = gifts.findOne(g.getId());
		gift.setTitle(g.getTitle());
		if (g.getText() != null)
			gift.setText(g.getText());
		gifts.save(gift);
		return g.getId();
	}

	@RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.POST)
	public @ResponseBody Collection<Long> deleteGift(@PathVariable("id") long id) {
		giftlist = (List<Gift>) gifts.findByParentId(id);
		if (!giftlist.isEmpty()) {
			gifts.delete(giftlist);
		}

		Gift gift;
		User user;
		List<Long> idList = new ArrayList<Long>();
		if (!giftlist.isEmpty()) {
			Iterator<Gift> it = giftlist.iterator();
			while (it.hasNext()) {
				gift = it.next();
				user = gift.getUser();
				user.numGiftsDec();
				long tCount = user.getTouchedCount() - gift.getTouches();
				user.setTouchedCount(tCount);
				users.save(user);
				idList.add(gift.getId());
			}
		}
		giftlist.clear();
		gift = gifts.findOne(id);
		user = gift.getUser();
		user.numGiftsDec();
		long tCount = user.getTouchedCount() - gift.getTouches();
		user.setTouchedCount(tCount);
		users.save(user);
		idList.add(id);
		gifts.delete(id);

		return idList;
	}

	@RequestMapping(value = GIFT_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Gift> findByTitle(
			@RequestParam(TITLE_PARAMETER) String title) {
		return gifts.findByTitle(title);
	}

	@RequestMapping(value = GIFT_PARENTID_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Gift> findByParentId(
			@RequestParam(PARENTID_PARAMETER) long parentId) {
		return gifts.findByParentId(parentId);
	}

	@RequestMapping(value = GIFT_DATA_PATH, method = RequestMethod.POST)
	public @ResponseBody GiftStatus setGiftData(@PathVariable("id") long id,
			@RequestParam("data") MultipartFile giftData,
			HttpServletResponse resp) throws ServletException, IOException {

		GiftFileManager giftDataMgr = GiftFileManager.get();

		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		} else {
			giftDataMgr.saveGiftData(gifts.findOne(id),
					giftData.getInputStream());
			return new GiftStatus(GiftState.READY);
		}
		return null;
	}

	@RequestMapping(value = GIFT_DATA_PATH, method = RequestMethod.GET)
	public @ResponseBody Gift getData(@PathVariable("id") long id,
			HttpServletResponse resp) throws ServletException, IOException {

		GiftFileManager giftDataMgr = GiftFileManager.get();

		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		} else {

			Gift gift = gifts.findOne(id);
			if (giftDataMgr.hasGiftData(gift))
				giftDataMgr.copyGiftData(gift, resp.getOutputStream());
			else {
				resp.setContentType("text/plain");
				resp.sendError(404);
			}
		}
		return null;
	}

	@RequestMapping(value = GIFT_SVC_PATH + "/{id}/touch", method = RequestMethod.POST)
	public @ResponseBody Long touchGift(@PathVariable("id") long id,
			HttpServletResponse resp, Principal p) throws ServletException,
			IOException {

		resp.setContentType("text/plain");

		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		}

		Gift gift = gifts.findOne(id);

		if (gift != null) {
			people = giftmap.get(id);
			if (people == null || !people.contains(p.getName())) {
				if (!gift.isTouched()) {
					User user = gift.getUser();
					user.touchedCountInc();
					users.save(user);
					gift.touchesInc();
					if (p.getName().equals(user.getName()))
						gift.setTouched(true);
					gifts.save(gift);
					if (people != null)
						people = giftmap.remove(id);
					else
						people = new ArrayList<String>();
					people.add(p.getName());
					giftmap.put(id, people);
					resp.setStatus(200);
				}
			} else {
				resp.sendError(400);
			}
		}
		return gift.getTouches();
	}

	@RequestMapping(value = GIFT_SVC_PATH + "/{id}/untouch", method = RequestMethod.POST)
	public @ResponseBody Long untouchGift(@PathVariable("id") long id,
			HttpServletResponse resp, Principal p) throws ServletException,
			IOException {
		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		}

		Gift gift = gifts.findOne(id);

		if (gift != null) {
			User user = gift.getUser();
			user.touchedCountDec();
			users.save(user);
			people = giftmap.get(id);
			if (people == null || !people.contains(p.getName())) {
				resp.sendError(400);
			} else {
				gift.touchesDec();
				if (p.getName().equals(user.getName()))
					gift.setTouched(false);
				gifts.save(gift);
				if (people != null) {
					people = giftmap.remove(id);
					people.remove(p.getName());
				} else {
					people = new ArrayList<String>();
				}

				giftmap.put(id, people);
				resp.setStatus(200);
			}
		}

		return gift.getTouches();
	}

	@RequestMapping(value = GIFT_OBSCENE_PATH, method = RequestMethod.POST)
	public @ResponseBody Boolean obsceneGift(@PathVariable("id") long id,
			HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/plain");

		if (!gifts.exists(id)) {
			resp.setContentType("text/plain");
			resp.sendError(404);
		}

		Gift gift = gifts.findOne(id);

		if (gift != null) {
			if (!gift.isObscene()) {
				User user = gift.getUser();
				users.save(user);

				gift.setObscene(true);
				gift.setTouched(false);

				gift.touchesDec();
				gifts.save(gift);
				resp.setStatus(200);
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{user}", method = RequestMethod.GET)
	public @ResponseBody Collection<Gift> getGiftListForUser(
			@PathVariable("user") String userName) {
		return Lists.newArrayList(gifts.findByUser(userName));
	}

	@RequestMapping(value = USER_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<User> getUserList() {
		return Lists.newArrayList(users.findAll(sortByTouchedCountDesc()));
	}

	private Sort sortByTouchedCountDesc() {
		return new Sort(Sort.Direction.DESC, "touchedCount");
	}

	private String getGiftDataUrl(long giftId) {
		String url = getUrlBaseForLocalServer() + "/gift/" + giftId + "/data";
		return url;
	}

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String base = "http://"
				+ request.getServerName()
				+ ((request.getServerPort() != 80) ? ":"
						+ request.getServerPort() : "");
		return base;
	}

	/*
	 * This part generates the unique ID for the gift and save it into a hashmap
	 */
	public Gift save(Gift entity) {
		checkAndSetId(entity);
		// giftlist.put(entity.getId(), entity);
		gifts.save(entity);
		return entity;
	}

	private void checkAndSetId(Gift entity) {
		if (entity.getId() == 0) {
			entity.setId(currentId.incrementAndGet());
		}
	}

}
