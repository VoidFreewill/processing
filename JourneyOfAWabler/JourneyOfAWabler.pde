import processing.sound.*;


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

void setup() {
  size(displayWidth, displayHeight);
  WIDTH = displayWidth;
  HEIGHT = displayHeight;
  DISPMULTIPLIER = float(HEIGHT) / 2560.0;
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

void draw() {
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

void backPressed() {
  if (! isHome ) { 
    isHome = true;
  }
  else{
    exit();
  }
}
