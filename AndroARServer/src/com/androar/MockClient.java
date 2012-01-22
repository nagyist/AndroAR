package com.androar;

import java.io.*;
import java.net.Socket;

import com.androar.CommunicationProtos.AuthentificationInfo;
import com.androar.CommunicationProtos.ClientMessage;
import com.androar.CommunicationProtos.ServerMessage;
import com.androar.CommunicationProtos.ClientMessage.ClientMessageType;
import com.androar.ImageFeaturesProtos.DetectedObject;
import com.androar.ImageFeaturesProtos.Image;
import com.androar.ImageFeaturesProtos.ImageContents;
import com.androar.ImageFeaturesProtos.ObjectBoundingBox;
import com.androar.ImageFeaturesProtos.DetectedObject.DetectedObjectType;
import com.google.protobuf.ByteString;

public class MockClient {
	
	public static void main(String[] args) {
		Socket socket;
		DataOutputStream out;
        DataInputStream in;
        
		try {
			socket = new Socket("localhost", 6666);
			out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
            // Read a message
            ServerMessage server_message = ServerMessage.parseFrom(Communication.readMessage(in));
            Logging.LOG(2, "***\n " + server_message.toString() + "\n***");
            
            // Assume that the message was a HELLO. Let's now send an image to see if this works.
            // We will read an image stored on the Hard Drive for now, it's path is being passed through params
            Logging.LOG(2, args[0]);
            File in_file = new File(args[0]);
            FileInputStream fin = new FileInputStream(in_file);
            byte file_contents[] = new byte[(int) in_file.length()];
            fin.read(file_contents);
            
            ByteString image_contents = ByteString.copyFrom(file_contents);
            
            Image image = Image.newBuilder().
            	addDetectedObjects(
            		DetectedObject.newBuilder().
            		setObjectType(DetectedObjectType.BUILDING).
            		setName("OBJECT_1").
            		setBoundingBox(
            			ObjectBoundingBox.newBuilder().
            			setTop(0).
            			setBottom(100).
            			setLeft(0).
            			setRight(100).
            			build()).
            		build()).
            	setImage(
            		ImageContents.newBuilder().
            		setImageHash("IMAGE_HASH").
            		setImageContents(image_contents)).
            	build();
            
            ClientMessage client_message = ClientMessage.newBuilder()
            	.setAuthentificationInfo(
            		AuthentificationInfo.newBuilder()
            		.setPhoneId("PHONE_ID")
            		.setHash("CURRENT_HASH_OF_PHONE_ID")
            		.build())
            	.setMessageType(ClientMessageType.IMAGE_TO_PROCESS)
            	.setImageToProcess(image)
            	.build();
            
            Logging.LOG(2, "***\n " + client_message.toString() + "\n***");
            
            Communication.sendMessage(client_message, out);
            
            socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}