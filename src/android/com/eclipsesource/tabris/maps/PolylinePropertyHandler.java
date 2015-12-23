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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;



public class PolylinePropertyHandler<T extends Polyline> implements TabrisPropertyHandler<T> {

  private final Activity activity;
  private final TabrisContext context;
  public float density = Resources.getSystem().getDisplayMetrics().density;

  public PolylinePropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
  }



  @Override
  public Object get( T polyline, String property ) {
    System.out.println( "get on " + polyline + ": " + property );
    Object retVal = null;
    switch (property) {


        case "width":
            retVal = polyline.getWidth() / this.density;
            break;
            
        case "zindex":
            retVal = polyline.getZIndex();
            break;
            
        case "color":
            retVal = polyline.getColor();
            break;        

        case "geodesic":
            retVal = polyline.isGeodesic();
            break;

        case "visible":
            retVal = polyline.isVisible();
            break;

    }
    return retVal;
  }

  @Override
  public void set( T polyline, Properties properties ) {
      System.out.println( "set: " + properties );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, polyline, properties );
      }
  }




  
  private void setProperty( String key, T polyline, Properties properties ) {
      System.out.println( "set: " + key );
      switch( key ) {
        case "color":
            polyline.setColor(PluginUtil.TabrisColorToColor(properties.getList( "color", Integer.class )));
            break;
            
            
        case "width":
            float width = properties.getFloat( "width" ) * this.density;
            polyline.setWidth(width);
            break;
            
            
        case "geodesic":
            polyline.setGeodesic(properties.getBoolean( "geodesic" ));
            break;  
            
        case "visible":
            polyline.setVisible(properties.getBoolean( "visible" ));
            break;  
            
        case "zindex":
            polyline.setZIndex(properties.getInteger( "zindex" ));
            break;          
      }
  }  
  
}
