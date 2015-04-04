package org.coursera.androidcapstone.potlatch.gift.client;

import java.util.Collection;

import org.coursera.androidcapstone.potlatch.gift.repository.Gift;
import org.coursera.androidcapstone.potlatch.gift.repository.GiftStatus;
import org.coursera.androidcapstone.potlatch.gift.repository.User;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface GiftSvcApi {

	public static final String TOKEN_PATH = "/oauth/token";

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String TITLE_PARAMETER = "title";

	public static final String OBSCENE_PARAMETER = "obscene";

	// The path where we expect the VideoSvc to live
	public static final String GIFT_SVC_PATH = "/gift";

	public static final String GIFT_DATA_PATH = GIFT_SVC_PATH + "/{id}/data";

	public static final String GIFT_UPDATE_PATH = GIFT_SVC_PATH + "/update";

	public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH
			+ "/search/findByTitle";

	public static final String GIFT_OBSCENE_PATH = GIFT_SVC_PATH
			+ "/{id}/obscene";

	@GET(GIFT_SVC_PATH)
	public Collection<Gift> getGiftList();

	@POST(GIFT_SVC_PATH)
	public Gift addGift(@Body Gift g);

	@POST(GIFT_SVC_PATH + "/{id}")
	public Collection<Long> deleteGift(@Path(ID_PARAMETER) long id);

	@Multipart
	@POST(GIFT_DATA_PATH)
	public GiftStatus setGiftData(@Path(ID_PARAMETER) long id,
			@Part(DATA_PARAMETER) TypedFile giftData);

	@GET(GIFT_DATA_PATH)
	Response getData(@Path(ID_PARAMETER) long id);

	@GET(GIFT_TITLE_SEARCH_PATH)
	public Collection<Gift> findByTitle(@Query(TITLE_PARAMETER) String title);

	@POST(GIFT_SVC_PATH + "/{id}/update")
	public Long updateGift(@Path("id") long id);

	@POST(GIFT_SVC_PATH + "/{id}/touch")
	public Long touchGift(@Path("id") long id);

	@GET(GIFT_SVC_PATH + "/{id}/touch")
	public Boolean isGiftTouchedUser(@Path("id") long id);

	@POST(GIFT_SVC_PATH + "/{id}/untouch")
	public Long untouchGift(@Path("id") long id);

	@POST(GIFT_OBSCENE_PATH)
	public Boolean obsceneGift(@Path("id") long id);

	public static final String USER_SVC_PATH = "/user";

	@GET(GIFT_SVC_PATH + "/{user}")
	public Collection<Gift> getGiftListForUser(@Path("user") User user);

	@POST(USER_SVC_PATH)
	public boolean addUser(@Body User user);

	@GET(USER_SVC_PATH)
	public Collection<User> getUserList();
}
