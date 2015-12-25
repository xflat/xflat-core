package cn.xflat.core;

import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.packet.Registration;

public class TestIm {

	public static void main(String[] args) {
		
		try {
			
			XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
			configBuilder.setUsernameAndPassword("cong", "123456");
			//configBuilder.setResource("SomeResource");
			configBuilder.setServiceName("113.106.92.68");
			configBuilder.setHost("113.106.92.68");
			configBuilder.setSecurityMode(SecurityMode.disabled);
			configBuilder.setCompressionEnabled(false);
			configBuilder.setDebuggerEnabled(true);
			
			//需要调用如下的方法，否则会出现错误: SASLErrorException: SASLError using DIGEST-MD5: not-authorized
			SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
			SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
			
			AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
			// Connect to the server
			connection.connect();
			// Log into the server
			connection.login();
			
			//获取好友列表
			Roster roster = Roster.getInstanceFor(connection);
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				System.out.println(">>>roster: " + entry);
			}
			
			Chat chat = ChatManager.getInstanceFor(connection)
					.createChat("ding@113.106.92.68", new ChatMessageListener(){
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
