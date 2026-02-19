import java.util.Stack;
import javax.swing.JOptionPane;

public class Tower 
{
    private Stack<Cup> cups;
    private Stack<Lid> lids;

    private boolean isVisible;
    private boolean isOK;

    private int baseX = 130;
    private int baseY = 200;

    private int maxHeight;

    public Tower(int maxHeight)
    {
        cups = new Stack<Cup>();
        lids = new Stack<Lid>();

        this.maxHeight = maxHeight;

        isVisible = false;
        isOK = true;
    }

    public void pushCup(int number, String color)
    {
        Cup nueva = new Cup(number, color);
        
        if (repSize(number)) {
            isOK = false;
            JOptionPane.showMessageDialog(null, "Error, ya existe una copa con este tamaño. Ingrese otro valor");
            return;
        }
        
        if (repColor(color)) {
            isOK = false;
            JOptionPane.showMessageDialog(null, "Error, ya existe una copa con este color. Ingrese otro color");
            return;
        }

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
    
    private int getTop(Cup c) {
        return c.getYPosition() - c.getHeight() * 5;
    }
    
    private Cup getHighestCup(Cup actualMasAlta, Cup candidata) {

        if (actualMasAlta == null) {
            return candidata;
        }
    
        if (getTop(candidata) < getTop(actualMasAlta)) {
            return candidata;
        }
    
        return actualMasAlta;
    }
    
    private boolean repSize(int number) {

        for (Cup c : cups) {
            if (c.getNumber() == number) {
                return true;
            }
        }
        return false;
    }
    
    private boolean repColor(String color) {

        for (Cup c : cups) {
            if (c.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }
    
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

    public void makeVisible()
    {
        isVisible = true;
        redraw();
        drawRule();
    }

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

    public boolean isOk()
    {
        return isOK;
    }

    public void exit()
    {
        makeInvisible();
        cups.clear();
        lids.clear();
        isOK = true;
    }
}