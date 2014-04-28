package com.boidzgame.gameplay;

import com.boidzgame.R;
import com.boidzgame.gameplay.boidz.level.BacteriaLevel;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.gameplay.boidz.level.SharkLevel;
import com.boidzgame.gameplay.boidz.level.SheepLevel;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static List<Level> levelList;

    private static void defineLevels() {
        Level level;
        levelList = new ArrayList<Level>();
        int i = 0;

        level = new SharkLevel();
        level.levelId = i++;
        level.titleResourceId = R.string.level1_title;
        level.descriptionResourceId = R.string.level1_description;
        levelList.add(level);

        level = new SheepLevel();
        level.levelId = i++;
        level.titleResourceId = R.string.level2_title;
        level.descriptionResourceId = R.string.level2_description;
        levelList.add(level);

        level = new BacteriaLevel();
        level.levelId = i++;
        level.titleResourceId = R.string.level3_title;
        level.descriptionResourceId = R.string.level3_description;
        levelList.add(level);
    }

    public static Level getLevel(int id) {
        if (levelList == null) {
            defineLevels();
        }
        return levelList.get(id);
    }

    public static int getLevelCount() {
        if (levelList == null) {
            defineLevels();
        }
        return levelList.size();
    }
}
