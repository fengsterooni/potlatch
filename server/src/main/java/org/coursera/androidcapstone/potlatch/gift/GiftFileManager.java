/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.coursera.androidcapstone.potlatch.gift;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.coursera.androidcapstone.potlatch.gift.repository.Gift;

/**
 * This class provides a simple implementation to store gift binary data on the
 * file system in a "gift" folder. The class provides methods for saving videos
 * and retrieving their binary data.
 * 
 * @author jules
 *
 */
public class GiftFileManager {

	/**
	 * This static factory method creates and returns a GiftFileManager object
	 * to the caller. Feel free to customize this method to take parameters,
	 * etc. if you want.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static GiftFileManager get() throws IOException {
		return new GiftFileManager();
	}

	private Path targetDir_ = Paths.get("gift");

	// The VideoFileManager.get() method should be used
	// to obtain an instance
	private GiftFileManager() throws IOException {
		if (!Files.exists(targetDir_)) {
			Files.createDirectories(targetDir_);
		}
	}

	// Private helper method for resolving gift image file paths
	private Path getGiftPath(Gift g) {
		assert (g != null);

		return targetDir_.resolve("gift" + g.getId() + ".jpg");
	}

	/**
	 * This method returns true if the specified Video has binary data stored on
	 * the file system.
	 */
	public boolean hasGiftData(Gift g) {
		Path source = getGiftPath(g);
		return Files.exists(source);
	}

	/**
	 * This method copies the binary data for the given video to the provided
	 * output stream. The caller is responsible for ensuring that the specified
	 * Gift has binary data associated with it. If not, this method will throw a
	 * FileNotFoundException.
	 */
	public void copyGiftData(Gift g, OutputStream out) throws IOException {
		Path source = getGiftPath(g);
		if (!Files.exists(source)) {
			throw new FileNotFoundException(
					"Unable to find the referenced gift file for giftId:"
							+ g.getId());
		}
		Files.copy(source, out);
	}

	/**
	 * This method reads all of the data in the provided InputStream and stores
	 * it on the file system. The data is associated with the Gift object that
	 * is provided by the caller.
	 */
	public void saveGiftData(Gift gift, InputStream giftData)
			throws IOException {
		assert (giftData != null);

		Path target = getGiftPath(gift);
		Files.copy(giftData, target, StandardCopyOption.REPLACE_EXISTING);
	}

}
