package com.example.minecraft.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Classe utilitária com métodos comuns para facilitar o desenvolvimento
 * Demonstra como criar métodos reutilizáveis para tarefas comuns
 */
public class PluginUtils {

    private static final Random random = new Random();

    /**
     * Cria um item personalizado com nome e descrição
     * @param material Material do item
     * @param name Nome do item
     * @param lore Descrição do item (cada string é uma linha)
     * @return ItemStack configurado
     */
    public static ItemStack createCustomItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(name);
        
        if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Aplica um efeito de poção a um jogador
     * @param player Jogador para aplicar o efeito
     * @param effectType Tipo de efeito
     * @param duration Duração em segundos
     * @param amplifier Amplificador (nível - 1)
     * @param ambient Se o efeito deve ser ambiente (partículas reduzidas)
     * @param particles Se deve mostrar partículas
     */
    public static void applyPotionEffect(Player player, PotionEffectType effectType, int duration, 
                                        int amplifier, boolean ambient, boolean particles) {
        player.addPotionEffect(new PotionEffect(
            effectType,
            duration * 20, // Converter segundos para ticks
            amplifier,
            ambient,
            particles
        ));
    }
    
    /**
     * Cria um efeito de partículas em uma localização
     * @param location Localização para criar o efeito
     * @param particle Tipo de partícula
     * @param count Quantidade de partículas
     * @param offsetX Deslocamento no eixo X
     * @param offsetY Deslocamento no eixo Y
     * @param offsetZ Deslocamento no eixo Z
     * @param speed Velocidade das partículas
     */
    public static void spawnParticles(Location location, Particle particle, int count,
                                     double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(
            particle,
            location,
            count,
            offsetX,
            offsetY,
            offsetZ,
            speed
        );
    }
    
    /**
     * Reproduz um som para um jogador
     * @param player Jogador para reproduzir o som
     * @param sound Tipo de som
     * @param volume Volume do som (0.0 a 1.0)
     * @param pitch Tom do som (0.5 a 2.0)
     */
    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
    
    /**
     * Verifica se um jogador tem permissão para uma ação
     * @param player Jogador para verificar
     * @param permission Permissão a verificar
     * @param sendMessage Se deve enviar mensagem ao jogador caso não tenha permissão
     * @return true se o jogador tem permissão
     */
    public static boolean hasPermission(Player player, String permission, boolean sendMessage) {
        if (player.hasPermission(permission)) {
            return true;
        }
        
        if (sendMessage) {
            player.sendMessage("§cVocê não tem permissão para fazer isso.");
        }
        
        return false;
    }
    
    /**
     * Obtém uma localização aleatória em torno de um ponto central
     * @param center Localização central
     * @param radiusX Raio no eixo X
     * @param radiusY Raio no eixo Y
     * @param radiusZ Raio no eixo Z
     * @return Localização aleatória
     */
    public static Location getRandomLocation(Location center, double radiusX, double radiusY, double radiusZ) {
        double x = center.getX() + (random.nextDouble() * 2 - 1) * radiusX;
        double y = center.getY() + (random.nextDouble() * 2 - 1) * radiusY;
        double z = center.getZ() + (random.nextDouble() * 2 - 1) * radiusZ;
        
        return new Location(center.getWorld(), x, y, z, center.getYaw(), center.getPitch());
    }
    
    /**
     * Formata uma mensagem com cores usando o código de cor '§'
     * @param message Mensagem a ser formatada
     * @return Mensagem formatada
     */
    public static String formatMessage(String message) {
        return message.replace("&", "§");
    }
    
    /**
     * Verifica se uma string é um número inteiro
     * @param str String a verificar
     * @return true se a string é um número inteiro
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Verifica se uma string é um número decimal
     * @param str String a verificar
     * @return true se a string é um número decimal
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Converte uma lista de strings em uma única string separada por um delimitador
     * @param list Lista de strings
     * @param delimiter Delimitador
     * @return String concatenada
     */
    public static String joinStrings(List<String> list, String delimiter) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            
            if (i < list.size() - 1) {
                builder.append(delimiter);
            }
        }
        
        return builder.toString();
    }
}
