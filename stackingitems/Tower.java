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

    /* =================================================
       AGREGAR TAZA
       ================================================= */

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

    /* =================================================
       REMOVER TAZA SUPERIOR
       ================================================= */

    public Cup popCup()
    {
        if (!cups.isEmpty()) {

            Cup removida = cups.pop();
            removida.makeInvisible(); // ← BORRA VISUALMENTE

            // Si tenía tapa arriba, quitarla también
            if (!lids.isEmpty()) {
                Lid tapa = lids.pop();
                tapa.makeInvisible(); // ← BORRA VISUALMENTE
            }

            redraw();
            isOK = true;
            return removida;
        }

        isOK = false;
        return null;
    }

    /* =================================================
       AGREGAR TAPA ARRIBA
       ================================================= */

    public void pushLid(String color)
    {
        if (!cups.isEmpty()) {

            Cup top = cups.peek();

            Lid nueva = new Lid(top.getNumber(), color);

            lids.clear(); // Solo permitimos 1 tapa arriba
            lids.push(nueva);

            redraw();
            isOK = true;
        }
        else {
            isOK = false;
        }
    }

    /* =================================================
       REMOVER TAPA
       ================================================= */

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

    /* =================================================
       REDIBUJAR TORRE COMPLETA
       ================================================= */

    private void redraw()
    {
        int alturaAcumulada = 0;

        // Dibujar cups
        for (Cup c : cups) {

            c.makeInvisible(); // limpia antes de mover

            int y = baseY - alturaAcumulada;

            c.setPosition(baseX, y);

            if (isVisible) {
                c.makeVisible();
            }

            alturaAcumulada += c.getHeight();
        }

        // Dibujar tapa solo arriba
        if (!lids.isEmpty()) {

            Lid topLid = lids.peek();

            topLid.makeInvisible(); // limpia antes de mover

            int yTapa = baseY - alturaAcumulada;

            // IMPORTANTE:
            // NO restamos nada extra
            // Lid.draw() ya resta su altura internamente
            topLid.setPosition(baseX, yTapa);

            if (isVisible) {
                topLid.makeVisible();
            }
        }
    }

    /* =================================================
       REGLA VISUAL CORRECTA
       ================================================= */

    public void drawRule()
    {
        int escala = 5;

        for (int i = 0; i <= maxHeight; i++) {

            Rectangle r = new Rectangle();

            if (i % 5 == 0) {
                r.changeSize(2, 20); // línea grande
            } else {
                r.changeSize(2, 10); // línea pequeña
            }

            r.moveHorizontal(baseX - 200);
            r.moveVertical(baseY - (i * escala));

            r.changeColor("black");
            r.makeVisible();
        }
    }

    /* =================================================
       ALTURA TOTAL
       ================================================= */

    public int getHeight()
    {
        int total = 0;

        for (Cup c : cups) {
            total += c.getHeight();
        }

        return total;
    }

    /* =================================================
       VISIBILIDAD
       ================================================= */

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

    /* =================================================
       ESTADO
       ================================================= */

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