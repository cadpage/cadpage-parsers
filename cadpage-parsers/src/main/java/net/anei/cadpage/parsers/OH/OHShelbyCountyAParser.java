package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHShelbyCountyAParser extends FieldProgramParser {
  
  public OHShelbyCountyAParser() {
    super(CITY_LIST, "SHELBY COUNTY", "OH",
           "ID TIME? CALL ADDR/S PLACENAME");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "cad@shelbycountysheriff.com,PFullenkamp@Plastipak.com,pfullenkamp@nktelco.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("Quick Look")) {
      int pt = body.indexOf("\n\n");
      if (pt < 0) return false;
      body = body.substring(pt+2).trim();
    } else {
      body = body.replace("\n>", "\n").trim();
      body = stripFieldStart(body,">");
    }
    return parseFields(body.split("\\|"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{6}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    
    public MyCallField() {
      setPattern(Pattern.compile(":?[A-Z0-9]+ ?- ?[A-Z0-9]*"), true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ":");
      super.parse(field, data);
    }
  }
  
  private static final Pattern X_MARK_PTN = Pattern.compile(" BETWE? | X OF ");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strSupp = p.getOptional('@');
      String sAddr = p.get(X_MARK_PTN);
      data.strCross = p.get().replaceAll("  +", " ");
      if (data.strCross.equals("&")) data.strCross = "";
      sAddr = sAddr.replace("@", "&");
      int pt = sAddr.lastIndexOf(',');
      if (pt >= 0) {
        parseAddress(sAddr.substring(0,pt).trim(), data);
        data.strCity = sAddr.substring(pt+1).trim();
      } else {
        super.parse(sAddr, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO " + super.getFieldNames() + " X";
    }
  }
  
  @Override
  protected boolean checkPlace(String name) {
    // If it has more than two blanks, consider it to be a place
    return (PLACE_PTN.matcher(name).matches());
  }
  private static final Pattern PLACE_PTN = Pattern.compile("[^ ]+ +[^ ]+ .*");

  private static final String[] CITY_LIST = new String[]{
    "SIDNEY",
    "ANNA",
    "BOTKINS",
    "FT LORAMIE",
    "JACKSON CENTER",
    "KETTLERSVILLE",
    "LOCKINGTON",
    "PORT JEFFERSON",
    "RUSSIA",
    "CLINTON TWP",
    "CYNTHIAN TWP",
    "DINSMORE TWP",
    "FRANKLIN TWP",
    "GREEN TWP",
    "JACKSON TWP",
    "LORAMIE TWP",
    "MCLEAN TWP",
    "ORANGE TWP",
    "PERRY TWP",
    "SALEM TWP",
    "SIDNEY",
    "TURTLE CREEK TWP",
    "VAN BUREN TWP",
    "WASHINGTON TWP",
    "NEWPORT",
    "HOUSTON",
    "KIRKWOOD",
    "MAPLEWOOD",
    "MCCARTYVILLE",
    "MONTRA",
    "MOUNT JEFFERSON",
    "PEMBERTON",
    "ST PATRICK",
    "SWANDERS",
    "TAWAWA",
    "TROY",
    "NEW KNOXVILLE"
  };
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "99 N I-75",                            "+40.396092,-84.160201",
      "100 N I-75",                           "+40.398207,-84.161508",
      "101 N I-75",                           "+40.412300,-84.166812",
      "102 N I-75",                           "+40.438418,-84.168167",
      "103 N I-75",                           "+40.441189,-84.169173",
      "104 N I-75",                           "+40.467331,-84.168360",
      "105 N I-75",                           "+40.469947,-84.169145",
      "106 N I-75",                           "+40.496942,-84.169135",
      "107 N I-75",                           "+40.511515,-84.169147",
      "108 N I-75",                           "+40.526111,-84.169301",
      "99 S I-75",                            "+40.395980,-84.162411",
      "100 S I-75",                           "+40.398207,-84.161508",
      "101 S I-75",                           "+40.412300,-84.166812",
      "102 S I-75",                           "+40.439692,-84.170227",
      "103 S I-75",                           "+40.441189,-84.169173",
      "104 S I-75",                           "+40.468800,-84.170034",
      "105 S I-75",                           "+40.469947,-84.169145",
      "106 S I-75",                           "+40.496917,-84.169436",
      "107 S I-75",                           "+40.511500,-84.169448",
      "108 S I-75",                           "+40.526104,-84.169625"
      
  });
}
