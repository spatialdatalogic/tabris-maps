package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileOverlayPropertyHandler<T extends Marker> implements IPropertyHandler<T> {

  private final Activity activity;
  private final TabrisContext context;
  private PluginTileProvider tileProvider;

  public TileOverlayPropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
  }

  public void setProvider(PluginTileProvider provider) {
      tileProvider = provider;
  }

  @Override
  public Object get( T tileOverlay, String property ) {
    System.out.println( "get on " + tileOverlay + ": " + property );
    return null;
  }

  @Override
  public void set( T tileOverlay, Properties properties ) {
      System.out.println( "set: " + properties );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, tileOverlay, properties );
      }
  }
  
  private void setProperty( String key, T tileOverlay, Properties properties ) {
      switch( key ) {

            
        case "opacity":
            tileProvider.setOpacity(properties.getFloat( "opacity" ));
            break;
            
        case "zindex":
            tileOverlay.setZIndex(properties.getInteger( "zindex" ));
            break;  
            
        case "visible":
            tileOverlay.setVisible(properties.getBoolean( "visible" ));
            break;  
            
          
      }
  }  
  
}
