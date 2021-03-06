package uk.co.maboughey.moqreq.utils;

import fr.d0p1.hookscord.Hookscord;
import fr.d0p1.hookscord.utils.Message;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;

import java.io.IOException;
import java.net.MalformedURLException;

public class Discord {
    public static void sendMod(String message, CommandSource src) {
        try {
            Hookscord hk = new Hookscord(Configuration.DiscordModHook);
            Message msg = new Message("New Mod Request");
            msg.setText("**"+src.getName()+"** has submitted a new mod request on **"+Configuration.ServerName+"** with the text: *"+message+"*");
            hk.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendAdmin(String message, int id, String reqMessage, CommandSource src) {
        try {
            Hookscord hk = new Hookscord(Configuration.DiscordAdminHook);
            Message msg = new Message("Escalated Mod Request");
            msg.setText("**"+src.getName()+"** wants an admin to look at mod request on **"+Configuration.ServerName+"**\n" +
                    "id: "+id+"\nMessage: *"+message+"*\n" +
                    "Request message: *"+reqMessage+"*");
            hk.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void closedRequest(String user, String message, CommandSource src, int id, String response) {
        String closer;
        if (src instanceof ConsoleSource)
            closer = "Console";
        else
            closer = src.getName();

        try {
            Hookscord hk = new Hookscord(Configuration.DiscordModHook);
            Message msg = new Message("Closed Mod Request");
            msg.setText("**"+closer+"** has closed mod request on **"+Configuration.ServerName+"**\n" +
                    "Id: *"+id+"*\n"+
                    "Player: "+user+
                    "\nRequest text: *"+message+"*\n" +
                    "Comment: *"+response+"*");
            hk.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
