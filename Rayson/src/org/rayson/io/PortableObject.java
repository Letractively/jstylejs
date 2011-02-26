package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;

import org.rayson.api.Transportable;
import org.rayson.util.Reflection;

public abstract class PortableObject<T> {
	private byte type;

	private static final byte BYTE_TYPE = 1;
	private static final byte CHAR_TYPE = 2;
	private static final byte SHORT_TYPE = 3;
	private static final byte INT_TYPE = 4;
	private static final byte LONG_TYPE = 5;
	private static final byte DOUBLE_TYPE = 6;
	private static final byte FLOAT_TYPE = 7;

	private static final byte STRING_TYPE = 10;
	private static final byte TRANSPORTABLE_TYPE = 30;
	private static final byte NULL_TYPE = 0;
	private static final byte ARRAY_TYPE = 19;
	private static final PortableObject<String> STRING = new PortableObject<String>(
			STRING_TYPE) {

		@Override
		public String read(DataInput in) throws IOException {
			return in.readUTF();
		}

		@Override
		public void write(DataOutput out, String value) throws IOException {
			out.writeUTF(value);
		}

		@Override
		public Class getJavaClass() {
			return String.class;
		}
	};

	private static final PortableObject<Object> ARRAY = new PortableObject<Object>(
			ARRAY_TYPE) {

		@Override
		public Object read(DataInput in) throws IOException {
			boolean portableArray = false;
			// Read array item type.
			byte itemType = in.readByte();
			// Read array length
			short arrayLength = in.readShort();
			PortableObject portableObject;
			try {
				portableObject = objectOf(itemType);
			} catch (UnsupportedIOObjectException e) {
				throw new IOException(e);
			}
			if (portableObject == TRANSPORTABLE)
				portableArray = true;
			Class componentType = portableObject.getJavaClass();
			if (portableArray) {
				// read getComponentType class name
				String componentTypeClassName = in.readUTF();
				try {
					componentType = Class.forName(componentTypeClassName);
				} catch (ClassNotFoundException e) {
					throw new IOException(e);
				}
			}
			Object array = Array.newInstance(componentType, arrayLength);

			for (int i = 0; i < arrayLength; i++) {
				Object arrayItem;
				if (portableArray)
					arrayItem = PortableObject.readObject(in);
				else
					arrayItem = portableObject.read(in);
				Array.set(array, i, arrayItem);
			}
			return array;
		}

		@Override
		public void write(DataOutput out, Object value) throws IOException {
			// write array item type.
			boolean portableArray = false;
			PortableObject commponentObject;
			try {
				commponentObject = objectOf(value.getClass().getComponentType());
				out.writeByte(commponentObject.getType());
				if (commponentObject == TRANSPORTABLE)
					portableArray = true;
			} catch (UnsupportedIOObjectException e) {
				throw new IOException(e);
			}
			// write array length
			short arrayLength = (short) Array.getLength(value);
			out.writeShort(arrayLength);
			if (portableArray) {
				// write getComponentType class name
				out.writeUTF(value.getClass().getComponentType().getName());
			}
			for (int i = 0; i < arrayLength; i++) {
				Object arrayItem = Array.get(value, i);
				if (portableArray) {
					writeObject(out, arrayItem);
				} else {
					commponentObject.write(out, arrayItem);
				}
			}

		}

		@Override
		public Class getJavaClass() {
			return Array.class;
		}
	};
	private static final PortableObject<Transportable> TRANSPORTABLE = new PortableObject<Transportable>(
			TRANSPORTABLE_TYPE) {

		@Override
		public Transportable read(DataInput in) throws IOException {
			// Read class name.
			String className = in.readUTF();
			// Read value.
			Transportable transportable = null;
			try {
				transportable = (Transportable) Reflection
						.newInstance(className);
			} catch (Exception e) {
				new IOException(e);
			}
			transportable.read(in);
			return transportable;
		}

		@Override
		public void write(DataOutput out, Transportable value)
				throws IOException {
			// Write class name.
			out.writeUTF(value.getClass().getName());
			// Write value.
			value.write(out);
		}

		@Override
		public Class getJavaClass() {
			return Transportable.class;
		}

	};
	private static final PortableObject<Object> NULL = new PortableObject<Object>(
			NULL_TYPE) {

		@Override
		public Object read(DataInput in) throws IOException {
			return null;
		}

		@Override
		public void write(DataOutput out, Object value) throws IOException {

		}

		@Override
		public Class getJavaClass() {
			return null;
		}

	};
	private static final PortableObject<Byte> BYTE = new PortableObject<Byte>(
			BYTE_TYPE) {

		@Override
		public Byte read(DataInput in) throws IOException {
			return in.readByte();
		}

		@Override
		public void write(DataOutput out, Byte value) throws IOException {
			out.writeByte(value);
		}

		@Override
		public Class getJavaClass() {
			return Byte.class;
		}
	};
	private static final PortableObject<Character> CHAR = new PortableObject<Character>(
			CHAR_TYPE) {

		@Override
		public Character read(DataInput in) throws IOException {
			return in.readChar();
		}

		@Override
		public void write(DataOutput out, Character value) throws IOException {
			out.writeChar(value);
		}

		@Override
		public Class getJavaClass() {
			return Character.class;
		}
	};

