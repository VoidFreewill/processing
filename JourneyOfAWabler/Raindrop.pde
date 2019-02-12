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
