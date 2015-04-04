package org.coursera.androidcapstone.potlatch.gift.repository;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	private String name;
	private long numGifts;
	private long touchedCount;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Collection<Gift> gifts;

	@Transient
	private ReentrantReadWriteLock mRWLock = new ReentrantReadWriteLock();

	User() {

	}

	User(String name, int numGifts, int touchedCount) {
		super();
		this.name = name;
		this.numGifts = numGifts;
		this.touchedCount = touchedCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumGifts() {
		mRWLock.readLock().lock();
		long value = this.numGifts;
		mRWLock.readLock().unlock();
		return value;
	}

	public void setNumGifts(long numGifts) {
		mRWLock.writeLock().lock();
		this.numGifts = numGifts;
		mRWLock.writeLock().unlock();
	}

	public long getTouchedCount() {
		mRWLock.readLock().lock();
		long value = this.touchedCount;
		mRWLock.readLock().unlock();
		return value;
	}

	public void setTouchedCount(long touchedCount) {
		mRWLock.writeLock().lock();
		this.touchedCount = touchedCount;
		mRWLock.writeLock().unlock();
	}

	public Collection<Gift> getGifts() {
		return gifts;
	}

	public void setGifts(Collection<Gift> gifts) {
		this.gifts = gifts;
	}

	public void numGiftsInc() {
		mRWLock.writeLock().lock();
		++this.numGifts;
		mRWLock.writeLock().unlock();
	}

	public void numGiftsDec() {
		mRWLock.writeLock().lock();
		if (this.numGifts > 0)
			--this.numGifts;
		mRWLock.writeLock().unlock();
	}

	public void touchedCountInc() {
		mRWLock.writeLock().lock();
		++this.touchedCount;
		mRWLock.writeLock().unlock();
	}

	public void touchedCountDec() {
		mRWLock.writeLock().lock();
		if (this.touchedCount > 0)
			--this.touchedCount;
		mRWLock.writeLock().unlock();
	}
}