package com.mousebirdconsulting.autotester.TestCases;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mousebird.maply.GlobeController;
import com.mousebird.maply.MapController;
import com.mousebird.maply.MaplyBaseController;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.SphericalMercatorCoordSystem;
import com.mousebird.maply.imagerypro.ImageSourceLayout;
import com.mousebird.maply.imagerypro.QuadImageTileLayer;
import com.mousebirdconsulting.autotester.ConfigOptions;
import com.mousebirdconsulting.autotester.Framework.MaplyTestCase;
import com.mousebirdconsulting.autotester.IndexTestTileSource;
import com.mousebirdconsulting.autotester.R;

/**
 * Test case for 8 bit Indexed data.
 * 8 bit slices with indexing.
 */
public class Index8BitStackTestCase extends MaplyTestCase
{
    public Index8BitStackTestCase(Activity activity) {
        super(activity);

        setTestName("Indexed Image, 8 bit slices");
        setDelay(20);
        this.implementation = TestExecutionImplementation.Both;
    }

    private QuadImageTileLayer setupImageLayer(ConfigOptions.TestType testType, MaplyBaseController baseController) throws Exception {

        IndexTestTileSource tileSource = new IndexTestTileSource(baseController,0,4,true,0);

        // Describe the input data sources
        ImageSourceLayout srcLayout = new ImageSourceLayout();
        srcLayout.slicesInLastImage = 4;
        srcLayout.indexed = true;
        srcLayout.sourceWidth = ImageSourceLayout.MaplyIProSourceWidth.MaplyIProWidth8Bits;
        srcLayout.slicesInLastImage = 4;

        Bitmap colorramp = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.colorramp);

        SphericalMercatorCoordSystem coordSystem = new SphericalMercatorCoordSystem();
        QuadImageTileLayer baseLayer = new QuadImageTileLayer(baseController, coordSystem, tileSource);
        baseLayer.setImageDepth(4);
        baseLayer.setSourceLayout(srcLayout);
        baseLayer.setInternalImageFormat(QuadImageTileLayer.MaplyIProInternalImageFormat.MaplyIProImage4Layer8Bit);
        baseLayer.setAnimationPeriod(6.0);
        baseLayer.setAnimationWrap(false);
        baseLayer.setRampImage(colorramp);

        baseLayer.setDrawPriority(MaplyBaseController.ImageLayerDrawPriorityDefault+100);
        return baseLayer;
    }

    @Override
    public boolean setUpWithGlobe(GlobeController globeVC) throws Exception {
        CartoDBMapTestCase baseCase = new CartoDBMapTestCase(getActivity());
        baseCase.setUpWithGlobe(globeVC);

        globeVC.addLayer(this.setupImageLayer(ConfigOptions.TestType.GlobeTest, globeVC));
        Point2d loc = Point2d.FromDegrees(-3.6704803, 40.5023056);
        globeVC.animatePositionGeo(loc.getX(), loc.getY(), 2.0, 1.0);
        return true;
    }

    @Override
    public boolean setUpWithMap(MapController mapVC) throws Exception {
        CartoDBMapTestCase baseCase = new CartoDBMapTestCase(getActivity());
        baseCase.setUpWithMap(mapVC);

        mapVC.addLayer(this.setupImageLayer(ConfigOptions.TestType.MapTest, mapVC));
        Point2d loc = Point2d.FromDegrees(-3.6704803, 40.5023056);
        mapVC.setPositionGeo(loc.getX(), loc.getY(), 2.0);
        return true;
    }
}