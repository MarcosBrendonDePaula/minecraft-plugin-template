package com.example.minecraft.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.example.minecraft.ExamplePlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Exemplo de classe para criar interfaces gráficas (GUIs) no Minecraft
 * Demonstra como criar menus interativos usando inventários
 */
public class MenuManager implements Listener {

    private final ExamplePlugin plugin;
    private final Map<UUID, MenuType> openMenus;

    public MenuManager(ExamplePlugin plugin) {
        this.plugin = plugin;
        this.openMenus = new HashMap<>();
        
        // Registrar eventos
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Abre o menu principal para um jogador
     * @param player Jogador para abrir o menu
     */
    public void openMainMenu(Player player) {
        // Criar inventário com 3 linhas (27 slots)
        Inventory inventory = Bukkit.createInventory(null, 27, "§6Menu Principal");
        
        // Adicionar itens ao menu
        inventory.setItem(10, createItem(Material.DIAMOND_SWORD, "§bOpções de Combate", 
                "§7Clique para ver opções de combate"));
        
        inventory.setItem(13, createItem(Material.GRASS_BLOCK, "§aOpções de Mundo", 
                "§7Clique para ver opções de mundo"));
        
        inventory.setItem(16, createItem(Material.PLAYER_HEAD, "§eOpções de Jogador", 
                "§7Clique para ver opções de jogador"));
        
        // Adicionar botão de fechar no canto inferior direito
        inventory.setItem(26, createItem(Material.BARRIER, "§cFechar", 
                "§7Clique para fechar o menu"));
        
        // Abrir inventário para o jogador
        player.openInventory(inventory);
        
        // Registrar que o jogador está com o menu principal aberto
        openMenus.put(player.getUniqueId(), MenuType.MAIN);
    }
    
    /**
     * Abre o submenu de combate para um jogador
     * @param player Jogador para abrir o menu
     */
    public void openCombatMenu(Player player) {
        // Criar inventário com 3 linhas (27 slots)
        Inventory inventory = Bukkit.createInventory(null, 27, "§bOpções de Combate");
        
        // Adicionar itens ao menu
        inventory.setItem(11, createItem(Material.IRON_SWORD, "§7Espada de Ferro", 
                "§7Clique para receber uma espada de ferro"));
        
        inventory.setItem(13, createItem(Material.BOW, "§7Arco", 
                "§7Clique para receber um arco"));
        
        inventory.setItem(15, createItem(Material.SHIELD, "§7Escudo", 
                "§7Clique para receber um escudo"));
        
        // Adicionar botão de voltar no canto inferior esquerdo
        inventory.setItem(18, createItem(Material.ARROW, "§aVoltar", 
                "§7Clique para voltar ao menu principal"));
        
        // Adicionar botão de fechar no canto inferior direito
        inventory.setItem(26, createItem(Material.BARRIER, "§cFechar", 
                "§7Clique para fechar o menu"));
        
        // Abrir inventário para o jogador
        player.openInventory(inventory);
        
        // Registrar que o jogador está com o menu de combate aberto
        openMenus.put(player.getUniqueId(), MenuType.COMBAT);
    }
    
    /**
     * Cria um item para o menu
     * @param material Material do item
     * @param name Nome do item
     * @param lore Descrição do item
     * @return ItemStack configurado
     */
    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Verificar se quem clicou é um jogador
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        
        // Verificar se o jogador está com um menu aberto
        if (!openMenus.containsKey(playerId)) {
            return;
        }
        
        // Cancelar o evento para evitar que o jogador pegue o item
        event.setCancelled(true);
        
        // Verificar se o clique foi em um item válido
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        
        // Obter o tipo de menu aberto
        MenuType menuType = openMenus.get(playerId);
        
        // Processar o clique com base no menu aberto
        switch (menuType) {
            case MAIN:
                handleMainMenuClick(player, event.getSlot(), event.getCurrentItem().getType());
                break;
            case COMBAT:
                handleCombatMenuClick(player, event.getSlot(), event.getCurrentItem().getType());
                break;
            // Adicione mais casos conforme necessário
        }
    }
    
    /**
     * Processa cliques no menu principal
     * @param player Jogador que clicou
     * @param slot Slot clicado
     * @param material Material do item clicado
     */
    private void handleMainMenuClick(Player player, int slot, Material material) {
        switch (slot) {
            case 10: // Opções de Combate
                openCombatMenu(player);
                break;
            case 13: // Opções de Mundo
                player.sendMessage("§aOpções de mundo em desenvolvimento...");
                player.closeInventory();
                openMenus.remove(player.getUniqueId());
                break;
            case 16: // Opções de Jogador
                player.sendMessage("§eOpções de jogador em desenvolvimento...");
                player.closeInventory();
                openMenus.remove(player.getUniqueId());
                break;
            case 26: // Fechar
                player.closeInventory();
                openMenus.remove(player.getUniqueId());
                break;
        }
    }
    
    /**
     * Processa cliques no menu de combate
     * @param player Jogador que clicou
     * @param slot Slot clicado
     * @param material Material do item clicado
     */
    private void handleCombatMenuClick(Player player, int slot, Material material) {
        switch (slot) {
            case 11: // Espada de Ferro
                player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                player.sendMessage("§aVocê recebeu uma espada de ferro!");
                break;
            case 13: // Arco
                player.getInventory().addItem(new ItemStack(Material.BOW));
                player.sendMessage("§aVocê recebeu um arco!");
                break;
            case 15: // Escudo
                player.getInventory().addItem(new ItemStack(Material.SHIELD));
                player.sendMessage("§aVocê recebeu um escudo!");
                break;
            case 18: // Voltar
                openMainMenu(player);
                break;
            case 26: // Fechar
                player.closeInventory();
                openMenus.remove(player.getUniqueId());
                break;
        }
    }
    
    /**
     * Tipos de menu disponíveis
     */
    public enum MenuType {
        MAIN,
        COMBAT,
        WORLD,
        PLAYER
    }
}
