////////////////////////////////////////////////////////////
//
// A word of caution: This code can be optimized - I made it in quite a hurry. 
// A large chunk is from an example from Traer Physics.
//
// Feel free to use / modify this code however you wish. 
// I would be happy if you would make a reference to the www.aharef.info site!
//
// Oh, and yes, don't forget to check out my alter ego art project, www.onethousandpaintings.com
//
////////////////////////////////////////////////////////////

import traer.physics.*;
import traer.animation.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import processing.net.*; 
import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.filters.*;
import org.htmlparser.nodes.*;

int totalNumberOfNodes = 0;
ArrayList elements = new ArrayList();
ArrayList parents = new ArrayList();
int nodesAdded = 0; 
int maxDegree = 0;
HashMap particleVertexLookup = new HashMap();
HashMap nodeVertexLookup = new HashMap();
ArrayList vertices = new ArrayList();
ArrayList textBoxes = new ArrayList();
ParticleSystem physics;
Smoother3D centroid;
Particle activeParticle;

// MAKE SURE YOU CHANGE THIS! I might change this site in the future.
// Simply point to a site on your own server that gets the html from any other site.
private String urlPath = "http://www.aharef.info/static/htmlgraph/getDataFromURL.php?url=http://www.msn.com";
private String content;

float NODE_SIZE = 30;
float EDGE_LENGTH = 50;
float EDGE_STRENGTH = 0.2;
float SPACER_STRENGTH = 2000;
  
final String GRAY = "155,155,155";
final String BLUE = "0,0,155";
final String ORANGE = "255,155,51";
final String YELLOW = "255,255,51";
final String RED = "255,0,0";
final String GREEN = "0,155,0";
final String VIOLET = "204,0,255";
final String BLACK = "0,0,0";

PFont font; 

void setup() {
  size(600, 600);
  // if you want to run it locally from within processing, comment the two following lines, and define urlPath as http://www.whateveryoursite.com
  //String urlFromForm = param("urlFromForm"); 
  //urlPath += urlFromForm;
  smooth();
  framerate(24);
  strokeWeight(2);
  ellipseMode(CENTER);
  physics = new ParticleSystem(0, 0.25);
  centroid = new Smoother3D(0.0, 0.0, 0.0, 0.8);
  initialize();
  //String[] fontList = PFont.list();
  //println(fontList);
  font = loadFont("CourierNewPSMT-24.vlw");
  this.getDataFromClient();
}

void getDataFromClient() {
  try {
    org.htmlparser.Parser ps = new org.htmlparser.Parser ();
    ps.setURL(urlPath);
    OrFilter orf = new OrFilter();
    NodeFilter[] nfls = new NodeFilter[1];
    nfls[0] = new TagNameFilter("html");
    orf.setPredicates(nfls);
    NodeList nList  = ps.parse(orf);
    Node node = nList.elementAt(0);
    this.parseTree(node);
    EDGE_STRENGTH = (1.0 / maxDegree) * 5;
    if (EDGE_STRENGTH > 0.2) EDGE_STRENGTH = 0.2;
  }
  catch (Exception e) {
     e.printStackTrace();
  }
}

void initialize() {
  physics.clear();
}

void parseTree(Node node) {
  int degree = 0;
  if (node == null) return;
  String nodeText = node.getText();
  if (node instanceof TagNode && !((TagNode)node).isEndTag())  {   
      // create a new vertex from that node
      Vertex newVertex = new Vertex(node); 
      // add it to the list of vertice
      vertices.add(newVertex);
      // add it to the lookup taple, so that we can later get the vertex as a function of the node
      nodeVertexLookup.put(node,newVertex);
      // what is the parent node?
      Node parentNode = node.getParent();
      if (parentNode != null) {
        // which vertex corresponds to the parentNode?
        Vertex parentVertex = (Vertex)nodeVertexLookup.get(parentNode);
        // set the parent Vertex
        newVertex.setParentVertex(parentVertex);
        // set the child vertex
        parentVertex.addToChildVertices(newVertex);
      }
      totalNumberOfNodes++;
   }
  NodeList children = node.getChildren();
  if (children == null) {
    return;
  }
  SimpleNodeIterator iter = children.elements();
  while(iter.hasMoreNodes()) {
    Node child = iter.nextNode();
    if (child instanceof TagNode && !((TagNode)child).isEndTag()) degree++;
    parseTree(child);
  }
  if (degree > maxDegree) maxDegree = degree;
}





void draw() {
  //uncomment this if you want your network saved as pdfs
  //beginRecord(PDF, "frameimage-####.pdf");
  if (nodesAdded < totalNumberOfNodes) {
    this.addNodesToGraph();
  }
  else {
    if (EDGE_STRENGTH < 0.05) EDGE_STRENGTH += 0.0001;  
  }
  physics.tick( 1.0 );
  if (physics.numberOfParticles() > 1) {
    updateCentroid();
  }
  centroid.tick();
  background(255);
  translate(width/2, height/2);
  scale(centroid.z());
  translate( -centroid.x(), -centroid.y() );
  drawNetwork();
  drawTextBoxes();
  //uncomment this if you want your network saved as pdfs
  //endRecord();
}

