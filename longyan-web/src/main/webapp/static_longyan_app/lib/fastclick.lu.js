/**
 * @preserve FastClick
 * @copyright The Financial Times Limited [All Rights Reserved]
 * @license MIT License (see LICENSE.txt)
 */
(function() {
	function e(o, n) {
		var p;
		n = n || {};
		this.trackingClick = false;
		this.trackingClickStart = 0;
		this.targetElement = null;
		this.touchStartX = 0;
		this.touchStartY = 0;
		this.lastTouchIdentifier = 0;
		this.touchBoundary = n.touchBoundary || 10;
		this.layer = o;
		this.tapDelay = n.tapDelay || 200;
		this.tapTimeout = n.tapTimeout || 700;
		if (e.notNeeded(o)) {
			return
		}

		function q(l, i) {
			return function() {
				return l.apply(i, arguments)
			}
		}
		var j = ["onMouse", "onClick", "onTouchStart", "onTouchMove", "onTouchEnd", "onTouchCancel"];
		var m = this;
		for (var k = 0, h = j.length; k < h; k++) {
			m[j[k]] = q(m[j[k]], m)
		}
		if (d) {
			o.addEventListener("mouseover", this.onMouse, true);
			o.addEventListener("mousedown", this.onMouse, true);
			o.addEventListener("mouseup", this.onMouse, true)
		}
		o.addEventListener("click", this.onClick, true);
		o.addEventListener("touchstart", this.onTouchStart, false);
		o.addEventListener("touchmove", this.onTouchMove, false);
		o.addEventListener("touchend", this.onTouchEnd, false);
		o.addEventListener("touchcancel", this.onTouchCancel, false);
		if (!Event.prototype.stopImmediatePropagation) {
			o.removeEventListener = function(l, s, i) {
				var r = Node.prototype.removeEventListener;
				if (l === "click") {
					r.call(o, l, s.hijacked || s, i)
				} else {
					r.call(o, l, s, i)
				}
			};
			o.addEventListener = function(r, s, l) {
				var i = Node.prototype.addEventListener;
				if (r === "click") {
					i.call(o, r, s.hijacked || (s.hijacked = function(t) {
						if (!t.propagationStopped) {
							s(t)
						}
					}), l)
				} else {
					i.call(o, r, s, l)
				}
			}
		}
		if (typeof o.onclick === "function") {
			p = o.onclick;
			o.addEventListener("click", function(i) {
				p(i)
			}, false);
			o.onclick = null
		}
	}
	var b = navigator.userAgent.indexOf("Windows Phone") >= 0;
	var d = navigator.userAgent.indexOf("Android") > 0 && !b;
	var f = /iP(ad|hone|od)/.test(navigator.userAgent) && !b;
	var a = f && (/OS 4_\d(_\d)?/).test(navigator.userAgent);
	var g = f && (/OS [6-7]_\d/).test(navigator.userAgent);
	var c = navigator.userAgent.indexOf("BB10") > 0;
	e.prototype.needsClick = function(h) {
		switch (h.nodeName.toLowerCase()) {
			case "button":
			case "select":
			case "textarea":
				if (h.disabled) {
					return true
				}
				break;
			case "input":
				if ((f && h.type === "file") || h.disabled) {
					return true
				}
				break;
			case "label":
			case "iframe":
			case "video":
				return true
		}
		return (/\bneedsclick\b/).test(h.className)
	};
	e.prototype.needsFocus = function(h) {
		switch (h.nodeName.toLowerCase()) {
			case "textarea":
				return true;
			case "select":
				return !d;
			case "input":
				switch (h.type) {
					case "button":
					case "checkbox":
					case "file":
					case "image":
					case "radio":
					case "submit":
						return false
				}
				return !h.disabled && !h.readOnly;
			default:
				return (/\bneedsfocus\b/).test(h.className)
		}
	};
	e.prototype.sendClick = function(h, i) {
		var j, k;
		if (document.activeElement && document.activeElement !== h) {
			document.activeElement.blur()
		}
		k = i.changedTouches[0];
		j = document.createEvent("MouseEvents");
		j.initMouseEvent(this.determineEventType(h), true, true, window, 1, k.screenX, k.screenY, k.clientX, k.clientY, false, false, false, false, 0, null);
		j.forwardedTouchEvent = true;
		h.dispatchEvent(j)
	};
	e.prototype.determineEventType = function(h) {
		if (d && h.tagName.toLowerCase() === "select") {
			return "mousedown"
		}
		return "click"
	};
	e.prototype.focus = function(i) {
		var h;
		if (f && i.setSelectionRange && i.type.indexOf("date") !== 0 && i.type !== "time" && i.type !== "month") {
			h = i.value.length;
			i.setSelectionRange(h, h)
		} else {
			i.focus()
		}
	};
	e.prototype.updateScrollParent = function(h) {
		var j, i;
		j = h.fastClickScrollParent;
		if (!j || !j.contains(h)) {
			i = h;
			do {
				if (i.scrollHeight > i.offsetHeight) {
					j = i;
					h.fastClickScrollParent = i;
					break
				}
				i = i.parentElement
			} while (i)
		}
		if (j) {
			j.fastClickLastScrollTop = j.scrollTop
		}
	};
	e.prototype.getTargetElementFromEventTarget = function(h) {
		if (h.nodeType === Node.TEXT_NODE) {
			return h.parentNode
		}
		return h
	};
	e.prototype.onTouchStart = function(j) {
		var i, k, h;
		if (j.targetTouches.length > 1) {
			return true
		}
		i = this.getTargetElementFromEventTarget(j.target);
		k = j.targetTouches[0];
		if (f) {
			h = window.getSelection();
			if (h.rangeCount && !h.isCollapsed) {
				return true
			}
			if (!a) {
				if (k.identifier && k.identifier === this.lastTouchIdentifier) {
					j.preventDefault();
					return false
				}
				this.lastTouchIdentifier = k.identifier;
				this.updateScrollParent(i)
			}
		}
		this.trackingClick = true;
		this.trackingClickStart = j.timeStamp;
		this.targetElement = i;
		this.touchStartX = k.pageX;
		this.touchStartY = k.pageY;
		if ((j.timeStamp - this.lastClickTime) < this.tapDelay) {
			j.preventDefault()
		}
		return true
	};
	e.prototype.touchHasMoved = function(h) {
		var j = h.changedTouches[0],
			i = this.touchBoundary;
		if (Math.abs(j.pageX - this.touchStartX) > i || Math.abs(j.pageY - this.touchStartY) > i) {
			return true
		}
		return false
	};
	e.prototype.onTouchMove = function(h) {
		if (!this.trackingClick) {
			return true
		}
		if (this.targetElement !== this.getTargetElementFromEventTarget(h.target) || this.touchHasMoved(h)) {
			this.trackingClick = false;
			this.targetElement = null
		}
		return true
	};
	e.prototype.findControl = function(h) {
		if (h.control !== undefined) {
			return h.control
		}
		if (h.htmlFor) {
			return document.getElementById(h.htmlFor)
		}
		return h.querySelector("button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea")
	};
	e.prototype.onTouchEnd = function(j) {
		var m, l, i, k, n, h = this.targetElement;
		if (!this.trackingClick) {
			return true
		}
		if ((j.timeStamp - this.lastClickTime) < this.tapDelay) {
			this.cancelNextClick = true;
			return true
		}
		if ((j.timeStamp - this.trackingClickStart) > this.tapTimeout) {
			return true
		}
		this.cancelNextClick = false;
		this.lastClickTime = j.timeStamp;
		l = this.trackingClickStart;
		this.trackingClick = false;
		this.trackingClickStart = 0;
		if (g) {
			n = j.changedTouches[0];
			h = document.elementFromPoint(n.pageX - window.pageXOffset, n.pageY - window.pageYOffset) || h;
			h.fastClickScrollParent = this.targetElement.fastClickScrollParent
		}
		i = h.tagName.toLowerCase();
		if (i === "label") {
			m = this.findControl(h);
			if (m) {
				this.focus(h);
				if (d) {
					return false
				}
				h = m
			}
		} else {
			if (this.needsFocus(h)) {
				if ((j.timeStamp - l) > 100 || (f && window.top !== window && i === "input")) {
					this.targetElement = null;
					return false
				}
				this.focus(h);
				this.sendClick(h, j);
				if (!f || i !== "select") {
					this.targetElement = null;
					j.preventDefault()
				}
				return false
			}
		}
		if (f && !a) {
			k = h.fastClickScrollParent;
			if (k && k.fastClickLastScrollTop !== k.scrollTop) {
				return true
			}
		}
		if (!this.needsClick(h)) {
			j.preventDefault();
			this.sendClick(h, j)
		}
		return false
	};
	e.prototype.onTouchCancel = function() {
		this.trackingClick = false;
		this.targetElement = null
	};
	e.prototype.onMouse = function(h) {
		if (!this.targetElement) {
			return true
		}
		if (h.forwardedTouchEvent) {
			return true
		}
		if (!h.cancelable) {
			return true
		}
		if (!this.needsClick(this.targetElement) || this.cancelNextClick) {
			if (h.stopImmediatePropagation) {
				h.stopImmediatePropagation()
			} else {
				h.propagationStopped = true
			}
			h.stopPropagation();
			h.preventDefault();
			return false
		}
		return true
	};
	e.prototype.onClick = function(h) {
		var i;
		if (this.trackingClick) {
			this.targetElement = null;
			this.trackingClick = false;
			return true
		}
		if (h.target.type === "submit" && h.detail === 0) {
			return true
		}
		i = this.onMouse(h);
		if (!i) {
			this.targetElement = null
		}
		return i
	};
	e.prototype.destroy = function() {
		var h = this.layer;
		if (d) {
			h.removeEventListener("mouseover", this.onMouse, true);
			h.removeEventListener("mousedown", this.onMouse, true);
			h.removeEventListener("mouseup", this.onMouse, true)
		}
		h.removeEventListener("click", this.onClick, true);
		h.removeEventListener("touchstart", this.onTouchStart, false);
		h.removeEventListener("touchmove", this.onTouchMove, false);
		h.removeEventListener("touchend", this.onTouchEnd, false);
		h.removeEventListener("touchcancel", this.onTouchCancel, false)
	};
	e.notNeeded = function(j) {
		var h;
		var l;
		var k;
		var i;
		if (typeof window.ontouchstart === "undefined") {
			return true
		}
		l = +(/Chrome\/([0-9]+)/.exec(navigator.userAgent) || [, 0])[1];
		if (l) {
			if (d) {
				h = document.querySelector("meta[name=viewport]");
				if (h) {
					if (h.content.indexOf("user-scalable=no") !== -1) {
						return true
					}
					if (l > 31 && document.documentElement.scrollWidth <= window.outerWidth) {
						return true
					}
				}
			} else {
				return true
			}
		}
		if (c) {
			k = navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/);
			if (k[1] >= 10 && k[2] >= 3) {
				h = document.querySelector("meta[name=viewport]");
				if (h) {
					if (h.content.indexOf("user-scalable=no") !== -1) {
						return true
					}
					if (document.documentElement.scrollWidth <= window.outerWidth) {
						return true
					}
				}
			}
		}
		if (j.style.msTouchAction === "none" || j.style.touchAction === "manipulation") {
			return true
		}
		i = +(/Firefox\/([0-9]+)/.exec(navigator.userAgent) || [, 0])[1];
		if (i >= 27) {
			h = document.querySelector("meta[name=viewport]");
			if (h && (h.content.indexOf("user-scalable=no") !== -1 || document.documentElement.scrollWidth <= window.outerWidth)) {
				return true
			}
		}
		if (j.style.touchAction === "none" || j.style.touchAction === "manipulation") {
			return true
		}
		return false
	};
	e.attach = function(i, h) {
		return new e(i, h)
	};
	if (typeof define === "function" && typeof define.amd === "object" && define.amd) {
		define(function() {
			return e
		})
	} else {
		if (typeof module !== "undefined" && module.exports) {
			module.exports = e.attach;
			module.exports.FastClick = e
		} else {
			window.FastClick = e
		}
	}
}());