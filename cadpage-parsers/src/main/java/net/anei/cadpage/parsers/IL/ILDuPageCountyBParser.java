package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILDuPageCountyBParser extends FieldProgramParser {
  
  private static final Pattern BOX2_PTN = Pattern.compile("/BOX CARD# +- +(\\d+)/(\\d{2}[A-Z]?)/");
  

  public ILDuPageCountyBParser() {
    super(CITY_LIST, "DUPAGE COUNTY", "IL",
        "ID DATE/d TIME ( MUTUAL_AID SKIP MACALL SKIP MAMAP MAINFO MACROSS? "
+                           "|"
+                       " CALL ADDR CITY? ( BOX X/Z+? MAP! "
+                                            "|"
+                                        " PLACE BOX X/Z+? MAP! "
+                                            "|"
+                                        " PLACE X/Z X/Z MAP! "
+                                            "|"
+                                        " X ( X/Z MAP | MAP | ) "
+                                            "|"
+                                        " PLACE X/Z MAP! "
+                                            "|"
+                                        " PLACE MAP! "
+                                            "|"
+                                        " MAP! "
+                                            "|"
+                                     " ) "
+                     ")"
+       " INFO+"
        );
     
  }

  @Override
  public String getFilter() {
    return ("cadmsg@downers.us");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    // Remove any \n in text body
    body = body.replace("\n", "");
    
    // There is one slash we want to protect as part of the data field
    body = BOX2_PTN.matcher(body).replaceFirst("/BOX CARD# - $1%$2/");

    // Split body on / and call parseFields()
    return parseFields(body.split("/"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d?\\-\\d\\d\\-\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d\\:\\d\\d\\:\\d\\d", true);
    if (name.equals("MACALL")) return new MutualAidCallField();
    if (name.equals("MUTUAL_AID")) return new CallField("MUTUAL AID", true);
    if (name.equals("MAMAP")) return new MapField("[A-Z]*\\d+", true);
    if (name.equals("MAINFO")) return new MutualAidInfoField();
    if (name.equals("MACROSS")) return new CrossField("(?i)CROSS +(.*)", true);
    if (name.equals("BOX")) return new BoxField("[AB]\\d{3}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MutualAidCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCall = data.strCall.replace("/", "-");
    }
  }
  
  private static final Pattern TRAIL_IN_PTN = Pattern.compile("(.*?) +IN", Pattern.CASE_INSENSITIVE);
  private class MutualAidInfoField extends AddressCityField {
    MutualAidInfoField() {}
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace(',', ' ').replaceAll("  +", " ");
      parseAddress(StartType.START_OTHER, field, data);
      data.strCall = append(data.strCall, " - ", getStart());
      data.strSupp = getLeft();
      Matcher m = TRAIL_IN_PTN.matcher(data.strAddress);
      if (m.matches()) data.strAddress = m.group(1);
    }
    
    @Override
    public String getFieldNames() {
      return ("CALL ADDR APT CITY INFO");
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldEnd(addr, " INTERSECTN");
    return addr;
  }
  
  private static final Pattern PLACE_BOX_PTN = Pattern.compile("(.*?)((?: +[AB]\\d{3})+)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_BOX_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strBox = append(data.strBox, " ", match.group(2).trim());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE BOX";
    }
  }
  
  private static final Pattern X_PTN = Pattern.compile("(?:\\d+ +)?\\b\\d+(?:ST|ND|RD|TH)|T\\d+|.* DN");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (X_PTN.matcher(field).matches()) {
        parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("F\\d{3}|\\d{2}|BOX CARD# +- +(.*)");
  private class MyMapField extends MapField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.matches()) return false;
      String fld = match.group(1);
      if (fld != null) field = fld.replace('%', '/');
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern UNIT_PATTERN = Pattern.compile("(?:\\b(?:\\d{3,4}|[A-Z]{1,4}|[A-Z]+\\d+|DCHIEF|general fire|afa)\\b[, ]*)+");
  private class MyInfoField extends InfoField {
    @Override 
    public void parse(String field, Data data) {
      if (UNIT_PATTERN.matcher(field).matches()) {
        field = field.replace(',', ' ').replaceAll("  +", " ").trim();
        data.strUnit = append(data.strUnit, " ", field);
      } else {
        data.strSupp = append(data.strSupp, " ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return (append(super.getFieldNames(), " ", "UNIT"));
    }
  }
  
  // Many of these cities are in Cook or Will county
  private static final String[] CITY_LIST = new String[] {
    "ADDISON",
    "ARLINGTON HEIGHTS",
    "ALGONQUIN",
    "ARGONNE",
    "AURORA",
    "BARTLETT",
    "BARRINGTON",
    "BOLINGBROOK",
    "BLOOMINGDALE",
    "BENSENVILLE",
    "BROOKFIELD",
    "BUFFALO GROVE",
    "BERKLEY",
    "BELLWOOD",
    "BURR RIDGE",
    "BATAVIA",
    "BROADVIEW",
    "CARY",
    "CHICAGO",
    "CLARENDON HILLS",
    "COOK COUNTY",
    "CAROL STREAM",
    "CARPENTERSVILLE",
    "DARIEN",
    "DOWNERS GROVE",
    "DES PLAINES",
    "DARIEN-WOODRIDGE",
    "ELBURN",
    "EAST DUNDEE",
    "ELMHURST",
    "ELK GROVE VILLAGE",
    "ELGIN",
    "EOLA",
    "ELK GROVE TWP",
    "FRANKLIN PARK",
    "FERMI LAB",
    "FOX RIVER",
    "GLEN ELLYN",
    "GLENDALE HEIGHTS",
    "GLENSIDE",
    "GENEVA",
    "HANOVER PARK",
    "HOFFMAN ESTATES",
    "HINSDALE",
    "HAMPSHIRE",
    "HILLSIDE",
    "ITASCA",
    "KANE COUNTY",
    "KENDALL COUNTY",
    "LOCKPORT",
    "LEMONT",
    "LAGRANGE",
    "LISLE",
    "LOMBARD",
    "LAGRANGE PARK",
    "LISLE-WOODRIDGE",
    "LEYDEN",
    "LAKE ZURICH",
    "MEDINAH",
    "MAPLE PARK",
    "MELROSE PARK",
    "MOUNT PROSPECT",
    "MAYWOOD",
    "NAPERVILLE",
    "NORTH AURORA",
    "NORTHLAKE",
    "OAKBROOK",
    "OAK BROOK",
    "OAK BROOK TERRACE",
    "OAKBROOK TERRACE",
    "PLAINFIELD",
    "PINGREE GROVE",
    "PROSPECT HEIGHTS",
    "PALATINE RURAL",
    "PALATINE",
    "PLEASANTVIEW",
    "RUTLAND DUNDEE",
    "ROSELLE",
    "ROLLING MEADOWS",
    "ROSEMONT",
    "ROMEOVILLE",
    "ST CHARLES",
    "ST. CHARLES",
    "SOUTH ELGIN",
    "S ELGIN",
    "S. ELGIN",
    "SCHAUMBURG",
    "SCHILLER PARK",
    "STREAMWOOD",
    "TRI-STATE",
    "VILLA PARK",
    "WAYNE",
    "WEST CHICAGO",
    "W CHICAGO",
    "W. CHICAGO",
    "WESTCHESTER",
    "WOOD DALE",
    "WOODDALE",
    "WEST DUNDEE",
    "WINFIELD",
    "WHEATON",
    "WILLOWBROOK",
    "WESTMONT",
    "WOODRIDGE",
    "WARRENVILLE",
    "WESTERN SPRINGS",
    "WHEELING",
    "WARRENVILLE",
    "WILL COUNTY",
    "YORK CENTER"
  };
}
