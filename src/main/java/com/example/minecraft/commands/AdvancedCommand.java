package com.example.minecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.example.minecraft.ExamplePlugin;
import com.example.minecraft.gui.MenuManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exemplo de comando avançado com suporte a subcomandos e autocompletar
 * Demonstra como implementar comandos complexos com várias funcionalidades
 */
public class AdvancedCommand implements CommandExecutor, TabCompleter {

    private final ExamplePlugin plugin;
    private final MenuManager menuManager;

    public AdvancedCommand(ExamplePlugin plugin, MenuManager menuManager) {
        this.plugin = plugin;
        this.menuManager = menuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verificar se o comando foi executado por um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        // Se não houver argumentos, mostrar ajuda
        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        // Processar subcomandos
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
                showHelp(player);
                break;
                
            case "menu":
                menuManager.openMainMenu(player);
                break;
                
            case "info":
                showInfo(player);
                break;
                
            case "set":
                if (args.length < 3) {
                    player.sendMessage("§cUso correto: /" + label + " set <chave> <valor>");
                    return true;
                }
                
                String key = args[1];
                String value = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                
                // Armazenar dados do jogador
                plugin.getCustomConfig().set("players." + player.getUniqueId() + ".data." + key, value);
                plugin.saveCustomConfig();
                
                player.sendMessage("§aValor '" + value + "' definido para a chave '" + key + "'.");
                break;
                
            case "get":
                if (args.length < 2) {
                    player.sendMessage("§cUso correto: /" + label + " get <chave>");
                    return true;
                }
                
                String getKey = args[1];
                String storedValue = plugin.getCustomConfig().getString("players." + player.getUniqueId() + ".data." + getKey);
                
                if (storedValue == null) {
                    player.sendMessage("§cNenhum valor encontrado para a chave '" + getKey + "'.");
                } else {
                    player.sendMessage("§aValor para a chave '" + getKey + "': §f" + storedValue);
                }
                break;
                
            default:
                player.sendMessage("§cSubcomando desconhecido. Use /" + label + " help para ver os comandos disponíveis.");
                break;
        }
        
        return true;
    }

    /**
     * Mostra a ajuda do comando para o jogador
     * @param player Jogador para mostrar a ajuda
     */
    private void showHelp(Player player) {
        player.sendMessage("§6=== Comandos Disponíveis ===");
        player.sendMessage("§e/advanced help §7- Mostra esta ajuda");
        player.sendMessage("§e/advanced menu §7- Abre o menu principal");
        player.sendMessage("§e/advanced info §7- Mostra informações sobre o plugin");
        player.sendMessage("§e/advanced set <chave> <valor> §7- Define um valor para uma chave");
        player.sendMessage("§e/advanced get <chave> §7- Obtém o valor de uma chave");
    }
    
    /**
     * Mostra informações sobre o plugin para o jogador
     * @param player Jogador para mostrar as informações
     */
    private void showInfo(Player player) {
        player.sendMessage("§6=== Informações do Plugin ===");
        player.sendMessage("§eNome: §f" + plugin.getDescription().getName());
        player.sendMessage("§eVersão: §f" + plugin.getDescription().getVersion());
        player.sendMessage("§eAutor: §f" + String.join(", ", plugin.getDescription().getAuthors()));
        player.sendMessage("§eDescrição: §f" + plugin.getDescription().getDescription());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // Subcomandos principais
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("help", "menu", "info", "set", "get");
            return filterCompletions(subCommands, args[0]);
        }
        
        // Subcomandos específicos
        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            
            if (subCommand.equals("get") || subCommand.equals("set")) {
                // Sugerir chaves existentes para o jogador
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String path = "players." + player.getUniqueId() + ".data";
                    
                    if (plugin.getCustomConfig().contains(path)) {
                        return filterCompletions(
                            plugin.getCustomConfig().getConfigurationSection(path).getKeys(false),
                            args[1]
                        );
                    }
                }
            }
        }
        
        return completions;
    }
    
    /**
     * Filtra as sugestões de autocompletar com base no texto digitado
     * @param options Lista de opções disponíveis
     * @param input Texto digitado pelo jogador
     * @return Lista filtrada de sugestões
     */
    private List<String> filterCompletions(List<String> options, String input) {
        return options.stream()
                .filter(option -> option.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra as sugestões de autocompletar com base no texto digitado
     * @param options Conjunto de opções disponíveis
     * @param input Texto digitado pelo jogador
     * @return Lista filtrada de sugestões
     */
    private List<String> filterCompletions(java.util.Set<String> options, String input) {
        return options.stream()
                .filter(option -> option.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
