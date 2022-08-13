package helpz;

public class Constants {
    public static class Projectiles {
        public static final int ARROW = 0;
        public static final int CHAINS = 1;
        public static final int BOMB = 2;

        public static float GetSpeed(int type) {
			return switch (type) {
				case ARROW -> 8f;
				case BOMB -> 4f;
				case CHAINS -> 6f;
				default -> 0f;
			};
		}
    }

    public static class Towers {
        public static final int CANNON = 0;
        public static final int ARCHER = 1;
        public static final int WIZARD = 2;

        public static int GetTowerCost(int towerType) {
			return switch (towerType) {
				case CANNON -> 65;
				case ARCHER -> 35;
				case WIZARD -> 50;
				default -> 0;
			};
		}

        public static String GetName(int towerType) {
			return switch (towerType) {
				case CANNON -> "Cannon";
				case ARCHER -> "Archer";
				case WIZARD -> "Wizard";
				default -> "";
			};
		}

        public static int GetStartDmg(int towerType) {
			return switch (towerType) {
				case CANNON -> 15;
				case ARCHER -> 5;
				case WIZARD -> 0;
				default -> 0;
			};
		}

        public static float GetDefaultRange(int towerType) {
			return switch (towerType) {
				case CANNON -> 75;
				case ARCHER -> 120;
				case WIZARD -> 100;
				default -> 0;
			};
		}

        public static float GetDefaultCooldown(int towerType) {
			return switch (towerType) {
				case CANNON -> 120;
				case ARCHER -> 35;
				case WIZARD -> 50;
				default -> 0;
			};
		}
    }

    public static class Direction {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Enemies {
        public static final int ORC = 0;
        public static final int BAT = 1;
        public static final int KNIGHT = 2;
        public static final int WOLF = 3;

        public static int GetReward(int enemyType) {
			return switch (enemyType) {
				case ORC, BAT -> 5;
				case KNIGHT -> 25;
				case WOLF -> 10;
				default -> 0;
			};
		}

        public static float GetSpeed(int enemyType) {
			return switch (enemyType) {
				case ORC -> 0.5f;
				case BAT -> 0.7f;
				case KNIGHT -> 0.45f;
				case WOLF -> 0.85f;
				default -> 0;
			};
		}

        public static int GetStartHealth(int enemyType) {
			return switch (enemyType) {
				case ORC -> 85;
				case BAT -> 100;
				case KNIGHT -> 400;
				case WOLF -> 125;
				default -> 0;
			};
		}
    }

    public static class Tiles {
        public static final int WATER_TILE = 0;
        public static final int GRASS_TILE = 1;
        public static final int ROAD_TILE = 2;
    }
}
