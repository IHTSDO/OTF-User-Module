function common() {
}
common.delegate = function (method, scope, context) {
	/// <summary>Creates an event handler / delegate with a specific scope and context parameter</summary>
	/// <param name="method" type="function">The function to be called</param>
	/// <param name="scope" type="function">The scope with which to call the function</param>
	/// <param name="context" type="object">A context object to be passed as the first parameter to the function</param>
	return function () {
		var params = [];

		if (context != null)
			params.push(context);

		for (var index = 0; index < arguments.length; index++)
			params.push(arguments[index]);

		return method.apply((scope != null) ? scope : this, params);
	};
};

Array.prototype.ascending = function (selector) {
	/// <summary>Sorts the array in an ascending order</summary>
	/// <param name="selector" type="function">A function which selects a property from each item whose value is used when sorting</param>
	/// <returns type="boolean">A chaining reference</returns>

	this.sort(function (itemA, itemB) {
		// Retrieve the property values from each item to be compared
		var valueA = (selector != null && typeof (selector) == "function") ? selector(itemA) : itemA;
		var valueB = (selector != null && typeof (selector) == "function") ? selector(itemB) : itemB;

		// Compare the property values
		if (valueA < valueB)
			return -1;
		else if (valueA == valueB)
			return 0;
		else if (valueA > valueB)
			return 1;
	});

	return this;
};

Array.prototype.all = function (evaluation) {
	/// <summary>Determines if all of the items in the datasource evaluate to true for the specified evaluation function</summary>
	/// <param name="evaluation" type="function">The evaluation function to execute against each item in the data source</param>
	/// <returns type="Array">True if all of the items in the data source evaluate to true for the specified evaluation function</returns>

	// Ensure we have an evaluation function
	if (evaluation == null || typeof (evaluation) != "function")
		return false;

	for (var index = 0; index < this.length; index++) {
		if (evaluation(this[index]) == false)
			return false;
	}

	return true;
};

Array.prototype.any = function (evaluation) {
	/// <summary>Determines if any of the items in the datasource evaluate to true for the specified evaluation function</summary>
	/// <param name="evaluation" type="function">The evaluation function to execute against each item in the data source</param>
	/// <returns type="boolean">True if any of the items in the data source evaluate to true for the specified evaluation function</returns>

	// Ensure we have an evaluation function
	if (evaluation == null || typeof (evaluation) != "function")
		return false;

	for (var index = 0; index < this.length; index++) {
		if (evaluation(this[index]) == true)
			return true;
	}

	return false;
};

Array.prototype.descending = function (selector) {
	/// <summary>Sorts the array in a descending order</summary>
	/// <param name="selector" type="function">A function which selects a property from each item whose value is used when sorting</param>
	/// <returns type="Array">A chaining reference</returns>

	this.sort(function (itemA, itemB) {
		// Retrieve the property values from each item to be compared
		var valueA = (selector != null && typeof (selector) == "function") ? selector(itemA) : itemA;
		var valueB = (selector != null && typeof (selector) == "function") ? selector(itemB) : itemB;

		// Compare the property values
		if (valueA < valueB)
			return 1;
		else if (valueA == valueB)
			return 0;
		else if (valueA > valueB)
			return -1;
	});

	return this;
};

Array.prototype.each = function (operation) {
	/// <summary>Steps through each item in the array and performs the specified operation</summary>
	/// <param name="operation" type="function">The operation to be called for each item in the array</param>
	/// <returns type="Array">A chaining reference</returns>

	// Ensure we have been passed an operation
	if (operation == null || typeof (operation) != "function")
		return this;

	// Step through each item and perform the operation.  If the operation returns false, break
	for (var index = 0; index < this.length; index++) {
		if (operation(this[index], index) == false)
			break;
	}

	// Return a chaining reference
	return this;
};

Array.prototype.skip = function (count) {
	/// <summary>Gets a new array with the specified number of top items removed</summary>
	/// <param name="count" type="Integer">The number of top items to remove from the array</param>
	/// <returns type="Array">An array with the specified number of top items removed</returns>

	return this.slice(count, this.length);
};

Array.prototype.select = function (selector) {
	/// <summary>Gets a new array containing the result of executing the specified selector function on each item in the array</summary>
	/// <param name="selector" type="function">A function which selects a value from each item</param>
	/// <returns type="Array">A new array containing the result of executing the specified selector function on each item in the array</returns>

	// Declare a new array to be returned
	var result = [];

	// Step through each item and add the result of executing the selector against the item to our result array
	for (var index = 0; index < this.length; index++) {
		if (selector != null && typeof (selector) == "function")
			result.push(selector(this[index]));
		else
			result.push(this[index]);
	}

	// Return an array containing the result
	return result;
};

