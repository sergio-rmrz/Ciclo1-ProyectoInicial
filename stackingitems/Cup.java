
/**
 * Write a description of class Cup here.
 * 
 * @author Yazid Sánchez - Sergio Ramírez
 * @version 1.0
 */
public class Cup
{
    // instance variables - replace the example below with your own
    private int number;
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private boolean hasLid;
    
    private static final int PIXEL_POR_CM = 5;
    
    private Rectangle shape1;
    private Rectangle shape2;
    
    

    /**
     * Constructor for objects of class Cup
     */
    public Cup(int number, String color) {
        this.number = number;
        this.height = calcularHeight(number);
        this.color = color;
        this.xPosition = 130; 
        this.yPosition = 200;
        this.width = calcularHeight(number);
        this.isVisible = false;
        
        this.hasLid = false; //esto es para indicar que por defecto ninguna copa viene con tapa (Lid)
    }
    
    public int calcularHeight(int number) {
        return ((2 * number) - 1) * PIXEL_POR_CM;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    public void makeInvisible() {
        erase();
        isVisible = false;
    }
    
    public void moverVerticar(int distancia) {
        erase();
        yPosition += distancia;
        draw();
    }
    
    public void moveTo(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }
    
    public void gotALid() {
        hasLid = true;
    }
    
    
    public void draw() {
        if (isVisible) {

            int grosor = 7;

            int esquinaX = xPosition - width / 2;
            int esquinaY = yPosition - height;

            shape1 = new Rectangle();
            shape1.changeSize(height, width);
            shape1.changeColor(color);
            shape1.moveHorizontal(esquinaX - 70);
            shape1.moveVertical(esquinaY - 15);
            shape1.makeVisible();
            
            shape2 = new Rectangle();
            shape2.changeSize(height - grosor, width - 2 * grosor);
            shape2.changeColor("white");
            shape2.moveHorizontal((esquinaX + grosor) - 70);
            shape2.moveVertical(esquinaY - 15);
            shape2.makeVisible();
        }
    }
    
    public void erase() {
        if (shape1 != null) shape1.makeInvisible();
        if (shape2 != null) shape2.makeInvisible();
    }

}