void addNodesToGraph() {
  Particle newParticle;
  Vertex currentVertex = (Vertex) vertices.get(nodesAdded);
  TagNode tagNodeToAdd = (TagNode) currentVertex.getNode();
  Node parentNode = tagNodeToAdd.getParent();
  newParticle = createParticle();
  currentVertex.setParticle(newParticle);
  particleVertexLookup.put(newParticle,currentVertex);
  Vertex parentVertex = currentVertex.getParentVertex();
  if (parentVertex != null) connectParticles(newParticle,currentVertex.getParentVertex().getParticle()); // all tags except html
  String nodeColor = GRAY;
  if (tagNodeToAdd.getTagName().equalsIgnoreCase("a")) nodeColor = BLUE;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("div")) nodeColor = GREEN;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("html")) nodeColor = BLACK;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("tr")) nodeColor = RED;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("td")) nodeColor = RED;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("table")) nodeColor =  RED;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("br")) nodeColor =  ORANGE;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("p")) nodeColor =  ORANGE;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("blockquote")) nodeColor =  ORANGE;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("img")) nodeColor =  VIOLET;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("form")) nodeColor =  YELLOW;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("input")) nodeColor =  YELLOW;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("textarea")) nodeColor =  YELLOW;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("select")) nodeColor =  YELLOW;
  else if (tagNodeToAdd.getTagName().equalsIgnoreCase("option")) nodeColor =  YELLOW;
  else nodeColor = GRAY;
  currentVertex.setColor(nodeColor);
  nodesAdded++;  
}


Particle createParticle() {
   Particle p = physics.makeParticle();
   return p;
  }

void connectParticles(Particle p1, Particle p2) {
  addSpacersToNode(p1, p2);
  makeEdgeBetween(p1, p2);
  float randomX =  (float)((Math.random() * 0.5) + 0.5);
  if (Math.random() < 0.5) randomX *= -1;
  float randomY = (float)((Math.random() * 0.5) + 0.5);
  if (Math.random() < 0.5) randomY *= -1;
  p1.moveTo(p2.position().x() + randomX, p2.position().y() + randomY, 0);
}


void drawNetwork() {
  // draw edges
  stroke(0);
  beginShape(LINES);
  for (int i = 0; i < physics.numberOfSprings(); ++i) {
    Spring e = physics.getSpring(i);
    Particle a = e.getOneEnd();
    Particle b = e.getTheOtherEnd();
    vertex(a.position().x(), a.position().y());
    vertex(b.position().x(), b.position().y());
  }
  endShape();
  // draw vertices
  for (int i = 0; i < physics.numberOfParticles(); ++i) {
    Particle p = physics.getParticle(i);
    Vertex currentVertex = (Vertex)particleVertexLookup.get(p);
    String nodeColor = currentVertex.getColor();
    if (nodeColor != null) {
      String[] nodeColors = nodeColor.split(",");
      int r = Integer.parseInt(nodeColors[0]);
      int g = Integer.parseInt(nodeColors[1]);
      int b = Integer.parseInt(nodeColors[2]);
      noStroke();
      strokeWeight(2);
      if (p == activeParticle) {
        strokeWeight(25);
        stroke(r,g,b);        
      }
      fill(r,g,b);
    }
    ellipse(p.position().x(), p.position().y(), NODE_SIZE, NODE_SIZE);
  }
  
}

void drawTextBoxes() {
  if (textBoxes.size() == 0) cursor(ARROW);
  for (int i = 0; i < textBoxes.size(); i++) {
    TextBox currentTextBox = (TextBox)textBoxes.get(i);
    textFont(font, 12/centroid.z()); 
    String s = currentTextBox.getText();
    String[] lines = s.split("\n");
    int longest = 0;
    String longestLine = "";
    for (int ii = 0; ii < lines.length; ii++) {
      if (lines[ii].length() > longest) {
        longest = lines[ii].length();
        longestLine = lines[ii];
      }
    }
    float sWidth = textWidth(longestLine);
    float offsetX = 10;
    float offsetY = 4.5;
    float boxWidth    = sWidth + (1/centroid.z())*2*offsetX;
    float boxHeight   = ((12 + 2*offsetY)*currentTextBox.getLines()) / centroid.z();
    float boxX        = currentTextBox.getX();
    float boxY        = currentTextBox.getY();
    float sX          = boxX + offsetX;// + (offsetX * centroid.z());
    float sY          = boxY  + 12 +offsetY;
    fill(255);
    stroke(0); 
    rect(scaleAbsToRelX(boxX), scaleAbsToRelY(boxY), boxWidth, boxHeight);
    fill(0);
    text(s, (int)scaleAbsToRelX(sX), (int)scaleAbsToRelY(sY));
    cursor(ARROW);
  }
}




