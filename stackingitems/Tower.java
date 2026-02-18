import java.util.Stack;

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

        if (getHeight() + nueva.getHeight() <= maxHeight) {

            cups.push(nueva);

            redraw();
            isOK = true;
        }
        else {
            isOK = false;
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

    private void redraw(){

        int yActual = baseY;
        Cup anterior = null;
    
        for (Cup c : cups) {
    
            c.makeInvisible();
    
            if (anterior == null) {
                c.setPosition(baseX, yActual);
            }
            else {
    
                if (c.getNumber() > anterior.getNumber()) {
                    // Más grande → se apila completo
                    yActual -= anterior.getHeight()*5;
                }
                else {
                    // Más pequeña → se mete pero baja el grosor
                    yActual -= 7;
                }
    
                c.setPosition(baseX, yActual);
            }
    
            if (isVisible) {
                c.makeVisible();
            }
    
            anterior = c;
        }
    
        // ---- TAPA ----
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

    public int getHeight()
    {
        int total = 0;

        for (Cup c : cups) {
            total += c.getHeight();
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