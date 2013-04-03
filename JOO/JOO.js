/**
 * An javascript object oriented library.
 * 
 * @author Nick Zhang
 */

/**
 * TODO: comments later.
 */
var __JOO_CLASS__ = function() {
	var __hashCode__ = 0;
	/**
	 * Basic Object of JOO.
	 */
	var JOBJECT = function() {
		var hashCode = __hashCode__++;
		this.toString = function() {
			return "@" + hashCode;
		};
		this.hashCode = function() {
			return hashCode;
		};
		this.getClass = function() {
			return this.Class;
		}
	};
	// Extends nothing.
	JOBJECT.Extends = function() {

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
	JCLASS.Constructor = function(parameters) {
		var paramCount = parameters.length;
		var matchConstructor = this.constructors[0]
		for (constructor in this.constructors) {
			if (constructor.length == paramCount) {
				matchConstructor = constructor;
				break;
			}
		}
		var result = {};
		result.Class = this;
		result.constructor = matchConstructor;
		result.supper = this.Extends;
		result.constructor.prototype = new JOBJECT();
		var bodyObj = new this.body();
		for (v in bodyObj) {
			result[v] = bodyObj[v];
		}
		result.constructor.call(result, parameters);
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
			return JCLASS.Constructor.call(klass, arguments);
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
	// Define JOBJECT class itself.
	JOBJECT.Class = this.Class({

	});
};

var JOO = new __JOO_CLASS__();

var TestClass = JOO.Class({
	constructors : [ function(a, b) {
		this.a = a;
		_b = b;
	} ],
	body : function() {
		function privateEcho() {
			print("echo some");
		}
		this.echo = function() {
			privateEcho();
			this.b = "adddfeddd";
		};
	}
});

var testObject = new TestClass("aaaa", "bddd");
testObject.echo();

print("public field a: " + testObject.a);
print("private field b: " + testObject.b);
quit();