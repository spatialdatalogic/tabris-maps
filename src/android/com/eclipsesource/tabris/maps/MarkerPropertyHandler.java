package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisWidgetPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerPropertyHandler extends TabrisWidgetPropertyHandler<Marker> {


  public MarkerPropertyHandler( Activity activity, TabrisContext context ) {
    super( activity, tabrisContext );
  }



  @Override
  public Object get( Marker marker, String property ) {
    System.out.println( "get on " + marker + ": " + property );
    Object retVal = null;
    switch (property) {


        case "opacity":
            retVal = marker.getAlpha();
            break;

        case "rotation":
            retVal = marker.getRotation();
            break;

        case "flat":
            retVal = marker.getFlat();
            break;

        case "visible":
            retVal = marker.getVisible();
            break;

        case "title":
            retVal = marker.getTitle();
            break;

        case "snippet":
            retVal = marker.getSnippet();
            break;
    }
    return retVal;
  }

  @Override
  public void set( Marker marker, Properties properties ) {
      System.out.println( "set: " + properties );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, marker, properties );
      }
  }




  
  private void setProperty( String key, T marker, Properties properties ) {
      System.out.println( "set: " + key );
      switch( key ) {
        case "color":
            float[] hsv = new float[3];
            List<Integer> arrayRGBA = properties.getList( "color", Integer.class );
            Color.RGBToHSV(arrayRGBA.get(0), arrayRGBA.get(1), arrayRGBA.get(2), hsv);          
            float hue = hsv[0];
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
            break;

        case "image":
            //[src, width, height, scale]
            //TODO


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
