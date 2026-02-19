import java.util.Stack;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

public class Tower 
{   
    /** Estructura (optamos por hacerlo en Stacks)*/
    private Stack<Cup> cups;
    private Stack<Lid> lids;
    /** Muestra si la torres es o no visible*/
    private boolean isVisible;
    
    /** Indica si la uĺtima acción tuvo exito*/
    private boolean isOK;

    /** Coordenadas en las que quisimos que se dibuje la torre*/
    private int baseX = 130;
    private int baseY = 200;
    
    /** Limite de altura de la torre*/
    private int maxHeight;
    
    
    /**
     * Constructor de la torre
     * @param maxHeight Altura máxima permitida 
     */
    public Tower(int maxHeight)
    {
        cups = new Stack<Cup>();
        lids = new Stack<Lid>();

        this.maxHeight = maxHeight;

        isVisible = false;
        isOK = true;
    }
    
    /**
     * Agrega un copa en la torre
     * Realiza validaciones para que no se repitan ni colores ni tamaños
     */
    public void pushCup(int number, String color)
    {
        Cup nueva = new Cup(number, color);
        
        // Revisa que no existan tamaños repetidos
        if (repSize(number)) {
            isOK = false;
            JOptionPane.showMessageDialog(null, "Error, ya existe una copa con este tamaño. Ingrese otro valor");
            return;
        }
        
        //  Revisa que no existan colores repetidos 
        if (repColor(color)) {
            isOK = false;
            JOptionPane.showMessageDialog(null, "Error, ya existe una copa con este color. Ingrese otro color");
            return;
        }
        
        // Revisa que no se pase el limite de altura de la torre
        if (getHeight() + nueva.getHeight() <= maxHeight) {

            cups.push(nueva);

            redraw();
            isOK = true;
        }
        else {
            isOK = false;
            JOptionPane.showMessageDialog(null, "Esta copa sobrepasa el límite máximo de la torre");
        }
    }

    
    /**
     * Elimina la última copa que se colocó
     */
    public Cup popCup()
    {
        if (!cups.isEmpty()) {

            Cup removida = cups.pop();
            removida.makeInvisible(); 

            if (!lids.isEmpty()) {
                Lid tapa = lids.pop();
                tapa.makeInvisible(); 
            }

            redraw();
            isOK = true;
            return removida;
        }

        isOK = false;
        return null;
    }

    /**
     * Agrega tapas a la torre sobre una copa
     */
    public void pushLid(String color)
    {
        if (!cups.isEmpty()) {

            Cup top = cups.peek();

            Lid nueva = new Lid(top.getNumber(), color);

            lids.clear();
            lids.push(nueva);

            redraw();
            isOK = true;
        }
        else {
            isOK = false;
        }
    }

    /**
     * Elimina la última tapa que se colocó
     */        
    public Lid popLid()
    {
        if (!lids.isEmpty()) {

            Lid l = lids.pop();
            l.makeInvisible();

            redraw();
            isOK = true;
            return l;
        }

        isOK = false;
        return null;
    }
    
    /**
     * Calcula el top (o el más alto) en la torre
     */
    private int getTop(Cup c) {
        return c.getYPosition() - c.getHeight() * 5;
    }
    
    /**
     * Calcula entre dos copas quien es la más alta
     */
    private Cup getHighestCup(Cup actualMasAlta, Cup candidata) {

        if (actualMasAlta == null) {
            return candidata;
        }
    
        if (getTop(candidata) < getTop(actualMasAlta)) {
            return candidata;
        }
    
        return actualMasAlta;
    }
    
