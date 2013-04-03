/**
 * An javascript object oriented library.
 * 
 * @author Nick Zhang
 */

/**
 * TODO: comments later.
 */
var __JOO_CLASS__ = function() {
	/*
	 * Do log informations.
	 */
	var LOGGER = {};
	var __hashCode__ = 0;
	/**
	 * Basic Object of JOO.
	 */
	var JOBJECT = function(Class) {
		var hashCode = __hashCode__++;
		this.toString = function() {
			return "@" + hashCode;
		};
		this.hashCode = function() {
			return hashCode;
		};
		this.getClass = function() {
			return Class;
		};
	};
	// Extends nothing.
	JOBJECT.Extends = function() {
		print("OBJECT constructor");
	};
	/**
	 * Basic Class of JOO.
	 */
	var JCLASS = function() {

	};

	/**
	 * Construct a new JOBJECT.
	 * 
	 * @param this
	 *            The class define
	 * @param parameters
	 *            all the constructor fields.
	 */
	JCLASS.Constructor = function() {
		var argCount = arguments.length;
		var matchConstructor = this.constructors[0];
		var constructor;
		for (v in this.constructors) {
			constructor = this.constructors[v];
			if (constructor.length == argCount) {
				matchConstructor = constructor;
				break;
			}
		}
		var result = new JOBJECT(this);
		result.constructor = matchConstructor;
		result.supper = this.Extends;
		var bodyObj = new this.body();
		for (v in bodyObj) {
			result[v] = bodyObj[v];
		}

		result.constructor.apply(result, arguments);
		return result;
	};

	/**
	 * 
	 * @param script
	 *            Java script define of the new class.
	 * @returns An JOO class.
	 */
	this.Class = function(script) {
		// Create a new class definition.
		function klass() {
			return JCLASS.Constructor.apply(klass, arguments);
		}
		var body = script.body ? script.body : function() {
		};
		var constructors = script.constructors ? script.constructors
				: [ function() {
				} ];
		var Extends = script.Extends ? script.Extends : JOBJECT.Extends;
		klass.prototype = new JCLASS();
		klass.Extends = Extends;
		klass.constructors = constructors;
		klass.body = body;
		return klass;
	};
};

var JOO = new __JOO_CLASS__();

var ParentClass = JOO.Class({
	constructors : [ function(a, b) {
		print("constructor(a£¬b)");
		this.supper();
		this.a = a;
		this.b = b;
	}, function(a) {
		print("constructor(a)");
		this.a = a;
	} ],
	body : function() {
		function privateEcho() {
			print("echo some");
		}
		this.echo = function() {
			privateEcho();
			this.b = "adddfeddd";
		};
		this.toString = function() {
			return "{a: " + this.a + ", b:" + this.b + " }";
		};
	}
});

/**
 * The following codes is just for testing.
 */

var parentObj = new ParentClass("aaaa", "v");
parentObj.echo();
print(parentObj.toString());
print("public field a: " + parentObj.a);
print("private field b: " + parentObj.b);
quit();