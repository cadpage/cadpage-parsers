package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOPerryCountyBParser extends DispatchBCParser {

  public MOPerryCountyBParser() {
    super(CITY_LIST, "PERRY COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
    removeWords("STE");
  }

  @Override
  public String getFilter() {
    return "PERRY@OMNIGO.COM,noreply@omnigo.com,louisianapd.dispatch@gmail.com";
  }

  private static final Pattern STE_PTN = Pattern.compile("\\bSTE\\b");

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {

      // SAINTE is abbreviated STE which is confused as an suite identifier
      // so we will expand it before trying anything else
      field = STE_PTN.matcher(field).replaceAll("SAINTE");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends BaseCrossField {
    @Override
    public void parse(String field, Data data) {

      // SAINTE is abbreviated STE which is confused as an suite identifier
      // so we will expand it before trying anything else
      field = STE_PTN.matcher(field).replaceAll("SAINTE");
      super.parse(field, data);
    }
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
