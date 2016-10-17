package net.anei.cadpage.parsers.SD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Pennington County, SD
 */
public class SDPenningtonCountyParser extends FieldProgramParser {
  
  private boolean nextIntersect;
  
  public SDPenningtonCountyParser() {
    super(CITY_LIST, "PENNINGTON COUNTY", "SD",
          "SRC EMPTY? UNIT CALL ADDR! INFODATETIME+");
    setupCallList(CALL_LIST);
  }

  @Override
  public String getFilter() {
    return "dispatch@co.pennington.sd.us,dispatch@pennco.org,777,74121";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DELIM = Pattern.compile("- |\\b--\\b");
  private static final Pattern VALID_ADDRESS_PTN = Pattern.compile("[+-]?\\d+\\..*|\\d+ .*|MM .*|EXIT .*|.*&.*", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    nextIntersect = false;
    String[] flds = body.split("\\|");
    if (flds.length >= 4) {
      if (!parseFields(body.split("\\|"), 4, data)) return false;
    }
    else if (subject.equalsIgnoreCase("Dispatch")) {
      if (!parseFireCall(body, data)) return false;
    }
    else if (subject.equalsIgnoreCase("MEDICAL")) {
      if (!parseMedicalCall(body, data)) return false;
    }
    else if (body.startsWith(":")) {
      if (!parseFields(DELIM.split(body.substring(1)), data)) return false;
    }
    else return false;
    
    // They do things strangely
    // If address looks like a place name, and we have a valid looking intersection in the
    // cross street field, swap things around
    if (data.strCross.contains("/") && !VALID_ADDRESS_PTN.matcher(data.strAddress).matches()) {
      data.strPlace = data.strAddress;
      data.strAddress = "";
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "PLACE ADDR");
  }

  // *************************************************************************
  // Parse new pipe or dash delimited format
  // *************************************************************************
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFODATETIME")) return new MyInfoDateTimeField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", " ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?) *, *([A-Za-z ]+?) *, +SD(?: +\\d{5})?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCity = match.group(2);
        fixCity(data);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private static final Pattern INFO_INTERSECT_PTN= Pattern.compile("Nearest Inter +- *(.*)");
  private static final Pattern INFO_AND_PTN = Pattern.compile("\\bAND\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d(?::\\d\\d)?)(?: +- *(.*))?");
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(\\S+)");
  private static final String TRUNC_DATE_TIME_MARK = "NN/NN/NN NN:NN:NN";
  private class MyInfoDateTimeField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = INFO_INTERSECT_PTN.matcher(field);
      if (match.matches()) {
        nextIntersect = true;
        field = match.group(1);
      }
      
      else if (field.equals("Nearest Inter") || field.equals("Nearest  Inter")) {
        nextIntersect = true;
        return;
      }
      
      if (nextIntersect) {
        nextIntersect = false;
        field = INFO_AND_PTN.matcher(field).replaceAll("/");
        field  = stripFieldStart(field, "/");
        field = stripFieldEnd(field, "/");
        data.strCross = append(data.strCross, " / ", field);
        return;
      }
      
      // What makes this complicated is that sometimes fields are split by | and contain " - "  markers
      // and sometimes they are split by " - " markers.  We have to cover both cases
      String connect = "\n";
      for (String part : field.split(";")) {
        part = part.trim();
        if (part.length() == 0) continue;
        if (part.equalsIgnoreCase("None")) continue;
        match = INFO_APT_PTN.matcher(part);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          continue;
        }
        match = INFO_DATE_TIME_PTN.matcher(part);
        if (match.matches()) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
          data.strSupp = append(data.strSupp, "\n", getOptGroup(match.group(3)));
          continue;
        }
        if (TRUNC_DATE_TIME_MARK.startsWith(part.replaceAll("\\d", "N"))) continue;
        data.strSupp = append(data.strSupp, connect, part);
        connect = "; ";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO X APT DATE TIME";
    }
  }

  // *************************************************************************
  // Parse old fire format
  // *************************************************************************
  private static final Pattern UNIT_PTN = Pattern.compile("^([A-Z0-9]+)(?: +\\(Primary\\))?; *");
  private static final Pattern UNIT_PTN2 = Pattern.compile("^([A-Z0-9]+) +(?:\\(Primary\\) +)?");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("[- ]*\\b(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d(?::\\d\\d)?)\\b[- ]*");
  private static final Pattern CITY_PTN = Pattern.compile("(.*?) *, *([A-Z ]+?) *, SD +\\d{5} *(.*?)");
  private static final Pattern CODE_PTN1 = Pattern.compile(" *\\bCode: *([-A-Z0-9]+): *");
  private static final Pattern CODE_PTN2 = Pattern.compile("^Code: *([-A-Z0-9]+): *");
  
  private boolean parseFireCall(String body, Data data) {
    setFieldList("UNIT CALL ADDR APT CITY CODE INFO DATE TIME");
    
    // Parser unit information
    while (true) {
      Matcher match = UNIT_PTN.matcher(body);
      if (!match.lookingAt()) break;
      data.strUnit = append(data.strUnit, " ", match.group(1));
      body = body.substring(match.end());
    }
    Matcher match = UNIT_PTN2.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = append(data.strUnit, " ", match.group(1));
    body = body.substring(match.end());
    
    // Process Date/time splits 
    match = DATE_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      String save = body.substring(0,match.start());
      int last = match.end();
      String info = "";
      while (match.find()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        info = append(info, "\n", body.substring(last,match.start()));
        last = match.end();
      }
      data.strSupp = append(info, "\n", body.substring(last));
      body = save;
    }
    
    String callAddr = null;
    match = CITY_PTN.matcher(body);
    if (match.matches()) {
      callAddr = match.group(1);
      data.strCity = match.group(2);
      body = match.group(3);
    } else {
      int pt = body.indexOf(',');
      if (pt >= 0) {
        String extra = body.substring(pt+1).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, extra, data);
        if (data.strCity.length() > 0) {
          callAddr =  body.substring(0,pt).trim();
          body = getLeft();
        }
      }
    }
    if (callAddr != null) {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_NO_CITY | FLAG_ANCHOR_END, callAddr, data);
      if (data.strCode.length() == 0) {
        match = CODE_PTN2.matcher(body);
        if (match.find()) {
          data.strCode = match.group(1);
          body = body.substring(match.end());
        }
      }
      data.strSupp = append(data.strSupp, "\n", body);
    } else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IGNORE_AT, body, data);
      if (data.strAddress.length() == 0) return false;
      String info = getLeft();
      match = CODE_PTN1.matcher(info);
      if (match.find()) {
        data.strCode = match.group(1);
        info = append(info.substring(0,match.start()), " / ", info.substring(match.end()));
      }
      data.strSupp = append(data.strSupp, "\n", info);
    }
    fixCity(data);
        
    return true;
  }

  private void fixCity(Data data) {
    if (data.strCity.equalsIgnoreCase("PENNCO")) data.strCity = "";
    else if (data.strCity.equalsIgnoreCase("JACKCO")) data.strCity = "JACKSON COUNTY";
    else if (data.strCity.equalsIgnoreCase("MEADCO")) data.strCity = "MEADE COUNTY";
  }
  

  // *************************************************************************
  // Parse old medical format
  // *************************************************************************
  private static final Pattern MED_SPLIT_PTN = Pattern.compile("(.*?)(?: +FOR +|  +)(.*)");
  private boolean parseMedicalCall(String body, Data data) {
    setFieldList("ADDR APT CITY CALL");
    body = stripFieldEnd(body, "[Attachment(s) removed]");
    Matcher match = MED_SPLIT_PTN.matcher(body);
    if (match.matches()) {
      parseAddress(StartType.START_ADDR, match.group(1), data);
      data.strCall = match.group(2);
      return true;
    }
    else {
      parseAddress(StartType.START_ADDR, body, data);
      data.strCall = getLeft();
      return data.strCall.length() > 0;
    }
  }
  
  // Call list is only needed for old format calls that are (probably) no longer sued
  // so we aren't bothering to maintain it any more
  private static final CodeSet CALL_LIST = new CodeSet(
      "AB",
      "AB-C",
      "AB-C",
      "ACC",
      "ACCHR",
      "ACCI",
      "ACCUI",
      "ALLERGY",
      "ALLERGY-C",
      "ALT3",
      "ASLT",
      "ASSIST",
      "BACK",
      "BACK-C",
      "BLEED",
      "BLEED-B",
      "BLEED-D",
      "BREATH",
      "BREATH-D1",
      "CARDIAC",
      "CARDIAC-E",
      "CHEST",
      "CHEST-D",
      "CHOKE",
      "CHOKE-D",
      "CIV",
      "DIABETIC",
      "DIABETIC-C",
      "DOA",
      "ELE",
      "ELEVATOR",
      "EMS",
      "EXPOSURE-A",
      "FALARM DELTA",
      "FALARM",
      "FALL",
      "FALL-A3",
      "FALL-B",
      "FALL-D",
      "FALL-D2",
      "FATAL",
      "FIGHT",
      "FIRE",
      "FUEL",
      "FUEL-C",
      "GRASSF",
      "GRASSF2",
      "HEAD",
      "HEART-C",
      "HEART-D",
      "INTOX",
      "LGFIRE",
      "MP",
      "MUTUAL",
      "PG-C",
      "PG-D",
      "POISON",
      "RESCUE-D",
      "ROAD HAZARD",
      "SEIZURE",
      "SEIZURE-C",
      "SEIZURE-D",
      "SEIZURE-D2",
      "SICK PERSON DELTA LEVEL",
      "SICK",
      "SICK-C",
      "SICK-D",
      "SIG1",
      "SIG2",
      "SMFIRE",
      "SMFIRE-B1B",
      "SMOKE",
      "SRV",
      "STBY",
      "STROKE-C",
      "STROKE-C1",
      "STROKE-C3",
      "STROKE-C4",
      "STRUCF",
      "STRUCF-D4",
      "STRUCF-D9",
      "STRUCF2",
      "SUIC",
      "TRANSFER",
      "TRAUMA",
      "TRAUMA-B",
      "TRAUMA-D",
      "TRAUMA-D1",
      "TX",
      "TX2",
      "TX3",
      "UNCON CHILD",
      "UNCON",
      "UNCON-C",
      "UNCON-D",
      "UNCON-D1",
      "UNK",
      "VEHF",
      "VEHF-B1",
      "VEHF-D4",
      "WAR",
      "WATER",
      "WATER-D6",
      "WEATHER",
      "WILDF"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "PENNCO",
    
    "ASHLAND HEIGHTS",
    "COLONIAL PINE HILLS",
    "CREIGHTON",
    "GREEN VALLEY",
    "HILL CITY",
    "KEYSTONE",
    "NEW UNDERWOOD",
    "OWANKA",
    "QUINN",
    "RAPID CITY",
    "RAPID VALLEY",
    "WALL",
    "WASTA",
    "WICKSVILLE",
    "PENNCO",
    
    "ELK VALE",
    "ELLSWORTH AFB",
    "MUD BUTTE",
    "PIEDMONT",
    "TILFORD",
    "WHITE OWL",
    "BOX ELDER",
    "STURGIS",
    "BLACKHAWK",
    "SUMMERSET",
    "FAITH",
    "MEADCO"
  };
}
