package cz.cvut.fit;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class CoordinatesConverter {
    // EPSG:5514 isn't supported by Geotools, using WKT (well known text) definition isntead
    private static final String wkt = "PROJCS[\"S-JTSK / Krovak East North\"," +
            "GEOGCS[\"S-JTSK\",DATUM[\"System_Jednotne_Trigonometricke_Site_Katastralni\"," +
            "SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]]], " +
            "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]]," +
            "UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4156\"]]," +
            "PROJECTION[\"Krovak\"],PARAMETER[\"latitude_of_center\",49.5]," +
            "PARAMETER[\"longitude_of_center\",24.83333333333333],PARAMETER[\"azimuth\",30.28813972222222]," +
            "PARAMETER[\"pseudo_standard_parallel_1\",78.5],PARAMETER[\"scale_factor\",0.9999]," +
            "PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1," +
            "AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],AUTHORITY[\"EPSG\",\"5514\"]]";

    public static DirectPosition2D convert(Double x, Double y) throws FactoryException, TransformException {
        x = -x;
        y = -y;
        CoordinateReferenceSystem sourceCRS = CRS.parseWKT(wkt);
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
        DirectPosition2D point = new DirectPosition2D(sourceCRS, y, x);
        DirectPosition2D result = new DirectPosition2D();
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
        transform.transform(point, result);

        return result;
    }
}
