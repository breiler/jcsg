package eu.mihosoft.vrl.v3d.javafx;

import eu.mihosoft.vrl.v3d.CSG;

import eu.mihosoft.vrl.v3d.Color;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.PropertyStorage;
import eu.mihosoft.vrl.v3d.Sphere;
import eu.mihosoft.vrl.v3d.Transform;
import eu.mihosoft.vrl.v3d.Vector3d;
import eu.mihosoft.vrl.v3d.parametrics.IParametric;
import com.neuronrobotics.interaction.CadInteractionEvent;
import eu.mihosoft.vrl.v3d.parametrics.IRegenerate;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Mesh extends CSG {
    /**
     * The current.
     */
    private MeshView current;

    private final CSG csg;

    private Affine manipulator;

    private IRegenerate regenerate = null;
    private boolean markForRegeneration = false;

    public Mesh(CSG csg) {
        this.csg = csg;
    }

    public void setMeshColor(Color color) {
        if (current != null) {
            PhongMaterial m = new PhongMaterial(convertColor(color));
            current.setMaterial(m);
        }
    }

    /**
     * Sets the Temporary color.
     *
     * @param color the new Temporary color
     */
    public Mesh setTemporaryColor(Color color) {
        if (current != null) {
            PhongMaterial m = new PhongMaterial(convertColor(color));
            current.setMaterial(m);
        }
        return this;
    }

    /**
     * Sets the manipulator.
     *
     * @param manipulator the manipulator
     * @return the affine
     */
    public Mesh setManipulator(Affine manipulator) {
        if (manipulator == null)
            return this;
        Affine old = manipulator;
        this.manipulator = manipulator;
        if (current != null) {
            current.getTransforms().clear();
            current.getTransforms().add(manipulator);
        }
        return this;
    }

    /**
     * Gets the mesh.
     *
     * @return the mesh
     */
    public MeshView getMesh() {
        if (current != null)
            return current;
        current = newMesh();
        return current;
    }

    /**
     * Gets the mesh.
     *
     * @return the mesh
     */
    public MeshView newMesh() {

        MeshContainer meshContainer = toJavaFXMesh(null);

        MeshView current = meshContainer.getAsMeshViews().get(0);

        PhongMaterial m = new PhongMaterial(convertColor(csg.getColor()));
        current.setMaterial(m);

        boolean hasManipulator = getManipulator() != null;
        boolean hasAssembly = csg.getAssemblyStorage().getValue("AssembleAffine") != Optional.empty();

        if (hasManipulator || hasAssembly)
            current.getTransforms().clear();

        if (hasManipulator)
            current.getTransforms().add(getManipulator());
        if (hasAssembly)
            current.getTransforms().add((Affine) csg.getAssemblyStorage().getValue("AssembleAffine").get());

        current.setCullFace(CullFace.NONE);
        if (csg.isWireFrame())
            current.setDrawMode(DrawMode.LINE);
        else
            current.setDrawMode(DrawMode.FILL);
        return current;
    }

    private javafx.scene.paint.Color convertColor(Color color) {
        return new javafx.scene.paint.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public Affine getManipulator() {
        if (manipulator == null)
            manipulator = new Affine();
        return manipulator;
    }

    /**
     * To java fx mesh.
     *
     * @param interact the interact
     * @return the mesh container
     */
    // TODO finish experiment (20.7.2014)
    public MeshContainer toJavaFXMesh(CadInteractionEvent interact) {

        return toJavaFXMeshSimple(interact);

        // TODO test obj approach with multiple materials
        // try {
        // ObjImporter importer = new ObjImporter(toObj());
        //
        // List<Mesh> meshes = new ArrayList<>(importer.getMeshCollection());
        // return new MeshContainer(getBounds().getMin(), getBounds().getMax(),
        // meshes, new ArrayList<>(importer.getMaterialCollection()));
        // } catch (IOException ex) {
        // Logger.getLogger(CSG.class.getName()).log(Level.SEVERE, null, ex);
        // }
        // // we have no backup strategy for broken streams :(
        // return null;
    }

    /**
     * Returns the CSG as JavaFX triangle mesh.
     *
     * @param interact the interact
     * @return the CSG as JavaFX triangle mesh
     */
    public MeshContainer toJavaFXMeshSimple(CadInteractionEvent interact) {

        return CSGtoJavafx.meshFromPolygon(csg.getPolygons());
    }

    public CSG setParameterNewValue(String key, double newValue) {
        IParametric function = csg.getMapOfparametrics().get(key);
        if (function != null) {
            Mesh setManipulator = new Mesh(function.change(csg, key, new Long((long) (newValue * 1000)))).setManipulator(this.getManipulator());
            setManipulator.setColor(csg.getColor());
            return setManipulator;
        }
        return this;
    }


    public CSG regenerate() {
        this.markForRegeneration = false;
        if (regenerate == null)
            return this;
        CSG regenerate2 = regenerate.regenerate(this);
        if (regenerate2 != null)
            return new Mesh(regenerate2).setManipulator(this.getManipulator()).historySync(this);
        ;
        return this;
    }

    public boolean isMarkedForRegeneration() {
        return markForRegeneration;
    }

    public CSG markForRegeneration() {
        this.markForRegeneration = true;
        return this;
    }


    public CSG addAssemblyStep(int stepNumber, Transform explodedPose) {
        String key = "AssemblySteps";
        PropertyStorage incomingGetStorage = getAssemblyStorage();
        if (incomingGetStorage.getValue(key) == Optional.empty()) {
            HashMap<Integer, Transform> map = new HashMap<>();
            incomingGetStorage.set(key, map);
        }
        if (incomingGetStorage.getValue("MaxAssemblyStep") == Optional.empty()) {
            incomingGetStorage.set("MaxAssemblyStep", Integer.valueOf(stepNumber));
        }
        Integer max = (Integer) incomingGetStorage.getValue("MaxAssemblyStep").get();
        if (stepNumber > max.intValue()) {
            incomingGetStorage.set("MaxAssemblyStep", Integer.valueOf(stepNumber));
        }
        HashMap<Integer, Transform> map = (HashMap<Integer, Transform>) incomingGetStorage.getValue(key).get();
        map.put(stepNumber, explodedPose);
        if (incomingGetStorage.getValue("AssembleAffine") == Optional.empty())
            incomingGetStorage.set("AssembleAffine", new Affine());
        return this;
    }

    public static CSG outerFillet(CSG base, double rad) {
        List<Polygon> polys = Slice.slice(base);
        return base.union(outerFillet(polys, rad));
    }

    public static CSG outerFillet(List<Polygon> polys, double rad) {

        ArrayList<CSG> parts = new ArrayList<>();
        for (Polygon p : polys) {
            int size = p.vertices.size();
            for (int i = 0; i < size; i++) {
                // if(i>1)
                // continue;
                int next = i + 1;
                if (next == size)
                    next = 0;
                int nextNext = next + 1;
                if (nextNext == size)
                    nextNext = 0;
                Vector3d position0 = p.vertices.get(i).pos;
                Vector3d position1 = p.vertices.get(next).pos;
                Vector3d position2 = p.vertices.get(nextNext).pos;
                Vector3d seg1 = position0.minus(position1);
                Vector3d seg2 = position2.minus(position1);
                double len = seg1.magnitude();
                double angle = Math.toDegrees(seg1.angle(seg2));
                double angleAbs = Math.toDegrees(seg1.angle(Vector3d.Y_ONE));
                CSG fillet = new Fillet(rad, len).toCSG().toYMax();
                // .roty(90)
                if (seg1.x < 0) {
                    angleAbs = 360 - angleAbs;
                    // fillet=fillet.toYMax()
                }
                if (Math.abs(angle) > 0.01 && Math.abs(angle) < 180) {
                    parts.add(corner(rad, angle).rotz(angleAbs).move(position0));
                }
                // println "Fillet corner Angle = "+angle
                parts.add(fillet.rotz(angleAbs).move(position0));
            }
        }
        return CSG.unionAll(parts);
    }

    public static CSG corner(double rad, double angle) {
        return CSG.unionAll(Extrude.revolve(new Fillet(rad, 0.01).toCSG().rotz(-90), 0, angle, 4))
                .difference(Extrude.revolve(new Sphere(rad).toCSG().toYMin().toZMin(), 0, angle, 4));
        // .rotz(180)
    }

    public CSG setRegenerate(IRegenerate function) {
        regenerate = function;
        return this;
    }
}
