package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

import java.util.Properties;
import java.util.regex.*;

public class NYLivingstonCountyAParser extends DispatchA5Parser {

  public NYLivingstonCountyAParser() {
    super(CITY_CODES, STANDARD_CODES, "LIVINGSTON COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "@CO.LIVINGSTON.NY.US,alarms@livoniafiredept.org";
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if(!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("COUNTY OUT") || data.strCity.equals("OUT OF COUNTY")) {
      data.strCity = "";
      data.defCity = "";
    }
    return true;
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField() ;
    return super.getField(name);
  }

  @Override
  public String adjustMapAddress(String sAddress) {

    sAddress = MA_PTN.matcher(sAddress).replaceAll("MANOR");
    sAddress = EX_PTN.matcher(sAddress).replaceAll("EXPY");
    sAddress = IFO_PTN.matcher(sAddress).replaceAll("");
    return sAddress;
  }
  private static final Pattern MA_PTN = Pattern.compile("\\bMA\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern EX_PTN = Pattern.compile("\\bEX\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern IFO_PTN = Pattern.compile(" +IFO$", Pattern.CASE_INSENSITIVE);

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GROVELAN",   "GROVELAND",
      "SPRINGWA",   "SPRINGWATER",

      "AVOS", "AVOCA",
      "AVOT", "AVON",
      "AVOV", "AVON",
      "BURT", "BURNS",
      "CALT", "CALEDONIA",
      "CALV", "CALEDONIA",
      "CANA", "CANANDAIGUA",
      "CANT", "CANADICE",
      "CANV", "CANASERAGA",
      "CAST", "CASTILE",
      "CHUR", "CHURCHVILLE",
      "COHT", "COHOCTON",
      "COHV", "COHOCTON",
      "COLL", "COLLINS",
      "CONT", "CONESUS",
      "CORN", "CORNING",
      "DANS", "DANSVILLE STEUBEN CO",
      "DANT", "NORTH DANSVILLE",
      "DANV", "DANSVILLE",
      "GENT", "GENESEO",
      "GENV", "GENESEO",
      "GFAL", "GENESEE FALLS",
      "GRAA", "GRANGER",
      "GROA", "GROVE",
      "GROT", "GROVELAND",
      "HENT", "HENRIETTA",
      "HFAV", "HONEOYE FALLS",
      "LEIT", "LEICESTER",
      "LEIV", "LEICESTER",
      "LERT", "LEROY",
      "LERV", "LEROY",
      "LIMT", "LIMA",
      "LIMV", "LIMA",
      "LIVT", "LIVONIA",
      "LIVV", "LIVONIA",
      "MENT", "MENDON",
      "MTMT", "MT MORRIS",
      "MTMV", "MT MORRIS",
      "NAPT", "NAPLES",
      "NUNT", "NUNDA",
      "NUNV", "NUNDA",
      "OOC",  "OUT OF COUNTY",
      "OSST", "OSSIAN",
      "PAVT", "PAVILION",
      "PERT", "PERRY",
      "PERV", "PERRY",
      "PORT", "PORTAGE",
      "RICT", "RICHMOND",
      "ROCH", "ROCHESTER",
      "RUST", "RUSH",
      "SPAT", "SPARTA",
      "SPRT", "SPRINGWATER",
      "VILL", "VILLAGE",
      "WAYT", "WAYLAND",
      "WAYV", "WAYLAND",
      "WBLT", "WEST BLOOMFIELD",
      "WHET", "WHEATLAND",
      "WSPT", "WEST SPARTA",
      "YORT", "YORK"
  });

  private static final StandardCodeTable STANDARD_CODES = new StandardCodeTable();
}
