package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.Marker;

public class MarkerPropertyHandler<T extends Marker> implements IPropertyHandler<T> {

  private final Activity activity;
  private final TabrisContext context;

  public MarkerPropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
  }

  @Override
  public Object get( T marker, String property ) {
    System.out.println( "get on " + marker + ": " + property );
    return null;
  }

  @Override
  public void set( T marker, Properties properties ) {
      System.out.println( "set: " + properties );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, marker, properties );
      }
  }
  
  private void setProperty( String key, T marker, Properties properties ) {
      switch( key ) {
        case "color":
            float[] hsv = new float[3];
            List<Integer> arrayRGBA = properties.getList( "color", Integer.class );
            Color.RGBToHSV(arrayRGBA.get(0), arrayRGBA.get(1), arrayRGBA.get(2), hsv);          
            float hue = hsv[0];
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
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
