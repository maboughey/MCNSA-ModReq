package uk.co.maboughey.moqreq.commands;

import com.mysql.cj.core.util.TestUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.BookViewBuilder;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.List;

public class ListCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!ModReq.isEnabled) {
            Messaging.sendMessage(src, "&4Plugin is currently disabled");
            return CommandResult.success();
        }
        //Make sure person sending command is a player
        if (!(src instanceof Player)) {
            Messaging.sendMessage(src, "&4You must be a player to use this command");
            return CommandResult.success();
        }

        //Get the mod requests
        List<ModRequest> requests = DBModRequest.getUsersRequests(((Player) src).getUniqueId());

        //Check if there is any
        if (requests.size() < 1) {
            //No requests. Lets tell them and finish
            Messaging.sendMessage(src, "&6You have no mod requests to view");
            return CommandResult.success();
        }

        //Show them to the user
        ((Player) src).sendBookView(BookViewBuilder.playerBook(requests));
        return CommandResult.success();
    }
}