	private static final PortableObject<Short> SHORT = new PortableObject<Short>(
			SHORT_TYPE) {

		@Override
		public Class getJavaClass() {
			return Short.class;
		}

		@Override
		public Short read(DataInput in) throws IOException {
			return in.readShort();
		}

		@Override
		public void write(DataOutput out, Short value) throws IOException {
			out.writeShort(value);
		}
	};

	private static final PortableObject<Long> LONG = new PortableObject<Long>(
			LONG_TYPE) {

		public Class getJavaClass() {
			return Short.class;
		};

		@Override
		public Long read(DataInput in) throws IOException {
			return in.readLong();
		}

		@Override
		public void write(DataOutput out, Long value) throws IOException {
			out.writeLong(value);
		}
	};
	private static final PortableObject<Double> DOUBLE = new PortableObject<Double>(
			DOUBLE_TYPE) {

		@Override
		public Double read(DataInput in) throws IOException {
			return in.readDouble();
		}

		@Override
		public void write(DataOutput out, Double value) throws IOException {
			out.writeDouble(value);
		}

		@Override
		public Class getJavaClass() {
			return Double.class;
		}
	};
	private static final PortableObject<Float> FLOAT = new PortableObject<Float>(
			FLOAT_TYPE) {

		@Override
		public Class getJavaClass() {
			return Float.class;
		};

		@Override
		public Float read(DataInput in) throws IOException {
			return in.readFloat();
		}

		@Override
		public void write(DataOutput out, Float value) throws IOException {
			out.writeFloat(value);
		}
	};

	private static final PortableObject<Integer> INT = new PortableObject<Integer>(
			INT_TYPE) {

		@Override
		public Integer read(DataInput in) throws IOException {
			return in.readInt();
		}

		@Override
		public void write(DataOutput out, Integer value) throws IOException {
			out.writeInt(value);
		}

		@Override
		public Class getJavaClass() {
			return Integer.class;
		}
	};

	private static HashMap<Byte, PortableObject> TYPE_OBJECTS = new HashMap<Byte, PortableObject>();
	static {
		TYPE_OBJECTS.put(BYTE_TYPE, BYTE);
		TYPE_OBJECTS.put(CHAR_TYPE, CHAR);
		TYPE_OBJECTS.put(SHORT_TYPE, SHORT);
		TYPE_OBJECTS.put(INT_TYPE, INT);
		TYPE_OBJECTS.put(LONG_TYPE, LONG);
		TYPE_OBJECTS.put(FLOAT_TYPE, FLOAT);
		TYPE_OBJECTS.put(DOUBLE_TYPE, DOUBLE);
		TYPE_OBJECTS.put(TRANSPORTABLE_TYPE, TRANSPORTABLE);
		TYPE_OBJECTS.put(NULL_TYPE, NULL);
		TYPE_OBJECTS.put(ARRAY_TYPE, ARRAY);
		TYPE_OBJECTS.put(STRING_TYPE, STRING);
	}

	private static HashMap<Class, PortableObject> CLASS_OBJECTS = new HashMap<Class, PortableObject>();
	static {
		CLASS_OBJECTS.put(Byte.class, BYTE);
		CLASS_OBJECTS.put(Character.class, CHAR);
		CLASS_OBJECTS.put(Short.class, SHORT);
		CLASS_OBJECTS.put(Integer.class, INT);
		CLASS_OBJECTS.put(Long.class, LONG);
		CLASS_OBJECTS.put(Float.class, FLOAT);
		CLASS_OBJECTS.put(Double.class, DOUBLE);
		CLASS_OBJECTS.put(String.class, STRING);
	}

	public static void writeObject(DataOutput out, Object value)
			throws IOException {
		PortableObject object;
		try {
			object = objectOf(value);
		} catch (UnsupportedIOObjectException e) {
			throw new IOException(e);
		}
		out.writeByte(object.getType());
		object.write(out, value);
	}

	public static Object readObject(DataInput in) throws IOException {
		byte type = in.readByte();
		PortableObject object;
		try {
			object = objectOf(type);
		} catch (UnsupportedIOObjectException e) {
			throw new IOException(e);
		}
		return object.read(in);
	}

	private static PortableObject objectOf(Class klass)
			throws UnsupportedIOObjectException {
		if (klass.isArray())
			return ARRAY;
		if (Transportable.class.isAssignableFrom(klass))
			return TRANSPORTABLE;
		PortableObject ioObject = CLASS_OBJECTS.get(klass);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	private static PortableObject objectOf(byte type)
			throws UnsupportedIOObjectException {
		PortableObject ioObject = TYPE_OBJECTS.get(type);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	private static PortableObject objectOf(Object value)
			throws UnsupportedIOObjectException {
		if (value == null)
			return NULL;
		return objectOf(value.getClass());
	}

	private PortableObject(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public static void main(String[] args) throws UnsupportedIOObjectException {
		System.out.println(objectOf((byte) 1));
		System.out.println(objectOf('c'));
		System.out.println(objectOf(1));
		System.out.println(objectOf(2L));
		System.out.println(objectOf(3F));
		System.out.println(objectOf(4D));
		System.out.println(objectOf(""));
		System.out.println(objectOf(new byte[] {}));
		// System.out.println(objectOf(new Invocation("", null, null)));

	}

	public final String getName() {
		return getJavaClass().getSimpleName();
	}

	public abstract Class getJavaClass();

	@Override
	public String toString() {
		return getName() + ":" + getType();
	}

	public abstract T read(DataInput in) throws IOException;

	public abstract void write(DataOutput out, T value) throws IOException;
}