package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;

public class PolygonOperator extends AbstractTabrisOperator<Polygon> {

  private static final String WIDGET_TYPE = "tabris.maps.polygon";
  private static final String EVENT_TAP = "tap";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final PolygonPropertyHandler propertyHandler;
  private String mapId;
  

  public PolygonOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    propertyHandler = new PolygonPropertyHandler( activity, tabrisContext );
  }


   @Override
   public TabrisPropertyHandler<Polygon> getPropertyHandler() {
     return propertyHandler;
   }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public Polygon create( Properties properties ) {
    Log.d( LOG_TAG, String.format( "Creating new polygon. %s", properties ) );
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "geometry" ) ) {
      mapId = properties.getString( PROP_PARENT );
      
      PolygonOptions options = PluginUtil.GeoJsonToPolygon(properties.getString( "geometry", String.class ));
      
      if (properties.hasProperty("fillColor")) {
        options.fillColor(PluginUtil.TabrisColorToColor(properties.getList( "fillColor", Integer.class )));
      }
      if (properties.hasProperty("strokeColor")) {
        options.strokeColor(PluginUtil.TabrisColorToColor(properties.getList( "strokeColor", Integer.class )));
      }    
      if (properties.hasProperty("strokeWidth")) {
        options.strokeWidth(properties.getFloat( "strokeWidth", Float.class ) * PluginUtil.density);
      }
      
      MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
      return mapHolderView.getGoogleMap().addPolygon( options );      
    } else {
      throw new RuntimeException( "Invalid polygon properties: " + properties );
    }
  }

  @Override
  public void destroy( Polygon polygon ) {
    Log.d( LOG_TAG, String.format( "Removing polygon: %s (id: %s)", polygon, polygon.getId() ) );
    polygon.remove();
  }

  @Override
  public void listen( final Polygon polygon, String event, boolean listen ) {
    switch( event ) {
      case EVENT_TAP:
        if( listen ) {
          attachOnTapListener();
        } else {
          removeOnTapListener();
        }
        break;
    }
  }

  public GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    MapValidator.validateGoogleMap( googleMap,
        "Widgets can only be used after the Map's 'ready' event has been fired." );
    return googleMap;
  }

  

  private void attachOnTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    Log.d( LOG_TAG, String.format( "Attaching listener to: %s", googleMap ) );
    googleMap.setOnPolygonClickListener( new GoogleMap.OnPolygonClickListener() {
      @Override
      public boolean onPolygonClick( Polygon polygon ) {
        Log.d( LOG_TAG, String.format( "Polygon %s with id %s was tapped.", polygon, polygon.getId()
        ) );
        getObjectRegistry().getRemoteObjectForObject( polygon ).notify( EVENT_TAP );
        return true;
      }
    } );
  }

  private void removeOnTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnPolygonClickListener( null );
  }

  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
