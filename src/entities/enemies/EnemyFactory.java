package entities.enemies;
import entities.Enemy;

public class EnemyFactory {
    public static Enemy createEnemy(int type, float x, float y, float speed,int scoreValue, int damage, int health, float verticalSpeed) {
        if (type == 1) {
            return new Enemy1(x, y, speed, scoreValue, damage, health, verticalSpeed);
        } else if (type == 2) {
            return new Enemy2(x, y, speed, scoreValue, damage, health, verticalSpeed);
        } else if (type == 3) {
            return new Enemy3(x, y, speed,scoreValue, damage, health, verticalSpeed);
        } else if (type == 4) {
            return new Enemy4(x, y, speed,scoreValue, damage, health, verticalSpeed);
        } else {
            throw new IllegalArgumentException("Nieznany typ przeciwnika: " + type);
        }
    }

}
