package networkGUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.io.SerialUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class SimDrawingSupplier implements DrawingSupplier, Cloneable,
		PublicCloneable, Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -7339847061039422538L;

	/** The default fill paint sequence. */
	public static final Paint[] LINE_PAINT_SEQUENCE = new Paint[] {
			Color.black,
			Color.LIGHT_GRAY

	};

	public static final Paint[] LINE_PAINT_SEQUENCE_FOR_STIM_COL = new Paint[] {
			Color.red,
			Color.LIGHT_GRAY

	};

	public static final Paint[] DEFAULT_PAINT_SEQUENCE = new Paint[] {
			Color.red, // lts
			Color.blue, // fs
			new Color(0, 139, 0), // rs
			new Color(0, 255, 10), // ib
	};

	/** The default outline paint sequence. */
	public static final Paint[] DEFAULT_OUTLINE_PAINT_SEQUENCE = new Paint[] {
			Color.lightGray };

	/** The default fill paint sequence. */
	public static final Paint[] DEFAULT_FILL_PAINT_SEQUENCE = new Paint[] {
			Color.white };

	/** The default stroke sequence. */
	public static final Stroke[] DEFAULT_STROKE_SEQUENCE = new Stroke[] {
			new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL) };

	/** The default outline stroke sequence. */
	public static final Stroke[] DEFAULT_OUTLINE_STROKE_SEQUENCE = new Stroke[] { new BasicStroke(1.0f,
			BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_BEVEL) };

	/** The default shape sequence. */
	public static final Shape[] DEFAULT_SHAPE_SEQUENCE = createStandardSeriesShapes();

	/** The paint sequence. */
	private transient Paint[] paintSequence;

	/** The current paint index. */
	private int paintIndex;

	/** The outline paint sequence. */
	private transient Paint[] outlinePaintSequence;

	/** The current outline paint index. */
	private int outlinePaintIndex;

	/** The fill paint sequence. */
	private transient Paint[] fillPaintSequence;

	/** The current fill paint index. */
	private int fillPaintIndex;

	/** The stroke sequence. */
	private transient Stroke[] strokeSequence;

	/** The current stroke index. */
	private int strokeIndex;

	/** The outline stroke sequence. */
	private transient Stroke[] outlineStrokeSequence;

	/** The current outline stroke index. */
	private int outlineStrokeIndex;

	/** The shape sequence. */
	private transient Shape[] shapeSequence;

	/** The current shape index. */
	private int shapeIndex;

	/**
	 * Creates a new supplier, with default sequences for fill paint, outline
	 * paint, stroke and shapes.
	 */

	public SimDrawingSupplier(String n) {
		this.fillPaintSequence = DEFAULT_FILL_PAINT_SEQUENCE;
		this.outlinePaintSequence = DEFAULT_OUTLINE_PAINT_SEQUENCE;
		this.strokeSequence = DEFAULT_STROKE_SEQUENCE;
		this.outlineStrokeSequence = DEFAULT_OUTLINE_STROKE_SEQUENCE;
		this.shapeSequence = DEFAULT_SHAPE_SEQUENCE;
		if (n.equals("line")) {
			this.paintSequence = LINE_PAINT_SEQUENCE;
		} else {
			if (n.equals("stim")) {
				this.paintSequence = LINE_PAINT_SEQUENCE_FOR_STIM_COL;
			} else {
				this.paintSequence = DEFAULT_PAINT_SEQUENCE;
			}
		}
	}

	/**
	 * Creates a new supplier.
	 * 
	 * @param paintSequence
	 *            the fill paint sequence.
	 * @param outlinePaintSequence
	 *            the outline paint sequence.
	 * @param strokeSequence
	 *            the stroke sequence.
	 * @param outlineStrokeSequence
	 *            the outline stroke sequence.
	 * @param shapeSequence
	 *            the shape sequence.
	 */
	public SimDrawingSupplier(Paint[] paintSequence,
			Paint[] outlinePaintSequence,
			Stroke[] strokeSequence,
			Stroke[] outlineStrokeSequence,
			Shape[] shapeSequence) {

		this.paintSequence = paintSequence;
		this.fillPaintSequence = DEFAULT_FILL_PAINT_SEQUENCE;
		this.outlinePaintSequence = outlinePaintSequence;
		this.strokeSequence = strokeSequence;
		this.outlineStrokeSequence = outlineStrokeSequence;
		this.shapeSequence = shapeSequence;

	}

	/**
	 * Creates a new supplier.
	 * 
	 * @param paintSequence
	 *            the paint sequence.
	 * @param fillPaintSequence
	 *            the fill paint sequence.
	 * @param outlinePaintSequence
	 *            the outline paint sequence.
	 * @param strokeSequence
	 *            the stroke sequence.
	 * @param outlineStrokeSequence
	 *            the outline stroke sequence.
	 * @param shapeSequence
	 *            the shape sequence.
	 * 
	 * @since 1.0.6
	 */
	public SimDrawingSupplier(Paint[] paintSequence,
			Paint[] fillPaintSequence, Paint[] outlinePaintSequence,
			Stroke[] strokeSequence, Stroke[] outlineStrokeSequence,
			Shape[] shapeSequence) {

		this.paintSequence = paintSequence;
		this.fillPaintSequence = fillPaintSequence;
		this.outlinePaintSequence = outlinePaintSequence;
		this.strokeSequence = strokeSequence;
		this.outlineStrokeSequence = outlineStrokeSequence;
		this.shapeSequence = shapeSequence;
	}

	/**
	 * Returns the next paint in the sequence.
	 * 
	 * @return The paint.
	 */
	@Override
	public Paint getNextPaint() {
		Paint result = this.paintSequence[this.paintIndex % this.paintSequence.length];
		this.paintIndex++;
		return result;
	}

	/**
	 * Returns the next outline paint in the sequence.
	 * 
	 * @return The paint.
	 */
	@Override
	public Paint getNextOutlinePaint() {
		Paint result = this.outlinePaintSequence[
				this.outlinePaintIndex % this.outlinePaintSequence.length];
		this.outlinePaintIndex++;
		return result;
	}

	/**
	 * Returns the next fill paint in the sequence.
	 * 
	 * @return The paint.
	 * 
	 * @since 1.0.6
	 */
	@Override
	public Paint getNextFillPaint() {
		Paint result = this.fillPaintSequence[this.fillPaintIndex
				% this.fillPaintSequence.length];
		this.fillPaintIndex++;
		return result;
	}

	/**
	 * Returns the next stroke in the sequence.
	 * 
	 * @return The stroke.
	 */
	@Override
	public Stroke getNextStroke() {
		Stroke result = this.strokeSequence[
				this.strokeIndex % this.strokeSequence.length];
		this.strokeIndex++;
		return result;
	}

	/**
	 * Returns the next outline stroke in the sequence.
	 * 
	 * @return The stroke.
	 */
	@Override
	public Stroke getNextOutlineStroke() {
		Stroke result = this.outlineStrokeSequence[
				this.outlineStrokeIndex % this.outlineStrokeSequence.length];
		this.outlineStrokeIndex++;
		return result;
	}

	/**
	 * Returns the next shape in the sequence.
	 * 
	 * @return The shape.
	 */
	@Override
	public Shape getNextShape() {
		Shape result = this.shapeSequence[
				this.shapeIndex % this.shapeSequence.length];
		this.shapeIndex++;
		return result;
	}

	/**
	 * Creates an array of standard shapes to display for the items in series on
	 * charts.
	 * 
	 * @return The array of shapes.
	 */
	public static Shape[] createStandardSeriesShapes() {

		Shape[] result = new Shape[10];

		double size = 4.0;
		double delta = size / 2.0;
		int[] xpoints = null;
		int[] ypoints = null;

		// square
		result[0] = new Rectangle2D.Double(-size / 3, -size / 2, size, size);
		// circle
		result[1] = new Ellipse2D.Double(-delta, -delta, size, size);

		// up-pointing triangle
		xpoints = intArray(0.0, delta, -delta);
		ypoints = intArray(-delta, delta, delta);
		result[2] = new Polygon(xpoints, ypoints, 3);

		// diamond
		xpoints = intArray(0.0, delta, 0.0, -delta);
		ypoints = intArray(-delta, 0.0, delta, 0.0);
		result[3] = new Polygon(xpoints, ypoints, 4);

		// horizontal rectangle
		result[4] = new Rectangle2D.Double(-delta, -delta / 2, size, size / 2);

		// down-pointing triangle
		xpoints = intArray(-delta, +delta, 0.0);
		ypoints = intArray(-delta, -delta, delta);
		result[5] = new Polygon(xpoints, ypoints, 3);

		// horizontal ellipse
		result[6] = new Ellipse2D.Double(-delta, -delta / 2, size, size / 2);

		// right-pointing triangle
		xpoints = intArray(-delta, delta, -delta);
		ypoints = intArray(-delta, 0.0, delta);
		result[7] = new Polygon(xpoints, ypoints, 3);

		// vertical rectangle
		result[8] = new Rectangle2D.Double(-delta / 2, -delta, size / 2, size);

		// left-pointing triangle
		xpoints = intArray(-delta, delta, delta);
		ypoints = intArray(0.0, -delta, +delta);
		result[9] = new Polygon(xpoints, ypoints, 3);

		return result;

	}

	/**
	 * Tests this object for equality with another object.
	 * 
	 * @param obj
	 *            the object (<code>null</code> permitted).
	 * 
	 * @return A boolean.
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof DefaultDrawingSupplier)) {
			return false;
		}

		SimDrawingSupplier that = (SimDrawingSupplier) obj;

		if (!Arrays.equals(this.paintSequence, that.paintSequence)) {
			return false;
		}
		if (this.paintIndex != that.paintIndex) {
			return false;
		}
		if (!Arrays.equals(this.outlinePaintSequence,
				that.outlinePaintSequence)) {
			return false;
		}
		if (this.outlinePaintIndex != that.outlinePaintIndex) {
			return false;
		}
		if (!Arrays.equals(this.strokeSequence, that.strokeSequence)) {
			return false;
		}
		if (this.strokeIndex != that.strokeIndex) {
			return false;
		}
		if (!Arrays.equals(this.outlineStrokeSequence,
				that.outlineStrokeSequence)) {
			return false;
		}
		if (this.outlineStrokeIndex != that.outlineStrokeIndex) {
			return false;
		}
		if (!equalShapes(this.shapeSequence, that.shapeSequence)) {
			return false;
		}
		if (this.shapeIndex != that.shapeIndex) {
			return false;
		}
		return true;

	}

	/**
	 * A utility method for testing the equality of two arrays of shapes.
	 * 
	 * @param s1
	 *            the first array (<code>null</code> permitted).
	 * @param s2
	 *            the second array (<code>null</code> permitted).
	 * 
	 * @return A boolean.
	 */
	private boolean equalShapes(Shape[] s1, Shape[] s2) {
		if (s1 == null) {
			return s2 == null;
		}
		if (s2 == null) {
			return false;
		}
		if (s1.length != s2.length) {
			return false;
		}
		for (int i = 0; i < s1.length; i++) {
			if (!ShapeUtilities.equal(s1[i], s2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Handles serialization.
	 * 
	 * @param stream
	 *            the output stream.
	 * 
	 * @throws IOException
	 *             if there is an I/O problem.
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

		int paintCount = this.paintSequence.length;
		stream.writeInt(paintCount);
		for (int i = 0; i < paintCount; i++) {
			SerialUtilities.writePaint(this.paintSequence[i], stream);
		}

		int outlinePaintCount = this.outlinePaintSequence.length;
		stream.writeInt(outlinePaintCount);
		for (int i = 0; i < outlinePaintCount; i++) {
			SerialUtilities.writePaint(this.outlinePaintSequence[i], stream);
		}

		int strokeCount = this.strokeSequence.length;
		stream.writeInt(strokeCount);
		for (int i = 0; i < strokeCount; i++) {
			SerialUtilities.writeStroke(this.strokeSequence[i], stream);
		}

		int outlineStrokeCount = this.outlineStrokeSequence.length;
		stream.writeInt(outlineStrokeCount);
		for (int i = 0; i < outlineStrokeCount; i++) {
			SerialUtilities.writeStroke(this.outlineStrokeSequence[i], stream);
		}

		int shapeCount = this.shapeSequence.length;
		stream.writeInt(shapeCount);
		for (int i = 0; i < shapeCount; i++) {
			SerialUtilities.writeShape(this.shapeSequence[i], stream);
		}

	}

	/**
	 * Restores a serialized object.
	 * 
	 * @param stream
	 *            the input stream.
	 * 
	 * @throws IOException
	 *             if there is an I/O problem.
	 * @throws ClassNotFoundException
	 *             if there is a problem loading a class.
	 */
	private void readObject(ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		int paintCount = stream.readInt();
		this.paintSequence = new Paint[paintCount];
		for (int i = 0; i < paintCount; i++) {
			this.paintSequence[i] = SerialUtilities.readPaint(stream);
		}

		int outlinePaintCount = stream.readInt();
		this.outlinePaintSequence = new Paint[outlinePaintCount];
		for (int i = 0; i < outlinePaintCount; i++) {
			this.outlinePaintSequence[i] = SerialUtilities.readPaint(stream);
		}

		int strokeCount = stream.readInt();
		this.strokeSequence = new Stroke[strokeCount];
		for (int i = 0; i < strokeCount; i++) {
			this.strokeSequence[i] = SerialUtilities.readStroke(stream);
		}

		int outlineStrokeCount = stream.readInt();
		this.outlineStrokeSequence = new Stroke[outlineStrokeCount];
		for (int i = 0; i < outlineStrokeCount; i++) {
			this.outlineStrokeSequence[i] = SerialUtilities.readStroke(stream);
		}

		int shapeCount = stream.readInt();
		this.shapeSequence = new Shape[shapeCount];
		for (int i = 0; i < shapeCount; i++) {
			this.shapeSequence[i] = SerialUtilities.readShape(stream);
		}

	}

	/**
	 * Helper method to avoid lots of explicit casts in getShape(). Returns an
	 * array containing the provided doubles cast to ints.
	 * 
	 * @param a
	 *            x
	 * @param b
	 *            y
	 * @param c
	 *            z
	 * 
	 * @return int[3] with converted params.
	 */
	private static int[] intArray(double a, double b, double c) {
		return new int[] { (int) a, (int) b, (int) c };
	}

	/**
	 * Helper method to avoid lots of explicit casts in getShape(). Returns an
	 * array containing the provided doubles cast to ints.
	 * 
	 * @param a
	 *            x
	 * @param b
	 *            y
	 * @param c
	 *            z
	 * @param d
	 *            t
	 * 
	 * @return int[4] with converted params.
	 */
	private static int[] intArray(double a, double b, double c, double d) {
		return new int[] { (int) a, (int) b, (int) c, (int) d };
	}

	/**
	 * Returns a clone.
	 * 
	 * @return A clone.
	 * 
	 * @throws CloneNotSupportedException
	 *             if a component of the supplier does not support cloning.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		SimDrawingSupplier clone = (SimDrawingSupplier) super.clone();
		return clone;
	}
}
