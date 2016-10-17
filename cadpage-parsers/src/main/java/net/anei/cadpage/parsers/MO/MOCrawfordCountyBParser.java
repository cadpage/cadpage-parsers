package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOCrawfordCountyBParser extends FieldProgramParser {
 
  public MOCrawfordCountyBParser() {
    super(CITY_LIST, "CRAWFORD COUNTY", "MO",
      "Location:ADDR! Category:CALL! Note:INFO INFO+");
    removeWords("ROAD");
  }
  
  @Override
  public String getProgram() {
    return append(super.getProgram(), " ", "ID PLACE");
  }
  
  @Override
  public String getFilter() {
    return "CRAWFORDCO@PUBLICSAFETYSOFTWARE.NET";
  }
  
  private static final Pattern INCIDENT_PATTERN
    = Pattern.compile("Incident Number\\:(.*?)\\n(.*)", Pattern.DOTALL);
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // I guess these are RUN REPORTs
    Matcher m = INCIDENT_PATTERN.matcher(body);
    if (m.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = m.group(1);
      data.strSupp = m.group(2);
      return true;
    }
    
    // North Crawford County Ambulance Dist. has "CALL" for subject
    if (parseFields(body.split("\n"), data)) {
      if (data.strCity.equalsIgnoreCase("STEELVILE")) data.strCity = "STEELVILLE";
      else if (data.strCity.equalsIgnoreCase("CHERRYVILE")) data.strCity = "CHERRYVILLE";
      return true;
    }
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PREFIX_PTN = Pattern.compile("(UNIT \\S+)  +(.*)");
  private static final Pattern MULT_BLANK_PTN = Pattern.compile("  +");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      field = stripFieldStart(field, "'");
      
      Matcher match = ADDR_PREFIX_PTN.matcher(field);
      if (match.matches()) {
        data.strPlace = match.group(1);
        field = match.group(2);
      }
      int pt = field.lastIndexOf("     ");
      if (pt >= 0) {
        data.strCross = field.substring(pt+5).trim();
        field = field.substring(0,pt);
      }
      else {
        String[] parts = MULT_BLANK_PTN.split(field);
        for (int ndx = parts.length-1; ndx>=0; ndx--) {
          String part = parts[ndx];
          if (isCity(part)) {
            data.strCity = part.replace(".", "");
            field = buildParts(parts, 0, ndx);
            data.strCross = buildParts(parts, ndx+1, parts.length);
          }
        }
      }
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, field, data);
    }
    
    private String buildParts(String[] parts, int lowNdx, int highNdx) {
      if (lowNdx >= highNdx) return "";
      if (lowNdx+1 == highNdx) return parts[lowNdx];
      StringBuilder sb = new StringBuilder();
      for (int ndx = lowNdx; ndx<highNdx; ndx++) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(parts[ndx]);
      }
      return sb.toString();
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY X";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Note:")) {
        data.strSupp = append(data.strSupp, "\n", field.substring(5).trim());
      } else {
        data.strSupp = append(data.strSupp, " ", field);
      }
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("INDIAN HILLS")) return "CUBA";
    return city;
  }
  
  private static final String[] CITY_LIST = {
      "BENTON",
      "BERRYMAN",
      "BOONE",
      "BOURBON",
      "CHERRYVILE",   // Mispelled
      "CHERRYVILLE",
      "COOK STATION",
      "COURTOIS",
      "CUBA",
      "DAVISVILLE",
      "DILLARD",
      "FANNING",
      "HUZZAH",
      "INDIAN HILLS",
      "KNOBVIEW",
      "LEASBURG",
      "LIBERTY",
      "MERAMEC",
      "OAK HILL",
      "OSAGE",
      "OWENSVILLE",
      "ST. CLOUD",
      "ST CLOUD",
      "SAINT CLOUD",
      "STEELVILE",    // Mispelled
      "STEELVILLE",
      "SULLIVAN",
      "UNION",
      "WESCO",
      "WEST SULLIVAN",
      "W SULLIVAN",
      "W. SULLIVAN",
      
      // Franklin County
      "STANTON",
      
      // Philips County
      "ST JAMES",
      
      // Washington County
      "POTOSI"
  };
}