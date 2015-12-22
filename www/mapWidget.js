tabris.registerWidget("ESMap", {
  _type: "tabris.Map",
  _supportsChildren: true,
  _properties: {
    liteMode: {type: "boolean", default: false},
    center: {type: "array", nocache: true},
    zoom: {type: "any", nocache: true},
    minZoomLevel: {type: "any", nocache: true},
    maxZoomLevel: {type: "any", nocache: true},
    region: {type: "array", nocache: true},
    mapType: {type: ["choice", ["none", "hybrid", "normal", "satellite", "terrain", "satelliteflyover", "hybridflyover"]], nocache: true},
    myLocationEnabled: {type: "boolean", nocache: true},
    trafficEnabled: {type: "boolean", nocache: true},
    indoorEnabled: {type: "boolean", nocache: true},
    compassEnabled: {type: "boolean", nocache: true}
  },
  _events: {
    tap: {
      trigger: function(event) {this.trigger("tap", this, event.latLng);}
    },
    longpress: {
      trigger: function(event) {this.trigger("longpress", this, event.latLng);}
    },
    ready: {
      trigger: function() {this.trigger("ready", this);}
    },
    pan: {
      trigger: function(event) {this.trigger("pan", this, event.latLng);}
    }
  },
  animateCamera: function(centerLatLng, zoom) {
    // TODO check types
    this._nativeCall("animateCamera", {
      "centerLatLng": centerLatLng,
      "zoom": zoom
    });
  },
  animateCameraToBounds: function(northWestLatLng, southEastLatLng) {
    // TODO check types
    this._nativeCall("animateCameraToBounds", {
      "northWestLatLng": northWestLatLng,
      "southEastLatLng": southEastLatLng
    });
  },
  animateCameraToPointGroup: function(latLngPointGroup) {
    // TODO check types
    this._nativeCall("animateCameraToPointGroup", {
      "latLngPointGroup": latLngPointGroup
    });
  },  
  setPadding: function(left, top, right, bottom) {
    this._nativeCall("setPadding", {
      "left": left,
      "top": top,
      "right": right,
      "bottom": bottom
    });
  },
  createMarker: function(mapOptions) {
    return tabris.create("_ESMarker", mapOptions).appendTo(this);
  },
  createTileOverlay: function(mapOptions) {
    return tabris.create("_ESTileOverlay", mapOptions).appendTo(this);
  }
});
