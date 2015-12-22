tabris.registerWidget("_ESPolyline", {
  _type: "tabris.maps.polyline",
  _properties: {
    visible: {type: "boolean", nocache: true},
    clickable: {type: "boolean", nocache: true},
    geodesic: {type: "boolean", nocache: true},
    color: {type: "color", nocache: true},
    width: {type: "number", nocache: true},
    zindex: {type: "number", nocache: true},
  },
  _events: {tap: true},
  setGeometry: function(geojson) {
    this._nativeCall("setGeometry", {
      "geojson": geojson
    });  
  }
});
