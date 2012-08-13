package uk.ac.standrews.grasp.ide.figures;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Base class for first-class figures
 * @author Dilyan Rusev
 *
 */
abstract class AbstractFirstClassFigure implements IFirstClassFigure {
	private BodyFigure body;
	private Label header;
	private Figure impl;
	
	/**
	 * Create a new figure. Override {@link #createBorder()} and {@link #createHeadLabel()} 
	 * to customise behaviour. Add child figures via {@link #getBody()}
	 */
	public AbstractFirstClassFigure() {
		impl = new Figure();
		body = new BodyFigure();		
		ToolbarLayout layout = new ToolbarLayout();				
		setLayoutManager(layout);		
		setBorder(createBorder());
		setBackgroundColor(createBackgroundColour());
		setOpaque(true);
		
		header = createHeadLabel();
		impl.add(header);
		impl.add(body);
	}
	
	/**
	 * Create the label to be displayed at the top of the figure
	 * @return Text and Icon for the header of the figure
	 */
	protected abstract Label createHeadLabel();
	
	/**
	 * Create the backbround colour
	 * @return
	 */
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(255, 255, 206);
	}
	
	/**
	 * Create a custom border to encompass the figure. By default, creates
	 * a 1-pixel black line border
	 * @return Border for the outline of the figure
	 */
	protected Border createBorder() {
		return new LineBorder(ColorConstants.black, 1);
	}	
	
	@Override
	public BodyFigure getBody() {
		return body;
	}	
	
	@Override
	public void setHeaderText(String text) {
		header.setText(text);
	}

	@Override
	public void add(IFigure child) {
		body.add(child);		
	}

	@Override
	public void add(IFigure child, int i) {		
		body.add(child, i);
	}

	@Override
	public void add(IFigure child, Object obj) {
		body.add(child, obj);		
	}

	@Override
	public void add(IFigure child, Object obj, int i) {
		body.add(child, obj, i);
	}

	@Override
	public void addAncestorListener(AncestorListener listener) {
		impl.addAncestorListener(listener);		
	}

	@Override
	public void addCoordinateListener(CoordinateListener listener) {
		impl.addCoordinateListener(listener);		
	}

	@Override
	public void addFigureListener(FigureListener listener) {
		impl.addFigureListener(listener);
	}

	@Override
	public void addFocusListener(FocusListener listener) {
		impl.addFocusListener(listener);
	}

	@Override
	public void addKeyListener(KeyListener listener) {
		impl.addKeyListener(listener);
	}

	@Override
	public void addLayoutListener(LayoutListener listener) {
		impl.addLayoutListener(listener);
	}

	@Override
	public void addMouseListener(MouseListener listener) {
		impl.addMouseListener(listener);
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener listener) {
		impl.addMouseMotionListener(listener);
	}

	@Override
	public void addNotify() {
		impl.addNotify();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		impl.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		impl.addPropertyChangeListener(property, listener);
	}

	@Override
	public boolean containsPoint(Point point) {
		return impl.containsPoint(point);
	}

	@Override
	public boolean containsPoint(int x, int y) {
		return impl.containsPoint(x, y);
	}

	@Override
	public void erase() {
		impl.erase();		
	}

	@Override
	public IFigure findFigureAt(Point point) {
		return impl.findFigureAt(point);
	}

	@Override
	public IFigure findFigureAt(int x, int y) {
		return impl.findFigureAt(x, y);
	}

	@Override
	public IFigure findFigureAt(int x, int y, TreeSearch algorithm) {
		return impl.findFigureAt(x, y, algorithm);
	}

	@Override
	public IFigure findFigureAtExcluding(int x, int y, 
			@SuppressWarnings("rawtypes") Collection collection) {
		return impl.findFigureAtExcluding(x, y, collection);
	}

	@Override
	public IFigure findMouseEventTargetAt(int x, int y) {
		return impl.findMouseEventTargetAt(x, y);
	}

	@Override
	public Color getBackgroundColor() {
		return impl.getBackgroundColor();
	}

	@Override
	public Border getBorder() {
		return impl.getBorder();
	}

	@Override
	public Rectangle getBounds() {
		return impl.getBounds();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IFigure> getChildren() {
		return impl.getChildren();
	}

	@Override
	public Rectangle getClientArea() {
		return impl.getClientArea();
	}

	@Override
	public Rectangle getClientArea(Rectangle rect) {
		return impl.getClientArea(rect);
	}

	@Override
	public IClippingStrategy getClippingStrategy() {
		return impl.getClippingStrategy();
	}

	@Override
	public Cursor getCursor() {
		return impl.getCursor();
	}

	@Override
	public Font getFont() {
		return impl.getFont();
	}

	@Override
	public Color getForegroundColor() {
		return impl.getForegroundColor();
	}

	@Override
	public Insets getInsets() {
		return impl.getInsets();
	}

	@Override
	public LayoutManager getLayoutManager() {
		return impl.getLayoutManager();
	}

	@Override
	public Color getLocalBackgroundColor() {
		return impl.getLocalBackgroundColor();
	}

	@Override
	public Color getLocalForegroundColor() {
		return impl.getLocalForegroundColor();
	}

	@Override
	public Dimension getMaximumSize() {
		return impl.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return impl.getMinimumSize();
	}

	@Override
	public Dimension getMinimumSize(int w, int h) {
		return impl.getMinimumSize(w, h);
	}

	@Override
	public IFigure getParent() {
		return impl.getParent();
	}

	@Override
	public Dimension getPreferredSize() {
		return impl.getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize(int w, int h) {
		return impl.getPreferredSize(w, h);
	}

	@Override
	public Dimension getSize() {
		return impl.getSize();
	}

	@Override
	public IFigure getToolTip() {
		return impl.getToolTip();
	}

	@Override
	public UpdateManager getUpdateManager() {
		return impl.getUpdateManager();
	}

	@Override
	public void handleFocusGained(FocusEvent event) {
		impl.handleFocusLost(event);
	}

	@Override
	public void handleFocusLost(FocusEvent event) {
		impl.handleFocusLost(event);
	}

	@Override
	public void handleKeyPressed(KeyEvent event) {
		impl.handleKeyPressed(event);
	}

	@Override
	public void handleKeyReleased(KeyEvent event) {
		impl.handleKeyReleased(event);
	}

	@Override
	public void handleMouseDoubleClicked(MouseEvent event) {
		impl.handleMouseDoubleClicked(event);
	}

	@Override
	public void handleMouseDragged(MouseEvent event) {
		impl.handleMouseDragged(event);
	}

	@Override
	public void handleMouseEntered(MouseEvent event) {
		impl.handleMouseEntered(event);
	}

	@Override
	public void handleMouseExited(MouseEvent event) {
		impl.handleMouseExited(event);		
	}

	@Override
	public void handleMouseHover(MouseEvent event) {
		impl.handleMouseHover(event);
	}

	@Override
	public void handleMouseMoved(MouseEvent event) {
		impl.handleMouseMoved(event);
	}

	@Override
	public void handleMousePressed(MouseEvent event) {
		impl.handleMousePressed(event);
	}

	@Override
	public void handleMouseReleased(MouseEvent event) {
		impl.handleMouseReleased(event);
	}

	@Override
	public boolean hasFocus() {
		return impl.hasFocus();
	}

	@Override
	public EventDispatcher internalGetEventDispatcher() {
		return impl.internalGetEventDispatcher();
	}

	@Override
	public boolean intersects(Rectangle rectangle) {
		return impl.intersects(rectangle);
	}

	@Override
	public void invalidate() {
		impl.invalidate();
	}

	@Override
	public void invalidateTree() {
		impl.invalidateTree();
	}

	@Override
	public boolean isCoordinateSystem() {
		return impl.isCoordinateSystem();
	}

	@Override
	public boolean isEnabled() {
		return impl.isEnabled();
	}

	@Override
	public boolean isFocusTraversable() {
		return impl.isFocusTraversable();
	}

	@Override
	public boolean isMirrored() {
		return impl.isMirrored();
	}

	@Override
	public boolean isOpaque() {
		return impl.isOpaque();
	}

	@Override
	public boolean isRequestFocusEnabled() {
		return impl.isRequestFocusEnabled();
	}

	@Override
	public boolean isShowing() {
		return impl.isShowing();
	}

	@Override
	public boolean isVisible() {
		return impl.isVisible();
	}

	@Override
	public void paint(Graphics g) {
		impl.paint(g);
	}

	@Override
	public void remove(IFigure child) {
		body.remove(child);
	}

	@Override
	public void removeAncestorListener(AncestorListener listener) {
		impl.removeAncestorListener(listener);
	}

	@Override
	public void removeCoordinateListener(CoordinateListener listener) {
		impl.removeCoordinateListener(listener);
	}

	@Override
	public void removeFigureListener(FigureListener listener) {
		impl.removeFigureListener(listener);
	}

	@Override
	public void removeFocusListener(FocusListener listener) {
		impl.removeFocusListener(listener);
	}

	@Override
	public void removeKeyListener(KeyListener listener) {
		impl.removeKeyListener(listener);
	}

	@Override
	public void removeLayoutListener(LayoutListener listener) {
		impl.removeLayoutListener(listener);
	}

	@Override
	public void removeMouseListener(MouseListener listener) {
		impl.removeMouseListener(listener);
	}

	@Override
	public void removeMouseMotionListener(MouseMotionListener listener) {
		impl.removeMouseMotionListener(listener);
	}

	@Override
	public void removeNotify() {
		impl.removeNotify();
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		impl.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		impl.removePropertyChangeListener(property, listener);
	}

	@Override
	public void repaint() {
		impl.repaint();
	}

	@Override
	public void repaint(Rectangle rectangle) {
		impl.repaint(rectangle);
	}

	@Override
	public void repaint(int x, int y, int w, int h) {
		impl.repaint(x, y, w, h);
	}

	@Override
	public void requestFocus() {
		impl.requestFocus();
	}

	@Override
	public void revalidate() {
		impl.revalidate();	
	}

	@Override
	public void setBackgroundColor(Color color) {
		impl.setBackgroundColor(color);
	}

	@Override
	public void setBorder(Border border) {
		impl.setBorder(border);
	}

	@Override
	public void setBounds(Rectangle rectangle) {
		impl.setBounds(rectangle);
	}

	@Override
	public void setClippingStrategy(IClippingStrategy strategy) {
		impl.setClippingStrategy(strategy);
	}

	@Override
	public void setConstraint(IFigure child, Object constraint) {
		body.setConstraint(child, constraint);
	}

	@Override
	public void setCursor(Cursor cursor) {
		impl.setCursor(cursor);
	}

	@Override
	public void setEnabled(boolean flag) {
		impl.setEnabled(flag);
	}

	@Override
	public void setFocusTraversable(boolean flag) {
		impl.setFocusTraversable(flag);		
	}

	@Override
	public void setFont(Font font) {
		impl.setFont(font);
	}

	@Override
	public void setForegroundColor(Color color) {
		impl.setForegroundColor(color);
	}

	@Override
	public void setLayoutManager(LayoutManager manager) {
		impl.setLayoutManager(manager);
	}

	@Override
	public void setLocation(Point point) {
		impl.setLocation(point);
	}

	@Override
	public void setMaximumSize(Dimension dimension) {
		impl.setMaximumSize(dimension);
	}

	@Override
	public void setMinimumSize(Dimension dimension) {
		impl.setMinimumSize(dimension);
	}

	@Override
	public void setOpaque(boolean flag) {
		impl.setOpaque(flag);
	}

	@Override
	public void setParent(IFigure parent) {
		impl.setParent(parent);
	}

	@Override
	public void setPreferredSize(Dimension dimension) {
		impl.setPreferredSize(dimension);
	}

	@Override
	public void setRequestFocusEnabled(boolean flag) {
		impl.setRequestFocusEnabled(flag);
	}

	@Override
	public void setSize(Dimension dimension) {
		impl.setSize(dimension);
	}

	@Override
	public void setSize(int w, int h) {
		impl.setSize(w, h);
	}

	@Override
	public void setToolTip(IFigure ifigure) {
		impl.setToolTip(ifigure);
	}

	@Override
	public void setVisible(boolean flag) {
		impl.setVisible(flag);
	}

	@Override
	public void translate(int dx, int dy) {
		impl.translate(dx, dy);
	}

	@Override
	public void translateFromParent(Translatable translatable) {
		impl.translateFromParent(translatable);
	}

	@Override
	public void translateToAbsolute(Translatable translatable) {
		impl.translateToAbsolute(translatable);
	}

	@Override
	public void translateToParent(Translatable translatable) {
		impl.translateToParent(translatable);
	}

	@Override
	public void translateToRelative(Translatable translatable) {
		impl.translateToRelative(translatable);
	}

	@Override
	public void validate() {
		impl.validate();
	}
}
