package org.coursera.androidcapstone.potlatch.gift.repository;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fluentinterface.ReflectionBuilder;
import com.fluentinterface.builder.Builder;
import com.google.common.base.Objects;

@Entity
// @Table(name = "Gift")
public class Gift {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long parentId;
	private String title;
	private String dataUrl;
	private String text;
	private String contentType;
	private long touches;
	private boolean touched;

	private boolean obscene;

	@Transient
	private final ReentrantReadWriteLock mRWLock = new ReentrantReadWriteLock();

	@ManyToOne
	private User user;

	public Gift() {

	}

	public Gift(String title, String url, long parentId, String contentType,
			User user, long touches, boolean touched, boolean obscene) {
		super();
		this.title = title;
		this.dataUrl = url;
		this.parentId = parentId;
		this.contentType = contentType;
		this.user = user;
		this.touches = touches;
		this.touched = touched;
		this.obscene = obscene;
	}

	public static GiftBuilder create() {
		return ReflectionBuilder.implementationFor(GiftBuilder.class).create();
	}

	public interface GiftBuilder extends Builder<Gift> {
		public GiftBuilder withTitle(String title);

		public GiftBuilder withParentId(long parentId);

		public GiftBuilder withContentType(String contentType);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getTouches() {
		mRWLock.readLock().lock();
		long value = this.touches;
		mRWLock.readLock().unlock();
		return value;
	}

	public void setTouches(long touches) {
		mRWLock.writeLock().lock();
		this.touches = touches;
		mRWLock.writeLock().unlock();
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public boolean isObscene() {
		return obscene;
	}

	public void setObscene(boolean obscene) {
		this.obscene = obscene;
	}

	public void touchesInc() {
		mRWLock.writeLock().lock();
		++this.touches;
		mRWLock.writeLock().unlock();
	}

	public void touchesDec() {
		mRWLock.writeLock().lock();
		if (this.touches > 0)
			--this.touches;
		mRWLock.writeLock().unlock();
	}

	/**
	 * Two Gifts will generate the same hashcode if they have exactly the same
	 * values for their name, url, and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(title, dataUrl);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Gift) {
			Gift other = (Gift) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(title, other.title)
					&& Objects.equal(dataUrl, other.dataUrl);

		} else {
			return false;
		}
	}
}
