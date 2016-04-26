
public class TextBox {
  
  private String text;
  private float x;
  private float y;
  private int lines;
  
  public TextBox(String text, float x, float y, int lines) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.lines = lines;
  }
  
  public String getText(){
    return this.text;  
  }
  
   public float getX(){
    return this.x;  
  }
  
   public float getY(){
    return this.y;  
  }
  
  public int getLines() {
    return this.lines;
  }
  

  
  
}
