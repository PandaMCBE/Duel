package fr.pandamcbe.duel.arenas;

import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class getArenas {

    private List<Arena> arenaList;

    public getArenas() {
        this.arenaList = new ArrayList<>();
        createSGArenas();
        createMlgRushArenas();
    }

    public List<Arena> getArenaList() {
        return arenaList;
    }

    public void setArenaList(List<Arena> arenaList) {
        this.arenaList = arenaList;
    }

    public void createSGArenas() {
        DuelArena sg1 = new DuelArena(AllNames.SG.getName(), new Location(Bukkit.getWorld("world"), 42,14,0),  "sg-a#1", 2, 2, 2);
        sg1.getSpawnByTeam().put(ChatColor.BLUE, (new Location(sg1.getMiddlespawn().getWorld(), 23, 6, 0, -90.0f, -1.0f)));
        sg1.getSpawnByTeam().put(ChatColor.RED, (new Location(sg1.getMiddlespawn().getWorld(), 60, 6, 0, 90.0f, 1.0f)));
        sg1.getWaitingRoom().setLocation(sg1.getMiddlespawn());
        sg1.getTeams().add(new Team(ChatColor.BLUE.name(), ChatColor.BLUE));
        sg1.getTeams().add(new Team(ChatColor.RED.name(), ChatColor.RED));
        arenaList.add(sg1);

        DuelArena sg2 = new DuelArena(AllNames.SG.getName(), new Location(Bukkit.getWorld("world"), -42,14,0), "sg-a#2", 2, 2,2);
        sg2.getSpawnByTeam().put(ChatColor.BLUE, (new Location(sg2.getMiddlespawn().getWorld(), -60, 6, 0, -90.0f, -1.0f)));
        sg2.getSpawnByTeam().put(ChatColor.RED, (new Location(sg2.getMiddlespawn().getWorld(), -23, 6, 0, 90.0f, 1.0f)));
        sg2.getWaitingRoom().setLocation(sg2.getMiddlespawn());
        sg2.getTeams().add(new Team(ChatColor.BLUE.name(), ChatColor.BLUE));
        sg2.getTeams().add(new Team(ChatColor.RED.name(), ChatColor.RED));
        arenaList.add(sg2);
    }

    public void createMlgRushArenas(){
        DuelArena mr1 = new DuelArena(AllNames.MLGRUSH.getName(), new Location(Bukkit.getWorld("world"), 80,23,-217), "mlgrush-a#1", 2, 2,2);
        mr1.getTeams().add(new Team(ChatColor.BLUE.name(), ChatColor.BLUE));
        mr1.getTeams().add(new Team(ChatColor.RED.name(), ChatColor.RED));
        mr1.getSpawnByTeam().put(ChatColor.BLUE, (new Location(mr1.getMiddlespawn().getWorld(), 81, 20, -189, -180.0f, 1.0f)));
        mr1.getSpawnByTeam().put(ChatColor.RED, (new Location(mr1.getMiddlespawn().getWorld(), 81, 20, -245, 1.0f, 1.0f)));
        mr1.getGoalByTeam().put(ChatColor.BLUE, new Location(mr1.getMiddlespawn().getWorld(), 81, 11, -182));
        mr1.getGoalByTeam().put(ChatColor.RED, new Location(mr1.getMiddlespawn().getWorld(), 81, 11, -252));
        mr1.getWaitingRoom().setLocation(mr1.getMiddlespawn());
        arenaList.add(mr1);

        DuelArena mr2 = new DuelArena(AllNames.MLGRUSH.getName(), new Location(Bukkit.getWorld("world"), 77,23,-349), "mlgrush-a#2", 2, 2,2);
        mr2.getTeams().add(new Team(ChatColor.BLUE.name(), ChatColor.BLUE));
        mr2.getTeams().add(new Team(ChatColor.RED.name(), ChatColor.RED));
        mr2.getSpawnByTeam().put(ChatColor.BLUE, (new Location(mr2.getMiddlespawn().getWorld(), 77, 20, -321, -180.0f, 1.0f)));
        mr2.getSpawnByTeam().put(ChatColor.RED, (new Location(mr2.getMiddlespawn().getWorld(), 77, 20, -377, 1.0f, 1.0f)));
        mr2.getGoalByTeam().put(ChatColor.BLUE, new Location(mr2.getMiddlespawn().getWorld(), 77, 11, -314));
        mr2.getGoalByTeam().put(ChatColor.RED, new Location(mr2.getMiddlespawn().getWorld(), 77, 11, -384)); //ajouter 1 à Z pour décaler le lit d'un cran au milieu
        mr2.getWaitingRoom().setLocation(mr2.getMiddlespawn());
        arenaList.add(mr2);
    }


}