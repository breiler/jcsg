package eu.mihosoft.vrl.v3d;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.IntStream;


// TODO: Auto-generated Javadoc

/**
 * The Class Text.
 */

@SuppressWarnings("restriction")
public class TextExtrude {
    private static final String default_font = "FreeSerif";
    private final static int POINTS_CURVE = 10;

    private final String text;
    private List<Vector3d> points;
    private Vector3d p0;
    ArrayList<CSG> sections = new ArrayList<CSG>();
    ArrayList<CSG> holes = new ArrayList<CSG>();
    private double dir;


    private TextExtrude(String text, Font font, double dir) {
        if (dir <= 0)
            throw new NumberFormatException("length can not be negative");
        this.dir = dir;
        points = new ArrayList<>();
        this.text = text;

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        GlyphVector glyphVector = font.createGlyphVector(g2.getFontRenderContext(), text);
        Shape shape = glyphVector.getOutline(0, 0);


        double[] coords = new double[8];

        PathIterator pathIterator = shape.getPathIterator(null);
        while (!pathIterator.isDone()) {

            int i = pathIterator.currentSegment(coords);
            switch (i) {
                case PathIterator.SEG_MOVETO:
                    p0 = new Vector3d(coords[0], coords[1], 0);
                    points.add(p0);
                    break;

                case PathIterator.SEG_LINETO:
                    points.add(new Vector3d(coords[0], coords[1], 0));
                    break;

                case PathIterator.SEG_QUADTO:
                    expandQuadBezier(coords[0], coords[1], coords[2], coords[3]);
                    break;

                case PathIterator.SEG_CUBICTO:
                    expandCubicBezier(coords);
                    break;

                case PathIterator.SEG_CLOSE:
                    loadPoints();
                    break;

            }
            pathIterator.next();
        }


        for (int i = 0; i < sections.size(); i++) {
            for (CSG h : holes) {
                try {
                    if (sections.get(i).touching(h)) {
                        // println "Hole found "
                        CSG nl = sections.get(i).difference(h);

                        sections.set(i, nl);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private void expandCubicBezier(double[] coords) {
        Vector3d ini1 = (points.size() > 0 ? points.get(points.size() - 1) : p0);
        IntStream.rangeClosed(1, POINTS_CURVE).forEach(index -> points.add(evalCubicBezier(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], ini1, ((double) index) / POINTS_CURVE)));
    }

    private void expandQuadBezier(double x1, double y1, double x2, double y2) {
        final Vector3d start = (points.size() > 0 ? points.get(points.size() - 1) : p0);
        IntStream.rangeClosed(1, POINTS_CURVE).forEach(index -> points.add(evalQuadBezier(start, x1, y1, x2, y2, ((double) index) / POINTS_CURVE)));
    }

    /**
     * Extrudes the specified path (convex or concave polygon without holes or
     * intersections, specified in CCW) into the specified direction.
     *
     * @param dir  direction of extrusion
     * @param text text
     * @param font font configuration of the text
     * @return a CSG object that consists of the extruded polygon
     */
    @SuppressWarnings("restriction")
    public static List<CSG> text(double dir, String text, Font font) {

        TextExtrude te = new TextExtrude(text, font, dir);

        return te.sections;
    }

    private void loadPoints() {
        if (points.size() > 4) {
            points.remove(points.size() - 1);
            //points.remove(points.size() - 1);
            boolean hole = !Extrude.isCCW(Polygon.fromPoints(points));
            CSG newLetter = Extrude.points(new Vector3d(0, 0, dir), points);

            if (!hole)
                sections.add(newLetter);
            else
                holes.add(newLetter);
        }
        points = new ArrayList<>();
    }

    private Vector3d evalCubicBezier(double x1, double x2, double x3, double y1, double y2, double y3, Vector3d ini, double t) {
        Vector3d p = new Vector3d((float) (Math.pow(1 - t, 3) * ini.x +
                3 * t * Math.pow(1 - t, 2) * x2 +
                3 * (1 - t) * t * t * x3 +
                Math.pow(t, 3) * x1),
                (float) (Math.pow(1 - t, 3) * ini.y +
                        3 * t * Math.pow(1 - t, 2) * y2 +
                        3 * (1 - t) * t * t * y3 +
                        Math.pow(t, 3) * y1),
                0f);
        return p;
    }

    private Vector3d evalQuadBezier(Vector3d startPoint, double x1, double y1, double x2, double y2, double t) {
        Vector3d p = new Vector3d((float) (Math.pow(1 - t, 2) * startPoint.x +
                2 * (1 - t) * t * x1 +
                Math.pow(t, 2) * x2),
                (float) (Math.pow(1 - t, 2) * startPoint.y +
                        2 * (1 - t) * t * y1 +
                        Math.pow(t, 2) * y2),
                0f);
        return p;
    }
}
