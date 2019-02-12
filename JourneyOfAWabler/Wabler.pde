public class Wabler extends Rectangle { //initializing position

  private float dy = 0;
  private float dx = 0;
  private float size = 10; // multiplier
  private boolean isWalking = true; 
  private boolean isRight = true; //
  boolean isSinging = false;
  boolean isFirstloop = true;
  private int standing_state; // 0~9
  private int walking_state; //0~11
  private int singing_state =0; //0~13
  private int singing_start_ms = 0;
  private int singing_state_a = 1; // initialize to 1 for checking first run

  private float speed = 0.25;
  private int STANDING_FRAME_WIDTH = 20;
  private int STANDING_FRAME_HEIGHT = 32;
  private int WALKING_FRAME_WIDTH = 20;
  private int WALKING_FRAME_HEIGHT = 32;
  private int SINGING_FRAME_WIDTH = 30;
  private int SINGING_FRAME_HEIGHT = 44;
  private float yLim = size*STANDING_FRAME_HEIGHT/2;

  public Wabler(float x, float y, float dx, float dy, float size) {
    super (x, y, 10 * size, 16 * size); // x0.5 smaller than the body
    this.dx = dx;
    this.dy = dy;
    this.size = size;
  }

  public void move() {
    x += dx;
    y += dy;
    if (dx > 0) {
      setRight();
      setWalking(true);
    } else if (dx < 0) {
      setLeft();
      setWalking(true);
    } else {
      setWalking(false);
    }
    x = constrain(x, size*STANDING_FRAME_WIDTH/2, displayWidth - size*STANDING_FRAME_WIDTH/2 - 1);
    y = constrain(y, yLim, displayHeight - size*STANDING_FRAME_HEIGHT/2 - 1);
  }

  public void setYLim(float _yLim) {
    this.yLim = _yLim;
  }
  public void setSpeed(float speed) {
    this.speed = speed;
  }
  public void setDx(float dx) {
    this.dx = dx;
  }
  public void setDy(float dy) {
    this.dy = dy;
  }
  public void setDxy(float dxy[]){
    this.dx = dxy[0];
    this.dy = dxy[1];
    
  }
  public void setX(float _x) {
    x = _x;
  }
  public void setY(float _y) {
    y = _y;
  }
  public void setLeft() {
    this.isRight = false;
  }
  public void setRight() {
    this.isRight = true;
  }
  public void setWalking(boolean status) {
    this.isWalking = status; //
  }
  public float getSpeed() {
    return this.speed;
  }
  public float getDx() {
    return this.dx;
  }
  public float getDy() {
    return this.dy;
  }
  public float getX() {
    return this.x;
  }
  public float getY() {
    return this.y;
  }


  public void standing() {
    standing_state = (millis()/100) % N_WABLER_STANDING;
    if (isRight) {
      image(wabler_standings[standing_state], this.x, this.y, STANDING_FRAME_WIDTH * this.size, STANDING_FRAME_HEIGHT * this.size);
    } else {
      pushMatrix();
      scale(-1, 1);
      image(wabler_standings[standing_state], -1 * this.x, this.y, STANDING_FRAME_WIDTH * this.size, STANDING_FRAME_HEIGHT * this.size);
      scale(-1, 1);
      popMatrix();
    }
  }

  public void walking() {
    //println(millis());
    walking_state = (millis()/100) % N_WALBER_WALKING;
    if (isRight) {
      image(wabler_walkings[walking_state], this.x, this.y, WALKING_FRAME_WIDTH * this.size, WALKING_FRAME_HEIGHT * this.size);
    } else {
      pushMatrix();
      scale(-1, 1);
      image(wabler_walkings[walking_state], -1 * this.x, this.y, WALKING_FRAME_WIDTH * this.size, WALKING_FRAME_HEIGHT * this.size);
      scale(-1, 1);
      popMatrix();
    }
  }
  public void singing() {
    if (singing_state_a >= 1) {
      //println(millis());
      //println(singing_state); //DEBUG
      singing_start_ms = millis();
      //println(singing_start_ms); //DEBUG
      singing_state += 1;
      singing_state_a = 0;
    }
    //println(singing_state); //DEBUG
    singing_state_a = ((millis()-singing_start_ms)/100);
    //println(singing_state); //DEBUG    
    if (isRight) {
      image(wabler_singings[singing_state], this.x, this.y, SINGING_FRAME_WIDTH * this.size, SINGING_FRAME_HEIGHT * this.size);
    } else {
      pushMatrix();
      scale(-1, 1);
      image(wabler_singings[singing_state], -1 * this.x, this.y, SINGING_FRAME_WIDTH * this.size, SINGING_FRAME_HEIGHT * this.size);
      scale(-1, 1);
      popMatrix();
    }

    if (singing_state > N_WABLER_SINGING-2) { //animation ends
      singing_state = 0;
      singing_state_a = 1;
      isSinging = false;
    }
  }
  public void display() {
    imageMode(CENTER);
    if (isSinging) {
      singing();
    } else {
      if (isWalking) {
        walking();
      } else {
        standing();
      }
    }
  }
}
