package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewHavenCountyBParser extends FieldProgramParser {
  
  private Properties cityCodes = null;
  
  private static final String PROGRAM = "ID CALL? PLACE/Z ADDR/Z APT/Z CITY/Z ZIP MAP_X UNIT DATETIME! END";
  
  public CTNewHavenCountyBParser() {
    this(CITY_LIST, CITY_CODES, "NORTH BRANFORD", "CT");
  }
  
  public CTNewHavenCountyBParser(String defCity, String defState) {
    super(defCity, defState, PROGRAM);
  }
  
  public CTNewHavenCountyBParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM);
  }
  
  public CTNewHavenCountyBParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, PROGRAM);
  }
  
  public CTNewHavenCountyBParser(String[] cityList, Properties cityCodes, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM);
    this.cityCodes = cityCodes;
  }
  
  @Override
  public String getFilter() {
    return "FirePaging@hamdenfirefighters.org,paging@branfordfire.com,paging@easthavenfire.com,paging@easthavenpolice.com,paging@mail.nbpolicect.org,paging@nbpolicect.org,noreply@nexgenpss.com,pdpaging@farmington-ct.org,noreply@whpd.com";
  }
  
  private static final Pattern MARKER = Pattern.compile("(\\d{10}) +(?:(S\\d{2}) +)?");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +(\\d{6}) (\\d\\d:\\d\\d)(?:[ ,]|$)"); 
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile(" +\\d{6} [\\d:]+$| +\\d{1,6}$"); 
  private static final Pattern PRI_MARKER = Pattern.compile(" - PRI (\\d) - ");
  private static final Pattern ADDR_ST_MARKER = Pattern.compile("(.*) (\\d{5} .*)");
  private static final Pattern I_NN_HWY_PTN = Pattern.compile("\\b(I-?\\d+) +HWY\\b");
  private static final Pattern ADDR_END_MARKER = Pattern.compile("Apt ?#:|(?=(?:Prem )?Map -)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("\\S+(?: \\d+)?\\b");
  private static final Pattern MAP_PFX_PTN = Pattern.compile("(?: *(?:Prem )?Map -*)+", Pattern.CASE_INSENSITIVE);
  private static final Pattern MAP_PTN = Pattern.compile("(?:\\d{1,2}(?:[ ,]\\d{1,4})?(?:[- ]*[A-Z]{2} *\\d{1,3})?|(?:[CM]-?\\d+\\b *)+)\\b");
  private static final Pattern MAP_EXTRA_PTN = Pattern.compile("\\(Prem Map -*(.*?)\\)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) - (\\d{2}[A-Z]\\d{2}[A-Z]?)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // See if this is one of the delimited field formats
    String[] flds = body.split(" \\| ");
    if (flds.length > 5) {
      if (!parseFields(flds, data)) return false;
    }
    
    else {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      setFieldList("ID SRC CODE CALL PRI ADDR APT PLACE CITY MAP X UNIT DATE TIME INFO");
      data.strCallId = match.group(1);
      data.strSource = getOptGroup(match.group(2));
      body = body.substring(match.end());
      
      match =  DATE_TIME_PTN.matcher(body);
      if (match.find()) {
        String date = match.group(1);
        data.strDate = date.substring(2,4) + "/" + date.substring(4,6) + "/" + date.substring(0,2);
        data.strTime = match.group(2);
        data.strSupp = body.substring(match.end()).trim();
        body = body.substring(0,match.start());
      } else {
        match = TRUNC_DATE_TIME_PTN.matcher(body);
        if (match.find()) body = body.substring(0,match.start());
      }
      
      String field = null;
      boolean noCross = false;
      match = ADDR_END_MARKER.matcher(body);
      if (match.find()) {
        field = body.substring(match.end()).trim();
        body = body.substring(0,match.start()).trim();
        String mark = match.group();
        if (mark.length() > 0) {
          match = APT_PTN.matcher(field);
          if (match.lookingAt()) {
            data.strApt = append(data.strApt, "-", match.group());
            field = field.substring(match.end()).trim();
          }
        }
      }
      
      body = cleanCity(body, data);
      
      StartType st = StartType.START_CALL;
      match = PRI_MARKER.matcher(body);
      if (match.find()) {
        st = StartType.START_ADDR;
        data.strCall = body.substring(0,match.start()).trim();
        data.strPriority = match.group(1);
        body = body.substring(match.end()).trim();
      }
      else if ((match = ADDR_ST_MARKER.matcher(body)).matches()) {
        st = StartType.START_ADDR;
        data.strCall = match.group(1).trim();
        body = match.group(2);
      }
      
      // Remove I-nn HWY construct that causes problems
      body = I_NN_HWY_PTN.matcher(body).replaceAll("$1");
      
      int flags = FLAG_PAD_FIELD | FLAG_CROSS_FOLLOWS;
      if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
      if (field != null) flags |= FLAG_ANCHOR_END;
      parseAddress(st, flags, body, data);
      if (field == null) {
        field = getLeft();
        noCross = isMBlankLeft();
      }
      
      // If there is a pad field, treat it as a place or cross street
      String pad = getPadField();
      if (pad.contains("/") || isValidAddress(pad)) {
        data.strCross = append(data.strCross, " / ", stripFieldStart(pad, "/"));
      }
      else data.strPlace = pad;
      
      match = MAP_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        field = field.substring(match.end());
        noCross = field.startsWith("   ");
        field = field.trim();
        match = MAP_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strMap = match.group().trim();
          field = field.substring(match.end());
          noCross = field.startsWith("  ");
          field = field.trim();
        }
      }
      
      // Now we have to split what is left into a cross street and unit
      // If there is a premium map marker between them, things get easy
      if (field.startsWith("/")) field = field.substring(1).trim();
      match = MAP_EXTRA_PTN.matcher(field);
      if (match.find()) {
        data.strCross = append(data.strCross, " / ", field.substring(0, match.start()).trim());
        field = field.substring(match.end()).trim();
        if (data.strMap.length() == 0) data.strMap = match.group(1).trim();
      }
      
      // If not, our best approach is to looking for the first multiple blank delimiter.
      // which is a heck of a lot easier to do now that double blanks are preserved by
      // the getLeft() method.
      else {
        if (!noCross) {
          int pt = field.indexOf("  ");
          if (pt >= 0) {
            String cross = field.substring(0,pt);
            if (data.strCity.length() == 0) {
              parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, cross, data);
              cross = getStart();
            }
            data.strCross = append(data.strCross, " / ", cross);
            field = field.substring(pt+2).trim();
          }
          
          // If we didn't find one, we will have to use the smart address parser to figure out where
          // the cross street information ends
          else {
            flags = FLAG_ONLY_CROSS;
            if (data.strCity.length() == 0) flags |= FLAG_ONLY_CITY;
            Result res = parseAddress(StartType.START_ADDR, flags, field);
            if (res.isValid()) {
              res.getData(data);
              field = res.getLeft();
            }
          }
        }
      }
      
      // If we have not found a city, see if there is one here
      String city = data.strCity;
      data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
      if (data.strCity.length() > 0) field = getLeft();
      if (city.length() > 0) data.strCity = city;
      
      // Whatever is left becomes the unit
      data.strUnit = field.replaceAll("  +", " ");
      
      if (cityCodes != null) data.strCity = convertCodes(data.strCity, cityCodes);
    }

    Matcher match = CALL_CODE_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCall =  match.group(1).trim();
      data.strCode = match.group(2);
      String call = CALL_CODES.getCodeDescription(data.strCode, true);
      if (call != null) data.strCall = call;
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }
  
  private String cleanCity(String addr, Data data) {
    addr = SR_PTN.matcher(addr).replaceAll("SQ");
    Matcher match = CITY_CODE_PTN.matcher(addr);
    if (match.find()) {
      if (cityCodes != null) data.strCity = convertCodes(match.group(1), cityCodes);
      addr = match.replaceAll(" ").trim();
    }
    return addr;
  }
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b");
  private static final Pattern CITY_CODE_PTN = Pattern.compile(" *: *(FARM|UNVL)\\b *");
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ZIP")) return new SkipField("\\d{5}");
    if (name.equals("MAP_X")) return new MyMapCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private Pattern LEAD_ZERO_PTN = Pattern.compile("^0+");
  
  private String cleanAddress(String addr) {
    addr = MBLANK_PTN.matcher(addr).replaceAll(" ");
    addr = LEAD_ZERO_PTN.matcher(addr).replaceFirst("");
    return addr;
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = cleanAddress(field);
      if (field.equals(cleanAddress(getRelativeField(+1)))) return;
      super.parse(field, data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = cleanAddress(field);
      super.parse(field, data);;
    }
  }
  
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "APT#");
      super.parse(field, data);
    }
  }
  
  private class MyMapCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        field = field.substring(match.end()).trim();
        match = MAP_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strMap = match.group().trim();
          field = field.substring(match.end()).trim();
        }
      }
      
      match = MAP_EXTRA_PTN.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start()).trim();
        if (data.strMap.length() == 0) data.strMap = match.group(1).trim();
      }
      data.strCross = field;
    }
    
    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("(\\d\\d-\\d\\d-\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\\b.*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (!match.matches()) return false;;
      data.strDate = match.group(1).replace('-', '/');
      data.strTime = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final String[] CITY_LIST = new String[]{
      "BURLINGTON",
      "BRANFORD",
      "BRISTOL",
      "CANTON",
      "EAST HAVEN",
      "FARMINGTON",
      "GUILFORD",
      "HAMDEN",
      "NORTH BRANFORD",
      "NORTH HAVEN",
      "UNIONVILLE",
      "WALLINGFORD",
      "WEST HARTFORD",
      "WLFD"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FARM", "FARMINGTON",
      "UNVL", "UNIONVILLE",
      "WLFD", "WALLINGFORD"
  });

}
