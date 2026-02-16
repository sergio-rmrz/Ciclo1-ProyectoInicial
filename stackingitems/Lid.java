import java.awt.*;
/**
 * Write a description of class Lid here.
 * 
 * @author Yazid Sánchez - Sergio Ramírez
 * @version 1.0
 */
public class Lid
{
    // instance variables
    private int number;
    private int height; // Grosor
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    
    private static final int PIXEL_POR_CM = 5;
    
    private Rectangle shape;
    /**
     * Constructor for objects of class Lid
     */
    public Lid(int number, String color) {
        this.number = number;
        this.color = color;
        // Ancho igual al de la taza correspondiente
        this.width = ((2 * number) - 1) * PIXEL_POR_CM;
        
        // Grosor fijo de 1 cm (5 px)
        this.height = PIXEL_POR_CM; 
        
        this.xPosition = 130; 
        this.yPosition = 200;
        this.isVisible = false;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    public void makeInvisible() {
        erase();
        isVisible = false;
    }
    
    public void moveVertical(int distancia) {
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
    // Método añadido para compatibilidad con Tower
    public int getSize() {
        return number;
    }
    
    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }
    
    public void draw() {
        if (isVisible) {
            int esquinaX = xPosition - width / 2;
            // Dibuja la tapa en su posición (yPosition actúa como base)
            int esquinaY = yPosition - height * 2 * (number); 
            
            shape = new Rectangle();
            shape.changeSize(height, width);
            shape.changeColor(color);
            shape.moveHorizontal(esquinaX - 70);
            shape.moveVertical(esquinaY - 15);
            shape.makeVisible();
        }
    }
    
    public void erase() {
        if (shape != null) shape.makeInvisible();
    }
}