    /**
     * Verifica si ya existe una copa con el tamaño ingresado
     */
    private boolean repSize(int number) {

        for (Cup c : cups) {
            if (c.getNumber() == number) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica si ya existe una copa con el color ingresado
     */
    private boolean repColor(String color) {

        for (Cup c : cups) {
            if (c.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Realiza una verificacion de donde van las copas, si van por dentro o por fuera según sus tamaños, también las "coloca" en la torre
     */
    private void redraw(){

        int yActual = baseY;
        Cup anterior = null;
        Cup ultimaExterna = null;
        for (Cup c : cups) {
            c.makeInvisible();
            if (anterior == null) {
                c.setPosition(baseX, yActual);
                c.setInside(false);
                ultimaExterna = c;
            }
            else {
                if (c.getNumber() > ultimaExterna.getNumber()) {
                    Cup baseApilamiento = null;
                    for (Cup cupExistente : cups) {
                
                        if (cupExistente == c) {
                            break; 
                        }
                        baseApilamiento = getHighestCup(baseApilamiento, cupExistente);
                    }
                    yActual = getTop(baseApilamiento);
                    c.setInside(false);
                    ultimaExterna = c;
                }
                else {
                    if (anterior.isInside()) {
        
                        int topAnterior = anterior.getYPosition() - anterior.getHeight() * 5;
                        yActual = topAnterior;
                    }
                    else {
                        yActual = ultimaExterna.getYPosition() - 7;
                    }
                    c.setInside(true);
                }
                c.setPosition(baseX, yActual);
            }
            if (isVisible) {
                c.makeVisible();
            }
            anterior = c;
        }
    
        // PARTE DE LA TAPA
        if (!lids.isEmpty() && !cups.isEmpty()) {
    
            Lid topLid = lids.peek();
            topLid.makeInvisible();
            Cup topCup = cups.peek();
            int yTapa = topCup.getYPosition() - topCup.getHeight();
            topLid.setPosition(baseX, yTapa);
            if (isVisible) {
                topLid.makeVisible();
            }
        }
    }

    /**
     * Dibuja la regla guía de la tore
     */
    public void drawRule()
    {
        int escala = 5;

        for (int i = 0; i <= maxHeight; i++) {
            Rectangle r = new Rectangle();
            if (i % 5 == 0) {
                r.changeSize(2, 20);
            } else {
                r.changeSize(2, 10);
            }
            r.moveHorizontal(baseX - 200);
            r.moveVertical(baseY - (i * escala));
            r.changeColor("black");
            r.makeVisible();
        }
    }
    
    /**
     * Se remuveve la copa ingresada según su número, en caso de que no sea posible, se manda una notificación de error
     */
    public void removeCup(int number) {

        if (cups.isEmpty()) {
            isOK = false;
            JOptionPane.showMessageDialog(null, "La torre está vacía");
            return;
        }
    
        for (int i = 0; i < cups.size(); i++) {
            if (cups.get(i).getNumber() == number) {
                cups.get(i).makeInvisible();
                cups.remove(i);
                redraw();
                isOK = true;
                return;
            }
        }
        isOK = false;
        JOptionPane.showMessageDialog(null, "No hay una cup con este tamaño en esta torre :(");
    }
    
    
    /**
     * Se remuveve la tapa ingresada según su número, en caso de que no sea posible, se manda una notificación de error
     */
    public void removeLid(String color) {

        if (lids.isEmpty()) {
            isOK = false;
            JOptionPane.showMessageDialog(null,"No hay tapas en la torre");
            return;
        }
    
        for (int i = 0; i < lids.size(); i++) {
    
            if (lids.get(i).getColor().equals(color)) {
                lids.get(i).makeInvisible();
                lids.remove(i);
                redraw();
                isOK = true;
                return;
            }
        }
        isOK = false;
        JOptionPane.showMessageDialog(null,"No existe una tapa con ese color");
    }
    
    /**
     * Obtiene la altura de la torre
     */
    public int getHeight(){
        int total = 0;
        for (Cup c : cups){
            if (c.isInside()) {
            }
            else {
            total += c.getHeight();
            }
        }
        return total;
    }
    
    /**
     * Oderna la torre de menor a mayor (de arriba a abajo en este caso)
     */
    public void orderTower() {
        ArrayList<Cup> lista = new ArrayList<Cup>();
        for (Cup c : cups) {
            lista.add(c);
            c.makeInvisible();
        }
        Collections.sort(lista, Collections.reverseOrder(
            (c1, c2) -> c1.getNumber() - c2.getNumber()));
        cups.clear();
        int altura = 0;
        for (Cup c : lista) {
            if (altura + c.getHeight() <= maxHeight) {
                cups.push(c);
                altura += c.getHeight();
            }
        }
        redraw();
    }
    
    /**
     * Coloca la torre al revés de como se encuentra
     */
    public void reverseTower() {
        ArrayList<Cup> lista = new ArrayList<Cup>();
        for (Cup c : cups) {
            lista.add(c);
            c.makeInvisible();
        }
        Collections.reverse(lista);
        cups.clear();
        int altura = 0;
        for (Cup c : lista) {
    
            if (altura + c.getHeight() <= maxHeight) {
                cups.push(c);
                altura += c.getHeight();
            }
        }
        redraw();
    }
    
    /**
     * Hace visible la torre
     */
    public void makeVisible()
    {
        isVisible = true;
        redraw();
        drawRule();
    }
    
    /**
     * Hace invisible la torre
     */
    public void makeInvisible()
    {
        for (Cup c : cups) {
            c.makeInvisible();
        }

        for (Lid l : lids) {
            l.makeInvisible();
        }

        isVisible = false;
    }

    /**
     * Se comprueba si la última acción fue correcta
     */
    public boolean isOk()
    {
        return isOK;
    }

    /**
     * Se hace un "reset" de la torre
     */
    public void exit()
    {
        makeInvisible();
        cups.clear();
        lids.clear();
        isOK = true;
    }
}