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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;

public class MarkerOperator extends AbstractTabrisOperator<Marker> {

  private static final String WIDGET_TYPE = "tabris.maps.marker";
  private static final String EVENT_TAP = "tap";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final MarkerPropertyHandler markerPropertyHandler;
  private String mapId;

  public MarkerOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    markerPropertyHandler = new MarkerPropertyHandler( activity, tabrisContext );
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public Marker create( Properties properties ) {
    Log.d( LOG_TAG, String.format( "Creating new marker. %s", properties ) );
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "latLng" ) ) {
      mapId = properties.getString( PROP_PARENT );
      Marker marker = createMarker( properties.getList( "latLng", Double.class ) );
      for( String key : properties.getAll().keySet() ) {
        setProperty( key, marker, properties );
      }
      Log.d( LOG_TAG, String.format( "New marker is: %s (id: %s)", marker, marker.getId() ) );
      return marker;
    } else {
      throw new RuntimeException( "Invalid marker properties: " + properties );
    }
  }

  @Override
  public void destroy( Marker marker ) {
    Log.d( LOG_TAG, String.format( "Removing marker: %s (id: %s)", marker, marker.getId() ) );
    marker.remove();
  }

  @Override
  public void listen( final Marker marker, String event, boolean listen ) {
    switch( event ) {
      case EVENT_TAP:
        if( listen ) {
          attachOnMarkerTapListener();
        } else {
          removeOnMarkerTapListener();
        }
        break;
    }
  }

  public GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    MapValidator.validateGoogleMap( googleMap,
        "Markers can only be used after the Map's 'ready' event has been fired." );
    return googleMap;
  }

  @Override
  public Object get( Marker marker, String property ) {
    System.out.println( "get on " + marker + ": " + property );
    Object retVal = null;
    switch (property) {
      case "title": 
        retVal = marker.getTitle();
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
  
  private void setProperty( String key, Marker marker, Properties properties ) {
      System.out.println( "set: " + key );
      switch( key ) {
        case "color":
            float[] hsv = new float[3];
            List<Integer> arrayRGBA = properties.getList( "color", Integer.class );
            Color.RGBToHSV(arrayRGBA.get(0), arrayRGBA.get(1), arrayRGBA.get(2), hsv);          
            float hue = hsv[0];
            System.out.println( "set hue: " + hue );
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
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

  private Marker createMarker( List<Double> latLng ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    if( mapHolderView != null ) {
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( new LatLng( latLng.get( 0 ), latLng.get( 1 ) ) );
      return mapHolderView.getGoogleMap().addMarker( markerOptions );
    } else {
      throw new RuntimeException( "Can not find parent map for marker with id: " + mapId );
    }
  }

  private void attachOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    Log.d( LOG_TAG, String.format( "Attaching listener to: %s", googleMap ) );
    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick( Marker marker ) {
        Log.d( LOG_TAG, String.format( "Marker %s with id %s was tapped.", marker, marker.getId()
        ) );
        getObjectRegistry().getRemoteObjectForObject( marker ).notify( EVENT_TAP );
        return true;
      }
    } );
  }

  private void removeOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMarkerClickListener( null );
  }

  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
