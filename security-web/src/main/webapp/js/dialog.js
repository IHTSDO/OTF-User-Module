/// <reference path="../jquery-1.4.4-vsdoc.js" />
/// <reference path="common.js" />

(function ($) {

	function DialogWindow() {
		// Create the position dialog handler
		this._positionDialogHandler = common.delegate(this._positionDialog, this)
		this._keyUpHandler = common.delegate(this._keyUp, this);
	}
	DialogWindow.prototype = {
		_background: null,
		_dialog: null,
		_positionDialogHandler: null,
		_keyUpHandler: null,
		_elementStates: null,
        _closeHandler: null,

		show: function (options) {
			var defaults = {
				backgroundCss: "dialog-background",
				dialogCss: "dialog-window",
				url: null,
				title: null,
				withClose: false,
                close: null
			};

			// Get the settings for the dialog
			var settings = $.extend(defaults, options);
            this._closeHandler = settings.close;

			// Create the background and add it to the document
			this._background = this._createBackground(settings.backgroundCss);

			// Create the dialog
			this._dialog = this._createDialog(settings.dialogCss);

			// If we have a title or a close button, add them to the 
			if (!String.isNullOrEmpty(settings.title) || settings.withClose == true) {
				var titleContainer = $(document.createElement("div")).addClass("title-bar");

				if (!String.isNullOrEmpty(settings.title)) {
					$(document.createElement("h2")).text(settings.title).appendTo(titleContainer);
				}

				if (settings.withClose == true) {
					$(document.createElement("input")).attr("type", "button").addClass("close-button")
					.click(common.delegate(function () { this.hide(); }, this))
					.appendTo(titleContainer)
				}

				titleContainer.appendTo(this._dialog);
			}

			var url = settings.url + ((settings.url.indexOf("?") == -1)
				? "?cachbuster=" + new Date().getTime()
				: "&cachbuster=" + new Date().getTime());

			// Create the dialog content container
			$.get(url, common.delegate(this._initialized, this));

			// Return a chaining reference
			return this;
		},
		hide: function (result) {
			// Restore the page elements before we hide our dialog
			this._restoreElements();

			// Unhook the event handler for the scroll and resize events
			$(window).unbind("scroll", this._positionDialogHandler);
			$(window).unbind("resize", this._positionDialogHandler);
			$(window).unbind("keyup", this._keyUpHandler);

			// Delete our dialog and background from the document
			this._dialog.remove();
			this._background.remove();

			// Clear the elements from memory
			this._dialog = null;
			this._background = null;

            if (this._closeHandler != null)
                this._closeHandler(result);
		},
		_initialized: function (data, statusText) {
			// Do not do anything if an error occurred
			if (String.compare(statusText, "error") == true)
				return;

			// Disable the page elements before we show our dialog
			this._disableElements();

			// Add the background and dialog to the document
			this._background.appendTo(document.body);
			this._dialog.appendTo(document.body);

			// Add the content to a DOM element
			var div = document.createElement("div");
			div.className = "window-content";
			div.innerHTML = data;
			this._dialog.append(div);

			// Position the dialog
			this._positionDialog();

			// Hookup an event handler to handle scroll and resize events
			$(window).bind("scroll", this._positionDialogHandler);
			$(window).bind("resize", this._positionDialogHandler);

			// Add the key up handler to close the window when the escape key is pressed
			$(window).bind("keyup", this._keyUpHandler);
		},
		_keyUp: function (eventArgs) {
			if (eventArgs.which == 27 /* ESC */)
				this.hide();
		},
		_positionDialog: function (eventArgs) {
			// Get our window bounds and scroll position
			var bounds = {
				width: window.innerWidth,
				height: window.innerHeight,
				scroll: {
					left: $(document).scrollLeft(),
					top: $(document).scrollTop(),
                    width: Math.max(document.documentElement.scrollWidth, document.body.scrollWidth),
                    height: Math.max(document.documentElement.scrollHeight, document.body.scrollHeight)
				}
			};

    		// Resize the background to fill the window
			this._background.width(Math.max(bounds.width, bounds.scroll.width))
                .height(Math.max(bounds.height, bounds.scroll.height))
                .css({left: 0, top: 0});

			// Position the dialog window in the centre of the window
            var left = (((bounds.width / 2) - (this._dialog.width() / 2)) + bounds.scroll.left);
            var top = (((bounds.height / 2) - (this._dialog.height() / 2)) + bounds.scroll.top);

			this._dialog.css("left", left < bounds.scroll.left ? bounds.scroll.left : left);
			this._dialog.css("top", top < bounds.scroll.top ? bounds.scroll.top : top);
		},
		_createDialog: function (dialogCss) {
			// Create a div to hold our dialog content
			return $(document.createElement("div"))
				.addClass(dialogCss)
				.css("position", "absolute").css("zIndex", 65535)
		},
		_createBackground: function (backgroundCss) {
			// Create a div to cover the page
			return $(document.createElement("div"))
				.addClass(backgroundCss)
				.css("position", "absolute").css("zIndex", 65534)
				.css("top", "0px").css("left", "0px")
				.width(window.innerWidth).height(window.innerHeight);
		},
		_disableElements: function () {
			var elementStates = [];
			var elementTags = ["a", "input", "select", "textarea", "iframe", "button", "area", "object"];

			elementTags.each(function (tag) {
				// Get the list of elements to be disabled and whose tab indices are to be 
				var elements = document.getElementsByTagName(tag);

				for (var index = 0; index < elements.length; index++) {
					// Save the element's state to our restoration array
					elementStates.push({
						element: elements[index],
						disabled: elements[index].disabled,
						tabIndex: elements[index].tabIndex,
						visibility: elements[index].style.visibility
					});

					// Disable the element and remove its tab index
					elements[index].disabled = true;
					elements[index].tabIndex = -1;

					if (String.compare(tag, "select") && $.browser.msie && parseFloat($.browser.version) < 7)
						elements[index].style.visibility = "hidden";
				}
			});

			// Save the element states to the class
			this._elementStates = elementStates;
		},
		_restoreElements: function () {
			// Ensure we have a list of element states
			if (this._elementStates == null)
				return;

			// Step through each element state and restore its state
			this._elementStates.each(function (state) {
				state.element.disabled = state.disabled;
				state.element.tabIndex = state.tabIndex;
				state.element.style.visibility = state.visibility;
			});
		}
	};

	var _dialog = null;
	$.dialog = function (options) {
		// Create our dialog class if it does not exist
		if (_dialog == null)
			_dialog = new DialogWindow();

		// Perform the requested operation (open or close)
		if (String.isNullOrEmpty(options.operation) || String.compare(options.operation, "open") == true)
			_dialog.show(options);
		else if (String.compare(options.operation, "close") == true)
			_dialog.hide(options.result);
	};
})(jQuery);
