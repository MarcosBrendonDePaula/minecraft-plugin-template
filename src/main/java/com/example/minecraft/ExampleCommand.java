package com.example.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Exemplo de classe de comando simples para o plugin
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
            // Abrir menu principal se não houver argumentos
            plugin.getMenuManager().openMainMenu(player);
            return true;
        }

        // Processar subcomandos
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
                player.sendMessage("§6=== Comandos Disponíveis ===");
                player.sendMessage("§e/example §7- Abre o menu principal");
                player.sendMessage("§e/example help §7- Mostra esta ajuda");
                player.sendMessage("§e/example info §7- Mostra informações sobre o plugin");
                break;
                
            case "info":
                player.sendMessage("§6=== Informações do Plugin ===");
                player.sendMessage("§eNome: §f" + plugin.getDescription().getName());
                player.sendMessage("§eVersão: §f" + plugin.getDescription().getVersion());
                player.sendMessage("§eAutor: §f" + String.join(", ", plugin.getDescription().getAuthors()));
                break;
                
            default:
                player.sendMessage("§cSubcomando desconhecido. Use /example help para ver os comandos disponíveis.");
                break;
        }
        
        return true;
    }
}
