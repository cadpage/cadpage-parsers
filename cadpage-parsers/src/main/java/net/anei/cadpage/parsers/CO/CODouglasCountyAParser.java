package net.anei.cadpage.parsers.CO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CODouglasCountyAParser extends FieldProgramParser {
  
  private static final String WRAPPER_MARK = " SUBJECT: Dispatch BODY: ";
  private static final Pattern TRAIL_ID_PTN = Pattern.compile(" +(\\d{4}-\\d{8})$");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Time:|Additional Location Info:)");

  public CODouglasCountyAParser() {
    this("DOUGLAS COUNTY", "CO");
  }
  
  protected CODouglasCountyAParser(String defCity, String defState) {
    super(defCity, defState,
           "( Call:CALL! Location:ADDRCH/SXa! Map:MAP Units:UNITX! Common_Name:PLACE Time:DATETIME Narrative:INFO? Nature_Of_Call:INFO " +
           "| Call_Type:CALLID! Common_Name:PLACE! Location:ADDR/SXXx! Call_Time:DATETIME! Narrative:INFO Nature_Of_Call:INFO " +
           "| CALL! ( LOC:ADDRCITY/SXa! ( Closest_X:X! Map:MAP | Map:MAP! Closest_X:X? ) Units:UNIT Nar:INFO LOC_Name:PLACE ADDL:INFO CR:ID3 " +
                   "| Address:ADDRCITY/SXa! Closest_Intersection:X! Additional_Location_Info:INFO/N! OPS:CH! Map_Page:MAP! Units:UNIT! Primary_Incident:SKIP! Radio_Channel:CH/L! Common_Name:PLACE! Narrative:INFO/N ) Time:DATETIME3 GPS )");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getAliasCode() {
    return "CODouglasCounty";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public String getFilter() {
    return "@notifyall.com,@notifyatonce.com,dcso@douglas.co.us,EFPD@notifyall.com";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("([- /\\(\\)A-Z0-9]+) LOC: ([- /@A-Z0-9]+)(?:, ([ A-Za-z]+))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // One agency gets calls with nested message wrappers.  Normal preparsing took care of 
    // the outermost wrapper, but we have to deal with the inner wrapper here
    if (!subject.equals("Dispatch")) {
      int pt = body.indexOf(WRAPPER_MARK);
      if (pt >= 0) body = body.substring(pt+WRAPPER_MARK.length()).trim();
    }
    
    // Check for short format
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2).trim().replace('@', '&'), data);
      data.strCity = getOptGroup(match.group(3));
      return true;
    }
    
    match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start());
    }
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    body = body.replace(": ADDL:", " ADDL:");
    body = body.replace(" : Time:", " Time:");
    body = body.replace("\nCR", " CR:");
    body = body.replace('\n', ' ');
    
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCross.equals("No Cross Streets Found")) data.strCross = "";
    
    // Intersections are (sometimes) saved as cross roads :(
    if (data.strAddress.length() == 0 && data.strCross.length() > 0) {
      for (String addr : data.strCross.split(",")) {
        if (addr.contains("/")) {
          parseAddress(addr.trim(), data);
          break;
        }
      }
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
  
  @Override
  public Field getField(String  name) {
    if (name.equals("ADDRCH")) return new MyAddressChannelField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNITX")) return new MyUnitCrossField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALLID")) return new MyCallIdField();
    if (name.equals("ID3")) return new MyId3Field();
    if (name.equals("DATETIME3")) return new MyDateTime3Field();
    return super.getField(name);
  }
  
  
  // Address field should strip  trailing slash characters
  // and a trailing operations channel
  private static final Pattern OPS_PTN = Pattern.compile("\\bEOPS\\d$");
  private static final Pattern DIR_PTN = Pattern.compile("\\b(NO|EA|SO|WE)\\b");
  private class MyAddressChannelField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = OPS_PTN.matcher(field);
      if (match.find()) {
        data.strChannel = match.group();
        field = field.substring(0,match.start()).trim();
      }
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      
      // Dispatch frequently uses 2 letter direction abbreviations that the address
      // parsing logic does not understand.  So we turn those into one character abbreviations
      match = DIR_PTN.matcher(field);
      if (match.find()) {
        StringBuffer sb = new StringBuffer();
        int last = 0;
        do {
          sb.append(field.substring(last, match.start()+1));
          last = match.end();
        } while (match.find());
        sb.append(field.substring(last));
        field = sb.toString();
      }
      
      if (I25_MM_PTN.matcher(field).matches()) {
        data.strAddress = field;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
  
  // Address fields must turn @ to &
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (I25_MM_PTN.matcher(field).matches()) {
        data.strAddress = field;
      } else {
        field = field.replace('@', '&');
        super.parse(field, data);
      }
    }
  }
  
  // Ditto for Address/City fields must turn @ to &
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (I25_MM_PTN.matcher(field).matches()) {
        data.strAddress = field;
      } else if (field.startsWith("INT ")) {
        field = field.substring(4).trim();
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
       
      } else {
        field = field.replace('@', '&');
        super.parse(field, data);
      }
    }
  }
  
  // Unit field also contains cross field
  private class MyUnitCrossField extends UnitField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("  "), data);
      data.strCross = p.get();
      if (data.strCross.length() == 0 && 
          (data.strUnit.equals("No Cross Streets Found") || data.strUnit.contains("/"))) {
        data.strCross = data.strUnit;
        data.strUnit = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT X";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(data.strAddress)) {
        field = field.substring(0,field.length()-data.strAddress.length()).trim();
        if (field.equals("INT")) {
          field = "";
        } else {
          field = stripFieldEnd(field, " INT");
        }
      }
      super.parse(field, data);
    }
  }
  
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 14) super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("^Call Number \\d+ was created from Call Number \\d+\\b *|E911 Info - ");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("").trim();
      if (data.strChannel.length() > 0 && field.startsWith(data.strChannel)) {
        field = field.substring(data.strChannel.length()).trim();
      }
      super.parse(field, data);
    }
  }
  
  private class MyCallIdField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TRAIL_ID_PTN.matcher(field);
      if (!match.find()) abort();
      data.strCall = field.substring(0,match.start());
      data.strCallId = match.group(1);
    }
  }
  

  private static final Pattern DATE_TIME3_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d?:\\d\\d:? [AP]M)\\b:? *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTime3Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field,  ":");
      Matcher match = DATE_TIME3_PTN.matcher(field);
      if (!match.matches()) return;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      setGPSLoc(match.group(3), data);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME GPS";
    }
  }
  
  private class MyId3Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = I25_MM_PTN.matcher(address);
    if (match.matches()) address = match.group(1);
    return address;
  }
  private static final Pattern I25_MM_PTN = Pattern.compile("(I25 MM\\d+ [NS]B)(?: .*)?");


  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2220 CASTLEGATE DR",                   "+39.421939,-104.886390",
      "2240 CASTLEGATE DR",                   "+39.422312,-104.885827",
      "2246 CASTLEGATE DR",                   "+39.422801,-104.886057",
      "2252 CASTLEGATE DR",                   "+39.423153,-104.885140",
      "2258 CASTLEGATE DR",                   "+39.422474,-104.885204",
      "2264 CASTLEGATE DR",                   "+39.422639,-104.884470",
      "2270 CASTLEGATE DR",                   "+39.421935,-104.885038",
      "2276 CASTLEGATE DR",                   "+39.421624,-104.885660",
      "2282 CASTLEGATE DR",                   "+39.420571,-104.886138",
      "2288 CASTLEGATE DR",                   "+39.421073,-104.886041",
      "2294 CASTLEGATE DR",                   "+39.421322,-104.886739",
      "2300 CASTLEGATE DR",                   "+39.421044,-104.887323",
      "6001 CASTLEGATE DR",                   "+39.419511,-104.881068",
      "6005 CASTLEGATE DR",                   "+39.419179,-104.881755",
      "6009 CASTLEGATE DR",                   "+39.418964,-104.882565",
      "6013 CASTLEGATE DR",                   "+39.418512,-104.883332",
      "6017 CASTLEGATE DR",                   "+39.419063,-104.884250",
      "6021 CASTLEGATE DR",                   "+39.419308,-104.883724",
      "6025 CASTLEGATE DR",                   "+39.419610,-104.883241",
      "6029 CASTLEGATE DR",                   "+39.420198,-104.883123",
      "6033 CASTLEGATE DR",                   "+39.419718,-104.882415",
      "6037 CASTLEGATE DR",                   "+39.420033,-104.882093",
      "6041 CASTLEGATE DR",                   "+39.420704,-104.882125",
      "6045 CASTLEGATE DR",                   "+39.420331,-104.881680",
      "6181 CASTLEGATE DR",                   "+39.420990,-104.882699",
      "6185 CASTLEGATE DR",                   "+39.420542,-104.883488",
      "6189 CASTLEGATE DR",                   "+39.420315,-104.883799",
      "6193 CASTLEGATE DR",                   "+39.419846,-104.884169",
      "6197 CASTLEGATE DR",                   "+39.419739,-104.884754",
      "6201 CASTLEGATE DR",                   "+39.420277,-104.885489",
      "6205 CASTLEGATE DR",                   "+39.420696,-104.885081",
      "6209 CASTLEGATE DR",                   "+39.421330,-104.884636",
      "6213 CASTLEGATE DR",                   "+39.421732,-104.884314",
      "6217 CASTLEGATE DR",                   "+39.422283,-104.884046",
      "6220 CASTLEGATE DR",                   "+39.421135,-104.883584",
      "6225 CASTLEGATE DR",                   "+39.421753,-104.883375",

      "I25 MM170 NB", "39.217364,-104.877107",
      "I25 MM170 SB", "39.217364,-104.877107",
      "I25 MM171 NB", "39.231218,-104.877672",
      "I25 MM171 SB", "39.231218,-104.877672",
      "I25 MM172 NB", "39.245073,-104.879922",
      "I25 MM172 SB", "39.245073,-104.879922",
      "I25 MM173 NB", "39.257136,-104.890307",
      "I25 MM173 SB", "39.257136,-104.890307",
      "I25 MM174 NB", "39.270504,-104.896985",
      "I25 MM174 SB", "39.270504,-104.896985",
      "I25 MM175 NB", "39.285110,-104.896290",
      "I25 MM175 SB", "39.285110,-104.896290",
      "I25 MM176 NB", "39.298827,-104.890537",
      "I25 MM176 SB", "39.298827,-104.890537",
      "I25 MM177 NB", "39.312872,-104.885372",
      "I25 MM177 SB", "39.312872,-104.885372",
      "I25 MM178 NB", "39.326884,-104.880246",
      "I25 MM178 SB", "39.326884,-104.880246",
      "I25 MM179 NB", "39.340208,-104.875542",
      "I25 MM179 SB", "39.340208,-104.875542",
      "I25 MM180 NB", "39.354219,-104.870033",
      "I25 MM180 SB", "39.354219,-104.870033",
      "I25 MM181 NB", "39.368295,-104.864944",
      "I25 MM181 SB", "39.368295,-104.864944",
      "I25 MM182 NB", "39.382307,-104.860402",
      "I25 MM182 SB", "39.382307,-104.860402",
      "I25 MM183 NB", "39.396422,-104.860163",
      "I25 MM183 SB", "39.396422,-104.860163",
      "I25 MM184 NB", "39.410118,-104.866341",
      "I25 MM184 SB", "39.410118,-104.866341",
      "I25 MM185 NB", "39.423523,-104.875521",
      "I25 MM185 SB", "39.423523,-104.875521",
      "I25 MM186 NB", "39.437084,-104.878115",
      "I25 MM186 SB", "39.437084,-104.878115",
      "I25 MM187 NB", "39.450508,-104.875642",
      "I25 MM187 SB", "39.450508,-104.875642",
      "I25 MM188 NB", "39.466664,-104.873317",
      "I25 MM188 SB", "39.466664,-104.873317"

  });
}
