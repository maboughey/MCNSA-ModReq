package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.UUID;

public class ClaimCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!ModReq.isEnabled) {
            Messaging.sendMessage(src, "&4Plugin is currently disabled");
            return CommandResult.success();
        }
        //Get sender's info
        UUID uuid = null;
        if (src instanceof CommandBlockSource || src instanceof ConsoleSource) {
            return CommandResult.success();
        }
        else if (src instanceof Player) {
            uuid = ((Player)src).getUniqueId();
        }

        int id = args.<Integer>getOne(Text.of("id")).get();

        if (!(id >0)) {
            Messaging.sendMessage(src, "&4Invalid request id");
            return CommandResult.success();
        }

        //Try and get the Request
        ModRequest request = DBModRequest.getRequest(id);

        //Does the id exist?
        if (request == null) {
            Messaging.sendMessage(src, "&4Invalid request id");
            return CommandResult.success();
        }

        //Is it claimed?
        if (request.status != 0) {
            Messaging.sendMessage(src, "&4That request has already been claimed");
            return CommandResult.success();
        }

        //Has it been escalated?
        if (!request.escalated && !src.hasPermission("modreq.admin")) {
            Messaging.sendMessage(src, "&4That request has been escalated to admins");
            return CommandResult.success();
        }

        //Let's  claim it
        request.responder = uuid;
        request.status = 1;

        //Save the request
        DBModRequest.updateRequestClaimed(request);

        //Tell the user
        Messaging.sendMessage(src, "You have claimed request id: "+id);

        //end of command
        return CommandResult.success();
    }
}
