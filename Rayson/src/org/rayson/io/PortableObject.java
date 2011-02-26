package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.rayson.api.Transportable;
import org.rayson.util.Reflection;

public abstract class PortableObject<T> {
	private short type;

	private static final short BYTE_TYPE = 1;
	private static final short CHAR_TYPE = 2;
	private static final short SHORT_TYPE = 3;
	private static final short INT_TYPE = 4;
	private static final short LONG_TYPE = 5;
	private static final short DOUBLE_TYPE = 6;
	private static final short FLOAT_TYPE = 7;

	private static final short STRING_TYPE = 10;
	private static final short WRITABLE_TYPE = 30;
	private static final short NULL_TYPE = 0;
	private static final short ARRAY_TYPE = 19;
	private static final PortableObject<String> STRING = new PortableObject<String>(
			STRING_TYPE) {
		@Override
		public String getName() {
			return "STRING";
		}

		@Override
		public String read(DataInput in) throws IOException {
			return in.readUTF();
		}

		@Override
		public void write(DataOutput out, String value) throws IOException {
			out.writeUTF(value);
		}
	};

	private static final PortableObject<Object> ARRAY = new PortableObject<Object>(
			ARRAY_TYPE) {

		@Override
		public Object read(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void write(DataOutput out, Object value) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public String getName() {
			return "ARRAY";
		}
	};
	private static final PortableObject<Transportable> WRITABLE = new PortableObject<Transportable>(
			WRITABLE_TYPE) {

		@Override
		public Transportable read(DataInput in) throws IOException {
			// Read class name.
			String className = in.readUTF();
			// Read value.
			Transportable writable = null;
			try {
				writable = (Transportable) Reflection.newInstance(className);
			} catch (Exception e) {
				new IOException(e);
			}
			writable.read(in);
			return writable;
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
		public String getName() {
			return "WRITABLE";
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
		public String getName() {
			return "NULL";
		}

	};
	private static final PortableObject<Byte> BYTE = new PortableObject<Byte>(
			BYTE_TYPE) {

		@Override
		public String getName() {
			return "BYTE";
		}

		@Override
		public Byte read(DataInput in) throws IOException {
			return in.readByte();
		}

		@Override
		public void write(DataOutput out, Byte value) throws IOException {
			out.writeByte(value);
		}
	};
	private static final PortableObject<Character> CHAR = new PortableObject<Character>(
			CHAR_TYPE) {

		@Override
		public String getName() {
			return "CHAR";
		}

		@Override
		public Character read(DataInput in) throws IOException {
			return in.readChar();
		}

		@Override
		public void write(DataOutput out, Character value) throws IOException {
			out.writeChar(value);
		}
	};

	private static final PortableObject<Short> SHORT = new PortableObject<Short>(
			SHORT_TYPE) {

		@Override
		public String getName() {
			return "SHORT";
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

		@Override
		public String getName() {
			return "LONG";
		}

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
		public String getName() {
			return "DOUBLE";
		}

		@Override
		public Double read(DataInput in) throws IOException {
			return in.readDouble();
		}

		@Override
		public void write(DataOutput out, Double value) throws IOException {
			out.writeDouble(value);
		}
	};
	private static final PortableObject<Float> FLOAT = new PortableObject<Float>(
			FLOAT_TYPE) {

		@Override
		public String getName() {
			return "FLOAT";
		}

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
		public String getName() {
			return "INT";
		}
	};

	private static HashMap<Short, PortableObject> TYPE_OBJECTS = new HashMap<Short, PortableObject>();
	static {
		TYPE_OBJECTS.put(BYTE_TYPE, BYTE);
		TYPE_OBJECTS.put(CHAR_TYPE, CHAR);
		TYPE_OBJECTS.put(SHORT_TYPE, SHORT);
		TYPE_OBJECTS.put(INT_TYPE, INT);
		TYPE_OBJECTS.put(LONG_TYPE, LONG);
		TYPE_OBJECTS.put(FLOAT_TYPE, FLOAT);
		TYPE_OBJECTS.put(DOUBLE_TYPE, DOUBLE);
		TYPE_OBJECTS.put(WRITABLE_TYPE, WRITABLE);
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
		out.writeShort(object.getType());
		object.write(out, value);
	}

	public static Object readObject(DataInput in) throws IOException {
		short type = in.readShort();
		PortableObject object;
		try {
			object = objectOf(type);
		} catch (UnsupportedIOObjectException e) {
			throw new IOException(e);
		}
		return object.read(in);
	}

	public static PortableObject objectOf(short type)
			throws UnsupportedIOObjectException {
		PortableObject ioObject = TYPE_OBJECTS.get(type);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	public static PortableObject objectOf(Object value)
			throws UnsupportedIOObjectException {
		if (value == null)
			return NULL;
		Class klass = value.getClass();
		if (klass.isArray())
			return ARRAY;
		if (Transportable.class.isAssignableFrom(klass))
			return WRITABLE;
		PortableObject ioObject = CLASS_OBJECTS.get(klass);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	private PortableObject(short type) {
		this.type = type;
	}

	public short getType() {
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
		System.out.println(objectOf(new Invocation("", null, null)));

	}

	public abstract String getName();

	@Override
	public String toString() {
		return getName() + ":" + getType();
	}

	public abstract T read(DataInput in) throws IOException;

	public abstract void write(DataOutput out, T value) throws IOException;
}