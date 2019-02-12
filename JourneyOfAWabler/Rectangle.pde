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
  boolean intersects(Rectangle other) {
    float left = x-w/2;
    float right = x+w/2;
    float top = y-h/2;
    float bottom = y+h/2;

    float oleft = other.x - 0.5 * other.w;
    float oright = other.x + 0.5 * other.w;
    float otop = other.y - 0.5 * other.h;
    float obottom = other.y + 0.5 * other.h;


    return !(left > oright ||
      right < oleft ||
      top > obottom ||
      bottom < otop);
  }
}
