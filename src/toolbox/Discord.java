package toolbox;
import club.minnced.discord.rpc.*;

public class Discord {
	
	public static boolean isOn = true;
	public static String details;
	public static String state;
	
	public static void main(String args[]) {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String appID = "1044313022849630220";
		String steamID = "";
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = (user) -> System.out.println("Ready!");
		lib.Discord_Initialize(appID, handlers, true, steamID);
		DiscordRichPresence presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000; //epoch second
		
		//presence.partySize = 1;
		//presence.partyMax = 5;
		//presence.partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
		//presence.joinSecret = "MTI4NzM0OjFpMmhuZToxMjMxMjM=";
		
		presence.largeImageKey = "icon";
		presence.details = details;
		presence.state = state;
		lib.Discord_UpdatePresence(presence);
		//in a worker thread
		new Thread(() -> {
			while (isOn) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(0);
				} catch (InterruptedException ignored) {}
			}
		}, "RPC-Callback-Handler").start();
	}
}
