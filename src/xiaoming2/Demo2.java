package xiaoming2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * Created by Xman on 2017/6/12 0012.
 *
 */
public class Demo2 extends JFrame{
    MyPanel mp;
    public Demo2(){
        mp = new MyPanel();
        Thread threadMp = new Thread(mp);
        threadMp.start();
        this.add(mp);
        this.addKeyListener(mp);

        this.setSize(400,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    public static void main(String[] args){
        Demo2 dt = new Demo2();
    }
}

//游戏界面，监听按钮事件,并且要不断的更新界面(通过implements Runnable实现)
class MyPanel extends JPanel implements KeyListener,Runnable
{
    ///定义我的Hero
    Hero hero;

    //敌人
    Vector<Enemy> enemies = new Vector<>();

    ///爆炸效果图
    /// 三张图片的切换组成一个砸蛋
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    //界面中可能会多处出现爆炸
    Vector<Bomb> bombs = new Vector<>();

    //主界面构造函数
    public MyPanel(){
        setBackground(Color.black);
        //英雄
        hero = new Hero(10,10,Tank.UP);
        //敌人
        Enemy enemy = new Enemy(40,10,Tank.UP);
        Enemy enemy1 = new Enemy(90,10,Tank.RIGHT);
        Enemy enemy2 = new Enemy(40,100,Tank.DOWN);
        enemies.add(enemy);
        enemies.add(enemy1);
        enemies.add(enemy2);
        //先加载爆炸需要的图片放便爆炸时使用
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/1.png"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.png"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.png"));
    }

    //界面的更新操作通过该函数实现
    public void paint(Graphics g){
        super.paint(g);

        //画出我的Hero
        drawTank(hero.getX(),hero.getY(),g,hero.direction,hero.color);

        ///画出敌人坦克
        for(int i=0;i<enemies.size();i++){
            Enemy temp = enemies.get(i);
            if(temp.isLive){
                drawTank(temp.getX(),temp.getY(),g,temp.direction,temp.color);
            }else{
                enemies.remove(temp);
            }
        }

        /// 画出炸弹
        for(int i=0;i<bombs.size();i++){
            Bomb bomb = bombs.get(i);
            if(bomb.life>6){
                g.drawImage(image1,bomb.x,bomb.y,30,30,this);
            }else if(bomb.life>3){
                g.drawImage(image2,bomb.x,bomb.y,30,30,this);
            }else if(bomb.life>0){
                g.drawImage(image3,bomb.x,bomb.y,30,30,this);
            }
            bomb.lifeDown();
            if(!bomb.isLive){
                bombs.remove(bomb);
            }
        }

        ///画出子弹
        for(int i=0;i<hero.ss.size();i++){
            Shot s = hero.ss.get(i);
            if(s != null && s.isLive){
                g.setColor(hero.color);
                g.draw3DRect(s.x,s.y,2,2,false);
            }
            if(!s.isLive){
                hero.ss.remove(s);
            }
        }
    }

    //函数 判断子弹是否击中敌人的坦克
    public void isHitEnemy(Shot s,Enemy e){
        //判断坦克的方向：
        switch (e.direction){
            case Tank.UP:
            case Tank.DOWN:
                if((s.x>e.x &&s.x<e.x+20)
                    &&(s.y>e.y && s.y<e.y+30)){
                    //击中：子弹死亡，敌人死亡
                    s.isLive = false;
                    e.isLive = false;
                    //创建一个炸弹
                    Bomb bomb = new Bomb(e.x,e.y);
                    bombs.add(bomb);
                }
                break;
            case Tank.LEFT:
            case Tank.RIGHT:
                if((s.x>e.x &&s.x<e.x+30)
                        &&(s.y>e.y && s.y<e.y+20)){
                    //击中：子弹死亡，敌人死亡
                    s.isLive = false;
                    e.isLive = false;
                    Bomb bomb = new Bomb(e.x,e.y);
                    bombs.add(bomb);
                }
                break;
        }
    }

    //画出坦克的函数
    public void drawTank(int x,int y,Graphics g,int direction,Color color){
        g.setColor(color);
        switch(direction){
            case Tank.UP:
                g.fillRect(x,y,5,30);
                //2画出右边矩形
                g.fillRect(x+15,y,5,30);
                // 3.画出中间矩形
                g.fill3DRect(x+5,y+5,10,20,false);
                g.drawOval(x+5,y+10,10,10);
                g.drawLine(x+10,y+15,x+10,y);
                break;
            case Tank.DOWN:
                g.fillRect(x,y,5,30);
                //2画出右边矩形
                g.fillRect(x+15,y,5,30);
                // 3.画出中间矩形
                g.fill3DRect(x+5,y+5,10,20,false);
                g.drawOval(x+5,y+10,10,10);
                g.drawLine(x+10,y+15,x+10,y+30);
                break;
            case Tank.LEFT:
                //画出上面的矩形
                g.fillRect(x,y,30,5);
                //画出下面的矩形
                g.fillRect(x,y+15,30,5);
                //画出中间的矩形
                g.fill3DRect(x+5,y+5,20,10,false);
                //画出圆形
                g.fillOval(x+10,y+5,10,10);
                //画出炮筒
                g.drawLine(x+15,y+10,x,y+10);
                break;
            case Tank.RIGHT:
                //画出上面的矩形
                g.fillRect(x,y,30,5);
                //画出下面的矩形
                g.fillRect(x,y+15,30,5);
                //画出中间的矩形
                g.fill3DRect(x+5,y+5,20,10,false);
                //画出圆形
                g.fillOval(x+10,y+5,10,10);
                //画出炮筒
                g.drawLine(x+15,y+10,x+30,y+10);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //监听键盘上的上下左右按钮事件来移动hero
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP://up
                this.hero.setDirection(Tank.UP);
                this.hero.moveUp();
                break;
            case KeyEvent.VK_DOWN://down
                this.hero.setDirection(Tank.DOWN);
                this.hero.moveDown();
                break;
            case KeyEvent.VK_LEFT://left
                this.hero.setDirection(Tank.LEFT);
                this.hero.moveLeft();
                break;
            case KeyEvent.VK_RIGHT://right
                this.hero.setDirection(Tank.RIGHT);
                this.hero.moveRight();
                break;
        }

        //发射子弹
        if(e.getKeyChar()=='f'){
            if(hero.ss.size()<5){
                hero.shotEnemy();
            }
        }
        repaint();//更新界面
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //界面就是一个线程，他需要不断的更新界面
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //不停的判断英雄的子弹是否击中到敌人
            for(int i=0;i<hero.ss.size();i++){
                Shot s = hero.ss.get(i);
                if(s.isLive){
                    for(int j=0;j<enemies.size();j++){
                        Enemy et = enemies.get(j);
                        if(et.isLive){
                            isHitEnemy(s,et);
                        }
                    }
                }
            }
            repaint();
        }
    }
}

