package com.androar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.androar.comm.ImageFeaturesProtos.Image;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

public class CassandraConnection {

	/*
	 * Initializes a connection to the Cassandra cluster.
	 */
	public CassandraConnection() {
		//cluster = HFactory.getOrCreateCluster("TestCluster", "emerald:9160");
	}

	/*
	 * Processes and stores the information associated with the list of items sent by a client
	 * @param imagesToStoreList A list of Images, along with their metadata and bounding boxes.
	 */
	public void storeImages(List<Image> imagesToStoreList) {
		FileOutputStream fout;
		for (int i = 0; i < imagesToStoreList.size(); ++i) {
			// Let's just store the images for now
			// TODO(alex): Fix.
			try {
				fout = new FileOutputStream("out.jpeg");
				fout.write(imagesToStoreList.get(i).getImage().getImageContents().toByteArray());
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Processes a raw image sent by the client and sends back the relevant results.
	 * @param rawImage Image to be processed, along with localization features.
	 */
	public Image processImage(Image rawImage) {
		// TODO(alex): Let's just return the same image we got for now. Fix.
		return rawImage;
	}
	
	// The Cassandra cluster we are connected to.
	private Cluster cluster = null;
	
}