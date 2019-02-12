public class Home {

  Wabler p1 = new Wabler(WIDTH/2, HEIGHT*0.7, 0, 0, 15 * DISPMULTIPLIER);
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
      p1.setY(HEIGHT*0.7);
      isHome = false;
    }
  }
}
