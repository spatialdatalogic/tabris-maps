tabris.registerWidget("_ESPolygon", {
  _type: "tabris.maps.polygon",
  _properties: {
    visible: {type: "boolean", nocache: true},
    clickable: {type: "boolean", nocache: true},
    geodesic: {type: "boolean", nocache: true},
    strokeColor: {type: "color", nocache: true},
    fillColor: {type: "color", nocache: true},
    strokeWidth: {type: "number", nocache: true},
    zindex: {type: "number", nocache: true},
  },
  _events: {tap: true},
  setGeometry: function(geojson) {
    this._nativeCall("setGeometry", {
      "geojson": geojson
    });  
  }
});
