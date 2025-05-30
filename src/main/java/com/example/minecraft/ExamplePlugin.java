package com.example.minecraft;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe principal do plugin de exemplo para Minecraft 1.20.1
 * Este template pode ser usado como base para criar plugins mais complexos
 */
public class ExamplePlugin extends JavaPlugin implements Listener {

    // Exemplo de armazenamento de dados em memória
    private Map<UUID, String> playerData;
    
    // Exemplo de configuração personalizada
    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        // Salvar configuração padrão se não existir
        saveDefaultConfig();
        
        // Inicializar mapa de dados
        playerData = new HashMap<>();
        
        // Carregar configuração personalizada
        loadCustomConfig();
        
        // Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);
        
        // Registrar comandos
        getCommand("example").setExecutor(new ExampleCommand(this));
        
        getLogger().info("Plugin de exemplo ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Salvar dados ao desativar o plugin
        saveCustomConfig();
        
        getLogger().info("Plugin de exemplo desativado com sucesso!");
    }
    
    /**
     * Carrega a configuração personalizada
     */
    private void loadCustomConfig() {
        customConfigFile = new File(getDataFolder(), "custom.yml");
        
        if (!customConfigFile.exists()) {
            try {
                customConfigFile.getParentFile().mkdirs();
                customConfigFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Não foi possível criar o arquivo custom.yml: " + e.getMessage());
            }
        }
        
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }
    
    /**
     * Salva a configuração personalizada
     */
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            getLogger().severe("Não foi possível salvar o arquivo custom.yml: " + e.getMessage());
        }
    }
    
    /**
     * Obtém a configuração personalizada
     * @return Configuração personalizada
     */
    public FileConfiguration getCustomConfig() {
        return customConfig;
    }
    
    /**
     * Armazena dados do jogador
     * @param player Jogador
     * @param data Dados a serem armazenados
     */
    public void setPlayerData(Player player, String data) {
        playerData.put(player.getUniqueId(), data);
    }
    
    /**
     * Obtém dados do jogador
     * @param player Jogador
     * @return Dados armazenados ou null se não existir
     */
    public String getPlayerData(Player player) {
        return playerData.getOrDefault(player.getUniqueId(), null);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Exemplo de mensagem de boas-vindas
        player.sendMessage("§6Bem-vindo ao servidor! Este é um plugin de exemplo.");
        
        // Exemplo de agendamento de tarefa
        getServer().getScheduler().runTaskLater(this, () -> {
            player.sendMessage("§eEsta mensagem aparece 2 segundos após você entrar no servidor.");
        }, 40L); // 40 ticks = 2 segundos
    }
}
