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

  public COElPasoCountyAParser() {
    super("EL PASO COUNTY", "CO",
          "ID? SRC_UNIT ( DISTRICT CALL PLACE ADDR UNIT/C EMPTY! " +
                       "| CALL ADDR PLACE! x:X% ALRM:PRI? CMD:CH%? ID EMPTY? ( GPS_TRUNC | GPS/d | GPS1/d ( GPS_TRUNC | GPS2/d ) ) INFO/N+ " +
                       ") END");
  }

  @Override
  public String getFilter() {
    return "ept@ept911.info,ept@elpasoteller911.org,alerts@eptpaging.info";
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

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(?:([A-Z]{2,4}\\d{11}|\\d{6}-\\d{5}) +)?(.*?) +(D:.*?) *(E:.*?) *(S:.*?) *(PTC:.*?) *(T:.*?) *(AD:.*?) *(C:.*?) *(Page Req Time:.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("RGROUP:[")) {
      int pt1 = 8;
      int pt2 = body.indexOf(']', pt1);
      if (pt2 < 0) return false;
      subject = body.substring(pt1, pt2).trim();
      body = body.substring(pt2+1).trim();
    }

    // One page format requires using the original subject
    if (subject.startsWith("INFO from EPSO:")) {
      setFieldList("CALL ADDR APT PLACE CITY PRI");
      data.strCall = subject.substring(15).trim();
      FParser p = new FParser(body);
      parseAddress(p.get(30), data);
      if (!p.check("#")) return false;
      data.strApt = append(data.strApt, "-", p.get(5));
      if (!p.check("~")) return false;
      data.strPlace = p.get(30);
      if (!p.check("JURIS:")) return false;
      data.strCity = cvtJurisCity(p.get(30));
      if (!p.check("CMD:")) return false;
      data.strPriority = p.get();
      return true;
    }

    // Otherwise square bracket got turned into a subject and needs to be turned back
    if (subject.length() > 0) {
      body = '[' + subject + "] " + body;
    } else if (body.startsWith("[") && !body.contains("]")) {
      body = body.substring(1).trim();
    }

    FParser p = new FParser(body);
    if (p.check("To CSFD from EPSO:")) {
      data.strUnit = p.get(100);
      if (p.check("Requested to:")) {
        parseAddress(p.get(60), data);
        setFieldList("UNIT ADDR APT CALL CH INFO");
        if (!p.check(" - FOR A:")) return false;
        data.strCall = p.get(30);
        if (!p.check(" - DISPATCH CHANNEL:")) return false;
        data.strChannel = p.get(12);
        data.strSupp = p.get();
        return true;
      } else {
        parseAddress(p.get(60), data);
        if (!p.check(" ~")) return false;
        setFieldList("UNIT ADDR APT CALL CH GPS INFO");
        data.strCall = p.get(30);
        if (!p.check("~CMD:")) return false;
        data.strChannel = p.get(10);
        if (!p.check("~LAT:")) return false;
        String gps1 = p.get(10);
        if (!p.check("~LONG:")) return false;
        String gps2 = p.get(10);
        setGPSLoc(cvtGpsCoord(gps1) + ',' + cvtGpsCoord(gps2), data);
        data.strSupp = p.get();
        return true;
      }
    }

    // Not everyone is using it, but see if this is the new standard dispatch format
    String[] flds = body.split("~", -1);
    if (flds.length >= 5) {
      return parseFields(flds, data);
    }

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = getOptGroup(match.group(1));
      parseAddress(match.group(2), data);
      for (int ndx = 3; ndx <= match.groupCount(); ndx++) {
        String time = match.group(ndx);
        if (time != null && !time.endsWith(":")) {
          data.strSupp = append(data.strSupp, "\n", time);
        }
      }
      return true;
    }

    body = stripFieldStart(body, "~");
    body = stripFieldEnd(body, "~");

    if (p.check("FROM EPSO REF:")) {
      setFieldList("CALL ADDR APT PLACE CITY ID UNIT");
      data.strCall = p.get(30);
      parseAddress(p.get(40), data);
      data.strApt = append(data.strApt, "-", p.get(5));
      data.strPlace = p.get(40);
      if (!p.check("JURIS:")) return false;
      data.strCity = cvtJurisCity(p.get(30));
      data.strCallId = p.get(10);
      p.setOptional();
      if (!p.check("~Units:")) return false;
      data.strUnit = p.get();
      return true;
    }

    if (p.check("REF:")) {
      setFieldList("CALL ADDR APT");
      data.strCall = p.get(33);
      if (!p.check("THE LOC HAS CHANGED TO:")) return false;
      parseAddress(p.get(30), data);
      if (!p.check("#")) return false;
      data.strApt = p.get();
      return true;
    }

    return false;
  }

  private String cvtGpsCoord(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) {
      fld = fld.substring(0,pt) + '.' + fld.substring(pt);
    }
    return fld;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z0-9]{2,5}\\d{2}-\\d{5}", true);
    if (name.equals("SRC_UNIT")) return new MySourceUnitField();
    if (name.equals("DISTRICT")) return new MapField("District \\d+|Colorado S|El Paso Co|HWY 115 -", true);
    if (name.equals("GPS")) return new GPSField("\\d{8,9} +\\d{8,9}");
    if (name.equals("GPS_TRUNC")) return new MyGPSTruncField();
    return super.getField(name);
  }

  private static final Pattern SRC_UNIT_PTN = Pattern.compile("\\[(.*?):?\\][ ,]*(.*?)");
  private class MySourceUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1).trim();
      data.strUnit = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "SRC UNIT";
    }
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
      "EPSO Unincorporated Area",       "",
      "USAFA SF",                       "Air Force Academy"
  });
}
