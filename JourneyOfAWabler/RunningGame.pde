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
