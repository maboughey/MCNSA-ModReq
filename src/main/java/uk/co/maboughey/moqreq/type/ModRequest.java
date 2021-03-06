package uk.co.maboughey.moqreq.type;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.utils.Configuration;
import uk.co.maboughey.moqreq.utils.Messaging;
import uk.co.maboughey.moqreq.utils.Username;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import static org.spongepowered.api.Sponge.*;

public class ModRequest {
    public int id;
    public UUID user;
    public String message;
    public Location<World> location;
    public Vector3d rotation;
    public int status;
    public UUID responder;
    public String response;
    public String server = Configuration.ServerName;
    public Date date = new java.sql.Date((new java.util.Date()).getTime());
    public Boolean escalated = false;

    public void setRotation(Double x, Double y, Double z) {
        Vector3d v3d = new Vector3d(x,y,z);
        rotation = v3d;
    }
    public String getUser() {
        return Username.getUsername(user);
    }
    public String getResponder(){
        if (responder == null) {
            //Probably console
            return "Console";
        }
        else {
            return Username.getUsername(responder);
        }
    }
    public void setLocation(Double x, Double y, Double z, UUID world) {
        //Sanity check on world
        Optional<World> worldEntity = getServer().getWorld(world);

        if (!worldEntity.isPresent()) {
            ModReq.log.error("Could not find world "+world);
            return;
        }

        location = new Location<World>(worldEntity.get(), x, y, z);
    }
    public void teleport(Player player) {
        //Check to see if it is this server
        if (server.contains(Configuration.ServerName)) {
            player.setRotation(rotation);
            player.setLocation(location);
            Messaging.sendMessage(player, "Teleported to request: "+id);
        }
        else {
            Messaging.errorMessage(player, "This request is not from this server");
        }
    }

    public String getStatus() {
        String output = "";
        switch (this.status) {
            case 0: output = "&4Open"; break;
            case 1: output = "&4Claimed"; break;
            case 2: output = "&AUnread"; break;
            case 3: output = "&AClosed"; break;
        }
        return output;
    }
}
