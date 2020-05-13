/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.onarandombox.multiverseinventories;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.multiverseinventories.share.Sharables;
import com.onarandombox.multiverseinventories.util.TestInstanceCreator;
import junit.framework.Assert;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MultiverseInventories.class, PluginDescriptionFile.class, JavaPluginLoader.class, MultiverseCore.class})
@PowerMockIgnore("javax.script.*")
public class TestCommands {
    TestInstanceCreator creator;
    Server mockServer;
    CommandSender mockCommandSender;

    @Before
    public void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertTrue(creator.setUp());
        mockServer = creator.getServer();
        mockCommandSender = creator.getCommandSender();
    }

    public void addToInventory(PlayerInventory inventory, Map<Integer, ItemStack> items) {
        for (Map.Entry<Integer, ItemStack> invEntry : items.entrySet()) {
            inventory.setItem(invEntry.getKey(), invEntry.getValue());
        }
    }

    public static Map<Integer, ItemStack> getFillerInv() {
        Map<Integer, ItemStack> fillerItems = new HashMap<Integer, ItemStack>();
        fillerItems.put(3, new ItemStack(Material.BOW, 1));
        fillerItems.put(13, new ItemStack(Material.DIRT, 64));
        fillerItems.put(36, new ItemStack(Material.IRON_HELMET, 1));
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        fillerItems.put(1, book);
        ItemStack leather = new ItemStack(Material.LEATHER_BOOTS, 1);
        fillerItems.put(2, leather);
        return fillerItems;
    }

    @After
    public void tearDown() throws Exception {
        creator.tearDown();
    }

    @Test
    public void testDebugReload() {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("Multiverse-Inventories");
        MultiverseInventories inventories = (MultiverseInventories) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Make a fake server folder to fool MV into thinking a world folder exists.
        File serverDirectory = new File(creator.getPlugin().getServerFolder(), "world");
        serverDirectory.mkdirs();

        // Initialize a fake command
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("mvinv");

        Command mockCoreCommand = mock(Command.class);
        when(mockCoreCommand.getName()).thenReturn("mv");

        // Assert debug mode is off
        Assert.assertEquals(0, inventories.getMVIConfig().getGlobalDebug());

        // Send the debug command.
        String[] debugArgs = new String[]{"debug", "3"};
        plugin.onCommand(mockCommandSender, mockCoreCommand, "", debugArgs);

        Assert.assertEquals(3, inventories.getMVIConfig().getGlobalDebug());

        // Send the debug command.
        String[] reloadArgs = new String[] { "reload" };
        plugin.onCommand(mockCommandSender, mockCommand, "", reloadArgs);

        Assert.assertEquals(3, inventories.getMVIConfig().getGlobalDebug());
    }

    @Test
    public void testInfoCommand() {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("Multiverse-Inventories");
        MultiverseInventories inventories = (MultiverseInventories) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Initialize a fake command
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("mvinv");

        // Send the debug command.
        String[] debugArgs = new String[]{ "info", "default"};
        plugin.onCommand(mockCommandSender, mockCommand, "", debugArgs);
    }

    @Test
    public void testToggleCommand() {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("Multiverse-Inventories");
        MultiverseInventories inventories = (MultiverseInventories) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Initialize a fake command
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("mvinv");

        Assert.assertFalse(inventories.getMVIConfig().getOptionalShares().contains(Sharables.ECONOMY));
        // Send the debug command.
        String[] cmdArgs = new String[]{ "toggle", "economy" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
        Assert.assertTrue(inventories.getMVIConfig().getOptionalShares().contains(Sharables.ECONOMY));
        cmdArgs = new String[]{ "reload" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
        Assert.assertTrue(inventories.getMVIConfig().getOptionalShares().contains(Sharables.ECONOMY));
        cmdArgs = new String[]{ "toggle", "economy" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
        Assert.assertFalse(inventories.getMVIConfig().getOptionalShares().contains(Sharables.ECONOMY));
    }

    @Test
    public void testGroupNoWorlds() {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("Multiverse-Inventories");
        MultiverseInventories inventories = (MultiverseInventories) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Initialize a fake command
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("mvinv");

        String[] cmdArgs = new String[]{ "rmworld", "world", "default" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
        cmdArgs = new String[]{ "rmworld", "world_nether", "default" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
        cmdArgs = new String[]{ "rmworld", "world_the_end", "default" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);

        cmdArgs = new String[]{ "reload" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);

        cmdArgs = new String[]{ "info", "default" };
        plugin.onCommand(mockCommandSender, mockCommand, "", cmdArgs);
    }

    @Test
    public void testMigrateCommand() {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("Multiverse-Inventories");
        MultiverseInventories inventories = (MultiverseInventories) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Initialize a fake command
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("mvinv");

        Command mockCoreCommand = mock(Command.class);
        when(mockCoreCommand.getName()).thenReturn("mv");

        Player player = inventories.getServer().getPlayer("dumptruckman");
        Player player2 = inventories.getServer().getPlayer("dumptruckman2");

        addToInventory(player.getInventory(), getFillerInv());
        String originalInventory = player.getInventory().toString();
        String emptyInventory = player2.getInventory().toString();

        String[] cmdArgs = new String[]{"migrate", "dumptruckman", "dumptruckman2"};
        inventories.onCommand(mockCommandSender, mockCommand, "", cmdArgs);

        Assert.assertEquals(emptyInventory, player.getInventory().toString());
        Assert.assertEquals(originalInventory, player2.getInventory().toString());
    }
}
