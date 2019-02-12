public class Opening {

  public Opening() {
  }
  public void display() {
    int opening_state = (millis()/100) % N_OPENING_SCENES;
    imageMode(CORNERS);
    image(openings[opening_state], 0, 0, WIDTH, HEIGHT);
  }
}
