package cn.xflat.core;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class TestIm {

	public static void main(String[] args) {
		try {
			AbstractXMPPConnection connection = new XMPPTCPConnection("cong", "123456", "113.106.92.68");  
			connection.connect();  
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
