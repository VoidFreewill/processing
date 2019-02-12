public class MessageBox {
  Rectangle b1 = new Rectangle(WIDTH/2, HEIGHT*0.6, WIDTH/5, HEIGHT*0.05);
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
    rect(WIDTH/2, HEIGHT/2, WIDTH*0.8, HEIGHT/3, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER);

    //button
    noStroke();
    fill(126, 206, 246);
    rect(WIDTH/2, HEIGHT*0.6, WIDTH/5, HEIGHT*0.05, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER, 40*DISPMULTIPLIER);
  }
}
