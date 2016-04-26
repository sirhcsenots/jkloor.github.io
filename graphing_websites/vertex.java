import org.htmlparser.Node;
import traer.physics.Particle;
import java.util.ArrayList;

public class Vertex {
  
  private Vertex parentVertex;
  private ArrayList childVertices = new ArrayList();
  private Node node;
  private Particle particle;
  private String color;
  
  public Vertex(Node node) {
    this.node = node;
  }
  
  public Node getNode(){
    return this.node;  
  }
  
  public void setParentVertex(Vertex parentVertex) {
    this.parentVertex = parentVertex;  
  }
  
  public Vertex getParentVertex() {
    return this.parentVertex;  
  }
  
  public void addToChildVertices(Vertex childVertex) {
    this.childVertices.add(childVertex);
  }
  
  public ArrayList getChildren() {
    return this.childVertices;
  }
  
  public void setParticle(Particle particle) {
    this.particle = particle;  
  }
  
  public Particle getParticle() {
    return this.particle;  
  }
  
  public void setColor(String color) {
    this.color = color;  
  }
  
  public String getColor() {
    return this.color;  
  }
  
  
}
