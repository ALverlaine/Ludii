package view.component.custom;

import bridge.Bridge;
import game.equipment.component.Component;
import game.util.moves.Piece;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import other.context.Context;

import java.awt.*;

public class CardStyle extends PieceStyle {
    public CardStyle(Bridge bridge, Component component) {
        super(bridge, component);
    }

    @Override
    protected SVGGraphics2D getSVGImageFromFilePath(SVGGraphics2D g2dOriginal, Context context, int imageSize, String filePath, int containerIndex, int localState, int value, int hiddenValue, int rotation, boolean secondary) {
        final SVGGraphics2D g2d = new SVGGraphics2D(imageSize, imageSize);

        // Set rendering quality hints
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Fill the background of the card as a rectangle
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageSize, imageSize);

        // Draw the border of the card
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, imageSize - 1, imageSize - 1);

        // Get the name of the component (card name) to display inside the rectangle
        String cardName = component.name();

        // Set font and calculate position to center the text
        g2d.setFont(new Font("Arial", Font.BOLD, imageSize / 5));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(cardName);
        int textHeight = fontMetrics.getAscent();

        // Set color for text and draw the name of the card in the center
        g2d.setColor(Color.BLACK);
        g2d.drawString(cardName, (imageSize - textWidth) / 2, (imageSize + textHeight) / 2 - 10);

        return g2d;
    }
}
