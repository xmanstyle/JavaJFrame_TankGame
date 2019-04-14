package xiaoming2;

//炸弹效果类
public class Bomb {
    int x;
    int y;
    int life = 9;
    boolean isLive = true;
    public Bomb(int _x,int _y){
        x = _x;
        y = _y;
    }
    public void lifeDown(){//减少生命值
        if(life>0){
            life--;
        }else{
            isLive = false;
        }
    }
}
