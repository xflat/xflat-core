package cn.xflat.core;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class TestIm {

	public static void main(String[] args) {
		try {
			//SASLAuthentication.supportSASLMechanism("PLAIN",0);
			
			XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
			configBuilder.setUsernameAndPassword("cong", "123456");
			//configBuilder.setResource("SomeResource");
			configBuilder.setServiceName("113.106.92.68");
			//configBuilder.setHost("113.106.92.68");
			configBuilder.setSecurityMode(SecurityMode.disabled);
			
			
			AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
			// Connect to the server
			connection.connect();
			// Log into the server
			connection.login();
			
			Chat chat = ChatManager.getInstanceFor(connection).createChat("ding@113.106.92.68", new ChatMessageListener(){
				public void processMessage(Chat chat, Message message) {
					System.out.println("Received message: " + message); 
				}
			});
			chat.sendMessage("Howdy!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
