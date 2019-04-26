package spaceRhythm.Game.GameObjects;

import spaceRhythm.Animation.Animation;
import spaceRhythm.Game.Game;
import spaceRhythm.Game.Handler;
import spaceRhythm.SpriteSheet.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BossMinionBlue extends GameObject {

    private GameObject player;

    private Handler handler;
    private int timer = 30;
    private BufferedImage[] sprite = new BufferedImage[3];
    private Animation sprite_anim;


    public BossMinionBlue(int x, int y, ObjectID ID, Handler handler,
                          int mx, int my, SpriteSheet ss) {
        super(x, y, ID, ss);
        this.handler = handler;
        calculateVelocity(x, y, mx, my);
        sprite[0] = ss.grabImage(1, 4, 32, 32);
        sprite[1] = ss.grabImage(2, 4, 32, 32);
        sprite[2] = ss.grabImage(3, 4, 32, 32);

        sprite_anim = new Animation(5, sprite);
    }

    public void spawmPickup() {
        Random rand = new Random();
        int spawnChance = rand.nextInt(101);
        if (spawnChance <= 10) {
            handler.addObject(new Pickup((int) this.getX(), (int) this.getY(), ObjectID.Pickup, handler, ss));
        }
    }

    public void calculateVelocity(int fromX, int fromY,
                                  int toX, int toY) {
        double distance = Math.sqrt(Math.pow((toX - fromX), 2) + Math.pow((toY - fromY), 2));
        double speed = 5;
        velY = (int) ((toY - fromY) * speed / distance);
        velX = (int) ((toX - fromX) * speed / distance);
    }

    @Override
    public void tick() {
        timer--;
        x += velX;
        y += velY;

        if (timer <= 0) {
            for (int i = 0; i < handler.object.size(); i++) {
                if (handler.object.get(i).getID() == ObjectID.Player)
                    player = handler.object.get(i);
            }

            float distX = x - player.getX() - 16;
            float distY = y - player.getY() - 22;
            float distance = (float) Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2));

            velX = (float) ((-1.0 / distance) * distX * 3);
            velY = (float) ((-1.0 / distance) * distY * 3);
        }

        //collision
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getID() == ObjectID.Block) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(this);
                }
            }
            if (tempObject.getID() == ObjectID.Player) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(this);
                    Game.hp--;
                }
            }
            if (tempObject.getID() == ObjectID.BulletRed) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(tempObject);
                }
            }
            if (tempObject.getID() == ObjectID.BulletBlue) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(tempObject);
                    handler.removeObject(this);
                    spawmPickup();
                }
            }
            if (tempObject.getID() == ObjectID.Pickup) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(tempObject);
                }
            }
        }
        sprite_anim.runAnimation();
    }

    @Override
    public void render(Graphics g) {
//        g.setColor(Color.blue);
//        g.fillRect((int)x, (int)y, 32, 32);
        sprite_anim.drawAnimation(g, x, y, 0);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }
}
