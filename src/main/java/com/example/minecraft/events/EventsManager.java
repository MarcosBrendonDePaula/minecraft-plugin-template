package com.example.minecraft.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.example.minecraft.ExamplePlugin;
import com.example.minecraft.utils.PluginUtils;

/**
 * Exemplo de classe para manipulação de eventos do Bukkit
 * Demonstra como lidar com diferentes tipos de eventos do jogo
 */
public class EventsManager implements Listener {

    private final ExamplePlugin plugin;

    public EventsManager(ExamplePlugin plugin) {
        this.plugin = plugin;
        
        // Registrar eventos
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Manipula o evento de entrada de jogador no servidor
     * @param event Evento de entrada
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Personalizar mensagem de entrada
        event.setJoinMessage("§a[+] §e" + player.getName() + " §aentrou no servidor!");
        
        // Verificar se é a primeira vez que o jogador entra
        if (!player.hasPlayedBefore()) {
            // Dar itens iniciais
            player.getInventory().addItem(new ItemStack(Material.BREAD, 16));
            player.getInventory().addItem(PluginUtils.createCustomItem(
                Material.COMPASS, 
                "§6Bússola Mágica", 
                "§7Use para encontrar seu caminho",
                "§7Item especial para novos jogadores"
            ));
            
            // Reproduzir som de boas-vindas
            PluginUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            
            // Enviar mensagem de boas-vindas
            player.sendMessage("§6Bem-vindo ao servidor! §eAqui está um kit inicial para você.");
        }
        
        // Carregar dados do jogador
        loadPlayerData(player);
    }
    
    /**
     * Manipula o evento de saída de jogador do servidor
     * @param event Evento de saída
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Personalizar mensagem de saída
        event.setQuitMessage("§c[-] §e" + player.getName() + " §csaiu do servidor!");
        
        // Salvar dados do jogador
        savePlayerData(player);
    }
    
    /**
     * Manipula o evento de quebra de bloco
     * @param event Evento de quebra de bloco
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        // Exemplo: Dar recompensas especiais para minérios
        if (blockType == Material.DIAMOND_ORE || blockType == Material.DEEPSLATE_DIAMOND_ORE) {
            // Incrementar contador de diamantes minerados
            int count = plugin.getCustomConfig().getInt("players." + player.getUniqueId() + ".stats.diamonds_mined", 0);
            plugin.getCustomConfig().set("players." + player.getUniqueId() + ".stats.diamonds_mined", count + 1);
            plugin.saveCustomConfig();
            
            // Verificar se atingiu um marco
            if ((count + 1) % 10 == 0) {
                player.sendMessage("§b§lParabéns! §eVocê minerou §b" + (count + 1) + " §ediamantes no total!");
                PluginUtils.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }
    }
    
    /**
     * Manipula o evento de interação do jogador
     * @param event Evento de interação
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Verificar se o jogador está segurando um item específico
        if (event.hasItem() && event.getItem().getType() == Material.COMPASS) {
            // Verificar se o item tem um nome específico
            if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() &&
                event.getItem().getItemMeta().getDisplayName().equals("§6Bússola Mágica")) {
                
                // Cancelar o evento para evitar o comportamento padrão
                event.setCancelled(true);
                
                // Teletransportar o jogador para o spawn
                player.teleport(player.getWorld().getSpawnLocation());
                
                // Efeitos visuais e sonoros
                PluginUtils.spawnParticles(player.getLocation(), org.bukkit.Particle.PORTAL, 50, 0.5, 0.5, 0.5, 0.1);
                PluginUtils.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                
                player.sendMessage("§6Você foi teletransportado para o spawn!");
            }
        }
    }
    
    /**
     * Manipula o evento de dano entre entidades
     * @param event Evento de dano
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Verificar se o atacante é um jogador
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Verificar se o jogador tem um efeito especial ativado
            if (plugin.getCustomConfig().getBoolean("players." + attacker.getUniqueId() + ".effects.critical_hit", false)) {
                // Aumentar o dano em 50%
                event.setDamage(event.getDamage() * 1.5);
                
                // Efeitos visuais
                PluginUtils.spawnParticles(event.getEntity().getLocation(), 
                    org.bukkit.Particle.CRIT, 20, 0.5, 0.5, 0.5, 0.1);
                
                // Informar o jogador
                attacker.sendMessage("§c§lGolpe Crítico! §e+" + (int)(event.getDamage() * 0.5) + " de dano extra!");
            }
        }
    }
    
    /**
     * Carrega os dados do jogador da configuração
     * @param player Jogador para carregar os dados
     */
    private void loadPlayerData(Player player) {
        // Verificar se o jogador tem dados salvos
        if (plugin.getCustomConfig().contains("players." + player.getUniqueId())) {
            // Carregar última localização conhecida
            if (plugin.getCustomConfig().contains("players." + player.getUniqueId() + ".last_location")) {
                player.sendMessage("§eSeus dados foram carregados com sucesso!");
            }
        } else {
            // Criar entrada para o jogador
            plugin.getCustomConfig().set("players." + player.getUniqueId() + ".name", player.getName());
            plugin.getCustomConfig().set("players." + player.getUniqueId() + ".first_join", System.currentTimeMillis());
            plugin.saveCustomConfig();
        }
    }
    
    /**
     * Salva os dados do jogador na configuração
     * @param player Jogador para salvar os dados
     */
    private void savePlayerData(Player player) {
        // Salvar última localização
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.world", 
            player.getLocation().getWorld().getName());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.x", 
            player.getLocation().getX());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.y", 
            player.getLocation().getY());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.z", 
            player.getLocation().getZ());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.yaw", 
            player.getLocation().getYaw());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_location.pitch", 
            player.getLocation().getPitch());
        
        // Salvar última vez online
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".last_online", 
            System.currentTimeMillis());
        
        // Salvar nível e experiência
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".level", 
            player.getLevel());
        plugin.getCustomConfig().set("players." + player.getUniqueId() + ".exp", 
            player.getExp());
        
        plugin.saveCustomConfig();
    }
}
