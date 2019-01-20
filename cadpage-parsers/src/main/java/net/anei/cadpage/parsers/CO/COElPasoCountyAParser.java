package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class COElPasoCountyAParser extends FieldProgramParser {
  
  private static final Pattern JUNK_PTN = Pattern.compile("\\[[_A-Za-z]+\\]\\d+ chars\\.");
  
  private static final Pattern MASTER = 
      Pattern.compile("(?:(FC PMO):|\\[([-A-Z0-9 ]+): *([^\\]]*?)\\]) *");
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("([A-Z]{2,4}\\d{11}) +(.*?) +(D:.*) +(E:.*?) +(S:.*?) +(T:.*?) +(AD:.*?) +(C:.*?) +(Page Req Time:.*)");
  
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  
  public COElPasoCountyAParser() {
    super("EL PASO COUNTY", "CO",
          "UNIT CALL ADDR PLACE! x:X% ALRM:PRI? CMD:CH%? ID EMPTY? ( GPS_TRUNC | GPS/d | GPS1/d ( GPS_TRUNC | GPS2/d ) ) END");
  }
  
  @Override
  public String getFilter() {
    return "ept@ept911.info,ept@elpasoteller911.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 155; }    };
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Square bracket got turned into a subject and needs to be turned back
    if (subject.length() > 0) body = '[' + subject + "] " + body;
    
    // Strip off extra trailer
    body = stripFieldStart(body,  "Txt:");
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // And a new junk construct
    body = JUNK_PTN.matcher(body).replaceAll("");
    
    // Not everyone is using it, but see if this is the new standard dispatch format
    Matcher match = MASTER.matcher(body);
    if (match.lookingAt()) {
      String src = match.group(1);
      if (src != null) {
        data.strSource = src;
      } else {
        data.strSource = match.group(2).trim();
        data.strMap = match.group(3).trim();
      }
      body = body.substring(match.end());
      return parseFields(body.split("~"), data);
    }
    
    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      parseAddress(match.group(2), data);
      for (int ndx = 3; ndx <= match.groupCount(); ndx++) {
        String time = match.group(ndx);
        if (!time.endsWith(":")) data.strSupp = append(data.strSupp, "\n", time);
      }
      return true;
    }
   
    FParser p = new FParser(body);
    if (p.check("INFO from EPSO:")) {
      setFieldList("CALL ADDR APT PLACE CITY PRI UNIT");
      data.strCall = p.get(30);
      if (p.check(" ")) return false;
      parseAddress(p.get(30), data);
      if (p.checkAhead(105, "JURIS:")) p.skip(50);
      data.strApt = p.get(5);
      data.strPlace = p.get(50);
      String city;
      if (!p.check("JURIS:")) {
        if (!p.check(" ")) return false;
        city = p.get(30);
      } else {
        city = p.get(50);
      }
      data.strCity = cvtJurisCity(city);
      if (!p.check("ALRM:")) {
        if (p.check("ALRM")) {
          data.expectMore = true;
          return true;
        }
        return false;
      }
      if (!p.check("CMD:")) {
        data.strPriority = p.get(1);
        if (!p.check("CMD:")) return false;
      }
      data.strUnit = p.get();
      return true;
    }
    
    if (p.check("FROM EPSO REF:")) {
      setFieldList("CALL ADDR APT CITY ID");
      data.strCall = p.get(30);
      if (p.check(" ")) return false;
      parseAddress(p.get(90), data);
      if (!p.check("JURIS:")) return false;
      data.strCity = cvtJurisCity(p.get(30));
      if (p.check(" ")) return false;
      data.strCallId = p.get();
      return true;
    }
    
    if (p.check("From EPSO -")) {
      setFieldList("CALL ADDR APT INFO");
      data.strCall = p.get(24);
      if (!p.check(" ") || p.check(" ")) return false;
      parseAddress(p.get(50), data);
      if (!p.check("  ") || p.check(" ")) return false;
      data.strSupp = p.get();
      return true;
    }
    
    if (p.check("FCFES : ~")) {
      setFieldList("CALL ADDR APT INFO");
      data.strCall = p.get(40);
      if (!p.check("~")) return false;
      parseAddress(p.get(30), data);
      data.strApt = p.get(5);
      if (!p.check("~")) return false;
      data.strSupp = p.get();
      return true;
    }
    
    if (p.check("AD:")) {
      setFieldList("ADDR APT CALL ID TIME INFO");
      parseAddress(p.get(25), data);
      if (!p.check("Unit:")) return false;
      data.strApt = append(data.strApt, "-", p.get(3));
      if (!p.check("RE:")) return false;
      data.strCall = p.get(15);
      if (!p.check("Call#:")) return false;
      data.strCallId = p.get(9);
      if (!p.check(" ")) return false;
      String time = p.get(5);
      if (!TIME_PTN.matcher(time).matches()) return false;
      data.strTime = time;
      data.strSupp = p.get();
      return true;
    }
    
    return false;
  }
  
  @Override
  public String getProgram() {
    return "SRC MAP " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new GPSField("\\d{8,9} +\\d{8,9}");
    if (name.equals("GPS_TRUNC")) return new MyGPSTruncField();
    return super.getField(name);
  }
  
  private class MyGPSTruncField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!NUMERIC.matcher(field).matches()) return false;
      if (field.length() == 0 || field.length() >= 8) return false;
      data.expectMore = true;
      return true;
    }
  }
  
  private String cvtJurisCity(String city) {
    return convertCodes(city, JURIS_CITY_TABLE);
  }
  
  private static final Properties JURIS_CITY_TABLE = buildCodeTable(new String[]{
      "El Paso County SO",              "El Paso County",
      "EPSO Unincorporated Area",       ""
  });
}
