package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.util.Log;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;

public class TileOverlayOperator extends AbstractTabrisOperator<TileOverlay> {

  private static final String WIDGET_TYPE = "tabris.maps.tileoverlay";
  private static final String EVENT_TAP = "tap";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final TileOverlayPropertyHandler tileOverlayPropertyHandler;
  private String mapId;

  public TileOverlayOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    tileOverlayPropertyHandler = new TileOverlayPropertyHandler( activity, tabrisContext );
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public TileOverlay create( Properties properties ) {
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "tileUrlFormat" ) ) {
      mapId = properties.getString( PROP_PARENT );

      MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
      if( mapHolderView != null ) {
          Double properties.getDouble( "opacity" );
          String tileUrlFormat = properties.getString("tileUrlFormat");
          Integer tileSize = properties.getInteger("tileSize");
    
          PluginTileProvider tileProvider = new PluginTileProvider(tileUrlFormat, opacity, tileSize);
          tileOverlayPropertyHandler.setProvider(tileProvider);
      
          TileOverlayOptions options = new TileOverlayOptions();
          options.tileProvider(tileProvider);      
          return mapHolderView.getGoogleMap().addTileOverlay( options );
      } else {
          throw new RuntimeException( "Can not find parent map for tileoverlay with id: " + mapId );
      }      
      
      
      

      return null;
    } else {
      throw new RuntimeException( "Invalid TileOverlay properties: " + properties );
    }
  }

  @Override
  public void destroy( TileOverlay tileOverlay ) {
    Log.d( LOG_TAG, String.format( "Removing tileoverlay: %s (id: %s)", marker, marker.getId() ) );
    tileOverlay.remove();
  }



  public GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    MapValidator.validateGoogleMap( googleMap,
        "TileOverlay can only be used after the Map's 'ready' event has been fired." );
    return googleMap;
  }

  @Override
  public Object get( TileOverlay tileOverlay, String property ) {
    return null;
  }

  @Override
  public void set( TileOverlay tileOverlay, Properties properties ) {
    // do nothing
  }


  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
