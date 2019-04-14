package xiaoming2;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Xman on 2017/6/12 0012.
 */
/// tank 类
public class Tank
{
    int x = 0;
    int y = 0;
    int direction;
    int speed = 7;
    Color color;
    public static final int UP  = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public Tank(int x,int y,int dir){
        this.x = x;
        this.y = y;
        this.direction = dir;
    }
    public void setDirection(int dir){this.direction = dir;}
    public int getDirection(){return this.direction;}
    public void setSpeed(int s){this.speed = s;}
    public int getSpeed(){return this.speed;}
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void moveUp(){
        y -= speed;
    }
    public void moveDown(){
        y += speed;
    }
    public void moveLeft(){
        x -= speed;
    }
    public void moveRight(){
        x += speed;
    }

}

class Hero extends Tank
{
    //Hero 里面有子弹集合
    Vector<Shot> ss = new Vector<Shot>();

    public Hero(int x,int y,int dir){
        super(x,y,dir);
        this.color = Color.yellow;
    }

    //射击敌人
    public void shotEnemy(){
        int shotX = 0;
        int shotY = 0;
        //根据坦克当前的方向，创建一个子弹
        switch (this.direction){
            case Tank.UP:
                shotX = x+10;
                shotY = y;
                break;
            case Tank.DOWN:
                shotX = x+10;
                shotY = y+30;
                break;
            case Tank.LEFT:
                shotX = x;
                shotY = y+10;
                break;
            case Tank.RIGHT:
                shotX = x+30;
                shotY = y+10;
                break;
        }
        //开启子弹线程,并加入Hero 的子弹集合中
        Shot s = new Shot(shotX,shotY,direction);
        ss.add(s);
        Thread thread = new Thread(s);
        thread.start();
    }
}
class Enemy extends Tank implements Runnable
{
    boolean isLive = true;
    public Enemy(int x,int y,int dir){
        super(x,y,dir);
        this.color = Color.cyan;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            switch (this.direction){
                case Tank.UP:
                    y -= speed;
                    break;
                case Tank.DOWN:
                    y += speed;
                    break;
                case Tank.LEFT:
                    break;
                case Tank.RIGHT:
                    x += speed;
                    break;
            }
        }
    }
}

//子弹线程
class Shot implements Runnable
{
    int x;
    int y;
    int dir;
    int speed;
    boolean isLive;
    public Shot(int _x,int _y,int _dir){
        this.x = _x;
        this.y = _y;
        this.dir = _dir;
        this.speed = 6;
        this.isLive = true;
    }

    @Override
    public void run() {
        while(isLive){
            switch(dir){
                case Tank.UP:
                    y -= speed;
                    break;
                case Tank.DOWN:
                    y += speed;
                    break;
                case Tank.LEFT:
                    x -= speed;
                    break;
                case Tank.RIGHT:
                    x += speed;
                    break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ///子弹何时死亡(当子弹出了界面是就死亡)
            if(x < 0 || x > 400||y < 0 || y > 300){
                isLive = false;
            }
        }
    }
}
