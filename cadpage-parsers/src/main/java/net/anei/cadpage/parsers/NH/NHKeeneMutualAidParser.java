package net.anei.cadpage.parsers.NH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NHKeeneMutualAidParser extends FieldProgramParser {
  
  public NHKeeneMutualAidParser() {
    super("","",
           "URL? ADDR PLACE CITY CALL INFO+ Caller_Phone:PHONE? Caller_Name:NAME INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad_do_not_reply@firemutualaid.com,cad@firemutualaid.com";
  }

  @Override
  public String getLocName() {
    return "Keene Mutual Aid, NH";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Automated Event Notification")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO NAME")) return;
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strState = CITY_STATE_MAP.getProperty(field.toUpperCase(), "");
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private static final Pattern PRI_PTN = Pattern.compile("\\d");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("TAC:")) {
        data.strChannel = field.substring(4).trim();
      } else if (PRI_PTN.matcher(field).matches()) {
        data.strPriority = field;
      } else {
        if (field.startsWith("Notes")) field = field.substring(5).trim();
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PRI INFO CH";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("URL")) return new InfoUrlField("EMLive: (http://.*)");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_STATE_MAP = buildCodeTable(new String[]{
      "ACWORTH",        "NH",
      "ALSTEAD",        "NH",
      "AMHERST",        "NH",
      "ANTRIM",         "NH",
      "ATHENS",         "VT",
      "BELLOWS FALLS",  "VT",
      "BENNINGTON",     "VT",
      "BRATTLEBORO",    "VT",
      "CHARLESTOWN",    "NH",
      "CHESTER",        "VT",
      "CHESTERFIELD",   "NH",
      "CLAREMONT",      "NH",
      "DUBLIN",         "NH",
      "DUMMERSTON",     "VT",
      "EAST DOVER",     "VT",
      "EAST SWANZEY",   "NH",
      "FITZWILLIAM",    "NH",
      "FRANCESTOWN",    "NH",
      "GILSUM",         "NH",
      "GOSHEN",         "NH",
      "GRAFTON",        "NH",
      "GREENFIELD",     "NH",
      "GUILFORD",       "VT",
      "HALIFAX",        "VT",
      "HANCOCK",        "NH",
      "HARRISVILLE",    "NH",
      "HINSDALE",       "NH",
      "JAFFREY",        "NH",
      "JAMAICA",        "VT",
      "KEENE",          "NH",
      "LANGDON",        "NH",
      "LEMPSTER",       "NH",
      "LONDONDERRY",    "NH",
      "LYNDEBOROUGH",   "NH",
      "MARLBORO",       "VT",
      "MARLBOROUGH",    "NH",
      "MARLOW",         "NH",
      "MEADOWOOD",      "",
      "NELSON",         "NH",
      "NEW IPSWICH",    "NH",
      "NEWBROOK",       "VT",
      "NORTH WALPOLE",  "NH",
      "PERU",           "VT",
      "PETERBOROUGH",   "NH",
      "PUTNEY",         "VT",
      "READSBORO",      "VT",
      "RICHMOND",       "NH",
      "RINDGE",         "NH",
      "ROCKINGHAM",     "VT",
      "ROXBURY",        "NH",
      "ROYALSTON",      "",
      "SAXTONS RIVER",  "VT",
      "SOUTH LONDONDERRY", "VT",
      "SPOFFORD",       "NH",
      "SPRINGFIELD",    "VT",
      "STODDARD",       "NH",
      "STRATTON",       "VT",
      "SULLIVAN",       "NH",
      "SURRY",          "NH",
      "SWANZEY",        "NH",
      "TEMPLE",         "NH",
      "TOWNSHEND",      "VT",
      "TROY",           "NH",
      "UNITY",          "NH",
      "VERNON",         "VT",
      "WALPOLE",        "NH",
      "WARDSBORO",      "VT",
      "WASHINGTON",     "NH",
      "WEST CHESTERFIELD", "NH",
      "WEST DOVER",     "VT",
      "WEST SWANZEY",   "NH",
      "WESTMINSTER",    "NH",
      "WESTMORELAND",   "NH",
      "WESTON",         "VT",
      "WHITINGHAM",     "VT",
      "WILLIAMSVILLE",  "VT",
      "WILMINGTON",     "VT",
      "WINCHENDON",     "NH",
      "WINCHESTER",     "NH",
      "WINDHAM",        "VT",
      "WINHALL",        "VT"
  });
}
