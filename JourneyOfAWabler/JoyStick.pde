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
