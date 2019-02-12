package processing.test.journeyofawabler;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class JourneyOfAWabler extends PApplet {




RunningGame g1;
SoundFile ost;
Opening op1;
Home home1;

int N_WABLER_STANDING = 10;
int N_WALBER_WALKING = 12;
int N_WABLER_SINGING = 14;
int N_OPENING_SCENES = 15;
int N_HOME_SCENES =7;


public int WIDTH; //display width
public int HEIGHT; // display height
public float DISPMULTIPLIER; // multiplier for non-QHD display. 1 for 2560*1440. 

PImage[] wabler_standings;
PImage[] wabler_walkings;
PImage[] wabler_singings;
PImage[] openings;
PImage[] homes;

PImage myBackground;

boolean rGameCleared = false;
boolean isOstPlayed = false;
boolean isHome = true;

public void setup() {
  
  WIDTH = displayWidth;
  HEIGHT = displayHeight;
  DISPMULTIPLIER = PApplet.parseFloat(HEIGHT) / 2560.0f;
  background(50);
  noStroke();
  textAlign(CENTER, CENTER);

  g1 = new RunningGame();
  op1 = new Opening();
  home1 = new Home();

  // Sound processing
  ost = new SoundFile(this, "Journey_of_a_wabler.wav");
  //ost.loop();


  // Sprite Sheet processing
  wabler_standings = new PImage[N_WABLER_STANDING];
  wabler_walkings = new PImage[N_WALBER_WALKING];
  wabler_singings = new PImage[N_WABLER_SINGING];
  openings = new PImage[N_OPENING_SCENES];
  homes = new PImage[N_HOME_SCENES];

  myBackground = loadImage("background_upscaled.png");

  PImage wabler_standing_sheet = loadImage("wabler_standing-Sheet.png");
  PImage wabler_walking_sheet = loadImage("wabler_walking-Sheet.png");
  PImage wabler_singing_sheet = loadImage("wabler_singing-Sheet.png");
  PImage opening_sheet = loadImage("opening-Sheet.png");
  PImage home_sheet = loadImage("home-Sheet.png");

  int wabler_standing_width = wabler_standing_sheet.width / N_WABLER_STANDING;
  int wabler_standing_height = wabler_standing_sheet.height;

  int wabler_walking_width = wabler_walking_sheet.width / N_WALBER_WALKING;
  int wabler_walking_height = wabler_walking_sheet.height;
  
  int wabler_singing_width = wabler_singing_sheet.width/ N_WABLER_SINGING;
  int wabler_singing_height = wabler_singing_sheet.height;

  int opening_width = opening_sheet.width / N_OPENING_SCENES;
  int opening_height = opening_sheet.height;

  int home_width = home_sheet.width / N_HOME_SCENES;
  int home_height = home_sheet.height;

  for (int x =0; x < N_WABLER_STANDING; x++) {
    wabler_standings[x] = wabler_standing_sheet.get(x * wabler_standing_width, 0, wabler_standing_width, wabler_standing_height);
  }
  for (int x =0; x < N_WALBER_WALKING; x++) {
    wabler_walkings[x] = wabler_walking_sheet.get(x * wabler_walking_width, 0, wabler_walking_width, wabler_walking_height);
  }
  for (int x =0; x < N_WABLER_SINGING; x++) {
    wabler_singings[x] = wabler_singing_sheet.get(x * wabler_singing_width, 0, wabler_singing_width, wabler_singing_height);
  }
  for (int x = 0; x < N_OPENING_SCENES; x++) {
    openings[x] = opening_sheet.get(x * opening_width, 0, opening_width, opening_height);
  }
  for (int x = 0; x < N_HOME_SCENES; x++) {
    homes[x] = home_sheet.get(x * home_width, 0, home_width, home_height);
  }
}

public void draw() {
  //opening
  if (millis() < 3000) {
    op1.display();
    if (! isOstPlayed && millis()> 2500) {
      ost.loop();
      isOstPlayed = true;
    }
  } else {
    if (isHome) {
      home1.display();
    } else {
      if (! rGameCleared) {
        
        g1.play();
        if (g1.isCleared) {
          rGameCleared = true;
        }
      }
    }
  }
}

public void backPressed() {
  if (! isHome ) { 
    isHome = true;
  }
  else{
    exit();
  }
}
public class Controller {
  JoyStick j1 = new JoyStick();
  JoyStick j2 = new JoyStick();

  private int prevNTouch = 0;
  private ArrayList prevIds = new ArrayList();
  private ArrayList curIds = new ArrayList();
  public float[] p1Vals = new float[2];
  public float[] p2Vals = new float[2];


  public Controller() {
  }

  public void update() {
    curIds.clear();
    for (int i = 0; i < touches.length; i++) {
      curIds.add(touches[i].id);
    }
    if (touches.length == 0) { // no touches
      j1.id = -1;
      j2.id = -1;
      j1.isEnable = false;
      j2.isEnable = false;
    } else if (touches.length > prevNTouch) { //detect new touches
      for (int i = 0; i < touches.length; i++) {
        if (! prevIds.contains(touches[i].id)) { // new touch point
          if (! curIds.contains(j1.id)) {
            j1.isEnable = true;
            j1.setId(touches[i].id);
            j1.setCenter(touches[i].x, touches[i].y);
          } else if (! curIds.contains(j2.id)) { // j1 has dominance
            j2.isEnable = true;
            j2.setId(touches[i].id);
            j2.setCenter(touches[i].x, touches[i].y);
          }
        }
      }
    } else if (touches.length < prevNTouch) { // detect touch off
      if (! curIds.contains(j1.id)) {
        j1.id = -1;
        j1.isEnable = false;
      }
      if (! curIds.contains(j2.id)) {
        j2.id = -1;
        j2.isEnable = false;
      }
    }

    j1.detect();
    j1.display();
    p1Vals[0] = j1.getXSpeed();
    p1Vals[1] = j1.getYSpeed();

    j2.detect();
    j2.display();
    p2Vals[0] = j2.getXSpeed();
    p2Vals[1] = j2.getYSpeed();


    //post processing
    prevIds.clear();
    for (int i = 0; i < touches.length; i++) {
      prevIds.add(touches[i].id);
    }
    prevNTouch = touches.length;
  }
}
public class Home {

  Wabler p1 = new Wabler(WIDTH/2, HEIGHT*0.7f, 0, 0, 15 * DISPMULTIPLIER);
  Rectangle door1 = new Rectangle(0, HEIGHT/3, WIDTH/6, HEIGHT/6);
  Rectangle touch1;
  Controller c1 = new Controller();


  public Home() {
    p1.setYLim(HEIGHT/3);
  }

  public void display() {
    //background
    int home_state = (millis()/100) % N_HOME_SCENES;
    imageMode(CORNERS);
    image(homes[home_state], 0, 0, WIDTH, HEIGHT);

    //door
    noStroke();
    fill(5, 18, 53, 50);
    rectMode(CENTER);
    rect(0, HEIGHT/3, WIDTH/6, HEIGHT/6);

    //touch processing
    if (touches.length > 0) {
      touch1 = new Rectangle(touches[0].x, touches[0].y, 30, 30);
      if (c1.p1Vals[0] == 0 && c1.p1Vals[1] == 0) { //if not touched Joystick,
        if (touch1.intersects(p1)) {
          p1.isSinging= true;
        }
      }
    }


    //player movement
    c1.update();

    p1.setDx((c1.p1Vals[0]*p1.getSpeed()));
    p1.setDy((c1.p1Vals[1]*p1.getSpeed()));
    p1.move();
    p1.display();

    //enter the game
    if (p1.intersects(door1)) {
      p1.setX(WIDTH/2);
      p1.setY(HEIGHT*0.7f);
      isHome = false;
    }
  }
}
public class JoyStick {
  private float x_speed = 0;
  private float y_speed = 0;
  private float centerX = 0;
  private float centerY = 0;
  public int id = -1;
  public boolean isEnable = false;



  public JoyStick() {
  }


  public void detect() {
    noStroke();
    fill(5, 18, 53, 50);
    x_speed = 0;
    y_speed = 0;

    if (isEnable) {
      for (int i = 0; i < touches.length; i ++) {
        if (touches[i].id == id) { // for the matching touch point
          float px = touches[i].x;
          float py = touches[i].y;
          
          //polar coordinates
          double pi = Math.atan2(py-centerY, px-centerX);
          double r = Math.sqrt(Math.pow((px-centerX),2)+ Math.pow((py-centerY),2));
          
          r = constrain((float)r, 0, WIDTH/6);
          
          x_speed = (float)(r * Math.cos(pi)); //dx
          y_speed = (float)(r * Math.sin(pi)); //dy
          
          px = x_speed + centerX;
          py = y_speed + centerY;
          
          ellipse(px, py, WIDTH/12, WIDTH/12);

          
        }
      }
    }
  }
  public void setCenter( float x, float y) {
    centerX = x;
    centerY = y;
  }
  public void setId(int id) {
    this.id = id;
  }
  public float getXSpeed() {
    return this.x_speed;
  }
  public float getYSpeed() {
    return this.y_speed;
  }

  public void display() {
    if (isEnable) {
      //large circle boundary
      noStroke();
      fill(5, 18, 53, 50);
      float r = WIDTH/3 ;
      ellipse(centerX, centerY, r, r); 
      
      //center red + sign
      stroke(255,0,0);
      strokeWeight(4 * DISPMULTIPLIER);
      line(centerX-20*DISPMULTIPLIER, centerY, centerX+20*DISPMULTIPLIER, centerY);
      line(centerX, centerY-20*DISPMULTIPLIER, centerX, centerY+20*DISPMULTIPLIER);
    }
  }
}
public class MessageBox {
  Rectangle b1 = new Rectangle(WIDTH/2, HEIGHT*0.6f, WIDTH/5, HEIGHT*0.05f);
  String body = "";
  String title = "";
  String button = "";

  public boolean isFirst = true;

  public MessageBox() {
  }

  public boolean isClicked() {
    for (int i=0; i < touches.length; i++) {
      Rectangle t1 = new Rectangle(touches[i].x, touches[i].y, 10*DISPMULTIPLIER, 10*DISPMULTIPLIER);
      if (t1.intersects(b1)) {
        return true;
      }
    }
    return false;
  }

  public void setBody(String str) {
    this.body = str;
  }
  public void setTitle(String str) {
    this.title = str;
  }
  public void setButton(String str) {
    this.button = str;
  }


  public void display() {
    //background
    noStroke();
    fill(5, 18, 53, 50);
    rectMode(CORNERS);
    rect(0, 0, WIDTH, HEIGHT);

    //box
    stroke(0, 0, 50);
    strokeWeight(4*DISPMULTIPLIER);
    fill(255);
    rectMode(CENTER);
    rect(WIDTH/2, HEIGHT/2, WIDTH*0.8f, HEIGHT/3, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER);

    //button
    noStroke();
    fill(126, 206, 246);
    rect(WIDTH/2, HEIGHT*0.6f, WIDTH/5, HEIGHT*0.05f, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER);
  }
}
public class Opening {

  public Opening() {
  }
  public void display() {
    int opening_state = (millis()/100) % N_OPENING_SCENES;
    imageMode(CORNERS);
    image(openings[opening_state], 0, 0, WIDTH, HEIGHT);
  }
}
public class Raindrop extends Rectangle {

  public float size;  
  private float dy = 5;
  private float dx = 0;
  boolean out = false;

  public Raindrop(float x, float y, float dx, float dy, float size) {
    super(x, y, size, 2 * size);
    this.dx = dx;
    this.dy = dy;
    this.size = size;
  }

  public boolean isOut() {
    return out;
    
  }

  public void move() {
    x += dx;
    y += dy;
    if(x < -100 || x > WIDTH + 100){
      out = true;
    }
    if (y > HEIGHT) {
      out = true;
    }
  }
  public void setDy(float dy) {
    this.dy = dy;
  }
  public void display() {
    noStroke();
    fill(10, 40, 200, 100); //OPTIMIZE THIS: alpha value makes lots of load on cpu
    ellipse(x, y, size, size * 2);
  }
}
class Rectangle {
  float x;
  float y;
  float w;
  float h;

  Rectangle(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this. h = h;
  }
  public boolean intersects(Rectangle other) {
    float left = x-w/2;
    float right = x+w/2;
    float top = y-h/2;
    float bottom = y+h/2;

    float oleft = other.x - 0.5f * other.w;
    float oright = other.x + 0.5f * other.w;
    float otop = other.y - 0.5f * other.h;
    float obottom = other.y + 0.5f * other.h;


    return !(left > oright ||
      right < oleft ||
      top > obottom ||
      bottom < otop);
  }
}
public class RunningGame {

  private boolean isStageChanged = false;
  private int stage = 1;
  private int score = 0;
  private int nTry = 0;
  private int highestScore = 0;
  private int pScore = 0;
  boolean isCleared = false;
  boolean isAlert = false; // message box handler

  Controller c1 = new Controller();

  Wabler p1 = new Wabler(500, 1800, 0, 0, 10 * DISPMULTIPLIER);
  Wabler p2 = new Wabler(500, 1800, 0, 0, 10 * DISPMULTIPLIER);
  MessageBox b1 = new MessageBox();

  Raindrop[] rains;

  public RunningGame() {
    //player settings
    p1.setYLim(HEIGHT/2);

    //rain settings
    this.rains = new Raindrop[20];
    resetGame();
  }
  public void resetGame() {
    stage = 1;
    pScore = score;
    score = 0;
    int index = 0;
    p1 = new Wabler(500, 1800, 0, 0, 10 * DISPMULTIPLIER);
    p1.setYLim(HEIGHT/2);
    // wave 1
    for (int i = 0; i < 5; i++) {
      float x = random(10, displayWidth );
      float y = 0;
      float dx = random(-5, 5) * DISPMULTIPLIER;
      float dy = random(20, 40) * DISPMULTIPLIER;
      float rainSize = random(40, 80)* DISPMULTIPLIER;

      rains[index] = new Raindrop(x, y, dx, dy, rainSize);
      index ++;
    }
    for (int i = 0; i < 15; i++) {
      rains[index] = new Raindrop(0, 0, 0, 0, 0);
      index ++;
    }
  }

  public void play() {
    // settings
    if (isAlert) { // shows the message box
      if (b1.isFirst) {
        b1.display();
        b1.isFirst = false;
      }
      b1.isFirst = b1.isClicked();
      isAlert = ! b1.isClicked();
    } else {

      // ingame play
      imageMode(CORNERS);
      image(myBackground, 0, 0, WIDTH, HEIGHT);

      //score system
      if (score < 100) {
        stage = 1;
      } else if (score < 500) {
        stage = 2;
      } else if (score < 2000) {
        stage = 3;
      } else { //cleared
        isCleared = true;
      }

      // rain processing 
      int _i = 0;
      for (Raindrop rain : rains) {
        rain.move();
        rain.display();
        if (rain.isOut() || (rain.size == 0 && _i < 5 + (score/100))) { // if rain is out of screen or score has increased to the critical point
          score += 5;
          highestScore = max(highestScore, score);
          float x = random(10, displayWidth );
          float y = 0;
          float sqrt_stage = sqrt(stage);
          float dx = random(-5 * sqrt_stage, 5 * sqrt_stage) * DISPMULTIPLIER;
          float dy = random(20 * sqrt_stage, 40 * sqrt_stage) * DISPMULTIPLIER;
          float rainSize = random(40, 80) * DISPMULTIPLIER;
          rains[_i] = new Raindrop(x, y, dx, dy, rainSize);
        }
        if (p1.intersects(rain)) { //game over
          isAlert = true;
          resetGame();
          nTry += 1;
          println("game over");
          break;
        }
        _i ++;
      }

      // text processing

      textSize(70 * DISPMULTIPLIER);
      textAlign(LEFT, CENTER);
      rectMode(CORNER);
      fill(180);
      text("Score: "+ score, 50 * DISPMULTIPLIER, 50 * DISPMULTIPLIER);
      text("Stage: " + stage, 50 * DISPMULTIPLIER, 150 * DISPMULTIPLIER);
      text("Best: "+ highestScore, 50 * DISPMULTIPLIER, 250 * DISPMULTIPLIER);
      text("Before: " + pScore, 40 * DISPMULTIPLIER, 350 * DISPMULTIPLIER);
      text("Try: " +nTry, 50 * DISPMULTIPLIER, 450 * DISPMULTIPLIER);


      //movement
      c1.update();

      p1.setDx((c1.p1Vals[0]*p1.getSpeed()));
      p1.setDy((c1.p1Vals[1]*p1.getSpeed()));
      p1.move();
      p1.display();

      p2.setDx((c1.p2Vals[0]*p1.getSpeed()));
      p2.setDy((c1.p2Vals[1]*p1.getSpeed()));
      p2.move();
      p2.display();
    }
  }
}
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

  private float speed = 0.25f;
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
  public void settings() {  size(displayWidth, displayHeight); }
}
