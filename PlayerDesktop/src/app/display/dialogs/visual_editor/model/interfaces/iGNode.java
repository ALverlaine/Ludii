package app.display.dialogs.visual_editor.model.interfaces;

import app.display.dialogs.visual_editor.LayoutManagement.Math.Vector2D;

import java.util.List;

/**
 * An interface of a node used for layout graph
 * @author nic0gin
 */

public interface iGNode {

    int id();

    int parent();

    List<Integer> children();

    List<Integer> siblings();

    Vector2D pos();

    void setPos(Vector2D pos);

    int width();

    int height();

    void setDepth(int depth);

    int depth();

}
