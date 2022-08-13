package managers;

import enemies.*;
import helpz.Utilz;
import helpz.LoadSave;
import scenes.Playing;
import objects.PathPoint;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import static helpz.Constants.Enemies.*;
import static helpz.Constants.Direction.*;
import static helpz.Constants.Tiles.ROAD_TILE;

public class EnemyManager {
    private final Playing playing;
    private final BufferedImage[] enemyImgs;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final PathPoint start;
    private final PathPoint end;
    private final int HPbarWidth = 20;
    private BufferedImage slowEffect;
    private int[][] roadDirArr;

    public EnemyManager(Playing playing, PathPoint start, PathPoint end) {
        this.playing = playing;
        enemyImgs = new BufferedImage[4];
        this.start = start;
        this.end = end;

        loadEffectImg();
        loadEnemyImgs();
        loadRoadDirArr();
    }

    private void loadRoadDirArr() {
        roadDirArr = Utilz.GetRoadDirArr(playing.getGame().getTileManager().getTypeArr(), start, end);
    }

    private void loadEffectImg() {
        slowEffect = LoadSave.getSpriteAtlas().getSubimage(32 * 9, 32 * 2, 32, 32);
    }

    private void loadEnemyImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        for (int i = 0; i < 4; i++)
            enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);
    }

    public void update() {
        for (Enemy e : enemies)
            if (e.isAlive()) {
                updateEnemyMoveNew(e);
            }

    }

    private void updateEnemyMoveNew(Enemy e) {
        PathPoint currTile = getEnemyTile(e);
        int dir = roadDirArr[currTile.getyCord()][currTile.getxCord()];

        e.move(GetSpeed(e.getEnemyType()), dir);

        PathPoint newTile = getEnemyTile(e);

        if (!isTilesTheSame(currTile, newTile)) {
            if (isTilesTheSame(newTile, end)) {
                e.kill();
                playing.removeOneLife();
                return;
            }
            int newDir = roadDirArr[newTile.getyCord()][newTile.getxCord()];
            if (newDir != dir) {
                e.setPos(newTile.getxCord() * 32, newTile.getyCord() * 32);
                e.setLastDir(newDir);
            }
        }
    }

    private PathPoint getEnemyTile(Enemy e) {
        return switch (e.getLastDir()) {
            case LEFT -> new PathPoint((int) ((e.getX() + 31) / 32), (int) (e.getY() / 32));
            case UP -> new PathPoint((int) (e.getX() / 32), (int) ((e.getY() + 31) / 32));
            case RIGHT, DOWN -> new PathPoint((int) (e.getX() / 32), (int) (e.getY() / 32));
            default -> new PathPoint((int) (e.getX() / 32), (int) (e.getY() / 32));
        };
    }

    private boolean isTilesTheSame(PathPoint currTile, PathPoint newTile) {
        if (currTile.getxCord() == newTile.getxCord())
            return currTile.getyCord() == newTile.getyCord();
        return false;
    }

    public void updateEnemyMove(Enemy e) {
        if (e.getLastDir() == -1)
            setNewDirectionAndMove(e);

        int newX = (int) (e.getX() + getSpeedAndWidth(e.getLastDir(), e.getEnemyType()));
        int newY = (int) (e.getY() + getSpeedAndHeight(e.getLastDir(), e.getEnemyType()));

        if (getTileType(newX, newY) == ROAD_TILE) {
            e.move(GetSpeed(e.getEnemyType()), e.getLastDir());
        } else if (isAtEnd(e)) {
            e.kill();
            playing.removeOneLife();
        } else {
            setNewDirectionAndMove(e);
        }
    }

    private void setNewDirectionAndMove(Enemy e) {
        int dir = e.getLastDir();

        int xCord = (int) (e.getX() / 32);
        int yCord = (int) (e.getY() / 32);

        fixEnemyOffsetTile(e, dir, xCord, yCord);

        if (isAtEnd(e))
            return;

        if (dir == LEFT || dir == RIGHT) {
            int newY = (int) (e.getY() + getSpeedAndHeight(UP, e.getEnemyType()));
            if (getTileType((int) e.getX(), newY) == ROAD_TILE)
                e.move(GetSpeed(e.getEnemyType()), UP);
            else
                e.move(GetSpeed(e.getEnemyType()), DOWN);
        } else {
            int newX = (int) (e.getX() + getSpeedAndWidth(RIGHT, e.getEnemyType()));
            if (getTileType(newX, (int) e.getY()) == ROAD_TILE)
                e.move(GetSpeed(e.getEnemyType()), RIGHT);
            else
                e.move(GetSpeed(e.getEnemyType()), LEFT);

        }

    }

    private void fixEnemyOffsetTile(Enemy e, int dir, int xCord, int yCord) {
        switch (dir) {
            case RIGHT:
                if (xCord < 19)
                    xCord++;
                break;
            case DOWN:
                if (yCord < 19)
                    yCord++;
                break;
        }

        e.setPos(xCord * 32, yCord * 32);
    }

    private boolean isAtEnd(Enemy e) {
        if (e.getX() == end.getxCord() * 32)
            return e.getY() == end.getyCord() * 32;
        return false;
    }

    private int getTileType(int x, int y) {
        return playing.getTileType(x, y);
    }

    private float getSpeedAndHeight(int dir, int enemyType) {
        if (dir == UP)
            return -GetSpeed(enemyType);
        else if (dir == DOWN)
            return GetSpeed(enemyType) + 32;

        return 0;
    }

    private float getSpeedAndWidth(int dir, int enemyType) {
        if (dir == LEFT)
            return -GetSpeed(enemyType);
        else if (dir == RIGHT)
            return GetSpeed(enemyType) + 32;

        return 0;
    }

    public void spawnEnemy(int nextEnemy) {
        addEnemy(nextEnemy);
    }

    public void addEnemy(int enemyType) {
        int x = start.getxCord() * 32;
        int y = start.getyCord() * 32;

        switch (enemyType) {
            case ORC -> enemies.add(new Orc(x, y, 0, this));
            case BAT -> enemies.add(new Bat(x, y, 0, this));
            case KNIGHT -> enemies.add(new Knight(x, y, 0, this));
            case WOLF -> enemies.add(new Wolf(x, y, 0, this));
        }
    }

    public void draw(Graphics g) {
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                drawEnemy(e, g);
                drawHealthBar(e, g);
                drawEffects(e, g);
            }
        }
    }

    private void drawEffects(Enemy e, Graphics g) {
        if (e.isSlowed())
            g.drawImage(slowEffect, (int) e.getX(), (int) e.getY(), null);
    }

    private void drawHealthBar(Enemy e, Graphics g) {
        g.setColor(Color.red);
        g.fillRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 3);
    }

    private int getNewBarWidth(Enemy e) {
        return (int) (HPbarWidth * e.getHealthBarFloat());
    }

    private void drawEnemy(Enemy e, Graphics g) {
        g.drawImage(enemyImgs[e.getEnemyType()], (int) e.getX(), (int) e.getY(), null);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getAmountOfAliveEnemies() {
        int size = 0;
        for (Enemy e : enemies)
            if (e.isAlive())
                size++;

        return size;
    }

    public void rewardPlayer(int enemyType) {
        playing.rewardPlayer(enemyType);
    }

    public void reset() {
        enemies.clear();
    }
}
