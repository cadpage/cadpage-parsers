package net.anei.cadpage.parsers.WA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAOkanoganCountyParser extends FieldProgramParser {
  public WAOkanoganCountyParser() {
    super("OKANOGAN COUNTY", "WA", 
         "SRC CALL ADDR UNIT UNIT/S INFO+? DATETIME!");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField("Sent by OCSO(.*)", true);
    return super.getField(name);
  }
  
  @Override
  public String getFilter() {
    return "hiplink@co.okanogan.wa.us";
  }

  private static final Pattern RUN_REPORT_PATTERN
    = Pattern.compile(".*?(?:PAGED|ACKNO|ENRT |ARRVD|CMPLT|RETRN)\\-.*", Pattern.DOTALL);
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Email Copy Message From Hiplink")) return false;
    String[] field = body.split("\n");
    Matcher m = RUN_REPORT_PATTERN.matcher(body);
    if (m.matches()) {
      data.strCall = "RUN REPORT";
      data.strSource = field[0].trim();
      data.strUnit = field[1].trim();
      data.strCallId = field[2].trim();
      data.strPlace = body;
      return true;
    }
    infoState = 0;
    return parseFields(field, data);
  }

  private static final Pattern APT_PATTERN
    = Pattern.compile("(?:APT|RM|SP|LOT) *(.+)|\\d+[A-Z]?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(';');
      String apt = null;
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        Matcher m = APT_PATTERN.matcher(place);
        if (m.matches()) {
          apt = m.group(1);
          if (apt == null) apt = place;
        } else {
          data.strPlace = place;
        }
      }
      parseAddress(field, data);
      if (apt != null && !apt.equals(data.strApt)) {
        data.strApt = append(data.strApt, "-", apt);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" APT PLACE";
    }
  }
  
  private int infoState;
  
  private static final Pattern CALLBK_PTN = Pattern.compile("CALLB(?:AC)?K=(\\(\\d{3}\\)\\d{3}-\\d{4}) +(LAT=([-+]\\d{3}\\.\\d{6}) LON=([-+]\\d{3}\\.\\d{6}))? +.*");
  private static final Pattern PHONE_PATTERN = Pattern.compile(".*?\\(\\d{3}\\) *\\d{3}\\-\\d{4} [A-Z0-9]{4} .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher m = CALLBK_PTN.matcher(field);
      if (m.matches()) {
        data.strPhone = m.group(1);
        String lat = m.group(2);
        String lon = m.group(3);
        if (lat != null) {
          setGPSLoc(lat + ',' + lon,data);
        }
        return;
      }
      
      if (infoState == 2) return;
      
      if (infoState == 1) {
        data.strName = cleanWirelessCarrier(field);
        infoState++;
        return;
      }
      
      m = PHONE_PATTERN.matcher(field);
      if (m.matches()) {
        infoState++;
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" PHONE GPS NAME";
    }
  }
  
  private static final DateFormat MY_DT_FORMAT
    = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
  private class MyDateTimeField extends DateTimeField {
    MyDateTimeField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      setDateTime(MY_DT_FORMAT, field.trim(), data);
    }
  }
}
