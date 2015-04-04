package org.coursera.androidcapstone.potlatch.interfaces;

import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.models.GiftStatus;
import org.coursera.androidcapstone.potlatch.models.User;

import java.util.Collection;
import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * This interface defines an API for a GiftSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface GiftSvcApi {

    public static final String PASSWORD_PARAMETER = "password";

    public static final String USERNAME_PARAMETER = "username";

    public static final String TITLE_PARAMETER = "title";

    public static final String TOKEN_PATH = "/oauth/token";

    public static final String DATA_PARAMETER = "data";

    public static final String ID_PARAMETER = "id";

    public static final String OBSCENE_PARAMETER = "obscene";

    // The path where we expect the GiftSvc to live
    public static final String GIFT_SVC_PATH = "/gift";

    public static final String GIFT_DATA_PATH = GIFT_SVC_PATH + "/{id}/data";

    public static final String GIFT_OBSCENE_PATH = GIFT_SVC_PATH + "/{id}/obscene";

    public static final String GIFT_UPDATE_PATH = GIFT_SVC_PATH + "/update";

    public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByTitle";

    public static final String GIFT_OBSCENE_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByObscene";

    @GET(GIFT_SVC_PATH)
    public Collection<Gift> getGiftList();

    @POST(GIFT_SVC_PATH)
    public Gift addGift(@Body Gift gift);

    @POST(GIFT_SVC_PATH + "/{id}")
    public List<Long> deleteGift(@Path(ID_PARAMETER) long id);

    @GET(GIFT_TITLE_SEARCH_PATH)
    public Collection<Gift> findByTitle(@Query(TITLE_PARAMETER) String title);

    @Multipart
    @POST(GIFT_DATA_PATH)
    public GiftStatus setGiftData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile giftData);

    @GET(GIFT_DATA_PATH)
    Response getData(@Path(ID_PARAMETER) long id);

    @POST(GIFT_UPDATE_PATH)
    public Long updateGift(@Body Gift gift);

    @POST(GIFT_SVC_PATH + "/{id}/touch")
    public Long touchGift(@Path("id") long id);

    @POST(GIFT_SVC_PATH + "/{id}/untouch")
    public Long untouchGift(@Path("id") long id);

    @POST(GIFT_OBSCENE_PATH)
    public Boolean obsceneGift(@Path("id") long id);

    public static final String USER_SVC_PATH = "/user";

    @GET(USER_SVC_PATH)
    public Collection<User> getUserList();

    @GET(GIFT_SVC_PATH + "/{user}")
    public Collection<Gift> getGiftListForUser(@Path("user") User user);

    @POST(USER_SVC_PATH)
    public boolean addUser(@Body User user);
}