Array.prototype.single = function (evaluation) {
	/// <summary>Gets the first item for which the specified evaluation function returns true</summary>
	/// <param name="evaluation" type="function">The evaluation function to execute against each item in the data source</param>
	/// <returns type="object">The first item for which the evaluation function returns true otherwise null</returns>

	var result = null;

	// Ensure we have an evaluation function
	if (evaluation == null || typeof (evaluation) != "function")
		return result;

	// Step through each item and find the first item for which the evaluation function returns true
	for (var index = 0; index < this.length; index++) {
		if (evaluation(this[index]) == true)
			result = this[index];
	}

	// Return the result
	return result;
};

Array.prototype.take = function (count) {
	/// <summary>Gets a new array containing the specified number of top items</summary>
	/// <param name="count" type="Integer">The number of top items to take from the array</param>
	/// <returns type="Array">An array containing the specified number of top items</returns>

	return this.slice(0, count)
};

Array.prototype.where = function (evaluation) {
	/// <summary>Gets a new array with only those items for which the evaluation function returns true</summary>
	/// <param name="evaluation" type="function">The evaluation function to execute against each item in the data source</param>
	/// <returns type="Array">An array with only those items for which the evaluation function returned true</returns>

	// Declare a new array to be returned
	var result = [];

	// Ensure we have an evaluation function
	if (evaluation == null || typeof (evaluation) != "function")
		return result;

	// Step through each item and add all items to our result array for which the evaluation function returns true
	for (var index = 0; index < this.length; index++) {
		if (evaluation(this[index]) == true)
			result.push(this[index]);
	}

	// Return the array of matching items
	return result;
};

String.isNullOrEmpty = function (value) {
	/// <summary>Determines if the specified value is null or an empty string</summary>
	/// <param name="value" type="String">The value to be determined to be null or an empty string</param>
	/// <returns type="boolean">True if the value is null or an empty string otherwise false</returns>

	return value == null || value == "";
};

String.compare = function (stringA, stringB) {
	/// <summary>Performs a case insensitive comparison between stringA and stringB</summary>
	/// <param name="stringA">The string to be compared to stringB</param>
	/// <param name="stringB">The string to be compared to stringA</param>
	/// <returns type="boolean">True if the strings are case insensitively equal otherwise false</returns>

	if (stringA == null)
		return stringB == null;

	if (stringB == null)
		return false;

	return stringA.toLowerCase() == stringB.toLowerCase();
};

/* Asycnhronous event synchroniser */
common.asyncEventSynchroniser = function(handlers, invokeParameters) {
	this._handlers = handlers || [];
	this._invokeParameters = invokeParameters || [];
}
common.asyncEventSynchroniser.prototype = {
	_handlers: null,
	_invokeParameters: null,
	_completeHandler: null,
	_eventCount: 0,

	invoke: function (completeHandler) {
		/// <summary>Invokes the list of asynchronous event handlers</summary>
		/// <param name="completeHandler" type="Function">Handler that is called when all the event handlers have finished executing</param>

		// Save the function to be called when all event handlers have completed
		this._completeHandler = completeHandler;

		// If we do not have any event handlers and a complete handler, just call the complete handler
		if (this._handlers.length == 0 && this._completeHandler != null) {
			this._invokeParameters.splice(0, 1);
			this._completeHandler.apply(this, this._invokeParameters);
			return;
		}

		// Set this synchroniser object as the first parameter to the handlers
		this._invokeParameters.splice(0, 0, this);

		// Step through each event handler and call it
		this._handlers.each(common.delegate(function (item) { item.apply(null, this._invokeParameters); }, this));
	},

	complete: function () {
		/// <summary>Method that is called by each asychronous event handler to signify the event handler has finished</summary>

		// Increment the current event count
		this._eventCount++;

		// Check if all the event handlers have finished executing, if not wait for the next event handler to finish
		if (this._eventCount < this._handlers.length)
			return;

		// All event handlers have finished executing so notify our complete handler (if present)
		if (this._completeHandler != null) {
			this._invokeParameters.splice(0, 1);
			this._completeHandler.apply(this, this._invokeParameters);
		}
	}
};

/* Event Dispatch Table */
common.eventDispatchTable = function() {
}
common.eventDispatchTable.prototype = {
	_handlers: [],

	add: function (event, handler) {
		// Do not register the handler if it has been registered already
		if (this._handlers.any(function (item) { return item.event == event && item.handler == handler; }))
			return;

		// Add the handler to the list of handlers
		this._handlers.push({ event: event, handler: handler });
	},
	remove: function (event, handler) {
		// Remove the specified handler
		this._handlers = this._handlers.where(function (item) { return item.event != event || item.handler != handler; });
	},
	event: function (event) {
		var invokeParameters = [];

		// Copy any parameters passed to the invoke method ignoring the event parameter
		for (var index = 1; index < arguments.length; index++)
			invokeParameters.push(arguments[index]);

		// Get the list of event handlers to be called
		var handlers = this._handlers.where(function (item) { return item.event == event; }).select(function (item) { return item.handler; });

		// Create a new synchroniser to actually execute the event handlers
		return new common.asyncEventSynchroniser(handlers, invokeParameters);
	}
};