
/**
 * Namespace: Util
 */
UAPPassport.Util = {};

/** 
 * Function: getElement
 * This is the old $() from prototype
 */
UAPPassport.Util.getElement = function() {
    var elements = [];

    for (var i=0, len=arguments.length; i<len; i++) {
        var element = arguments[i];
        if (typeof element == 'string' || typeof element == "number") {
        	if (element) {
            	element = document.getElementById(element);
            } else {
            	element = "";
            }
        }
        if (arguments.length == 1) {
            return element;
        }
        elements.push(element);
    }
    return elements;
};



/**
 * APIFunction: extend
 * Copy all properties of a source object to a destination object.  Modifies
 *     the passed in destination object.  Any properties on the source object
 *     that are set to undefined will not be (re)set on the destination object.
 *
 * Parameters:
 * destination - {Object} The object that will be modified
 * source - {Object} The object with properties to be set on the destination
 *
 * Returns:
 * {Object} The destination object.
 */
UAPPassport.Util.extend = function(destination, source) {
    destination = destination || {};
    if(source) {
        for(var property in source) {
            var value = source[property];
            if(value !== undefined) {
                destination[property] = value;
            }
        }

        /**
         * IE doesn't include the toString property when iterating over an object's
         * properties with the for(property in object) syntax.  Explicitly check if
         * the source has its own toString property.
         */

        /*
         * FF/Windows < 2.0.0.13 reports "Illegal operation on WrappedNative
         * prototype object" when calling hawOwnProperty if the source object
         * is an instance of window.Event.
         */

        var sourceIsEvt = typeof window.Event == "function"
                          && source instanceof window.Event;

        if(!sourceIsEvt
           && source.hasOwnProperty && source.hasOwnProperty('toString')) {
            destination.toString = source.toString;
        }
    }
    return destination;
};

/**
 * Constructor: Mapabc.Class
 * Base class used to construct all other classes. Includes support for 
 *     multiple inheritance. 
 *     
 * This constructor is new in Mapabc 2.5.  At Mapabc 3.0, the old
 *     syntax for creating classes and dealing with inheritance 
 *     will be removed.
 * 
 * To create a new Mapabc-style class, use the following syntax:
 * > var MyClass = Mapabc.Class(prototype);
 *
 * To create a new Mapabc-style class with multiple inheritance, use the
 *     following syntax:
 * > var MyClass = Mapabc.Class(Class1, Class2, prototype);
 * Note that instanceof reflection will only reveil Class1 as superclass.
 * Class2 ff are mixins.
 *
 */
UAPPassport.Class = function() {
    var Class = function() {
        /**
         * This following condition can be removed at 3.0 - this is only for
         * backwards compatibility while the Class.inherit method is still
         * in use.  So at 3.0, the following three lines would be replaced with
         * simply:
         * this.initialize.apply(this, arguments);
         */
        if (arguments && arguments[0] != UAPPassport.Class.isPrototype) {
            this.initialize.apply(this, arguments);
        }
    };
    var extended = {};
    var parent, initialize;
    for(var i=0, len=arguments.length; i<len; ++i) {
        if(typeof arguments[i] == "function") {
            // make the class passed as the first argument the superclass
            if(i == 0 && len > 1) {
                initialize = arguments[i].prototype.initialize;
                // replace the initialize method with an empty function,
                // because we do not want to create a real instance here
                arguments[i].prototype.initialize = function() {};
                // the line below makes sure that the new class has a
                // superclass
                extended = new arguments[i];
                // restore the original initialize method
                if(initialize === undefined) {
                    delete arguments[i].prototype.initialize;
                } else {
                    arguments[i].prototype.initialize = initialize;
                }
            }
            // get the prototype of the superclass
            parent = arguments[i].prototype;
        } else {
            // in this case we're extending with the prototype
            parent = arguments[i];
        }
        UAPPassport.Util.extend(extended, parent);
    }
    Class.prototype = extended;
    return Class;
};

/**
 * Property: isPrototype
 * *Deprecated*.  This is no longer needed and will be removed at 3.0.
 */
UAPPassport.Class.isPrototype = function () {};

/**
 * APIFunction: Mapabc.create
 * *Deprecated*.  Old method to create an Mapabc style class.  Use the
 *     <Mapabc.Class> constructor instead.
 *
 * Returns:
 * An Mapabc class
 */
UAPPassport.Class.create = function() {
    return function() {
        if (arguments && arguments[0] != Mapabc.Class.isPrototype) {
            this.initialize.apply(this, arguments);
        }
    };
};


/**
 * APIFunction: inherit
 * *Deprecated*.  Old method to inherit from one or more Mapabc style
 *     classes.  Use the <Mapabc.Class> constructor instead.
 *
 * Parameters:
 * class - One or more classes can be provided as arguments
 *
 * Returns:
 * An object prototype
 */
UAPPassport.Class.inherit = function () {
    var superClass = arguments[0];
    var proto = new superClass(Mapabc.Class.isPrototype);
    for (var i=1, len=arguments.length; i<len; i++) {
        if (typeof arguments[i] == "function") {
            var mixin = arguments[i];
            arguments[i] = new mixin(Mapabc.Class.isPrototype);
        }
        UAPPassport.Util.extend(proto, arguments[i]);
    }
    return proto;
};

