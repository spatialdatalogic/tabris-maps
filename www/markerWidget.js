tabris.registerWidget("_ESMarker", {
  _type: "tabris.maps.marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    image: {type: "image", nocache: true},
    color: {type: "color", nocache: true}
  },
  _events: {tap: true}
});
