package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPerryCountyAParser extends DispatchA33Parser {


  public MOPerryCountyAParser() {
    super(CITY_LIST, "PERRY COUNTY", "MO", A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "louisianapd.dispatch@gmail.com,PERRY@OMNIGO.COM";
  }

  private static final String[] CITY_LIST = new String[] {

      "PERRY COUNTY",

      // Cities
      "ALTENBURG",
      "FROHNA",
      "PERRYVILLE",

      // Village
      "LONGTOWN",

      // Census-designated places
      "BIEHLE",
      "BREWER",
      "LITHIUM",
      "SHAKERTOWNE",

      // Unincorporated communities
      "ALLENS LANDING",
      "APPLE CREEK",
      "BARKS",
      "BELGIQUE",
      "BRAZEAU",
      "BREWER",
      "CLARYVILLE",
      "CORNERS",
      "CROSSTOWN",
      "EUREKA",
      "FARRAR",
      "FENWICK SETTLEMENT",
      "FRIEDENBERG",
      "HIGHLAND",
      "MCBRIDE",
      "MENFRO",
      "MILLHEIM",
      "OLD APPLETON",
      "POINTREST",
      "SCHALLS",
      "SCHUMER SPRINGS",
      "SERENO",
      "SEVENTY-SIX",
      "SILVER LAKE",
      "UNIONTOWN",
      "WITTENBERG",
      "YOUNT",

      /// Former communities
      "DRESDEN",
      "FENWICK SETTLEMENT",
      "FRIENDLYTOWN",
      "GIBONEY",
      "LE GRAND VILLAGE SAUVAGE",
      "POINTREST",
      "SEELITZ",
      "SEVENTY-SIX",
      "STARLANDING",
      "TUCKERS SETTLEMENT",
      "TOWNSHIPS",
      "BOIS BRULE",
      "BRAZEAU",
      "CENTRAL",
      "CINQUE HOMMES",
      "ST MARYS",
      "SALEM",
      "SALINE",
      "UNION",
      "ISLANDS",
      "GRAND TOWER ISLAND",

      // Bollinger County
      "BOLLINGER COUNTY",
      "SEDGEWICKVILLE"

  };
}