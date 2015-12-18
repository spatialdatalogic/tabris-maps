tabris.registerWidget("_ESMarker", {
  _type: "tabris.maps.marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    image: {type: "image", nocache: true},
    color: {type: "color", nocache: true},
    infoWindowVisible: {type: "boolean", nocache: true},
    visible: {type: "boolean", nocache: true},
    anchor: {type: "array", nocache: true},
    infoWindowAnchor: {type: "array", nocache: true},
    opacity: {type: "number", nocache: true},
    rotation: {type: "number", nocache: true},
    flat: {type: "boolean", nocache: true},
    title: {type: "string", nocache: true},
    snippet: {type: "string", nocache: true}
  },
  _events: {tap: true}
});
