package cz.cvut.fit.ruiansearch.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "ruian")
public class Address {
    @Id
    @Indexed
    @Getter
    private String id;

    /*
     * ADM stands for Adresní místo
     */
    @Indexed(name="K_d_ADM")
    @Getter
    private String admCode;

    @Indexed(name="N_zev_obce")
    @Getter
    private String cityName;

    /*
     * MOMC is czech abbreviation for city borough
     */
    @Indexed(name="N_zev_MOMC")
    @Getter
    private String boroughName;

    /*
     *  MOP is czech abbreviation for Prague's (Czech capital city) city borough
     */
    @Indexed(name="N_zev_MOP")
    @Getter
    private String pragueBoroughName;

    /*
     * Unlike boroughs city disctricts aren't self-governed
     */
    @Indexed(name="N_zev___sti_obce")
    @Getter
    private String districtName;

    @Indexed(name="N_zev_ulice")
    @Getter
    private String streetName;

    /*
     * SO stands for Stavební objekt (building)
     * buildingType can be either "č.p." (číslo popisné) or "č.ev" (číslo evidenční)
     */
    @Indexed(name="Typ_SO")
    @Getter
    private String buildingType;

    @Indexed(name="__slo_domovn_")
    @Getter
    private String houseNumber;

    @Indexed(name="__slo_orienta_n_")
    @Getter
    private String orientationalNumber;

    @Indexed(name="Znak___sla_orienta_n_ho")
    @Getter
    private String orientationalNumberLetter;

    @Indexed(name="PS_")
    @Getter
    private String postalCode;

    @Indexed(name="Sou_adnice_X")
    @Getter
    private Double coordinateX;

    @Indexed(name="Sou_adnice_Y")
    @Getter
    private Double coordinateY;

    @Indexed(name="Coordinates_lat_lon")
    @Getter
    private Point coordinatesLatLon;

    @Indexed(name="Obec")
    private String cityForQuery;

    @Indexed(name="Cast_obce")
    private String districtForQuery;

    @Indexed(name="Mestska_cast")
    private String boroughForQuery;

    @Indexed(name="Ulice")
    private String streetForQuery;

    @Indexed(name="Cislo_orientacni")
    private String orientationalNumberForQuery;

    @Indexed(name="Identifikace")
    @Getter
    private String identification;

    @Indexed(name="Identifikace_cs")
    private String identificationForQuery;

    @Indexed(value = "Text_adresy")
    @Getter
    private String addressText;

    @Indexed(value = "score", readonly = true)
    private Double score;
}
