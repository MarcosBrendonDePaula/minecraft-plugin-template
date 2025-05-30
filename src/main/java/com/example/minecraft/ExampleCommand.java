package com.example.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Exemplo de classe de comando para o plugin
 * Demonstra como implementar comandos básicos
 */
public class ExampleCommand implements CommandExecutor {

    private final ExamplePlugin plugin;

    public ExampleCommand(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verificar se o comando foi executado por um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        // Exemplo de processamento de argumentos
        if (args.length < 1) {
            player.sendMessage("§6[Exemplo] §eUso: /example <texto>");
            return true;
        }

        // Exemplo de armazenamento de dados
        String data = String.join(" ", args);
        plugin.setPlayerData(player, data);
        
        // Exemplo de resposta ao jogador
        player.sendMessage("§6[Exemplo] §aDados armazenados: §f" + data);
        
        return true;
    }
}
