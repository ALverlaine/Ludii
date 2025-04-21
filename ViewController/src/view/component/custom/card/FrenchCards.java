package view.component.custom.card;

import bridge.Bridge;
import game.equipment.component.Component;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import other.context.Context;
import view.component.custom.PieceStyle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FrenchCards extends PieceStyle {
    private static final Map<String, String> CARD_SVG_MAP = new HashMap<>();
    private static BridgeContext bridgeContext;
    private static UserAgent userAgent;

    static {
        initializeCardMap();
        initializeBatik();
    }

    private static void initializeCardMap() {
        // Standard 52 cards + 2 jokers
        String[] suits = {"H", "D", "C", "S"}; // Hearts, Diamonds, Clubs, Spades
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};

        for (String suit : suits) {
            for (String rank : ranks) {
                CARD_SVG_MAP.put(rank + suit, "FrenchCards/" + rank + suit + ".svg");
            }
        }

        // Jokers
        CARD_SVG_MAP.put("1J", "FrenchCards/1J.svg");
        CARD_SVG_MAP.put("2J", "FrenchCards/2J.svg");
    }

    private static void initializeBatik() {
        // Setup Batik SVG rendering environment
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        bridgeContext = new BridgeContext(userAgent, loader);
        bridgeContext.setDynamicState(BridgeContext.DYNAMIC);
    }

    public FrenchCards(Bridge bridge, Component component) {
        super(bridge, component);
    }

    @Override
    protected SVGGraphics2D getSVGImageFromFilePath(SVGGraphics2D g2dOriginal, Context context,
                                                    int imageSize, String filePath, int containerIndex, int localState,
                                                    int value, int hiddenValue, int rotation, boolean secondary) {

        String cardName = component.name();
        String svgPath = CARD_SVG_MAP.get(cardName);

        if (svgPath != null) {
            try (InputStream is = getClass().getResourceAsStream(svgPath)) {
                if (is != null) {
                    // Parse SVG document
                    String parser = XMLResourceDescriptor.getXMLParserClassName();
                    SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
                    org.w3c.dom.Document svgDocument = factory.createDocument(
                            getClass().getResource(svgPath).toString(),
                            is
                    );

                    // Create a GVT (Batik's graphics representation)
                    GVTBuilder builder = new GVTBuilder();
                    GraphicsNode graphicsNode = builder.build(bridgeContext, svgDocument);

                    // Create output image
                    SVGGraphics2D g2d = new SVGGraphics2D(imageSize, imageSize);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    // Calculate scaling to fit the card in the desired size
                    Rectangle2D bounds = graphicsNode.getPrimitiveBounds();
                    double scale = Math.min(
                            imageSize / bounds.getWidth(),
                            imageSize / bounds.getHeight()
                    );

                    // Apply scaling and centering
                    g2d.scale(scale, scale);
                    g2d.translate(
                            (imageSize/scale - bounds.getWidth())/2 - bounds.getX(),
                            (imageSize/scale - bounds.getHeight())/2 - bounds.getY()
                    );

                    // Render the SVG
                    graphicsNode.paint(g2d);

                    return g2d;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Fall through to default card
            }
        }

        // Fallback to default card design
        return createDefaultCard(imageSize, cardName);
    }

    private SVGGraphics2D createDefaultCard(int imageSize, String cardName) {
        SVGGraphics2D g2d = new SVGGraphics2D(imageSize, imageSize);

        // Set rendering quality hints
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Fill background (card face)
        g2d.setColor(new Color(255, 255, 240)); // Off-white
        g2d.fillRoundRect(0, 0, imageSize, imageSize, 20, 20);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, imageSize-3, imageSize-3, 20, 20);

        // Draw card name
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, imageSize / 6));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(cardName);
        int textHeight = fm.getAscent();

        // Center the text
        g2d.drawString(cardName,
                (imageSize - textWidth) / 2,
                (imageSize + textHeight) / 2);

        return g2d;
    }
}