void updateCentroid() {
  float
    xMax = Float.NEGATIVE_INFINITY,
    xMin = Float.POSITIVE_INFINITY,
    yMin = Float.POSITIVE_INFINITY,
    yMax = Float.NEGATIVE_INFINITY;
  for (int i = 0; i < physics.numberOfParticles(); ++i) {
    Particle p = physics.getParticle(i);
    xMax = max(xMax, p.position().x());
    xMin = min(xMin, p.position().x());
    yMin = min(yMin, p.position().y());
    yMax = max(yMax, p.position().y());
  }
  float deltaX = xMax-xMin;
  float deltaY = yMax-yMin;
  if (deltaY > deltaX) {
    centroid.setTarget(xMin + 0.5*deltaX, yMin + 0.5*deltaY, height/(deltaY+50));
  }
  else {
    centroid.setTarget(xMin + 0.5*deltaX, yMin + 0.5*deltaY, width/(deltaX+50));
  }
}

void addSpacersToNode( Particle p, Particle r ) {
  for ( int i = 0; i < physics.numberOfParticles(); ++i ) {
    Particle q = physics.getParticle(i);
    if (p != q && p != r) {
      physics.makeAttraction(p, q, -SPACER_STRENGTH, 20);
    }
  }
}

void makeEdgeBetween(Particle a, Particle b) {
  physics.makeSpring( a, b, EDGE_STRENGTH, EDGE_STRENGTH, EDGE_LENGTH );
}


// interactive stuff
void mousePressed() {
  this.handleMousePressed(activeParticle);
  return;
  }
  
void handleMousePressed(Particle p) {
  if (p == null) {
    this.textBoxes.clear();
    return;
  }
  Vertex activeVertex = (Vertex)particleVertexLookup.get(activeParticle);
  Node activeNode = activeVertex.getNode();
  int numberOfLines = 1; // tagname
  String s = ((TagNode)activeNode).getTagName();
  Hashtable attributes = ((TagNode)activeNode).getAttributes();
  Iterator iter = attributes.keySet().iterator();
  while (iter.hasNext()) {
    String attributeName = (String)iter.next();
    if (attributeName.charAt(0) == '$') continue;
    if (attributeName.charAt(0) == '/') continue;
    String attributeValue = (String)attributes.get(attributeName);
    s += "\n" + attributeName + "=\"" + attributeValue + "\"";
    numberOfLines++;
  }
  TextBox textBox = new TextBox(s,mouseX,mouseY,numberOfLines);
  if (this.textBoxes.isEmpty()) this.textBoxes.add(textBox);
  else this.textBoxes.set(0,textBox); 
}

void mouseMoved() {
  activeParticle = null;
  float currentMin = Float.MAX_VALUE;
  Particle pHTML = physics.getParticle(0);
  Vertex vHTML = (Vertex)particleVertexLookup.get(pHTML);
  for (int i = 0; i < physics.numberOfParticles(); ++i) {
    Particle p = physics.getParticle(i);
    float xPos =  width / 2 + ( (p.position().x() - centroid.x()) * centroid.z() );
    float yPos =  height / 2 + ( (p.position().y() - centroid.y()) * centroid.z() );
    float newMin = min(currentMin, dist(mouseX, mouseY, xPos, yPos));
    if (newMin < currentMin) {
       currentMin = newMin;
       if (currentMin <= NODE_SIZE * centroid.z()) {
         activeParticle = p;
         break; 
       }
    }
  }
  this.handleMouseOver(activeParticle);
  return;
}


void handleMouseOver(Particle p) {
  if (p == null) {
    this.textBoxes.clear();
    return;
  }
  Vertex activeVertex = (Vertex)particleVertexLookup.get(activeParticle);
  Node activeNode = activeVertex.getNode();
  TextBox textBox = new TextBox(((TagNode)activeNode).getTagName(),mouseX,mouseY,1);
  if (this.textBoxes.isEmpty()) this.textBoxes.add(textBox);
  else this.textBoxes.set(0,textBox); 
}

float scaleRelToAbsX(float x) {
  return width / 2 + ( (x - centroid.x() ) * centroid.z() ); 
}

float scaleRelToAbsY(float y) {
  return height / 2 + ( (y - centroid.y() ) * centroid.z() ); 
}

float scaleAbsToRelX(float x) {
  return ( (x - (width/2)) /  centroid.z() ) + centroid.x();
}

float scaleAbsToRelY(float y) {
  return ( (y - (height/2)) /  centroid.z() ) + centroid.y();
}








