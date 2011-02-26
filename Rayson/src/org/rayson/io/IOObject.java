package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

public abstract class IOObject<T> {
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
	private static final IOObject<String> STRING = new IOObject<String>(
			STRING_TYPE) {
		@Override
		public String getName() {
			return "STRING";
		}

		@Override
		String read(DataInput in) throws IOException {
			return in.readUTF();
		}

		@Override
		void write(DataOutput out, String value) throws IOException {
			out.writeUTF(value);
		}
	};

	private static final IOObject<Object> ARRAY = new IOObject<Object>(
			ARRAY_TYPE) {

		@Override
		Object read(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		void write(DataOutput out, Object value) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public String getName() {
			return "ARRAY";
		}
	};
	private static final IOObject<Writable> WRITABLE = new IOObject<Writable>(
			WRITABLE_TYPE) {

		@Override
		Writable read(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		void write(DataOutput out, Writable value) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public String getName() {
			return "WRITABLE";
		}

	};
	private static final IOObject<Object> NULL = new IOObject<Object>(NULL_TYPE) {

		@Override
		Object read(DataInput in) throws IOException {
			return null;
		}

		@Override
		void write(DataOutput out, Object value) throws IOException {

		}

		@Override
		public String getName() {
			return "NULL";
		}

	};
	private static final IOObject<Byte> BYTE = new IOObject<Byte>(BYTE_TYPE) {

		@Override
		public String getName() {
			return "BYTE";
		}

		@Override
		Byte read(DataInput in) throws IOException {
			return in.readByte();
		}

		@Override
		void write(DataOutput out, Byte value) throws IOException {
			out.writeByte(value);
		}
	};
	private static final IOObject<Character> CHAR = new IOObject<Character>(
			CHAR_TYPE) {

		@Override
		public String getName() {
			return "CHAR";
		}

		@Override
		Character read(DataInput in) throws IOException {
			return in.readChar();
		}

		@Override
		void write(DataOutput out, Character value) throws IOException {
			out.writeChar(value);
		}
	};

	private static final IOObject<Short> SHORT = new IOObject<Short>(SHORT_TYPE) {

		@Override
		public String getName() {
			return "SHORT";
		}

		@Override
		Short read(DataInput in) throws IOException {
			return in.readShort();
		}

		@Override
		void write(DataOutput out, Short value) throws IOException {
			out.writeShort(value);
		}
	};

	private static final IOObject<Long> LONG = new IOObject<Long>(LONG_TYPE) {

		@Override
		public String getName() {
			return "LONG";
		}

		@Override
		Long read(DataInput in) throws IOException {
			return in.readLong();
		}

		@Override
		void write(DataOutput out, Long value) throws IOException {
			out.writeLong(value);
		}
	};
	private static final IOObject<Double> DOUBLE = new IOObject<Double>(
			DOUBLE_TYPE) {

		@Override
		public String getName() {
			return "DOUBLE";
		}

		@Override
		Double read(DataInput in) throws IOException {
			return in.readDouble();
		}

		@Override
		void write(DataOutput out, Double value) throws IOException {
			out.writeDouble(value);
		}
	};
	private static final IOObject<Float> FLOAT = new IOObject<Float>(FLOAT_TYPE) {

		@Override
		public String getName() {
			return "FLOAT";
		}

		@Override
		Float read(DataInput in) throws IOException {
			return in.readFloat();
		}

		@Override
		void write(DataOutput out, Float value) throws IOException {
			out.writeFloat(value);
		}
	};

	private static final IOObject<Integer> INT = new IOObject<Integer>(INT_TYPE) {

		@Override
		Integer read(DataInput in) throws IOException {
			return in.readInt();
		}

		@Override
		void write(DataOutput out, Integer value) throws IOException {
			out.writeInt(value);
		}

		@Override
		public String getName() {
			return "INT";
		}
	};

	private static HashMap<Short, IOObject> TYPE_OBJECTS = new HashMap<Short, IOObject>();
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

	private static HashMap<Class, IOObject> CLASS_OBJECTS = new HashMap<Class, IOObject>();
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

	public static IOObject valueOf(short type)
			throws UnsupportedIOObjectException {
		IOObject ioObject = TYPE_OBJECTS.get(type);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	public static IOObject valueOf(Object value)
			throws UnsupportedIOObjectException {
		if (value == null)
			return NULL;
		Class klass = value.getClass();
		if (klass.isArray())
			return ARRAY;
		if (Writable.class.isAssignableFrom(klass))
			return WRITABLE;
		IOObject ioObject = CLASS_OBJECTS.get(klass);
		if (ioObject == null)
			throw new UnsupportedIOObjectException();
		return ioObject;
	}

	private IOObject(short type) {
		this.type = type;
	}

	public short getType() {
		return type;
	}

	public static void main(String[] args) throws UnsupportedIOObjectException {
		System.out.println(valueOf((byte) 1));
		System.out.println(valueOf('c'));
		System.out.println(valueOf(1));
		System.out.println(valueOf(2L));
		System.out.println(valueOf(3F));
		System.out.println(valueOf(4D));
		System.out.println(valueOf(""));
		System.out.println(valueOf(new byte[] {}));
		System.out.println(valueOf(new Invocation("", null, null)));

	}

	public abstract String getName();

	@Override
	public String toString() {
		return getName() + ":" + getType();
	}

	abstract T read(DataInput in) throws IOException;

	abstract void write(DataOutput out, T value) throws IOException;
}