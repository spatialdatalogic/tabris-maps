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


        case "strokeWidth":
            retVal = polygon.getStrokeWidth() / this.density;
            break;
            
        case "zindex":
            retVal = polygon.getZIndex();
            break;
            
        case "fillColor":
            retVal = polygon.getFillColor();
            break;        
            
        case "strokeColor":
            retVal = polygon.getStrokeColor();
            break;    
            
        case "geodesic":
            retVal = polygon.isGeodesic();
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
        case "fillColor":
            polygon.setFillColor(PluginUtil.TabrisColorToColor(properties.getList( "fillColor", Integer.class )));
            break;
            
        case "strokeColor":
            polygon.setStrokeColor(PluginUtil.TabrisColorToColor(properties.getList( "strokeColor", Integer.class )));
            break;
            
        case "strokeWidth":
            float width = properties.getFloat( "width" ) * this.density;
            polygon.setStrokeWidth(width);
            break;
            
            
        case "geodesic":
            polygon.setGeodesic(properties.getBoolean( "geodesic" ));
            break;  
            
        case "visible":
            polygon.setVisible(properties.getBoolean( "visible" ));
            break;  
            
        case "zindex":
            polygon.setZIndex(properties.getInteger( "zindex" ));
            break;          
      }
  }  
  
}
