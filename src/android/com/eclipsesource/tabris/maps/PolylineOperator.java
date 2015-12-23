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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;

public class PolylineOperator extends AbstractTabrisOperator<Polyline> {

  private static final String WIDGET_TYPE = "tabris.maps.polyline";
  private static final String EVENT_TAP = "tap";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final PolylinePropertyHandler propertyHandler;
  private String mapId;
  

  public PolylineOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    propertyHandler = new PolylinePropertyHandler( activity, tabrisContext );
  }


   @Override
   public TabrisPropertyHandler<Polyline> getPropertyHandler() {
     return propertyHandler;
   }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public Polyline create( Properties properties ) {
    Log.d( LOG_TAG, String.format( "Creating new polyline. %s", properties ) );
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "geometry" ) ) {
      mapId = properties.getString( PROP_PARENT );
      PolylineOptions options = PluginUtil.GeoJsonToPolyline(properties.getString( "geometry"));
      if (options == null ) {
        throw new RuntimeException( "Invalid Polyline properties: " + properties );
      }
      
      if (properties.hasProperty("color")) {
        options.color(PluginUtil.TabrisColorToColor(properties.getList( "color", Integer.class)));
      }
      if (properties.hasProperty("width")) {
        options.width(properties.getFloat( "width") * PluginUtil.density);
      }
      
      MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
      return mapHolderView.getGoogleMap().addPolyline( options );      
    } else {
      throw new RuntimeException( "Invalid Polyline properties: " + properties );
    }
  }

  @Override
  public void destroy( Polyline polyline ) {
    Log.d( LOG_TAG, String.format( "Removing polyline: %s (id: %s)", polyline, polyline.getId() ) );
    polyline.remove();
  }

  @Override
  public void listen( final Polyline polyline, String event, boolean listen ) {
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
    /*
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    Log.d( LOG_TAG, String.format( "Attaching listener to: %s", googleMap ) );
    googleMap.setOnPolylineClickListener( new GoogleMap.OnPolylineClickListener() {
      @Override
      public boolean onPolylineClick( Polyline polyline ) {
        Log.d( LOG_TAG, String.format( "Polyline %s with id %s was tapped.", polyline, polyline.getId()
        ) );
        getObjectRegistry().getRemoteObjectForObject( polyline ).notify( EVENT_TAP );
        return true;
      }
    } );
    */
  }

  private void removeOnTapListener() {
    /*
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnPolylineClickListener( null );
    */
  }

  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
