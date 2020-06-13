package net.anei.cadpage.parsers.IN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Madison County, IN (C)
 */
public class INMadisonCountyCParser extends FieldProgramParser {
  
  public INMadisonCountyCParser() {
    super(INMadisonCountyParser.CITY_LIST, "MADISON COUNTY", "IN",
          "( DATE:DATETIME! CFS#:SKIP? PLACE:PLACE! ADDR:ADDRCITY! ( CROSS_STREETS:X! CALL:CALL! UNIT:UNIT! | CALL:CALL! UNIT:UNIT! CROSS_STREETS:X! ) ALARM_LEVEL:PRI? INFO:INFO! FIRE_RD:CH! EMS_RD:CH ( INCIDENT#:ID! GPS_LAT:GPS1! GPS_LON:GPS2! NARRATIVE:INFO/N! INFO/N+ | RUN_#:ID! NARRATIVE:INFO/N! INFO/N+ CALLER-NAME:NAME! CALLER-PHONE:PHONE! CAD_#:SKIP! ALL_INCIDENTS:ID! GPS_LAT:GPS1! GPS_LON:GPS2! ) " +
          "| CALL:CALL! DATE:DATETIME! PLACE:PLACE! ADDR:ADDRCITY! INFO:INFO? ( MAP:MAP! CITY:CITY! ID:ID! PRI:PRI! UNIT:UNIT! X:X! SOURCE:SKIP! | UNIT:UNIT! X:X! MAP:MAP! ) CALLER-NAME:NAME! CALLER-PHONE:PHONE! INCIDENT#:SKIP! OTHER_INCIDENT#:SKIP? DISTRICT:SKIP? BEAT:MAP! LOCATION_DETAILS:INFO/N+ NARRATIVE:INFO/N+ RADIO_CHANNEL:CH ) END");
  }
  
  @Override
  public String getFilter() {
    return "cfs@madisoncountypaging.com,@madisoncounty.in.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS")) return false;
    body = body.replace("\nCAD #:", "\nINCIDENT#:");
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, field, data);
      } else {
        data.strTime = time;
      }
    }
    
  }
  
  private static final Pattern CORD_ST_PTN = Pattern.compile("\\b[NS] +CORD \\d+ [EW]\\b");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String addr = null;
      String addr2 = null;
      String place = null;
      for (String part : field.split(",")) {
        part = part.trim();
        if (part.length() == 0) continue;
        if (addr == null) {
          addr = part;
          addr2 = part.replace('@', '&');
        }
        else if (part.equals("IN")) {
          data.strState = part;
        }
        else {
          if (data.strCity.length() == 0) {
            parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, part, data);
            part = getLeft();
          }
          if (place == null) {
            place = part;
            if (data.strCity.length() == 0) {
              String part2 = part.replace('@', '&');
              if (myCheckAddress(part2) > myCheckAddress(addr2)) {
                place = addr;
                addr = part;
                addr2 = part2;
              }
            }
          } else {
            place = append(place, ", ", part);
          }
        }
      }
      if (addr2 == null) return;
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr2, data);
      if (place != null) {
        if (data.strCity.length() == 0) {
          data.strCity = place;
        } else {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      }
    }
    
    private int myCheckAddress(String address) {
      if (CORD_ST_PTN.matcher(address).find()) return STATUS_FULL_ADDRESS;
      return checkAddress(address);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals(data.strChannel)) return;
      data.strChannel = append(data.strChannel, "/", field);
    }
  }
  
  private static final Pattern ID_JUNK_PTN = Pattern.compile(" *\\(\\w+\\)");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = ID_JUNK_PTN.matcher(field).replaceAll("");
      if (field.equals("/")) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return CORD_PTN.matcher(address).replaceAll("");
  }
  private static final Pattern CORD_PTN = Pattern.compile("\\bCORD\\b");
}
