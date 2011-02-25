package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

public abstract class RpcObject<T> {
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
	private static final RpcObject<String> STRING = new RpcObject<String>(
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

	private static final RpcObject<Object> ARRAY = new RpcObject<Object>(
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
	private static final RpcObject<Writable> WRITABLE = new RpcObject<Writable>(
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
	private static final RpcObject<Object> NULL = new RpcObject<Object>(
			NULL_TYPE) {

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
	private static final RpcObject<Byte> BYTE = new RpcObject<Byte>(BYTE_TYPE) {

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
	private static final RpcObject<Character> CHAR = new RpcObject<Character>(
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

	private static final RpcObject<Short> SHORT = new RpcObject<Short>(
			SHORT_TYPE) {

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

	private static final RpcObject<Long> LONG = new RpcObject<Long>(LONG_TYPE) {

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
	private static final RpcObject<Double> DOUBLE = new RpcObject<Double>(
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
	private static final RpcObject<Float> FLOAT = new RpcObject<Float>(
			FLOAT_TYPE) {

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

	private static final RpcObject<Integer> INT = new RpcObject<Integer>(
			INT_TYPE) {

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

	private static HashMap<Short, RpcObject> TYPE_OBJECTS = new HashMap<Short, RpcObject>();
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

	private static HashMap<Class, RpcObject> CLASS_OBJECTS = new HashMap<Class, RpcObject>();
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

	public static RpcObject valueOf(short type)
			throws UnsupportedRpcObjectException {
		RpcObject rpcObject = TYPE_OBJECTS.get(type);
		if (rpcObject == null)
			throw new UnsupportedRpcObjectException();
		return rpcObject;
	}

	public static RpcObject valueOf(Object value)
			throws UnsupportedRpcObjectException {
		if (value == null)
			return NULL;
		Class klass = value.getClass();
		if (klass.isArray())
			return ARRAY;
		if (Writable.class.isAssignableFrom(klass))
			return WRITABLE;
		RpcObject rpcObject = CLASS_OBJECTS.get(klass);
		if (rpcObject == null)
			throw new UnsupportedRpcObjectException();
		return rpcObject;
	}

	private RpcObject(short type) {
		this.type = type;
	}

	public short getType() {
		return type;
	}

	public static void main(String[] args) throws UnsupportedRpcObjectException {
		System.out.println(valueOf((byte) 1));
		System.out.println(valueOf('c'));
		System.out.println(valueOf(1));
		System.out.println(valueOf(2L));
		System.out.println(valueOf(3F));
		System.out.println(valueOf(4D));
		System.out.println(valueOf(""));
	}

	public abstract String getName();

	@Override
	public String toString() {
		return getName() + ":" + getType();
	}

	abstract T read(DataInput in) throws IOException;

	abstract void write(DataOutput out, T value) throws IOException;
}