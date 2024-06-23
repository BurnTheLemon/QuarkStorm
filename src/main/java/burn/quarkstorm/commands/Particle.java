package burn.quarkstorm.commands;

import burn.quarkstorm.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Particle implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        String commandPermission = "quarkstorm.commands.quark";

        if(!commandSender.hasPermission(commandPermission)) {
            commandSender.sendMessage("Dir felt die Permission " + commandPermission);
            return false;
        }

        if(args.length == 0) {
            commandSender.sendMessage("/quark <partikel-effekt> [player] [xOffset] [yOffset] [zOffset]");
        }

        if(args.length >= 1) {
        ParticleUtil particleUtil = new ParticleUtil();

            String particleEffect = args[0];
            commandSender = (args.length >= 2 && Bukkit.getPlayer(args[1]) != null) ? Bukkit.getPlayer(args[1]) : commandSender;

           double xOffset = (args.length >= 3) ? Double.parseDouble(args[2]) : 0.0;
           double yOffset = (args.length >= 4) ? Double.parseDouble(args[3]) : 0.0;
           double zOffset = (args.length >= 5) ? Double.parseDouble(args[4]) : 0.0;

            if(commandSender instanceof Player)
                particleUtil.executeQuarkParticle(particleEffect, (Player) commandSender, xOffset, yOffset, zOffset);
            else if (commandSender != null) {
                commandSender.sendMessage("Der Angegebene Spieler existiert nicht UND du bist auch kein Spieler!");
            }
        }


        return false;
    }


}
