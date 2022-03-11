package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchProphoenixParser extends FieldProgramParser {
  
  private Properties cityCodes;
  private boolean hasCityList;
  
  public DispatchProphoenixParser(String defCity, String defState) {
    this(null, defCity, defState);
  }
  public DispatchProphoenixParser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, null, defCity, defState);
  }
  
  public DispatchProphoenixParser(Properties cityCodes, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "( RR_MARK/R! EMPTY? FDID:SKIP! Incident#:ID! CFSCode:CALL1! Alarm:SKIP! District:SKIP! Receive_Source:SKIP! EMPTY? ADDR_INFO! EMPTY? Location:ADDR1! Common_Name:PLACE! CSZ:SKIP! Dispatch_Priority:PRI! Lat/Long:SKIP! EMPTY? INCIDENT_TIMES! EMPTY? INFO/N+? INCIDENT_COMMENTS " +
          "| DATETIME CALL ADDR! Latitude:GPS1 Longitude:GPS2 CrossStreet1:X CrossStreet2:X CommonName:PLACE Units:UNIT/S+ Comments:INFO+ )");
    this.cityCodes = cityCodes;
    this.hasCityList = (cityList != null);
  }
  
  private static final Pattern MARKER = Pattern.compile("^(?:(\\d+)|(?:[ A-Z0-9]+:? +\\(#([A-Z0-9]+)\\) +)?([- A-Z0-9]+):) +");
  private static final Pattern TRAILER_PTN = Pattern.compile(" *;[A-Z0-9]+ +STOP$"); 
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // There are two kinds of headers.  Possibly one for text pages and another for SMTP pages
    // Not counting the run report heading
    if (body.startsWith("Incident Information\n")) {
      data.msgType = MsgType.RUN_REPORT;
      body = body.replace("> ", ": ");
    }
    else {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      data.strCallId = match.group(1);

      if (data.strCallId == null) {
        data.strCallId = match.group(2);
        if (data.strCallId == null) data.strCallId = "";
        data.strSource = match.group(3).trim();
      }
      body = body.substring(match.end());
      
      match = TRAILER_PTN.matcher(body);
      if (match.find()) body = body.substring(0,match.start());
      
      body = body.replace(" Longitude:", "\nLongitude:");
    }
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("RR_MARK"))  return new SkipField("Incident Information", true);
    if (name.equals("CALL1")) return new BaseCall1Field();
    if (name.equals("ADDR_INFO")) return new SkipField("Address Information", true);
    if (name.equals("ADDR1")) return new BaseAddress1Field();
    if (name.equals("INCIDENT_TIMES")) return new SkipField("Incident Times", true);
    if (name.equals("INCIDENT_COMMENTS")) return new SkipField("Incident Comments", true);
    
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private class BaseCall1Field extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern ADDR1_PTN = Pattern.compile("(.*)\\s{3,}(\\d+)");
  private class BaseAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR1_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private static final Pattern ID_DATE_TIME_PTN = 
    Pattern.compile("\\{(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\\}");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
  
  private static final Pattern CALL_PTN = Pattern.compile("([A-Z0-9 ]+) *- *(.*?) *\\{(\\d)\\}");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1).trim();
      data.strCall = match.group(2).trim();
      data.strPriority = match.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?)(?: (A[LKRZ]|C[AOT]|DE|FL|G[AU]|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|P[AR]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY])?(?: (\\d{5})))?");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      String zip = null;
      
      int pt = field.lastIndexOf(';');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (cityCodes != null) {
          data.strCity = convertCodes(data.strCity, cityCodes);
        }
      }
      else if (hasCityList) {
        Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
        if (match.matches()) {  // Can not fail
          field = match.group(1).trim();
          data.strState = getOptGroup(match.group(2));
          zip = match.group(3);
        }
        
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
        field = getStart();
      }
      
      String apt = "";
      pt = field.lastIndexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }
  }
  
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field.replaceAll("  +", " "));
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\{ *([A-Z0-9]+) *\\}");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      // First pick out any unit designation in curly braces.
      // These are added to the unit field if they are not already present there
      int lastPt = 0;
      Matcher match = UNIT_PTN.matcher(field);
      while (match.find()) {
        String sUnit = match.group(1);
        lastPt = match.end();
        Pattern ptn = Pattern.compile("\\b" + sUnit + "\\b");
        if (!ptn.matcher(data.strUnit).find()) data.strUnit = append(data.strUnit, " ", sUnit);
      }
      
      // Ignore everything up to the last unit
      field = field.substring(lastPt).trim();
      field = stripFieldStart(field, ";");
      
      if (field.startsWith(",")) field = field.substring(1).trim();
      
      // Filter out stuff we aren't interested in
      if (field.contains("Units Recommended:")) return;
      if (field.contains("Toner Alert Instantiated")) return;
      int pt = field.indexOf("Update reviewed by dispatcher");
      if (pt >= 0) field = field.substring(0,pt).trim();
      
      // ANything else is information
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
