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
