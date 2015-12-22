package com.eclipsesource.tabris.maps;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.graphics.Color;
import android.content.res.Resources;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisWidgetPropertyHandler;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;



public class PolygonPropertyHandler<T extends Polygon> implements TabrisPropertyHandler<T> {

  private final Activity activity;
  private final TabrisContext context;
  public float density = Resources.getSystem().getDisplayMetrics().density;

  public PolygonPropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
  }



  @Override
  public Object get( T polygon, String property ) {
    System.out.println( "get on " + polygon + ": " + property );
    Object retVal = null;
    switch (property) {


        case "width":
            retVal = polygon.getWidth() / this.density;
            break;
            
        case "zindex":
            retVal = polygon.getZIndex();
            break;
            
        case "color":
            retVal = polygon.getColor();
            break;            
            
        case "geodesic":
            retVal = polygon.isGeodesic();
            break;

        case "clickable":
            retVal = polygon.isClickable();
            break;

        case "visible":
            retVal = polygon.isVisible();
            break;

    }
    return retVal;
  }

  @Override
  public void set( T polygon, Properties properties ) {
      System.out.println( "set: " + properties );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, polygon, properties );
      }
  }




  
  private void setProperty( String key, T polygon, Properties properties ) {
      System.out.println( "set: " + key );
      switch( key ) {
        case "color":
            float[] hsv = new float[3];
            List<Integer> arrayRGBA = properties.getList( "color", Integer.class );
            Color.RGBToHSV(arrayRGBA.get(0), arrayRGBA.get(1), arrayRGBA.get(2), hsv);          
            float hue = hsv[0];
            if (hue <= 0){
              hue = 1.0f;  //gray, gmaps won't use hue value of 0
            }
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
            break;

        case "strokeWidth":
            float width = properties.getFloat( "width" ) * this.density;
            polygon.setStrokeWidth(width);
            break;
            
            
        case "infoWindowVisible":
            Boolean infoWindowVisible = properties.getBoolean( "infoWindowVisible" );
            if (infoWindowVisible) {
                marker.showInfoWindow();
            } else {
                marker.hideInfoWindow();
            }
            break; 
            
        case "anchor":
            List<Float> anchor = properties.getList( "anchor", Float.class );
            marker.setAnchor(anchor.get(0), anchor.get(1));
            break;

        case "infoWindowAnchor":
            List<Float> infoWindowAnchor = properties.getList( "infoWindowAnchor", Float.class );
            marker.setInfoWindowAnchor(infoWindowAnchor.get(0), infoWindowAnchor.get(1));
            break;            
            
        case "opacity":
            marker.setAlpha(properties.getFloat( "opacity" ));
            break;
            
        case "rotation":
            marker.setRotation(properties.getFloat( "rotation" ));
            break;
            
        case "flat":
            Boolean flat = properties.getBoolean( "flat" );
            marker.setFlat(flat);
            break;  
            
        case "visible":
            marker.setVisible(properties.getBoolean( "visible" ));
            break;  
            
        case "title":
            marker.setTitle(properties.getString( "title" ));
            break;      
            
        case "snippet":
            marker.setSnippet(properties.getString( "snippet" ));
            break;              
      }
  }  
  
}
